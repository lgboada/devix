package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoCatalogo.
 */
@Entity
@Table(name = "tipo_catalogo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoCatalogo implements Serializable {

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

    @NotNull
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoCatalogo")
    @JsonIgnoreProperties(value = { "tipoCatalogo" }, allowSetters = true)
    private Set<Catalogo> catalogos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoCatalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public TipoCatalogo noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public TipoCatalogo descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public TipoCatalogo categoria(String categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Set<Catalogo> getCatalogos() {
        return this.catalogos;
    }

    public void setCatalogos(Set<Catalogo> catalogos) {
        if (this.catalogos != null) {
            this.catalogos.forEach(i -> i.setTipoCatalogo(null));
        }
        if (catalogos != null) {
            catalogos.forEach(i -> i.setTipoCatalogo(this));
        }
        this.catalogos = catalogos;
    }

    public TipoCatalogo catalogos(Set<Catalogo> catalogos) {
        this.setCatalogos(catalogos);
        return this;
    }

    public TipoCatalogo addCatalogo(Catalogo catalogo) {
        this.catalogos.add(catalogo);
        catalogo.setTipoCatalogo(this);
        return this;
    }

    public TipoCatalogo removeCatalogo(Catalogo catalogo) {
        this.catalogos.remove(catalogo);
        catalogo.setTipoCatalogo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoCatalogo)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoCatalogo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoCatalogo{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", categoria='" + getCategoria() + "'" +
            "}";
    }
}
