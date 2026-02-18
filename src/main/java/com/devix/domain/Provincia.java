package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Provincia.
 */
@Entity
@Table(name = "provincia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Provincia implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "provincia")
    @JsonIgnoreProperties(value = { "clientes", "provincia" }, allowSetters = true)
    private Set<Ciudad> ciudads = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "provincias" }, allowSetters = true)
    private Pais pais;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Provincia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Provincia noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Provincia descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Ciudad> getCiudads() {
        return this.ciudads;
    }

    public void setCiudads(Set<Ciudad> ciudads) {
        if (this.ciudads != null) {
            this.ciudads.forEach(i -> i.setProvincia(null));
        }
        if (ciudads != null) {
            ciudads.forEach(i -> i.setProvincia(this));
        }
        this.ciudads = ciudads;
    }

    public Provincia ciudads(Set<Ciudad> ciudads) {
        this.setCiudads(ciudads);
        return this;
    }

    public Provincia addCiudad(Ciudad ciudad) {
        this.ciudads.add(ciudad);
        ciudad.setProvincia(this);
        return this;
    }

    public Provincia removeCiudad(Ciudad ciudad) {
        this.ciudads.remove(ciudad);
        ciudad.setProvincia(null);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Provincia pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Provincia)) {
            return false;
        }
        return getId() != null && getId().equals(((Provincia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Provincia{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
