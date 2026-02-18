package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factura implements Serializable {

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
    @Column(name = "serie", nullable = false)
    private String serie;

    @NotNull
    @Column(name = "no_fisico", nullable = false)
    private String noFisico;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "impuesto", nullable = false)
    private Double impuesto;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "impuesto_cero", nullable = false)
    private Double impuestoCero;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", nullable = false)
    private Double total;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "porcentaje_impuesto", nullable = false)
    private Double porcentajeImpuesto;

    @NotNull
    @Column(name = "cedula", nullable = false)
    private String cedula;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "factura")
    @JsonIgnoreProperties(value = { "factura", "producto" }, allowSetters = true)
    private Set<DetalleFactura> detalles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facturas", "eventos", "compania" }, allowSetters = true)
    private Centro centro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "direcciones", "facturas", "eventos", "documentos", "tipoCliente", "ciudad" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Factura noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getSerie() {
        return this.serie;
    }

    public Factura serie(String serie) {
        this.setSerie(serie);
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNoFisico() {
        return this.noFisico;
    }

    public Factura noFisico(String noFisico) {
        this.setNoFisico(noFisico);
        return this;
    }

    public void setNoFisico(String noFisico) {
        this.noFisico = noFisico;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Factura fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public Factura subtotal(Double subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getImpuesto() {
        return this.impuesto;
    }

    public Factura impuesto(Double impuesto) {
        this.setImpuesto(impuesto);
        return this;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getImpuestoCero() {
        return this.impuestoCero;
    }

    public Factura impuestoCero(Double impuestoCero) {
        this.setImpuestoCero(impuestoCero);
        return this;
    }

    public void setImpuestoCero(Double impuestoCero) {
        this.impuestoCero = impuestoCero;
    }

    public Double getDescuento() {
        return this.descuento;
    }

    public Factura descuento(Double descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return this.total;
    }

    public Factura total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPorcentajeImpuesto() {
        return this.porcentajeImpuesto;
    }

    public Factura porcentajeImpuesto(Double porcentajeImpuesto) {
        this.setPorcentajeImpuesto(porcentajeImpuesto);
        return this;
    }

    public void setPorcentajeImpuesto(Double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Factura cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Factura direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return this.email;
    }

    public Factura email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return this.estado;
    }

    public Factura estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<DetalleFactura> getDetalles() {
        return this.detalles;
    }

    public void setDetalles(Set<DetalleFactura> detalleFacturas) {
        if (this.detalles != null) {
            this.detalles.forEach(i -> i.setFactura(null));
        }
        if (detalleFacturas != null) {
            detalleFacturas.forEach(i -> i.setFactura(this));
        }
        this.detalles = detalleFacturas;
    }

    public Factura detalles(Set<DetalleFactura> detalleFacturas) {
        this.setDetalles(detalleFacturas);
        return this;
    }

    public Factura addDetalles(DetalleFactura detalleFactura) {
        this.detalles.add(detalleFactura);
        detalleFactura.setFactura(this);
        return this;
    }

    public Factura removeDetalles(DetalleFactura detalleFactura) {
        this.detalles.remove(detalleFactura);
        detalleFactura.setFactura(null);
        return this;
    }

    public Centro getCentro() {
        return this.centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Factura centro(Centro centro) {
        this.setCentro(centro);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return getId() != null && getId().equals(((Factura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
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
            "}";
    }
}
