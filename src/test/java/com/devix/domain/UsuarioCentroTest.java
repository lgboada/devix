package com.devix.domain;

import static com.devix.domain.CentroTestSamples.*;
import static com.devix.domain.UsuarioCentroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioCentroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioCentro.class);
        UsuarioCentro usuarioCentro1 = getUsuarioCentroSample1();
        UsuarioCentro usuarioCentro2 = new UsuarioCentro();
        assertThat(usuarioCentro1).isNotEqualTo(usuarioCentro2);

        usuarioCentro2.setId(usuarioCentro1.getId());
        assertThat(usuarioCentro1).isEqualTo(usuarioCentro2);

        usuarioCentro2 = getUsuarioCentroSample2();
        assertThat(usuarioCentro1).isNotEqualTo(usuarioCentro2);
    }

    @Test
    void centroTest() {
        UsuarioCentro usuarioCentro = getUsuarioCentroRandomSampleGenerator();
        Centro centroBack = getCentroRandomSampleGenerator();

        usuarioCentro.setCentro(centroBack);
        assertThat(usuarioCentro.getCentro()).isEqualTo(centroBack);

        usuarioCentro.centro(null);
        assertThat(usuarioCentro.getCentro()).isNull();
    }
}
