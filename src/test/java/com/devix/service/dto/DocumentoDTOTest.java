package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoDTO.class);
        DocumentoDTO documentoDTO1 = new DocumentoDTO();
        documentoDTO1.setId(1L);
        DocumentoDTO documentoDTO2 = new DocumentoDTO();
        assertThat(documentoDTO1).isNotEqualTo(documentoDTO2);
        documentoDTO2.setId(documentoDTO1.getId());
        assertThat(documentoDTO1).isEqualTo(documentoDTO2);
        documentoDTO2.setId(2L);
        assertThat(documentoDTO1).isNotEqualTo(documentoDTO2);
        documentoDTO1.setId(null);
        assertThat(documentoDTO1).isNotEqualTo(documentoDTO2);
    }
}
