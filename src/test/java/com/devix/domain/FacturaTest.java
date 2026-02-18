package com.devix.domain;

import static com.devix.domain.CentroTestSamples.*;
import static com.devix.domain.ClienteTestSamples.*;
import static com.devix.domain.DetalleFacturaTestSamples.*;
import static com.devix.domain.FacturaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FacturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factura.class);
        Factura factura1 = getFacturaSample1();
        Factura factura2 = new Factura();
        assertThat(factura1).isNotEqualTo(factura2);

        factura2.setId(factura1.getId());
        assertThat(factura1).isEqualTo(factura2);

        factura2 = getFacturaSample2();
        assertThat(factura1).isNotEqualTo(factura2);
    }

    @Test
    void detallesTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        DetalleFactura detalleFacturaBack = getDetalleFacturaRandomSampleGenerator();

        factura.addDetalles(detalleFacturaBack);
        assertThat(factura.getDetalles()).containsOnly(detalleFacturaBack);
        assertThat(detalleFacturaBack.getFactura()).isEqualTo(factura);

        factura.removeDetalles(detalleFacturaBack);
        assertThat(factura.getDetalles()).doesNotContain(detalleFacturaBack);
        assertThat(detalleFacturaBack.getFactura()).isNull();

        factura.detalles(new HashSet<>(Set.of(detalleFacturaBack)));
        assertThat(factura.getDetalles()).containsOnly(detalleFacturaBack);
        assertThat(detalleFacturaBack.getFactura()).isEqualTo(factura);

        factura.setDetalles(new HashSet<>());
        assertThat(factura.getDetalles()).doesNotContain(detalleFacturaBack);
        assertThat(detalleFacturaBack.getFactura()).isNull();
    }

    @Test
    void centroTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        Centro centroBack = getCentroRandomSampleGenerator();

        factura.setCentro(centroBack);
        assertThat(factura.getCentro()).isEqualTo(centroBack);

        factura.centro(null);
        assertThat(factura.getCentro()).isNull();
    }

    @Test
    void clienteTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        factura.setCliente(clienteBack);
        assertThat(factura.getCliente()).isEqualTo(clienteBack);

        factura.cliente(null);
        assertThat(factura.getCliente()).isNull();
    }
}
