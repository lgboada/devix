package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.DetalleFactura} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleFacturaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0")
    private Double precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    private Double subtotal;

    @NotNull
    @DecimalMin(value = "0")
    private Double descuento;

    @NotNull
    @DecimalMin(value = "0")
    private Double impuesto;

    @NotNull
    @DecimalMin(value = "0")
    private Double total;

    private FacturaDTO factura;

    private ProductoDTO producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(FacturaDTO factura) {
        this.factura = factura;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleFacturaDTO)) {
            return false;
        }

        DetalleFacturaDTO detalleFacturaDTO = (DetalleFacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detalleFacturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleFacturaDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", subtotal=" + getSubtotal() +
            ", descuento=" + getDescuento() +
            ", impuesto=" + getImpuesto() +
            ", total=" + getTotal() +
            ", factura=" + getFactura() +
            ", producto=" + getProducto() +
            "}";
    }
}
