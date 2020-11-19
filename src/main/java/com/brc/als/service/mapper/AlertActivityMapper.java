package com.brc.als.service.mapper;


import com.brc.als.domain.*;
import com.brc.als.service.dto.AlertActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlertActivity} and its DTO {@link AlertActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlertActivityMapper extends EntityMapper<AlertActivityDTO, AlertActivity> {



    default AlertActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        AlertActivity alertActivity = new AlertActivity();
//        alertActivity.setId(id);
        return alertActivity;
    }
}
