package com.brc.als.service;

import com.brc.als.service.dto.AlertDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brc.als.domain.Alert}.
 */
public interface AlertService {

    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    AlertDTO save(AlertDTO alertDTO);

    /**
     * Get all the alerts.
     *
     * @return the list of entities.
     */
    List<AlertDTO> findAll();


    /**
     * Get the "id" alert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertDTO> findOne(Long id);

    /**
     * Delete the "id" alert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
