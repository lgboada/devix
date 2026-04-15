package com.devix.service.sri.xml;

import com.devix.domain.Compania;
import com.devix.domain.DetalleFactura;
import com.devix.domain.Factura;
import com.devix.repository.FacturaRepository;
import com.devix.service.sri.SriXmlGenerator;
import com.devix.service.sri.dto.RespuestaAutorizacion;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Genera el XML de Factura electrónica según el schema SRI Ecuador v2.1.0.
 */
@Service
public class SriFacturaXmlGenerator implements SriXmlGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(SriFacturaXmlGenerator.class);
    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("America/Guayaquil"));
    private static final ZoneId ZONA_EC = ZoneId.of("America/Guayaquil");

    private final FacturaRepository facturaRepository;

    public SriFacturaXmlGenerator(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public String getTipoDocumento() {
        return "FACTURA";
    }

    @Override
    public String getCodigoSri() {
        return "01";
    }

    @Override
    public String[] getSerieYSecuencial(Long documentoId) {
        Factura f = cargarFactura(documentoId);
        return new String[] { f.getSerie(), f.getNoFisico() };
    }

    @Override
    public java.time.Instant getFechaEmision(Long documentoId) {
        return cargarFactura(documentoId).getFecha();
    }

    @Override
    @Transactional
    public String generarXml(Long documentoId, Compania compania, String claveAcceso) {
        Factura factura = facturaRepository
            .findByIdWithDetalles(documentoId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + documentoId));

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // <factura id="comprobante" version="2.1.0">
            Element root = doc.createElement("factura");
            root.setAttribute("id", "comprobante");
            root.setAttribute("version", "2.1.0");
            doc.appendChild(root);

            // <infoTributaria>
            root.appendChild(infoTributaria(doc, factura, compania, claveAcceso));

            // <infoFactura>
            root.appendChild(infoFactura(doc, factura, compania));

            // <detalles>
            root.appendChild(detalles(doc, factura));

            // <infoAdicional>
            root.appendChild(infoAdicional(doc, factura));

            return documentToString(doc);
        } catch (Exception e) {
            LOG.error("Error generando XML de factura {}", documentoId, e);
            throw new RuntimeException("Error generando XML factura: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void actualizarAutorizacion(Long documentoId, String claveAcceso, RespuestaAutorizacion respuesta) {
        Factura factura = cargarFactura(documentoId);
        factura.setClaveAcceso(claveAcceso);
        factura.setNumeroAutorizacion(respuesta.getNumeroAutorizacion());
        factura.setFechaAutorizacion(parsearFechaAutorizacion(respuesta.getFechaAutorizacion()));
        factura.setEstado("AUTORIZADA");
        facturaRepository.save(factura);
        LOG.debug("Factura {} actualizada con autorización SRI: {}", documentoId, respuesta.getNumeroAutorizacion());
    }

    // -------------------------------------------------------------------------
    // Secciones del XML
    // -------------------------------------------------------------------------

    private Element infoTributaria(Document doc, Factura factura, Compania compania, String claveAcceso) {
        Element e = doc.createElement("infoTributaria");
        addElem(doc, e, "ambiente", String.valueOf(compania.getAmbienteSri()));
        addElem(doc, e, "tipoEmision", "1");
        addElem(doc, e, "razonSocial", compania.getNombre());
        addElem(doc, e, "nombreComercial", compania.getNombre());
        addElem(doc, e, "ruc", compania.getDni());
        addElem(doc, e, "claveAcceso", claveAcceso);
        addElem(doc, e, "codDoc", "01");
        addElem(doc, e, "estab", factura.getSerie().substring(0, 3));
        addElem(doc, e, "ptoEmi", factura.getSerie().substring(3, 6));
        addElem(doc, e, "secuencial", String.format("%09d", Long.parseLong(factura.getNoFisico().replaceAll("\\D", ""))));
        addElem(doc, e, "dirMatriz", compania.getDireccion());
        return e;
    }

    private Element infoFactura(Document doc, Factura factura, Compania compania) {
        Element e = doc.createElement("infoFactura");
        addElem(doc, e, "fechaEmision", FECHA_FMT.format(factura.getFecha()));

        if (factura.getCentro() != null) {
            addElem(doc, e, "dirEstablecimiento", factura.getCentro().getDescripcion());
        }
        if (compania.getContribuyenteEspecial() != null && !compania.getContribuyenteEspecial().isBlank()) {
            addElem(doc, e, "contribuyenteEspecial", compania.getContribuyenteEspecial());
        }
        addElem(doc, e, "obligadoContabilidad", Boolean.TRUE.equals(compania.getObligadoContabilidad()) ? "SI" : "NO");

        // Tipo de identificación según XSD pattern [0][4-8]:
        // 04=RUC, 05=Cédula, 06=Pasaporte, 07=Consumidor final, 08=Identificación exterior
        String cedula = factura.getCedula();
        String tipoId;
        if ("9999999999999".equals(cedula)) {
            tipoId = "07";
        } else if (cedula != null && cedula.length() == 13) {
            tipoId = "04";
        } else {
            tipoId = "05";
        }
        addElem(doc, e, "tipoIdentificacionComprador", tipoId);
        addElem(doc, e, "razonSocialComprador", factura.getRazonSocial() != null ? factura.getRazonSocial() : "CONSUMIDOR FINAL");
        addElem(doc, e, "identificacionComprador", cedula != null ? cedula : "9999999999999");
        addElem(doc, e, "direccionComprador", factura.getDireccion() != null ? factura.getDireccion() : "S/N");

        addElem(doc, e, "totalSinImpuestos", formatDecimal(factura.getSubtotal()));
        addElem(doc, e, "totalDescuento", formatDecimal(factura.getDescuento()));

        // <totalConImpuestos> — orden del XSD: codigo, codigoPorcentaje, descuentoAdicional(opt), baseImponible, tarifa(opt), valor
        Element totalConImpuestos = doc.createElement("totalConImpuestos");

        double impuestoCero = factura.getImpuestoCero() != null ? factura.getImpuestoCero() : 0.0;
        double impuesto = factura.getImpuesto() != null ? factura.getImpuesto() : 0.0;
        double porcentaje = factura.getPorcentajeImpuesto() != null ? factura.getPorcentajeImpuesto() : 0.0;

        // IVA 0% — codigoPorcentaje=0
        if (impuestoCero > 0) {
            totalConImpuestos.appendChild(totalImpuesto(doc, "2", "0", "0.00", formatDecimal(impuestoCero), "0.00"));
        }
        // IVA con tarifa — codigoPorcentaje: 2=12%, 5=15%
        if (impuesto > 0) {
            String codPorcentaje = Math.abs(porcentaje - 15.0) < 0.001 ? "5" : "2";
            double baseGravada = factura.getSubtotal() - impuestoCero;
            totalConImpuestos.appendChild(
                totalImpuesto(doc, "2", codPorcentaje, formatDecimal(porcentaje), formatDecimal(baseGravada), formatDecimal(impuesto))
            );
        }
        // Si no hay impuestos (todo a 0%), agregar entrada con base total
        if (impuestoCero == 0 && impuesto == 0) {
            totalConImpuestos.appendChild(totalImpuesto(doc, "2", "0", "0.00", formatDecimal(factura.getSubtotal()), "0.00"));
        }
        e.appendChild(totalConImpuestos);

        addElem(doc, e, "importeTotal", formatDecimal(factura.getTotal()));
        addElem(doc, e, "moneda", "DOLAR");

        // <pagos> — minOccurs=0 en XSD pero el SRI lo valida en práctica
        // formaPago 01=efectivo por defecto; se puede extender agregando campo a Factura
        Element pagos = doc.createElement("pagos");
        Element pago = doc.createElement("pago");
        addElem(doc, pago, "formaPago", "01");
        addElem(doc, pago, "total", formatDecimal(factura.getTotal()));
        pagos.appendChild(pago);
        e.appendChild(pagos);

        return e;
    }

    /**
     * Construye un <totalImpuesto> según el XSD:
     * codigo, codigoPorcentaje, descuentoAdicional(opt), baseImponible, tarifa(opt), valor
     */
    private Element totalImpuesto(Document doc, String codigo, String codPorcentaje, String tarifa, String baseImponible, String valor) {
        Element ti = doc.createElement("totalImpuesto");
        addElem(doc, ti, "codigo", codigo);
        addElem(doc, ti, "codigoPorcentaje", codPorcentaje);
        addElem(doc, ti, "descuentoAdicional", "0.00");
        addElem(doc, ti, "baseImponible", baseImponible);
        addElem(doc, ti, "tarifa", tarifa);
        addElem(doc, ti, "valor", valor);
        return ti;
    }

    private Element detalles(Document doc, Factura factura) {
        Element detallesElem = doc.createElement("detalles");
        double porcentajeFactura = factura.getPorcentajeImpuesto() != null ? factura.getPorcentajeImpuesto() : 0.0;

        for (DetalleFactura df : factura.getDetalles()) {
            Element detalle = doc.createElement("detalle");

            // codigoPrincipal y descripcion según XSD (codigoPrincipal minOccurs=0, descripcion requerido)
            if (df.getProducto() != null) {
                addElem(doc, detalle, "codigoPrincipal", df.getProducto().getCodigo());
                addElem(doc, detalle, "descripcion", df.getProducto().getNombre());
            } else {
                addElem(doc, detalle, "descripcion", "Producto");
            }

            // cantidad y precioUnitario requieren 6 decimales según XSD (fractionDigits=6)
            addElem(doc, detalle, "cantidad", formatSeisDec(df.getCantidad().doubleValue()));
            addElem(doc, detalle, "precioUnitario", formatSeisDec(df.getPrecioUnitario()));
            addElem(doc, detalle, "descuento", formatDecimal(df.getDescuento()));
            addElem(doc, detalle, "precioTotalSinImpuesto", formatDecimal(df.getSubtotal()));

            // <impuestos> — orden XSD del tipo "impuesto":
            // codigo, codigoPorcentaje, tarifa, baseImponible, valor
            Element impuestos = doc.createElement("impuestos");
            Element impuesto = doc.createElement("impuesto");

            double impDet = df.getImpuesto() != null ? df.getImpuesto() : 0.0;
            String codPorc;
            double porcIva;
            if (impDet == 0.0) {
                codPorc = "0";
                porcIva = 0.0;
            } else {
                porcIva = porcentajeFactura;
                codPorc = Math.abs(porcIva - 15.0) < 0.001 ? "5" : "2";
            }

            addElem(doc, impuesto, "codigo", "2"); // 2 = IVA
            addElem(doc, impuesto, "codigoPorcentaje", codPorc);
            addElem(doc, impuesto, "tarifa", formatDecimal(porcIva));
            addElem(doc, impuesto, "baseImponible", formatDecimal(df.getSubtotal()));
            addElem(doc, impuesto, "valor", formatDecimal(impDet));
            impuestos.appendChild(impuesto);
            detalle.appendChild(impuestos);
            detallesElem.appendChild(detalle);
        }
        return detallesElem;
    }

    private Element infoAdicional(Document doc, Factura factura) {
        Element info = doc.createElement("infoAdicional");
        if (factura.getEmail() != null && !factura.getEmail().isBlank()) {
            Element campo = doc.createElement("campoAdicional");
            campo.setAttribute("nombre", "Email");
            campo.setTextContent(factura.getEmail());
            info.appendChild(campo);
        }
        if (factura.getTelefono() != null && !factura.getTelefono().isBlank()) {
            Element campo = doc.createElement("campoAdicional");
            campo.setAttribute("nombre", "Telefono");
            campo.setTextContent(factura.getTelefono());
            info.appendChild(campo);
        }
        return info;
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    private void addElem(Document doc, Element parent, String tag, String value) {
        Element e = doc.createElement(tag);
        e.setTextContent(value != null ? value : "");
        parent.appendChild(e);
    }

    private String formatDecimal(Double value) {
        if (value == null) return "0.00";
        return String.format("%.2f", value);
    }

    private String formatSeisDec(Double value) {
        if (value == null) return "0.000000";
        return String.format("%.6f", value);
    }

    private String documentToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter w = new StringWriter();
        t.transform(new DOMSource(doc), new StreamResult(w));
        return w.toString();
    }

    private java.time.Instant parsearFechaAutorizacion(String fechaStr) {
        if (fechaStr == null || fechaStr.isBlank()) return java.time.Instant.now();
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZONA_EC);
            return java.time.ZonedDateTime.parse(fechaStr, fmt).toInstant();
        } catch (Exception e) {
            return java.time.Instant.now();
        }
    }

    private Factura cargarFactura(Long id) {
        return facturaRepository.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada: " + id));
    }
}
