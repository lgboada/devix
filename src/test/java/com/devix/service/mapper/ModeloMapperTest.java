package com.devix.service.mapper;

import static com.devix.domain.ModeloAsserts.*;
import static com.devix.domain.ModeloTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModeloMapperTest {

    private ModeloMapper modeloMapper;

    @BeforeEach
    void setUp() {
        modeloMapper = new ModeloMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModeloSample1();
        var actual = modeloMapper.toEntity(modeloMapper.toDto(expected));
        assertModeloAllPropertiesEquals(expected, actual);
    }
}
