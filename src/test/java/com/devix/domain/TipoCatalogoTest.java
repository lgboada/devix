package com.devix.domain;

import static com.devix.domain.CatalogoTestSamples.*;
import static com.devix.domain.TipoCatalogoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoCatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoCatalogo.class);
        TipoCatalogo tipoCatalogo1 = getTipoCatalogoSample1();
        TipoCatalogo tipoCatalogo2 = new TipoCatalogo();
        assertThat(tipoCatalogo1).isNotEqualTo(tipoCatalogo2);

        tipoCatalogo2.setId(tipoCatalogo1.getId());
        assertThat(tipoCatalogo1).isEqualTo(tipoCatalogo2);

        tipoCatalogo2 = getTipoCatalogoSample2();
        assertThat(tipoCatalogo1).isNotEqualTo(tipoCatalogo2);
    }

    @Test
    void catalogoTest() {
        TipoCatalogo tipoCatalogo = getTipoCatalogoRandomSampleGenerator();
        Catalogo catalogoBack = getCatalogoRandomSampleGenerator();

        tipoCatalogo.addCatalogo(catalogoBack);
        assertThat(tipoCatalogo.getCatalogos()).containsOnly(catalogoBack);
        assertThat(catalogoBack.getTipoCatalogo()).isEqualTo(tipoCatalogo);

        tipoCatalogo.removeCatalogo(catalogoBack);
        assertThat(tipoCatalogo.getCatalogos()).doesNotContain(catalogoBack);
        assertThat(catalogoBack.getTipoCatalogo()).isNull();

        tipoCatalogo.catalogos(new HashSet<>(Set.of(catalogoBack)));
        assertThat(tipoCatalogo.getCatalogos()).containsOnly(catalogoBack);
        assertThat(catalogoBack.getTipoCatalogo()).isEqualTo(tipoCatalogo);

        tipoCatalogo.setCatalogos(new HashSet<>());
        assertThat(tipoCatalogo.getCatalogos()).doesNotContain(catalogoBack);
        assertThat(catalogoBack.getTipoCatalogo()).isNull();
    }
}
