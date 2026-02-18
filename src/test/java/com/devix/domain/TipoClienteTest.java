package com.devix.domain;

import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.TipoClienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoCliente.class);
        TipoCliente tipoCliente1 = getTipoClienteSample1();
        TipoCliente tipoCliente2 = new TipoCliente();
        assertThat(tipoCliente1).isNotEqualTo(tipoCliente2);

        tipoCliente2.setId(tipoCliente1.getId());
        assertThat(tipoCliente1).isEqualTo(tipoCliente2);

        tipoCliente2 = getTipoClienteSample2();
        assertThat(tipoCliente1).isNotEqualTo(tipoCliente2);
    }

    @Test
    void clienteTest() {
        TipoCliente tipoCliente = getTipoClienteRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        tipoCliente.addCliente(clienteBack);
        assertThat(tipoCliente.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getTipoCliente()).isEqualTo(tipoCliente);

        tipoCliente.removeCliente(clienteBack);
        assertThat(tipoCliente.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getTipoCliente()).isNull();

        tipoCliente.clientes(new HashSet<>(Set.of(clienteBack)));
        assertThat(tipoCliente.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getTipoCliente()).isEqualTo(tipoCliente);

        tipoCliente.setClientes(new HashSet<>());
        assertThat(tipoCliente.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getTipoCliente()).isNull();
    }
}
