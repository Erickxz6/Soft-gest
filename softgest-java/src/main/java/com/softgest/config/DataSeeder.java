package com.softgest.config;

import com.softgest.model.*;
import com.softgest.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = Logger.getLogger(DataSeeder.class.getName());

    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;
    private final PasswordEncoder encoder;

    public DataSeeder(UsuarioRepository ur, CategoriaRepository cr, ProductoRepository pr, ClienteRepository clr, PasswordEncoder enc) {
        this.usuarioRepo = ur; this.categoriaRepo = cr; this.productoRepo = pr; this.clienteRepo = clr; this.encoder = enc;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() > 0) { log.info("BD ya tiene datos. Omitiendo seed."); return; }
        log.info("Cargando datos semilla...");

        String pw = encoder.encode("123456");
        usuarioRepo.save(new Usuario("Carlos", "Admin", "admin@softgest.com", pw, Rol.ADMIN));
        usuarioRepo.save(new Usuario("Ana", "Vendedora", "vendedor@softgest.com", pw, Rol.VENDEDOR));
        usuarioRepo.save(new Usuario("Luis", "Bodeguero", "bodeguero@softgest.com", pw, Rol.BODEGUERO));
        usuarioRepo.save(new Usuario("Maria", "Domiciliaria", "domiciliario@softgest.com", pw, Rol.DOMICILIARIO));

        Categoria papeleria = categoriaRepo.save(new Categoria("Papeleria", "Utiles escolares y de oficina"));
        Categoria tecnologia = categoriaRepo.save(new Categoria("Tecnologia", "Accesorios y dispositivos"));
        Categoria oficina = categoriaRepo.save(new Categoria("Oficina", "Insumos de oficina"));

        productoRepo.save(new Producto("Cuaderno universitario 100h", new BigDecimal("5500"), 120, 20, papeleria));
        productoRepo.save(new Producto("Resma de papel carta", new BigDecimal("14000"), 45, 10, oficina));
        productoRepo.save(new Producto("Boligrafo Kilometrico x3", new BigDecimal("4200"), 200, 30, papeleria));
        productoRepo.save(new Producto("Mouse inalambrico", new BigDecimal("32000"), 25, 5, tecnologia));
        productoRepo.save(new Producto("Memoria USB 32GB", new BigDecimal("18500"), 40, 8, tecnologia));
        productoRepo.save(new Producto("Audifonos in-ear", new BigDecimal("15000"), 3, 5, tecnologia));
        productoRepo.save(new Producto("Carpeta legajadora", new BigDecimal("3800"), 60, 15, oficina));
        productoRepo.save(new Producto("Caja de colores x24", new BigDecimal("12000"), 35, 10, papeleria));
        productoRepo.save(new Producto("Cinta adhesiva transparente", new BigDecimal("2500"), 80, 20, oficina));
        productoRepo.save(new Producto("Regla 30cm", new BigDecimal("1800"), 2, 10, papeleria));

        clienteRepo.save(new Cliente("Distribuidora Escolar S.A.S.", "900123456", "3001234567", "escolar@mail.com", "Calle 45 #12-30, Cartagena", "EMPRESA"));
        clienteRepo.save(new Cliente("Juan Perez", "1042589632", "3109876543", "juan@mail.com", "Cra 15 #23-45, San Luis", "PERSONA"));
        clienteRepo.save(new Cliente("Maria Gonzalez", "45789632", "3157894561", "maria@mail.com", "Calle 78 #5-12, Bocagrande", "PERSONA"));
        clienteRepo.save(new Cliente("Papeleria Don Memo", "900987654", "3204567890", null, "Av. Pedro de Heredia #45-67", "EMPRESA"));
        clienteRepo.save(new Cliente("Colegio San Jose", "800111222", "6654321", "compras@sanjose.edu.co", "Calle 30 #10-50, Manga", "EMPRESA"));

        log.info("Datos semilla cargados exitosamente");
        log.info("Usuarios de prueba (clave: 123456): admin@softgest.com | vendedor@softgest.com | bodeguero@softgest.com");
    }
}
