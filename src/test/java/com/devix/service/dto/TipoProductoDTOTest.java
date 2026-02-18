package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoProductoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoProductoDTO.class);
        TipoProductoDTO tipoProductoDTO1 = new TipoProductoDTO();
        tipoProductoDTO1.setId(1L);
        TipoProductoDTO tipoProductoDTO2 = new TipoProductoDTO();
        assertThat(tipoProductoDTO1).isNotEqualTo(tipoProductoDTO2);
        tipoProductoDTO2.setId(tipoProductoDTO1.getId());
        assertThat(tipoProductoDTO1).isEqualTo(tipoProductoDTO2);
        tipoProductoDTO2.setId(2L);
        assertThat(tipoProductoDTO1).isNotEqualTo(tipoProductoDTO2);
        tipoProductoDTO1.setId(null);
        assertThat(tipoProductoDTO1).isNotEqualTo(tipoProductoDTO2);
    }
}
