package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoCatalogoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoCatalogoDTO.class);
        TipoCatalogoDTO tipoCatalogoDTO1 = new TipoCatalogoDTO();
        tipoCatalogoDTO1.setId(1L);
        TipoCatalogoDTO tipoCatalogoDTO2 = new TipoCatalogoDTO();
        assertThat(tipoCatalogoDTO1).isNotEqualTo(tipoCatalogoDTO2);
        tipoCatalogoDTO2.setId(tipoCatalogoDTO1.getId());
        assertThat(tipoCatalogoDTO1).isEqualTo(tipoCatalogoDTO2);
        tipoCatalogoDTO2.setId(2L);
        assertThat(tipoCatalogoDTO1).isNotEqualTo(tipoCatalogoDTO2);
        tipoCatalogoDTO1.setId(null);
        assertThat(tipoCatalogoDTO1).isNotEqualTo(tipoCatalogoDTO2);
    }
}
