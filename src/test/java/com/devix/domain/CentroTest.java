package com.devix.domain;

import static com.devix.domain.CentroTestSamples.*;
import static com.devix.domain.CompaniaTestSamples.*;
import static com.devix.domain.EventoTestSamples.*;
import static com.devix.domain.FacturaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CentroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Centro.class);
        Centro centro1 = getCentroSample1();
        Centro centro2 = new Centro();
        assertThat(centro1).isNotEqualTo(centro2);

        centro2.setId(centro1.getId());
        assertThat(centro1).isEqualTo(centro2);

        centro2 = getCentroSample2();
        assertThat(centro1).isNotEqualTo(centro2);
    }

    @Test
    void facturaTest() {
        Centro centro = getCentroRandomSampleGenerator();
        Factura facturaBack = getFacturaRandomSampleGenerator();

        centro.addFactura(facturaBack);
        assertThat(centro.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getCentro()).isEqualTo(centro);

        centro.removeFactura(facturaBack);
        assertThat(centro.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getCentro()).isNull();

        centro.facturas(new HashSet<>(Set.of(facturaBack)));
        assertThat(centro.getFacturas()).containsOnly(facturaBack);
        assertThat(facturaBack.getCentro()).isEqualTo(centro);

        centro.setFacturas(new HashSet<>());
        assertThat(centro.getFacturas()).doesNotContain(facturaBack);
        assertThat(facturaBack.getCentro()).isNull();
    }

    @Test
    void eventoTest() {
        Centro centro = getCentroRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        centro.addEvento(eventoBack);
        assertThat(centro.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getCentro()).isEqualTo(centro);

        centro.removeEvento(eventoBack);
        assertThat(centro.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getCentro()).isNull();

        centro.eventos(new HashSet<>(Set.of(eventoBack)));
        assertThat(centro.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getCentro()).isEqualTo(centro);

        centro.setEventos(new HashSet<>());
        assertThat(centro.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getCentro()).isNull();
    }

    @Test
    void companiaTest() {
        Centro centro = getCentroRandomSampleGenerator();
        Compania companiaBack = getCompaniaRandomSampleGenerator();

        centro.setCompania(companiaBack);
        assertThat(centro.getCompania()).isEqualTo(companiaBack);

        centro.compania(null);
        assertThat(centro.getCompania()).isNull();
    }
}
