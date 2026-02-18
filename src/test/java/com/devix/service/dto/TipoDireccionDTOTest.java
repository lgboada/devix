package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoDireccionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDireccionDTO.class);
        TipoDireccionDTO tipoDireccionDTO1 = new TipoDireccionDTO();
        tipoDireccionDTO1.setId(1L);
        TipoDireccionDTO tipoDireccionDTO2 = new TipoDireccionDTO();
        assertThat(tipoDireccionDTO1).isNotEqualTo(tipoDireccionDTO2);
        tipoDireccionDTO2.setId(tipoDireccionDTO1.getId());
        assertThat(tipoDireccionDTO1).isEqualTo(tipoDireccionDTO2);
        tipoDireccionDTO2.setId(2L);
        assertThat(tipoDireccionDTO1).isNotEqualTo(tipoDireccionDTO2);
        tipoDireccionDTO1.setId(null);
        assertThat(tipoDireccionDTO1).isNotEqualTo(tipoDireccionDTO2);
    }
}
