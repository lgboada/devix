package com.devix.domain;

import static com.devix.domain.ProductoTestSamples.*;
import static com.devix.domain.TipoProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TipoProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoProducto.class);
        TipoProducto tipoProducto1 = getTipoProductoSample1();
        TipoProducto tipoProducto2 = new TipoProducto();
        assertThat(tipoProducto1).isNotEqualTo(tipoProducto2);

        tipoProducto2.setId(tipoProducto1.getId());
        assertThat(tipoProducto1).isEqualTo(tipoProducto2);

        tipoProducto2 = getTipoProductoSample2();
        assertThat(tipoProducto1).isNotEqualTo(tipoProducto2);
    }

    @Test
    void productoTest() {
        TipoProducto tipoProducto = getTipoProductoRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        tipoProducto.addProducto(productoBack);
        assertThat(tipoProducto.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getTipoProducto()).isEqualTo(tipoProducto);

        tipoProducto.removeProducto(productoBack);
        assertThat(tipoProducto.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getTipoProducto()).isNull();

        tipoProducto.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(tipoProducto.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getTipoProducto()).isEqualTo(tipoProducto);

        tipoProducto.setProductos(new HashSet<>());
        assertThat(tipoProducto.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getTipoProducto()).isNull();
    }
}
