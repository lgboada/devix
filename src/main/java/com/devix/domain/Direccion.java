package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Direccion.
 */
@Entity
@Table(name = "direccion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Direccion implements Serializable {

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
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "direccions" }, allowSetters = true)
    private TipoDireccion tipoDireccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "direcciones", "facturas", "eventos", "documentos", "tipoCliente", "ciudad" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Direccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Direccion noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Direccion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Direccion telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getLatitud() {
        return this.latitud;
    }

    public Direccion latitud(Double latitud) {
        this.setLatitud(latitud);
        return this;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return this.longitud;
    }

    public Direccion longitud(Double longitud) {
        this.setLongitud(longitud);
        return this;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public TipoDireccion getTipoDireccion() {
        return this.tipoDireccion;
    }

    public void setTipoDireccion(TipoDireccion tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public Direccion tipoDireccion(TipoDireccion tipoDireccion) {
        this.setTipoDireccion(tipoDireccion);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Direccion cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Direccion)) {
            return false;
        }
        return getId() != null && getId().equals(((Direccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Direccion{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            "}";
    }
}
