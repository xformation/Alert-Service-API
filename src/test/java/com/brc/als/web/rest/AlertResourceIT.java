package com.brc.als.web.rest;

import com.brc.als.AlertserviceApp;
import com.brc.als.domain.Alert;
import com.brc.als.repository.AlertRepository;
import com.brc.als.service.AlertService;
import com.brc.als.service.dto.AlertDTO;
import com.brc.als.service.mapper.AlertMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AlertResource} REST controller.
 */
@SpringBootTest(classes = AlertserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AlertResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SEVERITY = "AAAAAAAAAA";
    private static final String UPDATED_SEVERITY = "BBBBBBBBBB";

    private static final String DEFAULT_MONITORCONDITION = "AAAAAAAAAA";
    private static final String UPDATED_MONITORCONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_AFFECTEDRESOURCE = "AAAAAAAAAA";
    private static final String UPDATED_AFFECTEDRESOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_MONITORSERVICE = "AAAAAAAAAA";
    private static final String UPDATED_MONITORSERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNALTYPE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNALTYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BRCSUBSCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BRCSUBSCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPRESSIONSTATE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPRESSIONSTATE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCEGROUP = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCEGROUP = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCES = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCES = "BBBBBBBBBB";

    private static final String DEFAULT_FIREDTIME = "AAAAAAAAAA";
    private static final String UPDATED_FIREDTIME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ALERT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_INCIDENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_INCIDENT_KEY = "BBBBBBBBBB";

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private AlertService alertService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertMockMvc;

    private Alert alert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createEntity(EntityManager em) {
        Alert alert = new Alert()
            .guid(DEFAULT_GUID)
            .name(DEFAULT_NAME)
            .severity(DEFAULT_SEVERITY)
            .monitorcondition(DEFAULT_MONITORCONDITION)
            .affectedresource(DEFAULT_AFFECTEDRESOURCE)
            .monitorservice(DEFAULT_MONITORSERVICE)
            .signaltype(DEFAULT_SIGNALTYPE)
            .brcsubscription(DEFAULT_BRCSUBSCRIPTION)
            .suppressionstate(DEFAULT_SUPPRESSIONSTATE)
            .resourcegroup(DEFAULT_RESOURCEGROUP)
            .resources(DEFAULT_RESOURCES)
            .firedtime(DEFAULT_FIREDTIME)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .alertState(DEFAULT_ALERT_STATE)
            .client(DEFAULT_CLIENT)
            .clientUrl(DEFAULT_CLIENT_URL)
            .description(DEFAULT_DESCRIPTION)
            .details(DEFAULT_DETAILS)
            .incidentKey(DEFAULT_INCIDENT_KEY);
        return alert;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createUpdatedEntity(EntityManager em) {
        Alert alert = new Alert()
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME)
            .severity(UPDATED_SEVERITY)
            .monitorcondition(UPDATED_MONITORCONDITION)
            .affectedresource(UPDATED_AFFECTEDRESOURCE)
            .monitorservice(UPDATED_MONITORSERVICE)
            .signaltype(UPDATED_SIGNALTYPE)
            .brcsubscription(UPDATED_BRCSUBSCRIPTION)
            .suppressionstate(UPDATED_SUPPRESSIONSTATE)
            .resourcegroup(UPDATED_RESOURCEGROUP)
            .resources(UPDATED_RESOURCES)
            .firedtime(UPDATED_FIREDTIME)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .alertState(UPDATED_ALERT_STATE)
            .client(UPDATED_CLIENT)
            .clientUrl(UPDATED_CLIENT_URL)
            .description(UPDATED_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .incidentKey(UPDATED_INCIDENT_KEY);
        return alert;
    }

    @BeforeEach
    public void initTest() {
        alert = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlert() throws Exception {
        int databaseSizeBeforeCreate = alertRepository.findAll().size();
        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);
        restAlertMockMvc.perform(post("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isCreated());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate + 1);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testAlert.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlert.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testAlert.getMonitorcondition()).isEqualTo(DEFAULT_MONITORCONDITION);
        assertThat(testAlert.getAffectedresource()).isEqualTo(DEFAULT_AFFECTEDRESOURCE);
        assertThat(testAlert.getMonitorservice()).isEqualTo(DEFAULT_MONITORSERVICE);
        assertThat(testAlert.getSignaltype()).isEqualTo(DEFAULT_SIGNALTYPE);
        assertThat(testAlert.getBrcsubscription()).isEqualTo(DEFAULT_BRCSUBSCRIPTION);
        assertThat(testAlert.getSuppressionstate()).isEqualTo(DEFAULT_SUPPRESSIONSTATE);
        assertThat(testAlert.getResourcegroup()).isEqualTo(DEFAULT_RESOURCEGROUP);
        assertThat(testAlert.getResources()).isEqualTo(DEFAULT_RESOURCES);
        assertThat(testAlert.getFiredtime()).isEqualTo(DEFAULT_FIREDTIME);
        assertThat(testAlert.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAlert.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testAlert.getAlertState()).isEqualTo(DEFAULT_ALERT_STATE);
        assertThat(testAlert.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testAlert.getClientUrl()).isEqualTo(DEFAULT_CLIENT_URL);
        assertThat(testAlert.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlert.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testAlert.getIncidentKey()).isEqualTo(DEFAULT_INCIDENT_KEY);
    }

    @Test
    @Transactional
    public void createAlertWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alertRepository.findAll().size();

        // Create the Alert with an existing ID
//        alert.setId(1L);
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertMockMvc.perform(post("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAlerts() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList
        restAlertMockMvc.perform(get("/api/alerts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].monitorcondition").value(hasItem(DEFAULT_MONITORCONDITION)))
            .andExpect(jsonPath("$.[*].affectedresource").value(hasItem(DEFAULT_AFFECTEDRESOURCE)))
            .andExpect(jsonPath("$.[*].monitorservice").value(hasItem(DEFAULT_MONITORSERVICE)))
            .andExpect(jsonPath("$.[*].signaltype").value(hasItem(DEFAULT_SIGNALTYPE)))
            .andExpect(jsonPath("$.[*].brcsubscription").value(hasItem(DEFAULT_BRCSUBSCRIPTION)))
            .andExpect(jsonPath("$.[*].suppressionstate").value(hasItem(DEFAULT_SUPPRESSIONSTATE)))
            .andExpect(jsonPath("$.[*].resourcegroup").value(hasItem(DEFAULT_RESOURCEGROUP)))
            .andExpect(jsonPath("$.[*].resources").value(hasItem(DEFAULT_RESOURCES)))
            .andExpect(jsonPath("$.[*].firedtime").value(hasItem(DEFAULT_FIREDTIME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].alertState").value(hasItem(DEFAULT_ALERT_STATE)))
            .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT)))
            .andExpect(jsonPath("$.[*].clientUrl").value(hasItem(DEFAULT_CLIENT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].incidentKey").value(hasItem(DEFAULT_INCIDENT_KEY)));
    }
    
    @Test
    @Transactional
    public void getAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{guid}", alert.getGuid()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(alert.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY))
            .andExpect(jsonPath("$.monitorcondition").value(DEFAULT_MONITORCONDITION))
            .andExpect(jsonPath("$.affectedresource").value(DEFAULT_AFFECTEDRESOURCE))
            .andExpect(jsonPath("$.monitorservice").value(DEFAULT_MONITORSERVICE))
            .andExpect(jsonPath("$.signaltype").value(DEFAULT_SIGNALTYPE))
            .andExpect(jsonPath("$.brcsubscription").value(DEFAULT_BRCSUBSCRIPTION))
            .andExpect(jsonPath("$.suppressionstate").value(DEFAULT_SUPPRESSIONSTATE))
            .andExpect(jsonPath("$.resourcegroup").value(DEFAULT_RESOURCEGROUP))
            .andExpect(jsonPath("$.resources").value(DEFAULT_RESOURCES))
            .andExpect(jsonPath("$.firedtime").value(DEFAULT_FIREDTIME))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.alertState").value(DEFAULT_ALERT_STATE))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT))
            .andExpect(jsonPath("$.clientUrl").value(DEFAULT_CLIENT_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.incidentKey").value(DEFAULT_INCIDENT_KEY));
    }
    @Test
    @Transactional
    public void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert
        Alert updatedAlert = null; //alertRepository.findById(alert.getGuid()).get();
        // Disconnect from session so that the updates on updatedAlert are not directly saved in db
        em.detach(updatedAlert);
        updatedAlert
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME)
            .severity(UPDATED_SEVERITY)
            .monitorcondition(UPDATED_MONITORCONDITION)
            .affectedresource(UPDATED_AFFECTEDRESOURCE)
            .monitorservice(UPDATED_MONITORSERVICE)
            .signaltype(UPDATED_SIGNALTYPE)
            .brcsubscription(UPDATED_BRCSUBSCRIPTION)
            .suppressionstate(UPDATED_SUPPRESSIONSTATE)
            .resourcegroup(UPDATED_RESOURCEGROUP)
            .resources(UPDATED_RESOURCES)
            .firedtime(UPDATED_FIREDTIME)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .alertState(UPDATED_ALERT_STATE)
            .client(UPDATED_CLIENT)
            .clientUrl(UPDATED_CLIENT_URL)
            .description(UPDATED_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .incidentKey(UPDATED_INCIDENT_KEY);
        AlertDTO alertDTO = alertMapper.toDto(updatedAlert);

        restAlertMockMvc.perform(put("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testAlert.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlert.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testAlert.getMonitorcondition()).isEqualTo(UPDATED_MONITORCONDITION);
        assertThat(testAlert.getAffectedresource()).isEqualTo(UPDATED_AFFECTEDRESOURCE);
        assertThat(testAlert.getMonitorservice()).isEqualTo(UPDATED_MONITORSERVICE);
        assertThat(testAlert.getSignaltype()).isEqualTo(UPDATED_SIGNALTYPE);
        assertThat(testAlert.getBrcsubscription()).isEqualTo(UPDATED_BRCSUBSCRIPTION);
        assertThat(testAlert.getSuppressionstate()).isEqualTo(UPDATED_SUPPRESSIONSTATE);
        assertThat(testAlert.getResourcegroup()).isEqualTo(UPDATED_RESOURCEGROUP);
        assertThat(testAlert.getResources()).isEqualTo(UPDATED_RESOURCES);
        assertThat(testAlert.getFiredtime()).isEqualTo(UPDATED_FIREDTIME);
        assertThat(testAlert.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAlert.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testAlert.getAlertState()).isEqualTo(UPDATED_ALERT_STATE);
        assertThat(testAlert.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testAlert.getClientUrl()).isEqualTo(UPDATED_CLIENT_URL);
        assertThat(testAlert.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlert.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testAlert.getIncidentKey()).isEqualTo(UPDATED_INCIDENT_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc.perform(put("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeDelete = alertRepository.findAll().size();

        // Delete the alert
//        restAlertMockMvc.perform(delete("/api/alerts/{id}", alert.getId())
//            .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
