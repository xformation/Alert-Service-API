package com.brc.als.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brc.als.web.rest.TestUtil;

public class AlertActivityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertActivity.class);
        AlertActivity alertActivity1 = new AlertActivity();
//        alertActivity1.setId(1L);
        AlertActivity alertActivity2 = new AlertActivity();
//        alertActivity2.setId(alertActivity1.getId());
        assertThat(alertActivity1).isEqualTo(alertActivity2);
//        alertActivity2.setId(2L);
        assertThat(alertActivity1).isNotEqualTo(alertActivity2);
//        alertActivity1.setId(null);
        assertThat(alertActivity1).isNotEqualTo(alertActivity2);
    }
}
