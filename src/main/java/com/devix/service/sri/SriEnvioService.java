package com.devix.service.sri;

import com.devix.domain.Compania;
import com.devix.domain.FacturaLog;
import com.devix.repository.CompaniaRepository;
import com.devix.service.FacturaLogService;
import com.devix.service.dto.FacturaLogDTO;
import com.devix.service.sri.dto.RespuestaAutorizacion;
import com.devix.service.sri.dto.RespuestaRecepcion;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquestador genérico de envío de comprobantes electrónicos al SRI Ecuador.
 * Sirve para TODOS los tipos de comprobante: FACTURA, NOTA_CREDITO, NOTA_DEBITO,
 * GUIA_REMISION, RETENCION, LIQUIDACION_COMPRA.
 *
 * La lógica de recepción → autorización → persistencia es idéntica para todos.
 * Solo el XML varía, delegado a cada SriXmlGenerator.
 */
@Service
public class SriEnvioService {

    private static final Logger LOG = LoggerFactory.getLogger(SriEnvioService.class);

    private final Map<String, SriXmlGenerator> generadores;
    private final SriClaveAccesoService sriClaveAccesoService;
    private final SriSignatureService sriSignatureService;
    private final SriSoapClientService sriSoapClientService;
    private final FacturaLogService facturaLogService;
    private final CompaniaRepository companiaRepository;

    public SriEnvioService(
        List<SriXmlGenerator> generadores,
        SriClaveAccesoService sriClaveAccesoService,
        SriSignatureService sriSignatureService,
        SriSoapClientService sriSoapClientService,
        FacturaLogService facturaLogService,
        CompaniaRepository companiaRepository
    ) {
        this.generadores = generadores.stream().collect(Collectors.toMap(SriXmlGenerator::getTipoDocumento, g -> g));
        this.sriClaveAccesoService = sriClaveAccesoService;
        this.sriSignatureService = sriSignatureService;
        this.sriSoapClientService = sriSoapClientService;
        this.facturaLogService = facturaLogService;
        this.companiaRepository = companiaRepository;
    }

