package com.devix.service.sri.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Respuesta del WebService de Autorización del SRI.
 * estado: AUTORIZADO | NO AUTORIZADO
 */
public class RespuestaAutorizacion {

    private String estado;
    private String numeroAutorizacion;
    private String fechaAutorizacion;
    private List<String> mensajes = new ArrayList<>();

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

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<String> mensajes) {
        this.mensajes = mensajes;
    }

    public String getMensajesAsString() {
        return String.join(" | ", mensajes);
    }
}
