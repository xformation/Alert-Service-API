package com.brc.als.service.impl;

import com.brc.als.service.AlertActivityService;
import com.brc.als.domain.AlertActivity;
import com.brc.als.repository.AlertActivityRepository;
import com.brc.als.service.dto.AlertActivityDTO;
import com.brc.als.service.mapper.AlertActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AlertActivity}.
 */
@Service
@Transactional
public class AlertActivityServiceImpl implements AlertActivityService {

    private final Logger log = LoggerFactory.getLogger(AlertActivityServiceImpl.class);

    private final AlertActivityRepository alertActivityRepository;

    private final AlertActivityMapper alertActivityMapper;

    public AlertActivityServiceImpl(AlertActivityRepository alertActivityRepository, AlertActivityMapper alertActivityMapper) {
        this.alertActivityRepository = alertActivityRepository;
        this.alertActivityMapper = alertActivityMapper;
    }

    /**
     * Save a alertActivity.
     *
     * @param alertActivityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AlertActivityDTO save(AlertActivityDTO alertActivityDTO) {
        log.debug("Request to save AlertActivity : {}", alertActivityDTO);
        AlertActivity alertActivity = alertActivityMapper.toEntity(alertActivityDTO);
        alertActivity = alertActivityRepository.save(alertActivity);
        return alertActivityMapper.toDto(alertActivity);
    }

    /**
     * Get all the alertActivities.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlertActivityDTO> findAll() {
        log.debug("Request to get all AlertActivities");
        return alertActivityRepository.findAll().stream()
            .map(alertActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one alertActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AlertActivityDTO> findOne(Long id) {
        log.debug("Request to get AlertActivity : {}", id);
        return alertActivityRepository.findById(id)
            .map(alertActivityMapper::toDto);
    }

    /**
     * Delete the alertActivity by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AlertActivity : {}", id);
        alertActivityRepository.deleteById(id);
    }
}
