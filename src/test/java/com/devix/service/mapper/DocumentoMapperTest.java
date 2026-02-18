package com.devix.service.mapper;

import static com.devix.domain.DocumentoAsserts.*;
import static com.devix.domain.DocumentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentoMapperTest {

    private DocumentoMapper documentoMapper;

    @BeforeEach
    void setUp() {
        documentoMapper = new DocumentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentoSample1();
        var actual = documentoMapper.toEntity(documentoMapper.toDto(expected));
        assertDocumentoAllPropertiesEquals(expected, actual);
    }
}
