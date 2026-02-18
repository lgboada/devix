package com.devix.domain;

import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.DireccionTestSamples.*;
import static com.devix.domain.TipoDireccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direccion.class);
        Direccion direccion1 = getDireccionSample1();
        Direccion direccion2 = new Direccion();
        assertThat(direccion1).isNotEqualTo(direccion2);

        direccion2.setId(direccion1.getId());
        assertThat(direccion1).isEqualTo(direccion2);

        direccion2 = getDireccionSample2();
        assertThat(direccion1).isNotEqualTo(direccion2);
    }

    @Test
    void tipoDireccionTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        TipoDireccion tipoDireccionBack = getTipoDireccionRandomSampleGenerator();

        direccion.setTipoDireccion(tipoDireccionBack);
        assertThat(direccion.getTipoDireccion()).isEqualTo(tipoDireccionBack);

        direccion.tipoDireccion(null);
        assertThat(direccion.getTipoDireccion()).isNull();
    }

    @Test
    void clienteTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        direccion.setCliente(clienteBack);
        assertThat(direccion.getCliente()).isEqualTo(clienteBack);

        direccion.cliente(null);
        assertThat(direccion.getCliente()).isNull();
    }
}
