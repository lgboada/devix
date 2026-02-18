package com.devix.service.mapper;

import static com.devix.domain.CompaniaAsserts.*;
import static com.devix.domain.CompaniaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompaniaMapperTest {

    private CompaniaMapper companiaMapper;

    @BeforeEach
    void setUp() {
        companiaMapper = new CompaniaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompaniaSample1();
        var actual = companiaMapper.toEntity(companiaMapper.toDto(expected));
        assertCompaniaAllPropertiesEquals(expected, actual);
    }
}
