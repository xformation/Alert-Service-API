package com.brc.als.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AlertActivityMapperTest {

    private AlertActivityMapper alertActivityMapper;

    @BeforeEach
    public void setUp() {
//        alertActivityMapper = new AlertActivityMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
//        assertThat(alertActivityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(alertActivityMapper.fromId(null)).isNull();
    }
}
