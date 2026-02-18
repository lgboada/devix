package com.devix.service.mapper;

import static com.devix.domain.TipoCatalogoAsserts.*;
import static com.devix.domain.TipoCatalogoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoCatalogoMapperTest {

    private TipoCatalogoMapper tipoCatalogoMapper;

    @BeforeEach
    void setUp() {
        tipoCatalogoMapper = new TipoCatalogoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoCatalogoSample1();
        var actual = tipoCatalogoMapper.toEntity(tipoCatalogoMapper.toDto(expected));
        assertTipoCatalogoAllPropertiesEquals(expected, actual);
    }
}
