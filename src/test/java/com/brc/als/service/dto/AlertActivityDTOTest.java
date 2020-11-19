package com.brc.als.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brc.als.web.rest.TestUtil;

public class AlertActivityDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertActivityDTO.class);
        AlertActivityDTO alertActivityDTO1 = new AlertActivityDTO();
        alertActivityDTO1.setId(1L);
        AlertActivityDTO alertActivityDTO2 = new AlertActivityDTO();
        assertThat(alertActivityDTO1).isNotEqualTo(alertActivityDTO2);
        alertActivityDTO2.setId(alertActivityDTO1.getId());
        assertThat(alertActivityDTO1).isEqualTo(alertActivityDTO2);
        alertActivityDTO2.setId(2L);
        assertThat(alertActivityDTO1).isNotEqualTo(alertActivityDTO2);
        alertActivityDTO1.setId(null);
        assertThat(alertActivityDTO1).isNotEqualTo(alertActivityDTO2);
    }
}
