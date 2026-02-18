package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogoDTO.class);
        CatalogoDTO catalogoDTO1 = new CatalogoDTO();
        catalogoDTO1.setId(1L);
        CatalogoDTO catalogoDTO2 = new CatalogoDTO();
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
        catalogoDTO2.setId(catalogoDTO1.getId());
        assertThat(catalogoDTO1).isEqualTo(catalogoDTO2);
        catalogoDTO2.setId(2L);
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
        catalogoDTO1.setId(null);
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
    }
}
