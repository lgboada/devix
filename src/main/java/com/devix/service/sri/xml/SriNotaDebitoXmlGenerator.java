package com.devix.service.sri.xml;

import com.devix.domain.Compania;
import com.devix.service.sri.SriXmlGenerator;
import com.devix.service.sri.dto.RespuestaAutorizacion;
import org.springframework.stereotype.Service;

/**
 * Generador de XML para Nota de Débito (código SRI: 05).
 * TODO: Implementar cuando se cree la entidad NotaDebito.
 */
@Service
public class SriNotaDebitoXmlGenerator implements SriXmlGenerator {

    @Override
    public String getTipoDocumento() {
        return "NOTA_DEBITO";
    }

    @Override
    public String getCodigoSri() {
        return "05";
    }

    @Override
    public String generarXml(Long documentoId, Compania compania, String claveAcceso) {
        throw new UnsupportedOperationException("Generador de Nota de Débito pendiente de implementación");
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
