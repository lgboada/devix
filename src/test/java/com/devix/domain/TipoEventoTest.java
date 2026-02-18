package com.devix.domain;

import static com.devix.domain.EventoTestSamples.*;
import static com.devix.domain.TipoEventoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoEventoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoEvento.class);
        TipoEvento tipoEvento1 = getTipoEventoSample1();
        TipoEvento tipoEvento2 = new TipoEvento();
        assertThat(tipoEvento1).isNotEqualTo(tipoEvento2);

        tipoEvento2.setId(tipoEvento1.getId());
        assertThat(tipoEvento1).isEqualTo(tipoEvento2);

        tipoEvento2 = getTipoEventoSample2();
        assertThat(tipoEvento1).isNotEqualTo(tipoEvento2);
    }

    @Test
    void eventoTest() {
        TipoEvento tipoEvento = getTipoEventoRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        tipoEvento.addEvento(eventoBack);
        assertThat(tipoEvento.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getTipoEvento()).isEqualTo(tipoEvento);

        tipoEvento.removeEvento(eventoBack);
        assertThat(tipoEvento.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getTipoEvento()).isNull();

        tipoEvento.eventos(new HashSet<>(Set.of(eventoBack)));
        assertThat(tipoEvento.getEventos()).containsOnly(eventoBack);
        assertThat(eventoBack.getTipoEvento()).isEqualTo(tipoEvento);

        tipoEvento.setEventos(new HashSet<>());
        assertThat(tipoEvento.getEventos()).doesNotContain(eventoBack);
        assertThat(eventoBack.getTipoEvento()).isNull();
    }
}
