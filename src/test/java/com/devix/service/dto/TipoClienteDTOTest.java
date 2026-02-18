package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoClienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoClienteDTO.class);
        TipoClienteDTO tipoClienteDTO1 = new TipoClienteDTO();
        tipoClienteDTO1.setId(1L);
        TipoClienteDTO tipoClienteDTO2 = new TipoClienteDTO();
        assertThat(tipoClienteDTO1).isNotEqualTo(tipoClienteDTO2);
        tipoClienteDTO2.setId(tipoClienteDTO1.getId());
        assertThat(tipoClienteDTO1).isEqualTo(tipoClienteDTO2);
        tipoClienteDTO2.setId(2L);
        assertThat(tipoClienteDTO1).isNotEqualTo(tipoClienteDTO2);
        tipoClienteDTO1.setId(null);
        assertThat(tipoClienteDTO1).isNotEqualTo(tipoClienteDTO2);
    }
}
