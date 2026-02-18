package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompaniaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompaniaDTO.class);
        CompaniaDTO companiaDTO1 = new CompaniaDTO();
        companiaDTO1.setId(1L);
        CompaniaDTO companiaDTO2 = new CompaniaDTO();
        assertThat(companiaDTO1).isNotEqualTo(companiaDTO2);
        companiaDTO2.setId(companiaDTO1.getId());
        assertThat(companiaDTO1).isEqualTo(companiaDTO2);
        companiaDTO2.setId(2L);
        assertThat(companiaDTO1).isNotEqualTo(companiaDTO2);
        companiaDTO1.setId(null);
        assertThat(companiaDTO1).isNotEqualTo(companiaDTO2);
    }
}
