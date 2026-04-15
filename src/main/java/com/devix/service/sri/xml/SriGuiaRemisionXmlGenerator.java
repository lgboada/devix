package com.devix.service.sri.xml;

import com.devix.domain.Compania;
import com.devix.service.sri.SriXmlGenerator;
import com.devix.service.sri.dto.RespuestaAutorizacion;
import org.springframework.stereotype.Service;

/**
 * Generador de XML para Guía de Remisión (código SRI: 06).
 * TODO: Implementar cuando se cree la entidad GuiaRemision.
 */
@Service
public class SriGuiaRemisionXmlGenerator implements SriXmlGenerator {

    @Override
    public String getTipoDocumento() {
        return "GUIA_REMISION";
    }

    @Override
    public String getCodigoSri() {
        return "06";
    }

    @Override
    public String generarXml(Long documentoId, Compania compania, String claveAcceso) {
        throw new UnsupportedOperationException("Generador de Guía de Remisión pendiente de implementación");
    }

    @Override
    public String[] getSerieYSecuencial(Long documentoId) {
        throw new UnsupportedOperationException("Pendiente de implementación");
    }

    @Override
    public java.time.Instant getFechaEmision(Long documentoId) {
        throw new UnsupportedOperationException("Pendiente de implementación");
    }

    @Override
    public void actualizarAutorizacion(Long documentoId, String claveAcceso, RespuestaAutorizacion respuesta) {
        throw new UnsupportedOperationException("Pendiente de implementación");
    }
}
