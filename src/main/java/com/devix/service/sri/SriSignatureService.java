package com.devix.service.sri;

import com.devix.domain.Compania;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * Firma comprobantes electrónicos con XAdES-BES usando el certificado .p12 de la compañía.
 * Cumple el estándar requerido por el SRI Ecuador.
 */
@Service
public class SriSignatureService {

    private static final Logger LOG = LoggerFactory.getLogger(SriSignatureService.class);
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(
        ZoneId.of("America/Guayaquil")
    );

    static {
        Init.init();
    }

    /**
     * Firma el XML del comprobante con XAdES-BES.
     *
     * @param xmlSinFirmar XML del comprobante generado
     * @param compania     compañía emisora (contiene pathCertificado y claveCertificado)
     * @return XML firmado como String
     */
    public String firmar(String xmlSinFirmar, Compania compania) {
        try {
            // 1. Cargar .p12
            KeyStore ks = KeyStore.getInstance("PKCS12");
            Path certPath = resolveCertificadoPath(compania);
            byte[] certBytes = java.nio.file.Files.readAllBytes(certPath);
            char[] password = compania.getClaveCertificado().toCharArray();
            ks.load(new ByteArrayInputStream(certBytes), password);

            // 2. Obtener alias, clave privada y certificado
            String alias = null;
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String a = aliases.nextElement();
                if (ks.isKeyEntry(a)) {
                    alias = a;
                    break;
                }
            }
            if (alias == null) throw new IllegalStateException("No se encontró clave privada en el certificado .p12");

            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password);
            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

            // 3. Parsear el XML del comprobante
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlSinFirmar)));
            Element root = doc.getDocumentElement();
            if (root != null && root.hasAttribute("id")) {
                root.setIdAttribute("id", true);
            }

            // 4. Crear la firma XML
            XMLSignature sig = new XMLSignature(
                doc,
                "",
                XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
                Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS
            );

            // Agregar Transforms: enveloped + c14n
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

            // Referencia al elemento raíz (id="comprobante")
            sig.addDocument("#comprobante", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // 5. Agregar KeyInfo con certificado X.509
            sig.addKeyInfo(cert);
            sig.addKeyInfo(cert.getPublicKey());

            // 6. Agregar elementos XAdES-BES (SignedProperties)
            agregarXadesSignedProperties(doc, sig, cert);

            // 7. Adjuntar firma al documento y firmar
            doc.getDocumentElement().appendChild(sig.getElement());
            sig.sign(privateKey);

            // 8. Serializar a String
            return documentToString(doc);
        } catch (Exception e) {
            LOG.error(
                "Error al firmar comprobante con certificado (configurado={}): {}",
                compania.getPathCertificado(),
                resolveCertificadoPathSafe(compania),
                e
            );
            throw new RuntimeException("Error al firmar el comprobante electrónico: " + e.getMessage(), e);
        }
    }

    /**
     * {@code pathCertificado} suele ser el nombre devuelto por {@code POST /api/files}; en ese caso la ruta absoluta
     * es {@code pathFileServer}/{@code filename}. Si {@code pathCertificado} ya es una ruta absoluta, se usa tal cual.
     */
    private Path resolveCertificadoPath(Compania compania) {
        String configured = compania.getPathCertificado();
        if (configured == null || configured.isBlank()) {
            throw new IllegalStateException("pathCertificado vacío");
        }
        Path p = Paths.get(configured.trim());
        if (p.isAbsolute()) {
            return p;
        }

        String rootDir = compania.getPathFileServer();
        if (rootDir == null || rootDir.isBlank()) {
            throw new IllegalStateException("pathFileServer requerido en la compañía para localizar el certificado .p12");
        }
        Path root = Paths.get(rootDir.trim());
        if (!root.isAbsolute()) {
            throw new IllegalStateException("pathFileServer debe ser una ruta absoluta");
        }

        Path resolved = root.resolve(p).normalize();
        if (!resolved.startsWith(root.toAbsolutePath().normalize())) {
            throw new IllegalStateException("Ruta de certificado inválida (fuera del directorio permitido)");
        }
        return resolved;
    }

    private String resolveCertificadoPathSafe(Compania compania) {
        try {
            return resolveCertificadoPath(compania).toString();
        } catch (Exception e) {
            return "(no resuelto: " + e.getMessage() + ")";
        }
    }

    /**
     * Agrega los elementos XAdES-BES requeridos por el SRI:
     * QualifyingProperties > SignedProperties > SignedSignatureProperties
     * (SigningTime + SigningCertificate)
     */
    private void agregarXadesSignedProperties(Document doc, XMLSignature sig, X509Certificate cert) throws Exception {
        String xadesNs = "http://uri.etsi.org/01903/v1.3.2#";
        String sigId = "Signature";
        org.w3c.dom.Element sigEl = sig.getElement();
        sigEl.setAttribute("Id", sigId);
        sigEl.setIdAttribute("Id", true);

        Element qualifyingProps = doc.createElementNS(xadesNs, "xades:QualifyingProperties");
        qualifyingProps.setAttribute("xmlns:xades", xadesNs);
        qualifyingProps.setAttribute("Target", "#" + sigId);

        Element signedProps = doc.createElementNS(xadesNs, "xades:SignedProperties");
        signedProps.setAttribute("Id", "SignedProperties");
        signedProps.setIdAttribute("Id", true);

        Element signedSigProps = doc.createElementNS(xadesNs, "xades:SignedSignatureProperties");

        // SigningTime
        Element signingTime = doc.createElementNS(xadesNs, "xades:SigningTime");
        signingTime.setTextContent(ISO_FMT.format(Instant.now()));
        signedSigProps.appendChild(signingTime);

        // SigningCertificate
        Element signingCert = doc.createElementNS(xadesNs, "xades:SigningCertificate");
        Element certElem = doc.createElementNS(xadesNs, "xades:Cert");
        Element certDigest = doc.createElementNS(xadesNs, "xades:CertDigest");
        Element digestMethod = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:DigestMethod");
        digestMethod.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
        Element digestValue = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:DigestValue");
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
        byte[] certDigestBytes = md.digest(cert.getEncoded());
        digestValue.setTextContent(java.util.Base64.getEncoder().encodeToString(certDigestBytes));
        certDigest.appendChild(digestMethod);
        certDigest.appendChild(digestValue);
        certElem.appendChild(certDigest);
        signingCert.appendChild(certElem);
        signedSigProps.appendChild(signingCert);

        signedProps.appendChild(signedSigProps);
        qualifyingProps.appendChild(signedProps);
        // XSD xmldsig: QualifyingProperties debe ir dentro de ds:Object, no como hijo directo de Signature.
        Element objectEl = doc.createElementNS(Constants.SignatureSpecNS, "ds:Object");
        objectEl.appendChild(qualifyingProps);
        sigEl.appendChild(objectEl);

        // Referencia a SignedProperties
        Transforms xadesTransforms = new Transforms(doc);
        xadesTransforms.addTransform(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        sig.addDocument(
            "#SignedProperties",
            xadesTransforms,
            Constants.ALGO_ID_DIGEST_SHA1,
            null,
            "http://uri.etsi.org/01903#SignedProperties"
        );
    }

    private String documentToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }
}
