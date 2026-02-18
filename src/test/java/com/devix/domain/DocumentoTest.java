package com.devix.domain;

import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.DocumentoTestSamples.*;
import static com.devix.domain.EventoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Documento.class);
        Documento documento1 = getDocumentoSample1();
        Documento documento2 = new Documento();
        assertThat(documento1).isNotEqualTo(documento2);

        documento2.setId(documento1.getId());
        assertThat(documento1).isEqualTo(documento2);

        documento2 = getDocumentoSample2();
        assertThat(documento1).isNotEqualTo(documento2);
    }

    @Test
    void clienteTest() {
        Documento documento = getDocumentoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        documento.setCliente(clienteBack);
        assertThat(documento.getCliente()).isEqualTo(clienteBack);

        documento.cliente(null);
        assertThat(documento.getCliente()).isNull();
    }

    @Test
    void eventoTest() {
        Documento documento = getDocumentoRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        documento.setEvento(eventoBack);
        assertThat(documento.getEvento()).isEqualTo(eventoBack);

        documento.evento(null);
        assertThat(documento.getEvento()).isNull();
    }
}
