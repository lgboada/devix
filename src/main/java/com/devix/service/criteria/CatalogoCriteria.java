package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Catalogo} entity. This class is used
 * in {@link com.devix.web.rest.CatalogoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /catalogos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter descripcion1;

    private StringFilter descripcion2;

    private StringFilter estado;

    private IntegerFilter orden;

    private StringFilter texto1;

    private StringFilter texto2;

    private LongFilter tipoCatalogoId;

    private Boolean distinct;

    public CatalogoCriteria() {}

    public CatalogoCriteria(CatalogoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.descripcion1 = other.optionalDescripcion1().map(StringFilter::copy).orElse(null);
        this.descripcion2 = other.optionalDescripcion2().map(StringFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.orden = other.optionalOrden().map(IntegerFilter::copy).orElse(null);
        this.texto1 = other.optionalTexto1().map(StringFilter::copy).orElse(null);
        this.texto2 = other.optionalTexto2().map(StringFilter::copy).orElse(null);
        this.tipoCatalogoId = other.optionalTipoCatalogoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CatalogoCriteria copy() {
        return new CatalogoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getNoCia() {
        return noCia;
    }

    public Optional<LongFilter> optionalNoCia() {
        return Optional.ofNullable(noCia);
    }

    public LongFilter noCia() {
        if (noCia == null) {
            setNoCia(new LongFilter());
        }
        return noCia;
    }

    public void setNoCia(LongFilter noCia) {
        this.noCia = noCia;
    }

    public StringFilter getDescripcion1() {
        return descripcion1;
    }

    public Optional<StringFilter> optionalDescripcion1() {
        return Optional.ofNullable(descripcion1);
    }

    public StringFilter descripcion1() {
        if (descripcion1 == null) {
            setDescripcion1(new StringFilter());
        }
        return descripcion1;
    }

    public void setDescripcion1(StringFilter descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public StringFilter getDescripcion2() {
        return descripcion2;
    }

    public Optional<StringFilter> optionalDescripcion2() {
        return Optional.ofNullable(descripcion2);
    }

    public StringFilter descripcion2() {
        if (descripcion2 == null) {
            setDescripcion2(new StringFilter());
        }
        return descripcion2;
    }

    public void setDescripcion2(StringFilter descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public Optional<StringFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public StringFilter estado() {
        if (estado == null) {
            setEstado(new StringFilter());
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public IntegerFilter getOrden() {
        return orden;
    }

    public Optional<IntegerFilter> optionalOrden() {
        return Optional.ofNullable(orden);
    }

    public IntegerFilter orden() {
        if (orden == null) {
            setOrden(new IntegerFilter());
        }
        return orden;
    }

    public void setOrden(IntegerFilter orden) {
        this.orden = orden;
    }

    public StringFilter getTexto1() {
        return texto1;
    }

    public Optional<StringFilter> optionalTexto1() {
        return Optional.ofNullable(texto1);
    }

    public StringFilter texto1() {
        if (texto1 == null) {
            setTexto1(new StringFilter());
        }
        return texto1;
    }

    public void setTexto1(StringFilter texto1) {
        this.texto1 = texto1;
    }

    public StringFilter getTexto2() {
        return texto2;
    }

    public Optional<StringFilter> optionalTexto2() {
        return Optional.ofNullable(texto2);
    }

    public StringFilter texto2() {
        if (texto2 == null) {
            setTexto2(new StringFilter());
        }
        return texto2;
    }

    public void setTexto2(StringFilter texto2) {
        this.texto2 = texto2;
    }

    public LongFilter getTipoCatalogoId() {
        return tipoCatalogoId;
    }

    public Optional<LongFilter> optionalTipoCatalogoId() {
        return Optional.ofNullable(tipoCatalogoId);
    }

    public LongFilter tipoCatalogoId() {
        if (tipoCatalogoId == null) {
            setTipoCatalogoId(new LongFilter());
        }
        return tipoCatalogoId;
    }

    public void setTipoCatalogoId(LongFilter tipoCatalogoId) {
        this.tipoCatalogoId = tipoCatalogoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CatalogoCriteria that = (CatalogoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(descripcion1, that.descripcion1) &&
            Objects.equals(descripcion2, that.descripcion2) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(orden, that.orden) &&
            Objects.equals(texto1, that.texto1) &&
            Objects.equals(texto2, that.texto2) &&
            Objects.equals(tipoCatalogoId, that.tipoCatalogoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, descripcion1, descripcion2, estado, orden, texto1, texto2, tipoCatalogoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalDescripcion1().map(f -> "descripcion1=" + f + ", ").orElse("") +
            optionalDescripcion2().map(f -> "descripcion2=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalOrden().map(f -> "orden=" + f + ", ").orElse("") +
            optionalTexto1().map(f -> "texto1=" + f + ", ").orElse("") +
            optionalTexto2().map(f -> "texto2=" + f + ", ").orElse("") +
            optionalTipoCatalogoId().map(f -> "tipoCatalogoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
