package com.devix.service.sri;

import com.devix.domain.Compania;
import com.devix.service.sri.dto.RespuestaAutorizacion;

/**
 * Contrato que debe implementar cada tipo de comprobante electrónico SRI.
 * SriEnvioService selecciona la implementación correcta por getTipoDocumento().
 */
public interface SriXmlGenerator {
    /**
     * Identificador del tipo de documento, debe coincidir con factura_log.tipo_documento.
     * Valores: FACTURA, NOTA_CREDITO, NOTA_DEBITO, GUIA_REMISION, RETENCION, LIQUIDACION_COMPRA
     */
    String getTipoDocumento();

    /**
     * Código de 2 dígitos usado por el SRI en la clave de acceso.
     * 01=Factura, 03=Liquidación, 04=Nota Crédito, 05=Nota Débito, 06=Guía, 07=Retención
     */
    String getCodigoSri();

    /**
     * Genera el XML del comprobante sin firmar.
     *
     * @param documentoId ID del documento en su tabla de origen
     * @param compania    datos del emisor
     * @param claveAcceso clave de acceso de 49 dígitos ya generada
     * @return XML como String (sin firma)
     */
    String generarXml(Long documentoId, Compania compania, String claveAcceso);

    /**
     * Recupera los datos necesarios para generar la clave de acceso (serie y secuencial).
     *
     * @return array de 2 elementos: [serie (6 dígitos), secuencial]
     */
    String[] getSerieYSecuencial(Long documentoId);

    /**
     * Recupera la fecha de emisión del documento.
     */
    java.time.Instant getFechaEmision(Long documentoId);

    /**
     * Actualiza la entidad de origen con los datos de autorización del SRI.
     */
    void actualizarAutorizacion(Long documentoId, String claveAcceso, RespuestaAutorizacion respuesta);
}
