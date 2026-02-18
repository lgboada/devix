package com.devix.service.mapper;

import static com.devix.domain.TipoDireccionAsserts.*;
import static com.devix.domain.TipoDireccionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoDireccionMapperTest {

    private TipoDireccionMapper tipoDireccionMapper;

    @BeforeEach
    void setUp() {
        tipoDireccionMapper = new TipoDireccionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoDireccionSample1();
        var actual = tipoDireccionMapper.toEntity(tipoDireccionMapper.toDto(expected));
        assertTipoDireccionAllPropertiesEquals(expected, actual);
    }
}
