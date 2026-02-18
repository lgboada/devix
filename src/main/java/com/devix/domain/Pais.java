package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pais.
 */
@Entity
@Table(name = "pais")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pais implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pais")
    @JsonIgnoreProperties(value = { "ciudads", "pais" }, allowSetters = true)
    private Set<Provincia> provincias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Pais noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Pais descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Provincia> getProvincias() {
        return this.provincias;
    }

    public void setProvincias(Set<Provincia> provincias) {
        if (this.provincias != null) {
            this.provincias.forEach(i -> i.setPais(null));
        }
        if (provincias != null) {
            provincias.forEach(i -> i.setPais(this));
        }
        this.provincias = provincias;
    }

    public Pais provincias(Set<Provincia> provincias) {
        this.setProvincias(provincias);
        return this;
    }

    public Pais addProvincia(Provincia provincia) {
        this.provincias.add(provincia);
        provincia.setPais(this);
        return this;
    }

    public Pais removeProvincia(Provincia provincia) {
        this.provincias.remove(provincia);
        provincia.setPais(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pais)) {
            return false;
        }
        return getId() != null && getId().equals(((Pais) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pais{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
