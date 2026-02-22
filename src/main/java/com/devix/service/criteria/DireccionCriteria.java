package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Direccion} entity. This class is used
 * in {@link com.devix.web.rest.DireccionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /direccions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DireccionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter descripcion;

    private StringFilter telefono;

    private DoubleFilter latitud;

    private DoubleFilter longitud;

    private LongFilter tipoDireccionId;

    private LongFilter clienteId;

    private Boolean distinct;

    public DireccionCriteria() {}

    public DireccionCriteria(DireccionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.latitud = other.optionalLatitud().map(DoubleFilter::copy).orElse(null);
        this.longitud = other.optionalLongitud().map(DoubleFilter::copy).orElse(null);
        this.tipoDireccionId = other.optionalTipoDireccionId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DireccionCriteria copy() {
        return new DireccionCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public DoubleFilter getLatitud() {
        return latitud;
    }

    public Optional<DoubleFilter> optionalLatitud() {
        return Optional.ofNullable(latitud);
    }

    public DoubleFilter latitud() {
        if (latitud == null) {
            setLatitud(new DoubleFilter());
        }
        return latitud;
    }

    public void setLatitud(DoubleFilter latitud) {
        this.latitud = latitud;
    }

    public DoubleFilter getLongitud() {
        return longitud;
    }

    public Optional<DoubleFilter> optionalLongitud() {
        return Optional.ofNullable(longitud);
    }

    public DoubleFilter longitud() {
        if (longitud == null) {
            setLongitud(new DoubleFilter());
        }
        return longitud;
    }

    public void setLongitud(DoubleFilter longitud) {
        this.longitud = longitud;
    }

    public LongFilter getTipoDireccionId() {
        return tipoDireccionId;
    }

    public Optional<LongFilter> optionalTipoDireccionId() {
        return Optional.ofNullable(tipoDireccionId);
    }

    public LongFilter tipoDireccionId() {
        if (tipoDireccionId == null) {
            setTipoDireccionId(new LongFilter());
        }
        return tipoDireccionId;
    }

    public void setTipoDireccionId(LongFilter tipoDireccionId) {
        this.tipoDireccionId = tipoDireccionId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
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
        final DireccionCriteria that = (DireccionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(latitud, that.latitud) &&
            Objects.equals(longitud, that.longitud) &&
            Objects.equals(tipoDireccionId, that.tipoDireccionId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, descripcion, telefono, latitud, longitud, tipoDireccionId, clienteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DireccionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalLatitud().map(f -> "latitud=" + f + ", ").orElse("") +
            optionalLongitud().map(f -> "longitud=" + f + ", ").orElse("") +
            optionalTipoDireccionId().map(f -> "tipoDireccionId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
