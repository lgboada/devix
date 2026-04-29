package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria for {@link com.devix.domain.Bodega}.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BodegaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter codigo;

    private StringFilter nombre;

    private BooleanFilter activa;

    private LongFilter centroId;

    private Boolean distinct;

    public BodegaCriteria() {}

    public BodegaCriteria(BodegaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.activa = other.optionalActiva().map(BooleanFilter::copy).orElse(null);
        this.centroId = other.optionalCentroId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BodegaCriteria copy() {
        return new BodegaCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public Optional<StringFilter> optionalCodigo() {
        return Optional.ofNullable(codigo);
    }

    public StringFilter codigo() {
        if (codigo == null) {
            setCodigo(new StringFilter());
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public BooleanFilter getActiva() {
        return activa;
    }

    public Optional<BooleanFilter> optionalActiva() {
        return Optional.ofNullable(activa);
    }

    public BooleanFilter activa() {
        if (activa == null) {
            setActiva(new BooleanFilter());
        }
        return activa;
    }

    public void setActiva(BooleanFilter activa) {
        this.activa = activa;
    }

    public LongFilter getCentroId() {
        return centroId;
    }

    public Optional<LongFilter> optionalCentroId() {
        return Optional.ofNullable(centroId);
    }

    public LongFilter centroId() {
        if (centroId == null) {
            setCentroId(new LongFilter());
        }
        return centroId;
    }

    public void setCentroId(LongFilter centroId) {
        this.centroId = centroId;
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
        final BodegaCriteria that = (BodegaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(activa, that.activa) &&
            Objects.equals(centroId, that.centroId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, codigo, nombre, activa, centroId, distinct);
    }

    @Override
    public String toString() {
        return (
            "BodegaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalActiva().map(f -> "activa=" + f + ", ").orElse("") +
            optionalCentroId().map(f -> "centroId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}"
        );
    }
}
