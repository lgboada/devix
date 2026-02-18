package com.devix.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.devix.domain.Evento} entity. This class is used
 * in {@link com.devix.web.rest.EventoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eventos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter noCia;

    private StringFilter descripcion;

    private InstantFilter fecha;

    private StringFilter estado;

    private StringFilter motivoConsulta;

    private StringFilter tratamiento;

    private StringFilter indicaciones;

    private StringFilter diagnostico1;

    private StringFilter diagnostico2;

    private StringFilter diagnostico3;

    private StringFilter diagnostico4;

    private StringFilter diagnostico5;

    private StringFilter diagnostico6;

    private StringFilter diagnostico7;

    private StringFilter observacion;

    private LongFilter documentoId;

    private LongFilter tipoEventoId;

    private LongFilter centroId;

    private LongFilter clienteId;

    private Boolean distinct;

    public EventoCriteria() {}

    public EventoCriteria(EventoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noCia = other.optionalNoCia().map(LongFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(InstantFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.motivoConsulta = other.optionalMotivoConsulta().map(StringFilter::copy).orElse(null);
        this.tratamiento = other.optionalTratamiento().map(StringFilter::copy).orElse(null);
        this.indicaciones = other.optionalIndicaciones().map(StringFilter::copy).orElse(null);
        this.diagnostico1 = other.optionalDiagnostico1().map(StringFilter::copy).orElse(null);
        this.diagnostico2 = other.optionalDiagnostico2().map(StringFilter::copy).orElse(null);
        this.diagnostico3 = other.optionalDiagnostico3().map(StringFilter::copy).orElse(null);
        this.diagnostico4 = other.optionalDiagnostico4().map(StringFilter::copy).orElse(null);
        this.diagnostico5 = other.optionalDiagnostico5().map(StringFilter::copy).orElse(null);
        this.diagnostico6 = other.optionalDiagnostico6().map(StringFilter::copy).orElse(null);
        this.diagnostico7 = other.optionalDiagnostico7().map(StringFilter::copy).orElse(null);
        this.observacion = other.optionalObservacion().map(StringFilter::copy).orElse(null);
        this.documentoId = other.optionalDocumentoId().map(LongFilter::copy).orElse(null);
        this.tipoEventoId = other.optionalTipoEventoId().map(LongFilter::copy).orElse(null);
        this.centroId = other.optionalCentroId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventoCriteria copy() {
        return new EventoCriteria(this);
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

    public InstantFilter getFecha() {
        return fecha;
    }

    public Optional<InstantFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            setFecha(new InstantFilter());
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
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

    public StringFilter getMotivoConsulta() {
        return motivoConsulta;
    }

    public Optional<StringFilter> optionalMotivoConsulta() {
        return Optional.ofNullable(motivoConsulta);
    }

    public StringFilter motivoConsulta() {
        if (motivoConsulta == null) {
            setMotivoConsulta(new StringFilter());
        }
        return motivoConsulta;
    }

    public void setMotivoConsulta(StringFilter motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public StringFilter getTratamiento() {
        return tratamiento;
    }

    public Optional<StringFilter> optionalTratamiento() {
        return Optional.ofNullable(tratamiento);
    }

    public StringFilter tratamiento() {
        if (tratamiento == null) {
            setTratamiento(new StringFilter());
        }
        return tratamiento;
    }

    public void setTratamiento(StringFilter tratamiento) {
        this.tratamiento = tratamiento;
    }

    public StringFilter getIndicaciones() {
        return indicaciones;
    }

    public Optional<StringFilter> optionalIndicaciones() {
        return Optional.ofNullable(indicaciones);
    }

    public StringFilter indicaciones() {
        if (indicaciones == null) {
            setIndicaciones(new StringFilter());
        }
        return indicaciones;
    }

    public void setIndicaciones(StringFilter indicaciones) {
        this.indicaciones = indicaciones;
    }

    public StringFilter getDiagnostico1() {
        return diagnostico1;
    }

    public Optional<StringFilter> optionalDiagnostico1() {
        return Optional.ofNullable(diagnostico1);
    }

    public StringFilter diagnostico1() {
        if (diagnostico1 == null) {
            setDiagnostico1(new StringFilter());
        }
        return diagnostico1;
    }

    public void setDiagnostico1(StringFilter diagnostico1) {
        this.diagnostico1 = diagnostico1;
    }

    public StringFilter getDiagnostico2() {
        return diagnostico2;
    }

    public Optional<StringFilter> optionalDiagnostico2() {
        return Optional.ofNullable(diagnostico2);
    }

    public StringFilter diagnostico2() {
        if (diagnostico2 == null) {
            setDiagnostico2(new StringFilter());
        }
        return diagnostico2;
    }

    public void setDiagnostico2(StringFilter diagnostico2) {
        this.diagnostico2 = diagnostico2;
    }

    public StringFilter getDiagnostico3() {
        return diagnostico3;
    }

    public Optional<StringFilter> optionalDiagnostico3() {
        return Optional.ofNullable(diagnostico3);
    }

    public StringFilter diagnostico3() {
        if (diagnostico3 == null) {
            setDiagnostico3(new StringFilter());
        }
        return diagnostico3;
    }

    public void setDiagnostico3(StringFilter diagnostico3) {
        this.diagnostico3 = diagnostico3;
    }

    public StringFilter getDiagnostico4() {
        return diagnostico4;
    }

    public Optional<StringFilter> optionalDiagnostico4() {
        return Optional.ofNullable(diagnostico4);
    }

    public StringFilter diagnostico4() {
        if (diagnostico4 == null) {
            setDiagnostico4(new StringFilter());
        }
        return diagnostico4;
    }

    public void setDiagnostico4(StringFilter diagnostico4) {
        this.diagnostico4 = diagnostico4;
    }

    public StringFilter getDiagnostico5() {
        return diagnostico5;
    }

    public Optional<StringFilter> optionalDiagnostico5() {
        return Optional.ofNullable(diagnostico5);
    }

    public StringFilter diagnostico5() {
        if (diagnostico5 == null) {
            setDiagnostico5(new StringFilter());
        }
        return diagnostico5;
    }

    public void setDiagnostico5(StringFilter diagnostico5) {
        this.diagnostico5 = diagnostico5;
    }

    public StringFilter getDiagnostico6() {
        return diagnostico6;
    }

    public Optional<StringFilter> optionalDiagnostico6() {
        return Optional.ofNullable(diagnostico6);
    }

    public StringFilter diagnostico6() {
        if (diagnostico6 == null) {
            setDiagnostico6(new StringFilter());
        }
        return diagnostico6;
    }

    public void setDiagnostico6(StringFilter diagnostico6) {
        this.diagnostico6 = diagnostico6;
    }

    public StringFilter getDiagnostico7() {
        return diagnostico7;
    }

    public Optional<StringFilter> optionalDiagnostico7() {
        return Optional.ofNullable(diagnostico7);
    }

    public StringFilter diagnostico7() {
        if (diagnostico7 == null) {
            setDiagnostico7(new StringFilter());
        }
        return diagnostico7;
    }

    public void setDiagnostico7(StringFilter diagnostico7) {
        this.diagnostico7 = diagnostico7;
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

    public LongFilter getDocumentoId() {
        return documentoId;
    }

    public Optional<LongFilter> optionalDocumentoId() {
        return Optional.ofNullable(documentoId);
    }

    public LongFilter documentoId() {
        if (documentoId == null) {
            setDocumentoId(new LongFilter());
        }
        return documentoId;
    }

    public void setDocumentoId(LongFilter documentoId) {
        this.documentoId = documentoId;
    }

    public LongFilter getTipoEventoId() {
        return tipoEventoId;
    }

    public Optional<LongFilter> optionalTipoEventoId() {
        return Optional.ofNullable(tipoEventoId);
    }

    public LongFilter tipoEventoId() {
        if (tipoEventoId == null) {
            setTipoEventoId(new LongFilter());
        }
        return tipoEventoId;
    }

    public void setTipoEventoId(LongFilter tipoEventoId) {
        this.tipoEventoId = tipoEventoId;
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
        final EventoCriteria that = (EventoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noCia, that.noCia) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(motivoConsulta, that.motivoConsulta) &&
            Objects.equals(tratamiento, that.tratamiento) &&
            Objects.equals(indicaciones, that.indicaciones) &&
            Objects.equals(diagnostico1, that.diagnostico1) &&
            Objects.equals(diagnostico2, that.diagnostico2) &&
            Objects.equals(diagnostico3, that.diagnostico3) &&
            Objects.equals(diagnostico4, that.diagnostico4) &&
            Objects.equals(diagnostico5, that.diagnostico5) &&
            Objects.equals(diagnostico6, that.diagnostico6) &&
            Objects.equals(diagnostico7, that.diagnostico7) &&
            Objects.equals(observacion, that.observacion) &&
            Objects.equals(documentoId, that.documentoId) &&
            Objects.equals(tipoEventoId, that.tipoEventoId) &&
            Objects.equals(centroId, that.centroId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noCia,
            descripcion,
            fecha,
            estado,
            motivoConsulta,
            tratamiento,
            indicaciones,
            diagnostico1,
            diagnostico2,
            diagnostico3,
            diagnostico4,
            diagnostico5,
            diagnostico6,
            diagnostico7,
            observacion,
            documentoId,
            tipoEventoId,
            centroId,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoCia().map(f -> "noCia=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalMotivoConsulta().map(f -> "motivoConsulta=" + f + ", ").orElse("") +
            optionalTratamiento().map(f -> "tratamiento=" + f + ", ").orElse("") +
            optionalIndicaciones().map(f -> "indicaciones=" + f + ", ").orElse("") +
            optionalDiagnostico1().map(f -> "diagnostico1=" + f + ", ").orElse("") +
            optionalDiagnostico2().map(f -> "diagnostico2=" + f + ", ").orElse("") +
            optionalDiagnostico3().map(f -> "diagnostico3=" + f + ", ").orElse("") +
            optionalDiagnostico4().map(f -> "diagnostico4=" + f + ", ").orElse("") +
            optionalDiagnostico5().map(f -> "diagnostico5=" + f + ", ").orElse("") +
            optionalDiagnostico6().map(f -> "diagnostico6=" + f + ", ").orElse("") +
            optionalDiagnostico7().map(f -> "diagnostico7=" + f + ", ").orElse("") +
            optionalObservacion().map(f -> "observacion=" + f + ", ").orElse("") +
            optionalDocumentoId().map(f -> "documentoId=" + f + ", ").orElse("") +
            optionalTipoEventoId().map(f -> "tipoEventoId=" + f + ", ").orElse("") +
            optionalCentroId().map(f -> "centroId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
