package com.softgest.service;

import com.softgest.model.Cliente;
import com.softgest.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) { this.repo = repo; }

    public List<Cliente> listarTodos() { return repo.findAll(); }
    public List<Cliente> listarActivos() { return repo.findByActivoTrue(); }
    public Optional<Cliente> buscarPorId(Long id) { return repo.findById(id); }
    public Cliente guardar(Cliente c) { return repo.save(c); }
    public List<Cliente> buscar(String q) {
        if (q == null || q.isBlank()) return listarTodos();
        return repo.findByNombreContainingIgnoreCaseOrDocumentoContaining(q, q);
    }
}
