package com.devix.domain;

import static com.devix.domain.CentroTestSamples.*;
import static com.devix.domain.CompaniaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompaniaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compania.class);
        Compania compania1 = getCompaniaSample1();
        Compania compania2 = new Compania();
        assertThat(compania1).isNotEqualTo(compania2);

        compania2.setId(compania1.getId());
        assertThat(compania1).isEqualTo(compania2);

        compania2 = getCompaniaSample2();
        assertThat(compania1).isNotEqualTo(compania2);
    }

    @Test
    void centrosTest() {
        Compania compania = getCompaniaRandomSampleGenerator();
        Centro centroBack = getCentroRandomSampleGenerator();

        compania.addCentros(centroBack);
        assertThat(compania.getCentros()).containsOnly(centroBack);
        assertThat(centroBack.getCompania()).isEqualTo(compania);

        compania.removeCentros(centroBack);
        assertThat(compania.getCentros()).doesNotContain(centroBack);
        assertThat(centroBack.getCompania()).isNull();

        compania.centros(new HashSet<>(Set.of(centroBack)));
        assertThat(compania.getCentros()).containsOnly(centroBack);
        assertThat(centroBack.getCompania()).isEqualTo(compania);

        compania.setCentros(new HashSet<>());
        assertThat(compania.getCentros()).doesNotContain(centroBack);
        assertThat(centroBack.getCompania()).isNull();
    }
}
