package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Factura} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String serie;

    @NotNull
    private String noFisico;

    @NotNull
    private Instant fecha;

    @NotNull
    @DecimalMin(value = "0")
    private Double subtotal;

    @NotNull
    @DecimalMin(value = "0")
    private Double impuesto;

    @NotNull
    @DecimalMin(value = "0")
    private Double impuestoCero;

    @NotNull
    @DecimalMin(value = "0")
    private Double descuento;

    @NotNull
    @DecimalMin(value = "0")
    private Double total;

    @NotNull
    @DecimalMin(value = "0")
    private Double porcentajeImpuesto;

    @NotNull
    private String cedula;

    @NotNull
    private String direccion;

    @NotNull
    private String email;

    @NotNull
    private String estado;

    private CentroDTO centro;

    private ClienteDTO cliente;

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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNoFisico() {
        return noFisico;
    }

    public void setNoFisico(String noFisico) {
        this.noFisico = noFisico;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getImpuestoCero() {
        return impuestoCero;
    }

    public void setImpuestoCero(Double impuestoCero) {
        this.impuestoCero = impuestoCero;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(Double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaDTO)) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", serie='" + getSerie() + "'" +
            ", noFisico='" + getNoFisico() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", subtotal=" + getSubtotal() +
            ", impuesto=" + getImpuesto() +
            ", impuestoCero=" + getImpuestoCero() +
            ", descuento=" + getDescuento() +
            ", total=" + getTotal() +
            ", porcentajeImpuesto=" + getPorcentajeImpuesto() +
            ", cedula='" + getCedula() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", email='" + getEmail() + "'" +
            ", estado='" + getEstado() + "'" +
            ", centro=" + getCentro() +
            ", cliente=" + getCliente() +
            "}";
    }
}
