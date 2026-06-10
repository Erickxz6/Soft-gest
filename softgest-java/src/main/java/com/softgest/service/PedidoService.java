package com.softgest.service;

import com.softgest.model.*;
import com.softgest.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;

    public PedidoService(PedidoRepository repo, ProductoRepository productoRepo, ClienteRepository clienteRepo) {
        this.repo = repo; this.productoRepo = productoRepo; this.clienteRepo = clienteRepo;
    }

    public List<Pedido> listarTodos() { return repo.findAllByOrderByCreadoEnDesc(); }
    public Optional<Pedido> buscarPorId(Long id) { return repo.findById(id); }
    public List<Pedido> listarPorEstado(EstadoPedido e) { return repo.findByEstado(e); }

    public String generarCodigo() {
        String prefijo = "PED-" + LocalDate.now().toString().replace("-", "");
        String ultimo = repo.findUltimoCodigo(prefijo);
        int seq = 1;
        if (ultimo != null) {
            String[] partes = ultimo.split("-");
            seq = Integer.parseInt(partes[partes.length - 1]) + 1;
        }
        return prefijo + "-" + String.format("%03d", seq);
    }

    @Transactional
    public Pedido crearPedido(Long clienteId, List<Long> productoIds, List<Integer> cantidades, String obs, Usuario vendedor) {
        Cliente cliente = clienteRepo.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCodigo(generarCodigo());
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setObservaciones(obs);

        for (int i = 0; i < productoIds.size(); i++) {
            Producto prod = productoRepo.findById(productoIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            int cant = cantidades.get(i);
            if (prod.getStock() < cant) {
                throw new RuntimeException("Stock insuficiente para: " + prod.getNombre() + " (disponible: " + prod.getStock() + ", solicitado: " + cant + ")");
            }
            DetallePedido det = new DetallePedido(pedido, prod, cant, prod.getPrecio());
            pedido.getDetalles().add(det);
            prod.setStock(prod.getStock() - cant);
            productoRepo.save(prod);
        }
        pedido.recalcularTotal();
        return repo.save(pedido);
    }

    @Transactional
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido p = repo.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        if (nuevoEstado == EstadoPedido.CANCELADO && p.getEstado() != EstadoPedido.ENTREGADO) {
            for (DetallePedido d : p.getDetalles()) {
                Producto prod = d.getProducto();
                prod.setStock(prod.getStock() + d.getCantidad());
                productoRepo.save(prod);
            }
        }
        p.setEstado(nuevoEstado);
        return repo.save(p);
    }

    public long pedidosHoy() { return repo.countPedidosDesde(LocalDate.now().atStartOfDay()); }
    public BigDecimal ventasHoy() {
        BigDecimal v = repo.sumVentasDesde(LocalDate.now().atStartOfDay());
        return v != null ? v : BigDecimal.ZERO;
    }
    public long pedidosPendientes() { return repo.countByEstado(EstadoPedido.PENDIENTE); }
    public long pedidosDespachados() { return repo.countByEstado(EstadoPedido.DESPACHADO); }
}
