package com.devix.domain;

import static com.devix.domain.DetalleFacturaTestSamples.*;
import static com.devix.domain.ModeloTestSamples.*;
import static com.devix.domain.ProductoTestSamples.*;
import static com.devix.domain.ProveedorTestSamples.*;
import static com.devix.domain.TipoProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void detalleFacturaTest() {
        Producto producto = getProductoRandomSampleGenerator();
        DetalleFactura detalleFacturaBack = getDetalleFacturaRandomSampleGenerator();

        producto.addDetalleFactura(detalleFacturaBack);
        assertThat(producto.getDetalleFacturas()).containsOnly(detalleFacturaBack);
        assertThat(detalleFacturaBack.getProducto()).isEqualTo(producto);

        producto.removeDetalleFactura(detalleFacturaBack);
        assertThat(producto.getDetalleFacturas()).doesNotContain(detalleFacturaBack);
        assertThat(detalleFacturaBack.getProducto()).isNull();

        producto.detalleFacturas(new HashSet<>(Set.of(detalleFacturaBack)));
        assertThat(producto.getDetalleFacturas()).containsOnly(detalleFacturaBack);
        assertThat(detalleFacturaBack.getProducto()).isEqualTo(producto);

        producto.setDetalleFacturas(new HashSet<>());
        assertThat(producto.getDetalleFacturas()).doesNotContain(detalleFacturaBack);
        assertThat(detalleFacturaBack.getProducto()).isNull();
    }

    @Test
    void modeloTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Modelo modeloBack = getModeloRandomSampleGenerator();

        producto.setModelo(modeloBack);
        assertThat(producto.getModelo()).isEqualTo(modeloBack);

        producto.modelo(null);
        assertThat(producto.getModelo()).isNull();
    }

    @Test
    void tipoProductoTest() {
        Producto producto = getProductoRandomSampleGenerator();
        TipoProducto tipoProductoBack = getTipoProductoRandomSampleGenerator();

        producto.setTipoProducto(tipoProductoBack);
        assertThat(producto.getTipoProducto()).isEqualTo(tipoProductoBack);

        producto.tipoProducto(null);
        assertThat(producto.getTipoProducto()).isNull();
    }

    @Test
    void proveedorTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Proveedor proveedorBack = getProveedorRandomSampleGenerator();

        producto.setProveedor(proveedorBack);
        assertThat(producto.getProveedor()).isEqualTo(proveedorBack);

        producto.proveedor(null);
        assertThat(producto.getProveedor()).isNull();
    }
}