    /**
     * Envía un comprobante electrónico al SRI.
     *
     * @param documentoId   ID del documento en su tabla de origen
     * @param tipoDocumento FACTURA | NOTA_CREDITO | NOTA_DEBITO | GUIA_REMISION | RETENCION | LIQUIDACION_COMPRA
     * @param noCia         número de compañía emisora
     * @return log del último intento (recepción o autorización)
     */
    @Transactional
    public FacturaLogDTO enviar(Long documentoId, String tipoDocumento, Long noCia) {
        LOG.info("Iniciando envío SRI: tipoDocumento={}, documentoId={}, noCia={}", tipoDocumento, documentoId, noCia);

        // 1. Obtener el generador apropiado
        SriXmlGenerator generador = generadores.get(tipoDocumento);
        if (generador == null) {
            throw new IllegalArgumentException("Tipo de documento no soportado: " + tipoDocumento);
        }

        // 2. Cargar compañía y validar configuración SRI
        Compania compania = companiaRepository
            .findByNoCia(noCia)
            .orElseThrow(() -> new RuntimeException("Compañía no encontrada: " + noCia));
        validarConfiguracionSri(compania);

        // 3. Generar clave de acceso
        String[] serieSeq = generador.getSerieYSecuencial(documentoId);
        java.time.Instant fechaEmision = generador.getFechaEmision(documentoId);
        String claveAcceso = sriClaveAccesoService.generar(fechaEmision, generador.getCodigoSri(), serieSeq[0], serieSeq[1], compania);
        LOG.debug("Clave de acceso generada: {}", claveAcceso);

        // 4. Generar XML del comprobante
        String xml = generador.generarXml(documentoId, compania, claveAcceso);

        // 5. Firmar XML con XAdES-BES
        String xmlFirmado = sriSignatureService.firmar(xml, compania);

        // 6. Enviar al WS de Recepción
        RespuestaRecepcion recepcion;
        try {
            recepcion = sriSoapClientService.enviarRecepcion(xmlFirmado, compania.getAmbienteSri());
        } catch (Exception e) {
            LOG.error("Error de comunicación con WS Recepción SRI", e);
            recepcion = new RespuestaRecepcion();
            recepcion.setEstado("ERROR_SISTEMA");
            recepcion.setMensajes(List.of("Error de comunicación: " + e.getMessage()));
        }

        FacturaLog logRecepcion = grabarLog(
            "RECEPCION",
            tipoDocumento,
            documentoId,
            noCia,
            recepcion.getEstado(),
            xmlFirmado,
            claveAcceso,
            null,
            recepcion.getMensajesAsString(),
            compania.getAmbienteSri()
        );

        // 7. Si fue RECIBIDA, consultar autorización
        if ("RECIBIDA".equals(recepcion.getEstado())) {
            RespuestaAutorizacion auth;
            try {
                auth = sriSoapClientService.consultarAutorizacion(claveAcceso, compania.getAmbienteSri());
            } catch (Exception e) {
                LOG.error("Error de comunicación con WS Autorización SRI", e);
                auth = new RespuestaAutorizacion();
                auth.setEstado("ERROR_SISTEMA");
                auth.setMensajes(List.of("Error de comunicación: " + e.getMessage()));
            }

            FacturaLog logAuth = grabarLog(
                "AUTORIZACION",
                tipoDocumento,
                documentoId,
                noCia,
                auth.getEstado(),
                xmlFirmado,
                claveAcceso,
                auth.getNumeroAutorizacion(),
                auth.getMensajesAsString(),
                compania.getAmbienteSri()
            );

            // 8. Si AUTORIZADO, actualizar el documento origen
            if ("AUTORIZADO".equals(auth.getEstado())) {
                generador.actualizarAutorizacion(documentoId, claveAcceso, auth);
                LOG.info(
                    "Comprobante autorizado exitosamente: tipoDocumento={}, documentoId={}, numAuth={}",
                    tipoDocumento,
                    documentoId,
                    auth.getNumeroAutorizacion()
                );
            }

            return facturaLogService.save(logAuth);
        }

        return facturaLogService.save(logRecepcion);
    }

    private FacturaLog grabarLog(
        String tipoAccion,
        String tipoDocumento,
        Long documentoId,
        Long noCia,
        String estado,
        String xmlFirmado,
        String claveAcceso,
        String numeroAutorizacion,
        String mensajes,
        int ambiente
    ) {
        FacturaLog log = new FacturaLog();
        log.setNoCia(noCia);
        log.setTipoDocumento(tipoDocumento);
        log.setDocumentoId(documentoId);
        log.setFechaIntento(Instant.now());
        log.setTipoAccion(tipoAccion);
        log.setClaveAcceso(claveAcceso);
        log.setEstado(estado);
        log.setNumeroAutorizacion(numeroAutorizacion);
        log.setXmlFirmado(xmlFirmado);
        if (mensajes != null && mensajes.length() > 2000) {
            log.setMensajesSri(mensajes.substring(0, 2000));
        } else {
            log.setMensajesSri(mensajes);
        }
        log.setAmbiente(ambiente);
        return log;
    }

    private void validarConfiguracionSri(Compania compania) {
        if (compania.getPathCertificado() == null || compania.getPathCertificado().isBlank()) {
            throw new IllegalStateException("La compañía no tiene configurado el certificado digital (pathCertificado)");
        }
        if (compania.getClaveCertificado() == null || compania.getClaveCertificado().isBlank()) {
            throw new IllegalStateException("La compañía no tiene configurada la clave del certificado (claveCertificado)");
        }
        if (compania.getAmbienteSri() == null) {
            throw new IllegalStateException("La compañía no tiene configurado el ambiente SRI (1=pruebas, 2=producción)");
        }
        if (compania.getDni() == null || compania.getDni().length() != 13) {
            throw new IllegalStateException("El RUC de la compañía debe tener 13 dígitos");
        }
    }
}
