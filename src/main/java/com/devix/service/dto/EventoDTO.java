package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Evento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    @NotNull
    private String descripcion;

    @NotNull
    private Instant fecha;

    private String estado;

    private String motivoConsulta;

    private String tratamiento;

    private String indicaciones;

    private String diagnostico1;

    private String diagnostico2;

    private String diagnostico3;

    private String diagnostico4;

    private String diagnostico5;

    private String diagnostico6;

    private String diagnostico7;

    private String observacion;

    private TipoEventoDTO tipoEvento;

    private CentroDTO centro;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDiagnostico1() {
        return diagnostico1;
    }

    public void setDiagnostico1(String diagnostico1) {
        this.diagnostico1 = diagnostico1;
    }

    public String getDiagnostico2() {
        return diagnostico2;
    }

    public void setDiagnostico2(String diagnostico2) {
        this.diagnostico2 = diagnostico2;
    }

    public String getDiagnostico3() {
        return diagnostico3;
    }

    public void setDiagnostico3(String diagnostico3) {
        this.diagnostico3 = diagnostico3;
    }

    public String getDiagnostico4() {
        return diagnostico4;
    }

    public void setDiagnostico4(String diagnostico4) {
        this.diagnostico4 = diagnostico4;
    }

    public String getDiagnostico5() {
        return diagnostico5;
    }

    public void setDiagnostico5(String diagnostico5) {
        this.diagnostico5 = diagnostico5;
    }

    public String getDiagnostico6() {
        return diagnostico6;
    }

    public void setDiagnostico6(String diagnostico6) {
        this.diagnostico6 = diagnostico6;
    }

    public String getDiagnostico7() {
        return diagnostico7;
    }

    public void setDiagnostico7(String diagnostico7) {
        this.diagnostico7 = diagnostico7;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public TipoEventoDTO getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEventoDTO tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventoDTO)) {
            return false;
        }

        EventoDTO eventoDTO = (EventoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", estado='" + getEstado() + "'" +
            ", motivoConsulta='" + getMotivoConsulta() + "'" +
            ", tratamiento='" + getTratamiento() + "'" +
            ", indicaciones='" + getIndicaciones() + "'" +
            ", diagnostico1='" + getDiagnostico1() + "'" +
            ", diagnostico2='" + getDiagnostico2() + "'" +
            ", diagnostico3='" + getDiagnostico3() + "'" +
            ", diagnostico4='" + getDiagnostico4() + "'" +
            ", diagnostico5='" + getDiagnostico5() + "'" +
            ", diagnostico6='" + getDiagnostico6() + "'" +
            ", diagnostico7='" + getDiagnostico7() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", tipoEvento=" + getTipoEvento() +
            ", centro=" + getCentro() +
            ", cliente=" + getCliente() +
            "}";
    }
}
