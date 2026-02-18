package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DetalleFactura.
 */
@Entity
@Table(name = "detalle_factura")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "no_cia", nullable = false)
    private Long noCia;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "impuesto", nullable = false)
    private Double impuesto;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", nullable = false)
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "detalles", "centro", "cliente" }, allowSetters = true)
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "detalleFacturas", "modelo", "tipoProducto", "proveedor" }, allowSetters = true)
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleFactura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public DetalleFactura noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public DetalleFactura cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return this.precioUnitario;
    }

    public DetalleFactura precioUnitario(Double precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public DetalleFactura subtotal(Double subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return this.descuento;
    }

    public DetalleFactura descuento(Double descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getImpuesto() {
        return this.impuesto;
    }

    public DetalleFactura impuesto(Double impuesto) {
        this.setImpuesto(impuesto);
        return this;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getTotal() {
        return this.total;
    }

    public DetalleFactura total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Factura getFactura() {
        return this.factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public DetalleFactura factura(Factura factura) {
        this.setFactura(factura);
        return this;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DetalleFactura producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleFactura)) {
            return false;
        }
        return getId() != null && getId().equals(((DetalleFactura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleFactura{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", subtotal=" + getSubtotal() +
            ", descuento=" + getDescuento() +
            ", impuesto=" + getImpuesto() +
            ", total=" + getTotal() +
            "}";
    }
}
