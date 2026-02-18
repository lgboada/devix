package com.devix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Catalogo.
 */
@Entity
@Table(name = "catalogo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalogo implements Serializable {

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
    @Column(name = "descripcion_1", nullable = false)
    private String descripcion1;

    @Column(name = "descripcion_2")
    private String descripcion2;

    @Column(name = "estado")
    private String estado;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "texto_1")
    private String texto1;

    @Column(name = "texto_2")
    private String texto2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "catalogos" }, allowSetters = true)
    private TipoCatalogo tipoCatalogo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public Catalogo noCia(Long noCia) {
        this.setNoCia(noCia);
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion1() {
        return this.descripcion1;
    }

    public Catalogo descripcion1(String descripcion1) {
        this.setDescripcion1(descripcion1);
        return this;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getDescripcion2() {
        return this.descripcion2;
    }

    public Catalogo descripcion2(String descripcion2) {
        this.setDescripcion2(descripcion2);
        return this;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public String getEstado() {
        return this.estado;
    }

    public Catalogo estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public Catalogo orden(Integer orden) {
        this.setOrden(orden);
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTexto1() {
        return this.texto1;
    }

    public Catalogo texto1(String texto1) {
        this.setTexto1(texto1);
        return this;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return this.texto2;
    }

    public Catalogo texto2(String texto2) {
        this.setTexto2(texto2);
        return this;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public TipoCatalogo getTipoCatalogo() {
        return this.tipoCatalogo;
    }

    public void setTipoCatalogo(TipoCatalogo tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public Catalogo tipoCatalogo(TipoCatalogo tipoCatalogo) {
        this.setTipoCatalogo(tipoCatalogo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogo)) {
            return false;
        }
        return getId() != null && getId().equals(((Catalogo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogo{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion1='" + getDescripcion1() + "'" +
            ", descripcion2='" + getDescripcion2() + "'" +
            ", estado='" + getEstado() + "'" +
            ", orden=" + getOrden() +
            ", texto1='" + getTexto1() + "'" +
            ", texto2='" + getTexto2() + "'" +
            "}";
    }
}
