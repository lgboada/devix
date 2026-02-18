package com.devix.service.mapper;

import static com.devix.domain.TipoEventoAsserts.*;
import static com.devix.domain.TipoEventoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoEventoMapperTest {

    private TipoEventoMapper tipoEventoMapper;

    @BeforeEach
    void setUp() {
        tipoEventoMapper = new TipoEventoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoEventoSample1();
        var actual = tipoEventoMapper.toEntity(tipoEventoMapper.toDto(expected));
        assertTipoEventoAllPropertiesEquals(expected, actual);
    }
}
