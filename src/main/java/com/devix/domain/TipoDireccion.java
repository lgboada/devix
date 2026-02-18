package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoDireccion.
 */
@Entity
@Table(name = "tipo_direccion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoDireccion implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDireccion")
    @JsonIgnoreProperties(value = { "tipoDireccion", "cliente" }, allowSetters = true)
    private Set<Direccion> direccions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoDireccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public TipoDireccion noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public TipoDireccion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Direccion> getDireccions() {
        return this.direccions;
    }

    public void setDireccions(Set<Direccion> direccions) {
        if (this.direccions != null) {
            this.direccions.forEach(i -> i.setTipoDireccion(null));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.setTipoDireccion(this));
        }
        this.direccions = direccions;
    }

    public TipoDireccion direccions(Set<Direccion> direccions) {
        this.setDireccions(direccions);
        return this;
    }

    public TipoDireccion addDireccion(Direccion direccion) {
        this.direccions.add(direccion);
        direccion.setTipoDireccion(this);
        return this;
    }

    public TipoDireccion removeDireccion(Direccion direccion) {
        this.direccions.remove(direccion);
        direccion.setTipoDireccion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoDireccion)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoDireccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoDireccion{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
