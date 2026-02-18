package com.devix.domain;

import static com.devix.domain.DireccionTestSamples.*;
import static com.devix.domain.TipoDireccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoDireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDireccion.class);
        TipoDireccion tipoDireccion1 = getTipoDireccionSample1();
        TipoDireccion tipoDireccion2 = new TipoDireccion();
        assertThat(tipoDireccion1).isNotEqualTo(tipoDireccion2);

        tipoDireccion2.setId(tipoDireccion1.getId());
        assertThat(tipoDireccion1).isEqualTo(tipoDireccion2);

        tipoDireccion2 = getTipoDireccionSample2();
        assertThat(tipoDireccion1).isNotEqualTo(tipoDireccion2);
    }

    @Test
    void direccionTest() {
        TipoDireccion tipoDireccion = getTipoDireccionRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        tipoDireccion.addDireccion(direccionBack);
        assertThat(tipoDireccion.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getTipoDireccion()).isEqualTo(tipoDireccion);

        tipoDireccion.removeDireccion(direccionBack);
        assertThat(tipoDireccion.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getTipoDireccion()).isNull();

        tipoDireccion.direccions(new HashSet<>(Set.of(direccionBack)));
        assertThat(tipoDireccion.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getTipoDireccion()).isEqualTo(tipoDireccion);

        tipoDireccion.setDireccions(new HashSet<>());
        assertThat(tipoDireccion.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getTipoDireccion()).isNull();
    }
}
