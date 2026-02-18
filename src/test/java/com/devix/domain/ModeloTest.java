package com.devix.domain;

import static com.devix.domain.MarcaTestSamples.*;
import static com.devix.domain.ModeloTestSamples.*;
import static com.devix.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ModeloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modelo.class);
        Modelo modelo1 = getModeloSample1();
        Modelo modelo2 = new Modelo();
        assertThat(modelo1).isNotEqualTo(modelo2);

        modelo2.setId(modelo1.getId());
        assertThat(modelo1).isEqualTo(modelo2);

        modelo2 = getModeloSample2();
        assertThat(modelo1).isNotEqualTo(modelo2);
    }

    @Test
    void productoTest() {
        Modelo modelo = getModeloRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        modelo.addProducto(productoBack);
        assertThat(modelo.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getModelo()).isEqualTo(modelo);

        modelo.removeProducto(productoBack);
        assertThat(modelo.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getModelo()).isNull();

        modelo.productos(new HashSet<>(Set.of(productoBack)));
        assertThat(modelo.getProductos()).containsOnly(productoBack);
        assertThat(productoBack.getModelo()).isEqualTo(modelo);

        modelo.setProductos(new HashSet<>());
        assertThat(modelo.getProductos()).doesNotContain(productoBack);
        assertThat(productoBack.getModelo()).isNull();
    }

    @Test
    void marcaTest() {
        Modelo modelo = getModeloRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        modelo.setMarca(marcaBack);
        assertThat(modelo.getMarca()).isEqualTo(marcaBack);

        modelo.marca(null);
        assertThat(modelo.getMarca()).isNull();
    }
}
