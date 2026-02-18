package com.devix.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetalleFacturaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleFacturaDTO.class);
        DetalleFacturaDTO detalleFacturaDTO1 = new DetalleFacturaDTO();
        detalleFacturaDTO1.setId(1L);
        DetalleFacturaDTO detalleFacturaDTO2 = new DetalleFacturaDTO();
        assertThat(detalleFacturaDTO1).isNotEqualTo(detalleFacturaDTO2);
        detalleFacturaDTO2.setId(detalleFacturaDTO1.getId());
        assertThat(detalleFacturaDTO1).isEqualTo(detalleFacturaDTO2);
        detalleFacturaDTO2.setId(2L);
        assertThat(detalleFacturaDTO1).isNotEqualTo(detalleFacturaDTO2);
        detalleFacturaDTO1.setId(null);
        assertThat(detalleFacturaDTO1).isNotEqualTo(detalleFacturaDTO2);
    }
}
