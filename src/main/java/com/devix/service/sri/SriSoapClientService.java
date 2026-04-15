package com.devix.service.sri;

import com.devix.service.sri.dto.RespuestaAutorizacion;
import com.devix.service.sri.dto.RespuestaRecepcion;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Cliente SOAP para los WebServices del SRI Ecuador.
 * Usa RestTemplate con XML crudo (sin JAX-WS).
 */
@Service
public class SriSoapClientService {

    private static final Logger LOG = LoggerFactory.getLogger(SriSoapClientService.class);

    private static final String WS_RECEPCION_PRUEBAS =
        "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline";
    private static final String WS_RECEPCION_PRODUCCION =
        "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline";
    private static final String WS_AUTORIZACION_PRUEBAS =
        "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline";
    private static final String WS_AUTORIZACION_PRODUCCION =
        "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline";

    private final RestTemplate restTemplate;

    public SriSoapClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Envía el comprobante firmado (en Base64) al WS de recepción del SRI.
     */
    public RespuestaRecepcion enviarRecepcion(String xmlFirmado, int ambiente) {
        String xmlB64 = Base64.getEncoder().encodeToString(xmlFirmado.getBytes(StandardCharsets.UTF_8));
        String url = ambiente == 2 ? WS_RECEPCION_PRODUCCION : WS_RECEPCION_PRUEBAS;

        String soap =
            """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:ec="http://ec.gob.sri.ws.recepcion">
              <soapenv:Header/>
              <soapenv:Body>
                <ec:validarComprobante>
                  <xml>%s</xml>
                </ec:validarComprobante>
              </soapenv:Body>
            </soapenv:Envelope>""".formatted(xmlB64);

        LOG.debug("Enviando recepcion al SRI (ambiente={})", ambiente);
        String response = callSoap(url, soap);
        return parsearRespuestaRecepcion(response);
    }

    /**
     * Consulta la autorización de un comprobante por su clave de acceso.
     */
    public RespuestaAutorizacion consultarAutorizacion(String claveAcceso, int ambiente) {
        String url = ambiente == 2 ? WS_AUTORIZACION_PRODUCCION : WS_AUTORIZACION_PRUEBAS;

        String soap =
            """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:ec="http://ec.gob.sri.ws.autorizacion">
              <soapenv:Header/>
              <soapenv:Body>
                <ec:autorizacionComprobante>
                  <claveAccesoComprobante>%s</claveAccesoComprobante>
                </ec:autorizacionComprobante>
              </soapenv:Body>
            </soapenv:Envelope>""".formatted(claveAcceso);

        LOG.debug("Consultando autorizacion SRI para claveAcceso={}", claveAcceso);
        String response = callSoap(url, soap);
        return parsearRespuestaAutorizacion(response);
    }

    private String callSoap(String url, String soapBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.set("SOAPAction", "");
        HttpEntity<String> entity = new HttpEntity<>(soapBody, headers);
        return restTemplate.postForObject(url, entity, String.class);
    }

    private RespuestaRecepcion parsearRespuestaRecepcion(String xml) {
        RespuestaRecepcion respuesta = new RespuestaRecepcion();
        try {
            Document doc = parseXml(xml);
            NodeList estadoNodes = doc.getElementsByTagNameNS("*", "estado");
            if (estadoNodes.getLength() > 0) {
                respuesta.setEstado(estadoNodes.item(0).getTextContent().trim());
            }
            List<String> mensajes = new ArrayList<>();
            NodeList mensajeNodes = doc.getElementsByTagNameNS("*", "mensaje");
            for (int i = 0; i < mensajeNodes.getLength(); i++) {
                mensajes.add(mensajeNodes.item(i).getTextContent().trim());
            }
            NodeList infoNodes = doc.getElementsByTagNameNS("*", "informacionAdicional");
            for (int i = 0; i < infoNodes.getLength(); i++) {
                mensajes.add(infoNodes.item(i).getTextContent().trim());
            }
            respuesta.setMensajes(mensajes);
        } catch (Exception e) {
            LOG.error("Error parseando respuesta recepcion SRI", e);
            respuesta.setEstado("ERROR_SISTEMA");
            respuesta.setMensajes(List.of("Error al parsear respuesta: " + e.getMessage()));
        }
        return respuesta;
    }

    private RespuestaAutorizacion parsearRespuestaAutorizacion(String xml) {
        RespuestaAutorizacion respuesta = new RespuestaAutorizacion();
        try {
            Document doc = parseXml(xml);
            NodeList autorizaciones = doc.getElementsByTagNameNS("*", "autorizacion");
            if (autorizaciones.getLength() > 0) {
                org.w3c.dom.Element auth = (org.w3c.dom.Element) autorizaciones.item(0);
                NodeList estadoNodes = auth.getElementsByTagNameNS("*", "estado");
                if (estadoNodes.getLength() > 0) {
                    respuesta.setEstado(estadoNodes.item(0).getTextContent().trim());
                }
                NodeList numNodes = auth.getElementsByTagNameNS("*", "numeroAutorizacion");
                if (numNodes.getLength() > 0) {
                    respuesta.setNumeroAutorizacion(numNodes.item(0).getTextContent().trim());
                }
                NodeList fechaNodes = auth.getElementsByTagNameNS("*", "fechaAutorizacion");
                if (fechaNodes.getLength() > 0) {
                    respuesta.setFechaAutorizacion(fechaNodes.item(0).getTextContent().trim());
                }
                List<String> mensajes = new ArrayList<>();
                NodeList mensajeNodes = auth.getElementsByTagNameNS("*", "mensaje");
                for (int i = 0; i < mensajeNodes.getLength(); i++) {
                    mensajes.add(mensajeNodes.item(i).getTextContent().trim());
                }
                respuesta.setMensajes(mensajes);
            }
        } catch (Exception e) {
            LOG.error("Error parseando respuesta autorizacion SRI", e);
            respuesta.setEstado("ERROR_SISTEMA");
            respuesta.setMensajes(List.of("Error al parsear respuesta: " + e.getMessage()));
        }
        return respuesta;
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}
