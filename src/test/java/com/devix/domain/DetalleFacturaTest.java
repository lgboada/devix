package com.devix.domain;

import static com.devix.domain.DetalleFacturaTestSamples.*;
import static com.devix.domain.FacturaTestSamples.*;
import static com.devix.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetalleFacturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleFactura.class);
        DetalleFactura detalleFactura1 = getDetalleFacturaSample1();
        DetalleFactura detalleFactura2 = new DetalleFactura();
        assertThat(detalleFactura1).isNotEqualTo(detalleFactura2);

        detalleFactura2.setId(detalleFactura1.getId());
        assertThat(detalleFactura1).isEqualTo(detalleFactura2);

        detalleFactura2 = getDetalleFacturaSample2();
        assertThat(detalleFactura1).isNotEqualTo(detalleFactura2);
    }

    @Test
    void facturaTest() {
        DetalleFactura detalleFactura = getDetalleFacturaRandomSampleGenerator();
        Factura facturaBack = getFacturaRandomSampleGenerator();

        detalleFactura.setFactura(facturaBack);
        assertThat(detalleFactura.getFactura()).isEqualTo(facturaBack);

        detalleFactura.factura(null);
        assertThat(detalleFactura.getFactura()).isNull();
    }

    @Test
    void productoTest() {
        DetalleFactura detalleFactura = getDetalleFacturaRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        detalleFactura.setProducto(productoBack);
        assertThat(detalleFactura.getProducto()).isEqualTo(productoBack);

        detalleFactura.producto(null);
        assertThat(detalleFactura.getProducto()).isNull();
    }
}
