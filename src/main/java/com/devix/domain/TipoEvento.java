package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoEvento.
 */
@Entity
@Table(name = "tipo_evento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoEvento implements Serializable {

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
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoEvento")
    @JsonIgnoreProperties(value = { "documentos", "tipoEvento", "centro", "cliente" }, allowSetters = true)
    private Set<Evento> eventos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoEvento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public TipoEvento noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoEvento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Evento> getEventos() {
        return this.eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        if (this.eventos != null) {
            this.eventos.forEach(i -> i.setTipoEvento(null));
        }
        if (eventos != null) {
            eventos.forEach(i -> i.setTipoEvento(this));
        }
        this.eventos = eventos;
    }

    public TipoEvento eventos(Set<Evento> eventos) {
        this.setEventos(eventos);
        return this;
    }

    public TipoEvento addEvento(Evento evento) {
        this.eventos.add(evento);
        evento.setTipoEvento(this);
        return this;
    }

    public TipoEvento removeEvento(Evento evento) {
        this.eventos.remove(evento);
        evento.setTipoEvento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoEvento)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoEvento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoEvento{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
