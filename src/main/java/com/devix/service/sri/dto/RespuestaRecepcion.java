package com.devix.service.sri.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Respuesta del WebService de Recepción del SRI.
 * estado: RECIBIDA | DEVUELTA
 */
public class RespuestaRecepcion {

    private String estado;
    private List<String> mensajes = new ArrayList<>();

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
