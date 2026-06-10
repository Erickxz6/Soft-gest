package com.softgest.service;

import com.softgest.model.Usuario;
import com.softgest.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() { return repo.findAll(); }
    public Optional<Usuario> buscarPorId(Long id) { return repo.findById(id); }
    public Optional<Usuario> buscarPorCorreo(String correo) { return repo.findByCorreo(correo); }

    public Usuario guardar(Usuario u) {
        if (u.getId() == null || (u.getContrasena() != null && !u.getContrasena().isEmpty() && !u.getContrasena().startsWith("$2a$"))) {
            u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        }
        return repo.save(u);
    }

    public void eliminar(Long id) { repo.deleteById(id); }
}
