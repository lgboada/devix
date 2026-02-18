package com.devix.service.mapper;

import static com.devix.domain.CentroAsserts.*;
import static com.devix.domain.CentroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CentroMapperTest {

    private CentroMapper centroMapper;

    @BeforeEach
    void setUp() {
        centroMapper = new CentroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCentroSample1();
        var actual = centroMapper.toEntity(centroMapper.toDto(expected));
        assertCentroAllPropertiesEquals(expected, actual);
    }
}
