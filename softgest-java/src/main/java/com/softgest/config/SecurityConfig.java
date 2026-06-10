package com.softgest.config;

import com.softgest.model.Usuario;
import com.softgest.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepo;

    public SecurityConfig(UsuarioRepository usuarioRepo) { this.usuarioRepo = usuarioRepo; }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public UserDetailsService userDetailsService() {
        return correo -> {
            Usuario u = usuarioRepo.findByCorreo(correo)
                    .orElseThrow(() -> new UsernameNotFoundException("No encontrado: " + correo));
            if (!u.getActivo()) throw new UsernameNotFoundException("Usuario inactivo");
            return new org.springframework.security.core.userdetails.User(
                    u.getCorreo(), u.getContrasena(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().name())));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
}
