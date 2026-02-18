package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Documento} entity. This class is used
 * in {@link com.devix.web.rest.DocumentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter tipo;

    private StringFilter observacion;

    private InstantFilter fechaCreacion;

    private InstantFilter fechaVencimiento;

    private StringFilter path;

    private LongFilter clienteId;

    private LongFilter eventoId;

    private Boolean distinct;

    public DocumentoCriteria() {}

    public DocumentoCriteria(DocumentoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(StringFilter::copy).orElse(null);
        this.observacion = other.optionalObservacion().map(StringFilter::copy).orElse(null);
        this.fechaCreacion = other.optionalFechaCreacion().map(InstantFilter::copy).orElse(null);
        this.fechaVencimiento = other.optionalFechaVencimiento().map(InstantFilter::copy).orElse(null);
        this.path = other.optionalPath().map(StringFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.eventoId = other.optionalEventoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentoCriteria copy() {
        return new DocumentoCriteria(this);
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

    public StringFilter getTipo() {
        return tipo;
    }

    public Optional<StringFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public StringFilter tipo() {
        if (tipo == null) {
            setTipo(new StringFilter());
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getObservacion() {
        return observacion;
    }

    public Optional<StringFilter> optionalObservacion() {
        return Optional.ofNullable(observacion);
    }

    public StringFilter observacion() {
        if (observacion == null) {
            setObservacion(new StringFilter());
        }
        return observacion;
    }

    public void setObservacion(StringFilter observacion) {
        this.observacion = observacion;
    }

    public InstantFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public Optional<InstantFilter> optionalFechaCreacion() {
        return Optional.ofNullable(fechaCreacion);
    }

    public InstantFilter fechaCreacion() {
        if (fechaCreacion == null) {
            setFechaCreacion(new InstantFilter());
        }
        return fechaCreacion;
    }

    public void setFechaCreacion(InstantFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public InstantFilter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Optional<InstantFilter> optionalFechaVencimiento() {
        return Optional.ofNullable(fechaVencimiento);
    }

    public InstantFilter fechaVencimiento() {
        if (fechaVencimiento == null) {
            setFechaVencimiento(new InstantFilter());
        }
        return fechaVencimiento;
    }

    public void setFechaVencimiento(InstantFilter fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public StringFilter getPath() {
        return path;
    }

    public Optional<StringFilter> optionalPath() {
        return Optional.ofNullable(path);
    }

    public StringFilter path() {
        if (path == null) {
            setPath(new StringFilter());
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
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

    public LongFilter getEventoId() {
        return eventoId;
    }

    public Optional<LongFilter> optionalEventoId() {
        return Optional.ofNullable(eventoId);
    }

    public LongFilter eventoId() {
        if (eventoId == null) {
            setEventoId(new LongFilter());
        }
        return eventoId;
    }

    public void setEventoId(LongFilter eventoId) {
        this.eventoId = eventoId;
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
        final DocumentoCriteria that = (DocumentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(observacion, that.observacion) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
            Objects.equals(path, that.path) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(eventoId, that.eventoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noCia, tipo, observacion, fechaCreacion, fechaVencimiento, path, clienteId, eventoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalObservacion().map(f -> "observacion=" + f + ", ").orElse("") +
            optionalFechaCreacion().map(f -> "fechaCreacion=" + f + ", ").orElse("") +
            optionalFechaVencimiento().map(f -> "fechaVencimiento=" + f + ", ").orElse("") +
            optionalPath().map(f -> "path=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalEventoId().map(f -> "eventoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
