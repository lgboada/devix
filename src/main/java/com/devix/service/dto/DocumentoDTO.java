package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.Documento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentoDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    private String tipo;

    private String observacion;

    private Instant fechaCreacion;

    private Instant fechaVencimiento;

    @NotNull
    private String path;

    private ClienteDTO cliente;

    private EventoDTO evento;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Instant fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public EventoDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoDTO evento) {
        this.evento = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoDTO)) {
            return false;
        }

        DocumentoDTO documentoDTO = (DocumentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", tipo='" + getTipo() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", path='" + getPath() + "'" +
            ", cliente=" + getCliente() +
            ", evento=" + getEvento() +
            "}";
    }
}
