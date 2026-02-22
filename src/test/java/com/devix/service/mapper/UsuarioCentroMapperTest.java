package com.devix.service.mapper;

import static com.devix.domain.UsuarioCentroAsserts.*;
import static com.devix.domain.UsuarioCentroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioCentroMapperTest {

    private UsuarioCentroMapper usuarioCentroMapper;

    @BeforeEach
    void setUp() {
        usuarioCentroMapper = new UsuarioCentroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUsuarioCentroSample1();
        var actual = usuarioCentroMapper.toEntity(usuarioCentroMapper.toDto(expected));
        assertUsuarioCentroAllPropertiesEquals(expected, actual);
    }
}
