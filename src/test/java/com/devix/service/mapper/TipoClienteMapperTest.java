package com.devix.service.mapper;

import static com.devix.domain.TipoClienteAsserts.*;
import static com.devix.domain.TipoClienteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoClienteMapperTest {

    private TipoClienteMapper tipoClienteMapper;

    @BeforeEach
    void setUp() {
        tipoClienteMapper = new TipoClienteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoClienteSample1();
        var actual = tipoClienteMapper.toEntity(tipoClienteMapper.toDto(expected));
        assertTipoClienteAllPropertiesEquals(expected, actual);
    }
}
