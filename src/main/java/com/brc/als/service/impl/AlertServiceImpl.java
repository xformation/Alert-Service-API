package com.brc.als.service.impl;

import com.brc.als.service.AlertService;
import com.brc.als.domain.Alert;
import com.brc.als.repository.AlertRepository;
import com.brc.als.service.dto.AlertDTO;
import com.brc.als.service.mapper.AlertMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Alert}.
 */
@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertServiceImpl(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AlertDTO save(AlertDTO alertDTO) {
        log.debug("Request to save Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    /**
     * Get all the alerts.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlertDTO> findAll() {
        log.debug("Request to get all Alerts");
        return alertRepository.findAll().stream()
            .map(alertMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one alert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AlertDTO> findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        return alertRepository.findById(id)
            .map(alertMapper::toDto);
    }

    /**
     * Delete the alert by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        alertRepository.deleteById(id);
    }
}
