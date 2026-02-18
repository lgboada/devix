package com.devix.service.mapper;

import static com.devix.domain.DetalleFacturaAsserts.*;
import static com.devix.domain.DetalleFacturaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetalleFacturaMapperTest {

    private DetalleFacturaMapper detalleFacturaMapper;

    @BeforeEach
    void setUp() {
        detalleFacturaMapper = new DetalleFacturaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDetalleFacturaSample1();
        var actual = detalleFacturaMapper.toEntity(detalleFacturaMapper.toDto(expected));
        assertDetalleFacturaAllPropertiesEquals(expected, actual);
    }
}
