package com.devix.domain;

import static com.devix.domain.PaisTestSamples.*;
import static com.devix.domain.ProvinciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pais.class);
        Pais pais1 = getPaisSample1();
        Pais pais2 = new Pais();
        assertThat(pais1).isNotEqualTo(pais2);

        pais2.setId(pais1.getId());
        assertThat(pais1).isEqualTo(pais2);

        pais2 = getPaisSample2();
        assertThat(pais1).isNotEqualTo(pais2);
    }

    @Test
    void provinciaTest() {
        Pais pais = getPaisRandomSampleGenerator();
        Provincia provinciaBack = getProvinciaRandomSampleGenerator();

        pais.addProvincia(provinciaBack);
        assertThat(pais.getProvincias()).containsOnly(provinciaBack);
        assertThat(provinciaBack.getPais()).isEqualTo(pais);

        pais.removeProvincia(provinciaBack);
        assertThat(pais.getProvincias()).doesNotContain(provinciaBack);
        assertThat(provinciaBack.getPais()).isNull();

        pais.provincias(new HashSet<>(Set.of(provinciaBack)));
        assertThat(pais.getProvincias()).containsOnly(provinciaBack);
        assertThat(provinciaBack.getPais()).isEqualTo(pais);

        pais.setProvincias(new HashSet<>());
        assertThat(pais.getProvincias()).doesNotContain(provinciaBack);
        assertThat(provinciaBack.getPais()).isNull();
    }
}
