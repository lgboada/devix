package com.devix.domain;

import static com.devix.domain.CiudadTestSamples.*;
import static com.devix.domain.PaisTestSamples.*;
import static com.devix.domain.ProvinciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProvinciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Provincia.class);
        Provincia provincia1 = getProvinciaSample1();
        Provincia provincia2 = new Provincia();
        assertThat(provincia1).isNotEqualTo(provincia2);

        provincia2.setId(provincia1.getId());
        assertThat(provincia1).isEqualTo(provincia2);

        provincia2 = getProvinciaSample2();
        assertThat(provincia1).isNotEqualTo(provincia2);
    }

    @Test
    void ciudadTest() {
        Provincia provincia = getProvinciaRandomSampleGenerator();
        Ciudad ciudadBack = getCiudadRandomSampleGenerator();

        provincia.addCiudad(ciudadBack);
        assertThat(provincia.getCiudads()).containsOnly(ciudadBack);
        assertThat(ciudadBack.getProvincia()).isEqualTo(provincia);

        provincia.removeCiudad(ciudadBack);
        assertThat(provincia.getCiudads()).doesNotContain(ciudadBack);
        assertThat(ciudadBack.getProvincia()).isNull();

        provincia.ciudads(new HashSet<>(Set.of(ciudadBack)));
        assertThat(provincia.getCiudads()).containsOnly(ciudadBack);
        assertThat(ciudadBack.getProvincia()).isEqualTo(provincia);

        provincia.setCiudads(new HashSet<>());
        assertThat(provincia.getCiudads()).doesNotContain(ciudadBack);
        assertThat(ciudadBack.getProvincia()).isNull();
    }

    @Test
    void paisTest() {
        Provincia provincia = getProvinciaRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        provincia.setPais(paisBack);
        assertThat(provincia.getPais()).isEqualTo(paisBack);

        provincia.pais(null);
        assertThat(provincia.getPais()).isNull();
    }
}
