package com.devix.service.sri.xml;

import com.devix.domain.Compania;
import com.devix.service.sri.SriXmlGenerator;
import com.devix.service.sri.dto.RespuestaAutorizacion;
import org.springframework.stereotype.Service;

/**
 * Generador de XML para Liquidación de Compra de Bienes y Prestación de Servicios (código SRI: 03).
 * TODO: Implementar cuando se cree la entidad LiquidacionCompra.
 */
@Service
public class SriLiquidacionCompraXmlGenerator implements SriXmlGenerator {

    @Override
    public String getTipoDocumento() {
        return "LIQUIDACION_COMPRA";
    }

    @Override
    public String getCodigoSri() {
        return "03";
    }

    @Override
    public String generarXml(Long documentoId, Compania compania, String claveAcceso) {
        throw new UnsupportedOperationException("Generador de Liquidación de Compra pendiente de implementación");
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
