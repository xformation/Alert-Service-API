package com.brc.als.web.rest;

import com.brc.als.service.AlertActivityService;
import com.brc.als.web.rest.errors.BadRequestAlertException;
import com.brc.als.service.dto.AlertActivityDTO;

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
 * REST controller for managing {@link com.brc.als.domain.AlertActivity}.
 */
@RestController
@RequestMapping("/api")
public class AlertActivityResource {

    private final Logger log = LoggerFactory.getLogger(AlertActivityResource.class);

    private static final String ENTITY_NAME = "alertserviceAlertActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertActivityService alertActivityService;

    public AlertActivityResource(AlertActivityService alertActivityService) {
        this.alertActivityService = alertActivityService;
    }

    /**
     * {@code POST  /alert-activities} : Create a new alertActivity.
     *
     * @param alertActivityDTO the alertActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertActivityDTO, or with status {@code 400 (Bad Request)} if the alertActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alert-activities")
    public ResponseEntity<AlertActivityDTO> createAlertActivity(@Valid @RequestBody AlertActivityDTO alertActivityDTO) throws URISyntaxException {
        log.debug("REST request to save AlertActivity : {}", alertActivityDTO);
        if (alertActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new alertActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlertActivityDTO result = alertActivityService.save(alertActivityDTO);
        return ResponseEntity.created(new URI("/api/alert-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alert-activities} : Updates an existing alertActivity.
     *
     * @param alertActivityDTO the alertActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertActivityDTO,
     * or with status {@code 400 (Bad Request)} if the alertActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alert-activities")
    public ResponseEntity<AlertActivityDTO> updateAlertActivity(@Valid @RequestBody AlertActivityDTO alertActivityDTO) throws URISyntaxException {
        log.debug("REST request to update AlertActivity : {}", alertActivityDTO);
        if (alertActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlertActivityDTO result = alertActivityService.save(alertActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alertActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alert-activities} : get all the alertActivities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertActivities in body.
     */
    @GetMapping("/alert-activities")
    public List<AlertActivityDTO> getAllAlertActivities() {
        log.debug("REST request to get all AlertActivities");
        return alertActivityService.findAll();
    }

    /**
     * {@code GET  /alert-activities/:id} : get the "id" alertActivity.
     *
     * @param id the id of the alertActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alert-activities/{id}")
    public ResponseEntity<AlertActivityDTO> getAlertActivity(@PathVariable Long id) {
        log.debug("REST request to get AlertActivity : {}", id);
        Optional<AlertActivityDTO> alertActivityDTO = alertActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alertActivityDTO);
    }

    /**
     * {@code DELETE  /alert-activities/:id} : delete the "id" alertActivity.
     *
     * @param id the id of the alertActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alert-activities/{id}")
    public ResponseEntity<Void> deleteAlertActivity(@PathVariable Long id) {
        log.debug("REST request to delete AlertActivity : {}", id);
        alertActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
