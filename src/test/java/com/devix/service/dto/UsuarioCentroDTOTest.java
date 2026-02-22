package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioCentroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioCentroDTO.class);
        UsuarioCentroDTO usuarioCentroDTO1 = new UsuarioCentroDTO();
        usuarioCentroDTO1.setId(1L);
        UsuarioCentroDTO usuarioCentroDTO2 = new UsuarioCentroDTO();
        assertThat(usuarioCentroDTO1).isNotEqualTo(usuarioCentroDTO2);
        usuarioCentroDTO2.setId(usuarioCentroDTO1.getId());
        assertThat(usuarioCentroDTO1).isEqualTo(usuarioCentroDTO2);
        usuarioCentroDTO2.setId(2L);
        assertThat(usuarioCentroDTO1).isNotEqualTo(usuarioCentroDTO2);
        usuarioCentroDTO1.setId(null);
        assertThat(usuarioCentroDTO1).isNotEqualTo(usuarioCentroDTO2);
    }
}
