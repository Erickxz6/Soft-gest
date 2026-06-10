package com.softgest.repository;
import com.softgest.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContainingIgnoreCaseOrDocumentoContaining(String nombre, String doc);
    List<Cliente> findByActivoTrue();
}
