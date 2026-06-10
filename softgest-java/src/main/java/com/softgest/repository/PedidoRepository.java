package com.softgest.repository;
import com.softgest.model.EstadoPedido;
import com.softgest.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findAllByOrderByCreadoEnDesc();
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.creadoEn >= :inicio")
    long countPedidosDesde(@Param("inicio") LocalDateTime inicio);
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.creadoEn >= :inicio AND p.estado != com.softgest.model.EstadoPedido.CANCELADO")
    BigDecimal sumVentasDesde(@Param("inicio") LocalDateTime inicio);
    long countByEstado(EstadoPedido estado);
    @Query("SELECT MAX(p.codigo) FROM Pedido p WHERE p.codigo LIKE :prefijo%")
    String findUltimoCodigo(@Param("prefijo") String prefijo);
}
