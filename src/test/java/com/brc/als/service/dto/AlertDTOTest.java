package com.brc.als.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brc.als.web.rest.TestUtil;

public class AlertDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertDTO.class);
        AlertDTO alertDTO1 = new AlertDTO();
        alertDTO1.setId(1L);
        AlertDTO alertDTO2 = new AlertDTO();
        assertThat(alertDTO1).isNotEqualTo(alertDTO2);
        alertDTO2.setId(alertDTO1.getId());
        assertThat(alertDTO1).isEqualTo(alertDTO2);
        alertDTO2.setId(2L);
        assertThat(alertDTO1).isNotEqualTo(alertDTO2);
        alertDTO1.setId(null);
        assertThat(alertDTO1).isNotEqualTo(alertDTO2);
    }
}
