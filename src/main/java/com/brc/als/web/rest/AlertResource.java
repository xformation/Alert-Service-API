package com.brc.als.web.rest;

import com.brc.als.service.AlertService;
import com.brc.als.web.rest.errors.BadRequestAlertException;
import com.brc.als.service.dto.AlertDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brc.als.domain.Alert}.
 */
@RestController
@RequestMapping("/api")
public class AlertResource {

    private final Logger log = LoggerFactory.getLogger(AlertResource.class);

    private static final String ENTITY_NAME = "alertserviceAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertService alertService;

    public AlertResource(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * {@code POST  /alerts} : Create a new alert.
     *
     * @param alertDTO the alertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertDTO, or with status {@code 400 (Bad Request)} if the alert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alerts")
    public ResponseEntity<AlertDTO> createAlert(@Valid @RequestBody AlertDTO alertDTO) throws URISyntaxException {
        log.debug("REST request to save Alert : {}", alertDTO);
        if (alertDTO.getId() != null) {
            throw new BadRequestAlertException("A new alert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlertDTO result = alertService.save(alertDTO);
        return ResponseEntity.created(new URI("/api/alerts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alerts} : Updates an existing alert.
     *
     * @param alertDTO the alertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertDTO,
     * or with status {@code 400 (Bad Request)} if the alertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alerts")
    public ResponseEntity<AlertDTO> updateAlert(@Valid @RequestBody AlertDTO alertDTO) throws URISyntaxException {
        log.debug("REST request to update Alert : {}", alertDTO);
        if (alertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlertDTO result = alertService.save(alertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alertDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alerts} : get all the alerts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alerts in body.
     */
    @GetMapping("/alerts")
    public List<AlertDTO> getAllAlerts() {
        log.debug("REST request to get all Alerts");
        return alertService.findAll();
    }

    /**
     * {@code GET  /alerts/:id} : get the "id" alert.
     *
     * @param id the id of the alertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alerts/{id}")
    public ResponseEntity<AlertDTO> getAlert(@PathVariable Long id) {
        log.debug("REST request to get Alert : {}", id);
        Optional<AlertDTO> alertDTO = alertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alertDTO);
    }

    /**
     * {@code DELETE  /alerts/:id} : delete the "id" alert.
     *
     * @param id the id of the alertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        log.debug("REST request to delete Alert : {}", id);
        alertService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
