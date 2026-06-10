package com.softgest.service;

import com.softgest.model.Producto;
import com.softgest.model.Categoria;
import com.softgest.repository.ProductoRepository;
import com.softgest.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaRepository catRepo;

    public ProductoService(ProductoRepository repo, CategoriaRepository catRepo) {
        this.repo = repo; this.catRepo = catRepo;
    }

    public List<Producto> listarTodos() { return repo.findAll(); }
    public List<Producto> listarActivos() { return repo.findByActivoTrue(); }
    public Optional<Producto> buscarPorId(Long id) { return repo.findById(id); }
    public Producto guardar(Producto p) { return repo.save(p); }
    public List<Producto> buscar(String q) { return q == null || q.isBlank() ? listarTodos() : repo.findByNombreContainingIgnoreCase(q); }
    public List<Producto> productosCriticos() { return repo.findProductosCriticos(); }
    public List<Categoria> listarCategorias() { return catRepo.findAll(); }
    public Categoria guardarCategoria(Categoria c) { return catRepo.save(c); }
    public long contarActivos() { return repo.countByActivoTrue(); }
}
