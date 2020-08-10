package com.brc.als.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brc.als.web.rest.TestUtil;

public class AlertTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alert.class);
        Alert alert1 = new Alert();
        alert1.setId(1L);
        Alert alert2 = new Alert();
        alert2.setId(alert1.getId());
        assertThat(alert1).isEqualTo(alert2);
        alert2.setId(2L);
        assertThat(alert1).isNotEqualTo(alert2);
        alert1.setId(null);
        assertThat(alert1).isNotEqualTo(alert2);
    }
}
