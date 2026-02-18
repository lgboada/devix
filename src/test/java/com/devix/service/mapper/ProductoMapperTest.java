package com.devix.service.mapper;

import static com.devix.domain.ProductoAsserts.*;
import static com.devix.domain.ProductoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoMapperTest {

    private ProductoMapper productoMapper;

    @BeforeEach
    void setUp() {
        productoMapper = new ProductoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductoSample1();
        var actual = productoMapper.toEntity(productoMapper.toDto(expected));
        assertProductoAllPropertiesEquals(expected, actual);
    }
}
