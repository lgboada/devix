package com.devix.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TipoCliente.
 */
@Entity
@Table(name = "tipo_cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoCliente implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoCliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public TipoCliente noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public TipoCliente descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoCliente)) {
            return false;
        }
        return getId() != null && getId().equals(((TipoCliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoCliente{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
