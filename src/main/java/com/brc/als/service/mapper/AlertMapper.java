package com.brc.als.service.mapper;


import com.brc.als.domain.*;
import com.brc.als.service.dto.AlertDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {



    default Alert fromId(Long id) {
        if (id == null) {
            return null;
        }
        Alert alert = new Alert();
        alert.setId(id);
        return alert;
    }
}
