package com.devix.service.mapper;

import static com.devix.domain.ProvinciaAsserts.*;
import static com.devix.domain.ProvinciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProvinciaMapperTest {

    private ProvinciaMapper provinciaMapper;

    @BeforeEach
    void setUp() {
        provinciaMapper = new ProvinciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProvinciaSample1();
        var actual = provinciaMapper.toEntity(provinciaMapper.toDto(expected));
        assertProvinciaAllPropertiesEquals(expected, actual);
    }
}
