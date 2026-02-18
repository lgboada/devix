package com.devix.domain;

import static com.devix.domain.CiudadTestSamples.*;
import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.DireccionTestSamples.*;
import static com.devix.domain.DocumentoTestSamples.*;
import static com.devix.domain.EventoTestSamples.*;
import static com.devix.domain.FacturaTestSamples.*;
import static com.devix.domain.TipoClienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void direccionesTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        cliente.addDirecciones(direccionBack);
        assertThat(cliente.getDirecciones()).containsOnly(direccionBack);
        assertThat(direccionBack.getCliente()).isEqualTo(cliente);

        cliente.removeDirecciones(direccionBack);
        assertThat(cliente.getDirecciones()).doesNotContain(direccionBack);
        assertThat(direccionBack.getCliente()).isNull();

        cliente.direcciones(new HashSet<>(Set.of(direccionBack)));
        assertThat(cliente.getDirecciones()).containsOnly(direccionBack);
        assertThat(direccionBack.getCliente()).isEqualTo(cliente);

        cliente.setDirecciones(new HashSet<>());
        assertThat(cliente.getDirecciones()).doesNotContain(direccionBack);
        assertThat(direccionBack.getCliente()).isNull();
    }

    @Test
    void facturasTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Factura facturaBack = getFacturaRandomSampleGenerator();

        cliente.addFacturas(facturaBack);
        assertThat(cliente.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getCliente()).isEqualTo(cliente);

        cliente.removeFacturas(facturaBack);
        assertThat(cliente.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getCliente()).isNull();

        cliente.facturas(new HashSet<>(Set.of(facturaBack)));
        assertThat(cliente.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getCliente()).isEqualTo(cliente);

        cliente.setFacturas(new HashSet<>());
        assertThat(cliente.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getCliente()).isNull();
    }

    @Test
    void eventoTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        cliente.addEvento(eventoBack);
        assertThat(cliente.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getCliente()).isEqualTo(cliente);

        cliente.removeEvento(eventoBack);
        assertThat(cliente.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getCliente()).isNull();

        cliente.eventos(new HashSet<>(Set.of(eventoBack)));
        assertThat(cliente.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getCliente()).isEqualTo(cliente);

        cliente.setEventos(new HashSet<>());
        assertThat(cliente.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getCliente()).isNull();
    }

    @Test
    void documentoTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Documento documentoBack = getDocumentoRandomSampleGenerator();

        cliente.addDocumento(documentoBack);
        assertThat(cliente.getDocumentos()).containsOnly(documentoBack);
        assertThat(documentoBack.getCliente()).isEqualTo(cliente);

        cliente.removeDocumento(documentoBack);
        assertThat(cliente.getDocumentos()).doesNotContain(documentoBack);
        assertThat(documentoBack.getCliente()).isNull();

        cliente.documentos(new HashSet<>(Set.of(documentoBack)));
        assertThat(cliente.getDocumentos()).containsOnly(documentoBack);
        assertThat(documentoBack.getCliente()).isEqualTo(cliente);

        cliente.setDocumentos(new HashSet<>());
        assertThat(cliente.getDocumentos()).doesNotContain(documentoBack);
        assertThat(documentoBack.getCliente()).isNull();
    }

    @Test
    void tipoClienteTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        TipoCliente tipoClienteBack = getTipoClienteRandomSampleGenerator();

        cliente.setTipoCliente(tipoClienteBack);
        assertThat(cliente.getTipoCliente()).isEqualTo(tipoClienteBack);

        cliente.tipoCliente(null);
        assertThat(cliente.getTipoCliente()).isNull();
    }

    @Test
    void ciudadTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Ciudad ciudadBack = getCiudadRandomSampleGenerator();

        cliente.setCiudad(ciudadBack);
        assertThat(cliente.getCiudad()).isEqualTo(ciudadBack);

        cliente.ciudad(null);
        assertThat(cliente.getCiudad()).isNull();
    }
}
