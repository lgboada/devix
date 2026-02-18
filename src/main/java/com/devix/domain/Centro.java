package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Centro.
 */
@Entity
@Table(name = "centro")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Centro implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centro")
    @JsonIgnoreProperties(value = { "detalles", "centro", "cliente" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centro")
    @JsonIgnoreProperties(value = { "documentos", "tipoEvento", "centro", "cliente" }, allowSetters = true)
    private Set<Evento> eventos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "centros" }, allowSetters = true)
    private Compania compania;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Centro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Centro noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Centro descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setCentro(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setCentro(this));
        }
        this.facturas = facturas;
    }

    public Centro facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Centro addFactura(Factura factura) {
        this.facturas.add(factura);
        factura.setCentro(this);
        return this;
    }

    public Centro removeFactura(Factura factura) {
        this.facturas.remove(factura);
        factura.setCentro(null);
        return this;
    }

    public Set<Evento> getEventos() {
        return this.eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        if (this.eventos != null) {
            this.eventos.forEach(i -> i.setCentro(null));
        }
        if (eventos != null) {
            eventos.forEach(i -> i.setCentro(this));
        }
        this.eventos = eventos;
    }

    public Centro eventos(Set<Evento> eventos) {
        this.setEventos(eventos);
        return this;
    }

    public Centro addEvento(Evento evento) {
        this.eventos.add(evento);
        evento.setCentro(this);
        return this;
    }

    public Centro removeEvento(Evento evento) {
        this.eventos.remove(evento);
        evento.setCentro(null);
        return this;
    }

    public Compania getCompania() {
        return this.compania;
    }

    public void setCompania(Compania compania) {
        this.compania = compania;
    }

    public Centro compania(Compania compania) {
        this.setCompania(compania);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Centro)) {
            return false;
        }
        return getId() != null && getId().equals(((Centro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Centro{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
