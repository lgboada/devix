package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaisDTO.class);
        PaisDTO paisDTO1 = new PaisDTO();
        paisDTO1.setId(1L);
        PaisDTO paisDTO2 = new PaisDTO();
        assertThat(paisDTO1).isNotEqualTo(paisDTO2);
        paisDTO2.setId(paisDTO1.getId());
        assertThat(paisDTO1).isEqualTo(paisDTO2);
        paisDTO2.setId(2L);
        assertThat(paisDTO1).isNotEqualTo(paisDTO2);
        paisDTO1.setId(null);
        assertThat(paisDTO1).isNotEqualTo(paisDTO2);
    }
}
