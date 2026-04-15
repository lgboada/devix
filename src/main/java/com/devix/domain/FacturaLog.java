package com.devix.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Registro de cada intento de envío/autorización de un comprobante electrónico al SRI.
 * Aplica para: FACTURA, NOTA_CREDITO, RETENCION, GUIA_REMISION, LIQUIDACION_COMPRA.
 */
@Entity
@Table(name = "factura_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facturaLogSequenceGenerator")
    @SequenceGenerator(name = "facturaLogSequenceGenerator", sequenceName = "factura_log_sequence", allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "no_cia", nullable = false)
    private Long noCia;

    /**
     * FACTURA | NOTA_CREDITO | RETENCION | GUIA_REMISION | LIQUIDACION_COMPRA
     */
    @NotNull
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private String tipoDocumento;

    /**
     * ID del comprobante en su tabla de origen (sin FK, aplica a varios tipos)
     */
    @NotNull
    @Column(name = "documento_id", nullable = false)
    private Long documentoId;

    @NotNull
    @Column(name = "fecha_intento", nullable = false)
    private Instant fechaIntento;

    /**
     * RECEPCION | AUTORIZACION
     */
    @NotNull
    @Column(name = "tipo_accion", nullable = false, length = 20)
    private String tipoAccion;

    @Column(name = "clave_acceso", length = 49)
    private String claveAcceso;

    /**
     * RECIBIDA | AUTORIZADA | NO_AUTORIZADA | DEVUELTA | ERROR_SISTEMA
     */
    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "numero_autorizacion", length = 37)
    private String numeroAutorizacion;

    @Column(name = "fecha_autorizacion")
    private Instant fechaAutorizacion;

    @Column(name = "xml_firmado", columnDefinition = "TEXT")
    private String xmlFirmado;

    @Column(name = "mensajes_sri", length = 2000)
    private String mensajesSri;

    @Column(name = "ambiente")
    private Integer ambiente;

    // getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return noCia;
    }

    public FacturaLog noCia(Long noCia) {
        this.noCia = noCia;
        return this;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public FacturaLog tipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getDocumentoId() {
        return documentoId;
    }

    public FacturaLog documentoId(Long documentoId) {
        this.documentoId = documentoId;
        return this;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    public Instant getFechaIntento() {
        return fechaIntento;
    }

    public FacturaLog fechaIntento(Instant fechaIntento) {
        this.fechaIntento = fechaIntento;
        return this;
    }

    public void setFechaIntento(Instant fechaIntento) {
        this.fechaIntento = fechaIntento;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public FacturaLog tipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
        return this;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public FacturaLog claveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
        return this;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getEstado() {
        return estado;
    }

    public FacturaLog estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public FacturaLog numeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
        return this;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Instant getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public FacturaLog fechaAutorizacion(Instant fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
        return this;
    }

    public void setFechaAutorizacion(Instant fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getXmlFirmado() {
        return xmlFirmado;
    }

    public FacturaLog xmlFirmado(String xmlFirmado) {
        this.xmlFirmado = xmlFirmado;
        return this;
    }

    public void setXmlFirmado(String xmlFirmado) {
        this.xmlFirmado = xmlFirmado;
    }

    public String getMensajesSri() {
        return mensajesSri;
    }

    public FacturaLog mensajesSri(String mensajesSri) {
        this.mensajesSri = mensajesSri;
        return this;
    }

    public void setMensajesSri(String mensajesSri) {
        this.mensajesSri = mensajesSri;
    }

    public Integer getAmbiente() {
        return ambiente;
    }

    public FacturaLog ambiente(Integer ambiente) {
        this.ambiente = ambiente;
        return this;
    }

    public void setAmbiente(Integer ambiente) {
        this.ambiente = ambiente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacturaLog)) return false;
        return getId() != null && getId().equals(((FacturaLog) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaLog{" +
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
