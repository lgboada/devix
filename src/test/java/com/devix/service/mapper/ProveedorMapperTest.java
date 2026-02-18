package com.devix.service.mapper;

import static com.devix.domain.ProveedorAsserts.*;
import static com.devix.domain.ProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProveedorMapperTest {

    private ProveedorMapper proveedorMapper;

    @BeforeEach
    void setUp() {
        proveedorMapper = new ProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProveedorSample1();
        var actual = proveedorMapper.toEntity(proveedorMapper.toDto(expected));
        assertProveedorAllPropertiesEquals(expected, actual);
    }
}
