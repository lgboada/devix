package com.devix.domain;

import static com.devix.domain.MarcaTestSamples.*;
import static com.devix.domain.ModeloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MarcaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marca.class);
        Marca marca1 = getMarcaSample1();
        Marca marca2 = new Marca();
        assertThat(marca1).isNotEqualTo(marca2);

        marca2.setId(marca1.getId());
        assertThat(marca1).isEqualTo(marca2);

        marca2 = getMarcaSample2();
        assertThat(marca1).isNotEqualTo(marca2);
    }

    @Test
    void modelosTest() {
        Marca marca = getMarcaRandomSampleGenerator();
        Modelo modeloBack = getModeloRandomSampleGenerator();

        marca.addModelos(modeloBack);
        assertThat(marca.getModelos()).containsOnly(modeloBack);
        assertThat(modeloBack.getMarca()).isEqualTo(marca);

        marca.removeModelos(modeloBack);
        assertThat(marca.getModelos()).doesNotContain(modeloBack);
        assertThat(modeloBack.getMarca()).isNull();

        marca.modelos(new HashSet<>(Set.of(modeloBack)));
        assertThat(marca.getModelos()).containsOnly(modeloBack);
        assertThat(modeloBack.getMarca()).isEqualTo(marca);

        marca.setModelos(new HashSet<>());
        assertThat(marca.getModelos()).doesNotContain(modeloBack);
        assertThat(modeloBack.getMarca()).isNull();
    }
}
