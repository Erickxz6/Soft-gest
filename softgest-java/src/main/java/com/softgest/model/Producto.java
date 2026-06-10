package com.softgest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 200)
    private String nombre;

    private String descripcion;

    @NotNull @DecimalMin("0.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal precio;

    @NotNull @Min(0)
    private Integer stock = 0;

    @NotNull @Min(0)
    private Integer stockMinimo = 5;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private Boolean activo = true;

    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() { this.creadoEn = LocalDateTime.now(); }

    public Producto() {}

    public Producto(String nombre, BigDecimal precio, int stock, int stockMinimo, Categoria categoria) {
        this.nombre = nombre; this.precio = precio; this.stock = stock;
        this.stockMinimo = stockMinimo; this.categoria = categoria; this.activo = true;
    }

    public String getSemaforo() {
        if (stock <= 0) return "danger";
        if (stock <= stockMinimo) return "warning";
        return "success";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
