package com.devix.service.mapper;

import static com.devix.domain.CatalogoAsserts.*;
import static com.devix.domain.CatalogoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogoMapperTest {

    private CatalogoMapper catalogoMapper;

    @BeforeEach
    void setUp() {
        catalogoMapper = new CatalogoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCatalogoSample1();
        var actual = catalogoMapper.toEntity(catalogoMapper.toDto(expected));
        assertCatalogoAllPropertiesEquals(expected, actual);
    }
}
