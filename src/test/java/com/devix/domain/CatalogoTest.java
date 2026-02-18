package com.devix.domain;

import static com.devix.domain.CatalogoTestSamples.*;
import static com.devix.domain.TipoCatalogoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogo.class);
        Catalogo catalogo1 = getCatalogoSample1();
        Catalogo catalogo2 = new Catalogo();
        assertThat(catalogo1).isNotEqualTo(catalogo2);

        catalogo2.setId(catalogo1.getId());
        assertThat(catalogo1).isEqualTo(catalogo2);

        catalogo2 = getCatalogoSample2();
        assertThat(catalogo1).isNotEqualTo(catalogo2);
    }

    @Test
    void tipoCatalogoTest() {
        Catalogo catalogo = getCatalogoRandomSampleGenerator();
        TipoCatalogo tipoCatalogoBack = getTipoCatalogoRandomSampleGenerator();

        catalogo.setTipoCatalogo(tipoCatalogoBack);
        assertThat(catalogo.getTipoCatalogo()).isEqualTo(tipoCatalogoBack);

        catalogo.tipoCatalogo(null);
        assertThat(catalogo.getTipoCatalogo()).isNull();
    }
}
