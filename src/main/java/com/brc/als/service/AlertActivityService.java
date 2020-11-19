package com.brc.als.service;

import com.brc.als.service.dto.AlertActivityDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brc.als.domain.AlertActivity}.
 */
public interface AlertActivityService {

    /**
     * Save a alertActivity.
     *
     * @param alertActivityDTO the entity to save.
     * @return the persisted entity.
     */
    AlertActivityDTO save(AlertActivityDTO alertActivityDTO);

    /**
     * Get all the alertActivities.
     *
     * @return the list of entities.
     */
    List<AlertActivityDTO> findAll();


    /**
     * Get the "id" alertActivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertActivityDTO> findOne(Long id);

    /**
     * Delete the "id" alertActivity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
