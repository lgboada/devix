package com.devix.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.devix.domain.FacturaLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Long noCia;

    /** FACTURA | NOTA_CREDITO | RETENCION | GUIA_REMISION | LIQUIDACION_COMPRA */
    @NotNull
    private String tipoDocumento;

    @NotNull
    private Long documentoId;

    @NotNull
    private Instant fechaIntento;

    /** RECEPCION | AUTORIZACION */
    @NotNull
    private String tipoAccion;

    private String claveAcceso;

    /** RECIBIDA | AUTORIZADA | NO_AUTORIZADA | DEVUELTA | ERROR_SISTEMA */
    private String estado;

    private String numeroAutorizacion;

    private Instant fechaAutorizacion;

    private String xmlFirmado;

    private String mensajesSri;

    private Integer ambiente;

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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    public Instant getFechaIntento() {
        return fechaIntento;
    }

    public void setFechaIntento(Instant fechaIntento) {
        this.fechaIntento = fechaIntento;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Instant getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Instant fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getXmlFirmado() {
        return xmlFirmado;
    }

    public void setXmlFirmado(String xmlFirmado) {
        this.xmlFirmado = xmlFirmado;
    }

    public String getMensajesSri() {
        return mensajesSri;
    }

    public void setMensajesSri(String mensajesSri) {
        this.mensajesSri = mensajesSri;
    }

    public Integer getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Integer ambiente) {
        this.ambiente = ambiente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacturaLogDTO)) return false;
        FacturaLogDTO that = (FacturaLogDTO) o;
        if (this.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaLogDTO{" +
            "id=" + getId() +
            ", noCia=" + getNoCia() +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", documentoId=" + getDocumentoId() +
            ", fechaIntento='" + getFechaIntento() + "'" +
            ", tipoAccion='" + getTipoAccion() + "'" +
            ", claveAcceso='" + getClaveAcceso() + "'" +
            ", estado='" + getEstado() + "'" +
            ", numeroAutorizacion='" + getNumeroAutorizacion() + "'" +
            ", fechaAutorizacion='" + getFechaAutorizacion() + "'" +
            ", ambiente=" + getAmbiente() +
            "}";
    }
}
