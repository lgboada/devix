package com.devix.domain;

import static com.devix.domain.CentroTestSamples.*;
import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.DocumentoTestSamples.*;
import static com.devix.domain.EventoTestSamples.*;
import static com.devix.domain.TipoEventoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evento.class);
        Evento evento1 = getEventoSample1();
        Evento evento2 = new Evento();
        assertThat(evento1).isNotEqualTo(evento2);

        evento2.setId(evento1.getId());
        assertThat(evento1).isEqualTo(evento2);

        evento2 = getEventoSample2();
        assertThat(evento1).isNotEqualTo(evento2);
    }

    @Test
    void documentoTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Documento documentoBack = getDocumentoRandomSampleGenerator();

        evento.addDocumento(documentoBack);
        assertThat(evento.getDocumentos()).containsOnly(documentoBack);
        assertThat(documentoBack.getEvento()).isEqualTo(evento);

        evento.removeDocumento(documentoBack);
        assertThat(evento.getDocumentos()).doesNotContain(documentoBack);
        assertThat(documentoBack.getEvento()).isNull();

        evento.documentos(new HashSet<>(Set.of(documentoBack)));
        assertThat(evento.getDocumentos()).containsOnly(documentoBack);
        assertThat(documentoBack.getEvento()).isEqualTo(evento);

        evento.setDocumentos(new HashSet<>());
        assertThat(evento.getDocumentos()).doesNotContain(documentoBack);
        assertThat(documentoBack.getEvento()).isNull();
    }

    @Test
    void tipoEventoTest() {
        Evento evento = getEventoRandomSampleGenerator();
        TipoEvento tipoEventoBack = getTipoEventoRandomSampleGenerator();

        evento.setTipoEvento(tipoEventoBack);
        assertThat(evento.getTipoEvento()).isEqualTo(tipoEventoBack);

        evento.tipoEvento(null);
        assertThat(evento.getTipoEvento()).isNull();
    }

    @Test
    void centroTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Centro centroBack = getCentroRandomSampleGenerator();

        evento.setCentro(centroBack);
        assertThat(evento.getCentro()).isEqualTo(centroBack);

        evento.centro(null);
        assertThat(evento.getCentro()).isNull();
    }

    @Test
    void clienteTest() {
        Evento evento = getEventoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        evento.setCliente(clienteBack);
        assertThat(evento.getCliente()).isEqualTo(clienteBack);

        evento.cliente(null);
        assertThat(evento.getCliente()).isNull();
    }
}
