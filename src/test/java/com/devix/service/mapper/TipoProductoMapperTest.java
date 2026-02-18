package com.devix.service.mapper;

import static com.devix.domain.TipoProductoAsserts.*;
import static com.devix.domain.TipoProductoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoProductoMapperTest {

    private TipoProductoMapper tipoProductoMapper;

    @BeforeEach
    void setUp() {
        tipoProductoMapper = new TipoProductoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoProductoSample1();
        var actual = tipoProductoMapper.toEntity(tipoProductoMapper.toDto(expected));
        assertTipoProductoAllPropertiesEquals(expected, actual);
    }
}
