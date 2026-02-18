package com.devix.domain;

import static com.devix.domain.CiudadTestSamples.*;
import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.ProvinciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CiudadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ciudad.class);
        Ciudad ciudad1 = getCiudadSample1();
        Ciudad ciudad2 = new Ciudad();
        assertThat(ciudad1).isNotEqualTo(ciudad2);

        ciudad2.setId(ciudad1.getId());
        assertThat(ciudad1).isEqualTo(ciudad2);

        ciudad2 = getCiudadSample2();
        assertThat(ciudad1).isNotEqualTo(ciudad2);
    }

    @Test
    void clienteTest() {
        Ciudad ciudad = getCiudadRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        ciudad.addCliente(clienteBack);
        assertThat(ciudad.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getCiudad()).isEqualTo(ciudad);

        ciudad.removeCliente(clienteBack);
        assertThat(ciudad.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getCiudad()).isNull();

        ciudad.clientes(new HashSet<>(Set.of(clienteBack)));
        assertThat(ciudad.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getCiudad()).isEqualTo(ciudad);

        ciudad.setClientes(new HashSet<>());
        assertThat(ciudad.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getCiudad()).isNull();
    }

    @Test
    void provinciaTest() {
        Ciudad ciudad = getCiudadRandomSampleGenerator();
        Provincia provinciaBack = getProvinciaRandomSampleGenerator();

        ciudad.setProvincia(provinciaBack);
        assertThat(ciudad.getProvincia()).isEqualTo(provinciaBack);

        ciudad.provincia(null);
        assertThat(ciudad.getProvincia()).isNull();
    }
}
