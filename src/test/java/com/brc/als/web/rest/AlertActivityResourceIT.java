package com.brc.als.web.rest;

import com.brc.als.AlertserviceApp;
import com.brc.als.domain.AlertActivity;
import com.brc.als.repository.AlertActivityRepository;
import com.brc.als.service.AlertActivityService;
import com.brc.als.service.dto.AlertActivityDTO;
import com.brc.als.service.mapper.AlertActivityMapper;

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
 * Integration tests for the {@link AlertActivityResource} REST controller.
 */
@SpringBootTest(classes = AlertserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AlertActivityResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ALERT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_STATE = "BBBBBBBBBB";

    private static final Long DEFAULT_TICKET_ID = 1L;
    private static final Long UPDATED_TICKET_ID = 2L;

    private static final String DEFAULT_TICKET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TICKET_URL = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TICKET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_LOG = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_LOG = "BBBBBBBBBB";

    private static final Instant DEFAULT_FIRED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIRED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AlertActivityRepository alertActivityRepository;

    @Autowired
    private AlertActivityMapper alertActivityMapper;

    @Autowired
    private AlertActivityService alertActivityService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertActivityMockMvc;

    private AlertActivity alertActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertActivity createEntity(EntityManager em) {
        AlertActivity alertActivity = new AlertActivity()
            .guid(DEFAULT_GUID)
            .name(DEFAULT_NAME)
            .action(DEFAULT_ACTION)
            .actionDescription(DEFAULT_ACTION_DESCRIPTION)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .alertState(DEFAULT_ALERT_STATE)
            .ticketId(DEFAULT_TICKET_ID)
            .ticketName(DEFAULT_TICKET_NAME)
            .ticketUrl(DEFAULT_TICKET_URL)
            .ticketDescription(DEFAULT_TICKET_DESCRIPTION)
            .userName(DEFAULT_USER_NAME)
            .eventType(DEFAULT_EVENT_TYPE)
            .changeLog(DEFAULT_CHANGE_LOG)
            .firedTime(DEFAULT_FIRED_TIME);
        return alertActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertActivity createUpdatedEntity(EntityManager em) {
        AlertActivity alertActivity = new AlertActivity()
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME)
            .action(UPDATED_ACTION)
            .actionDescription(UPDATED_ACTION_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .alertState(UPDATED_ALERT_STATE)
            .ticketId(UPDATED_TICKET_ID)
            .ticketName(UPDATED_TICKET_NAME)
            .ticketUrl(UPDATED_TICKET_URL)
            .ticketDescription(UPDATED_TICKET_DESCRIPTION)
            .userName(UPDATED_USER_NAME)
            .eventType(UPDATED_EVENT_TYPE)
            .changeLog(UPDATED_CHANGE_LOG)
            .firedTime(UPDATED_FIRED_TIME);
        return alertActivity;
    }

    @BeforeEach
    public void initTest() {
        alertActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlertActivity() throws Exception {
        int databaseSizeBeforeCreate = alertActivityRepository.findAll().size();
        // Create the AlertActivity
        AlertActivityDTO alertActivityDTO = alertActivityMapper.toDto(alertActivity);
        restAlertActivityMockMvc.perform(post("/api/alert-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the AlertActivity in the database
        List<AlertActivity> alertActivityList = alertActivityRepository.findAll();
        assertThat(alertActivityList).hasSize(databaseSizeBeforeCreate + 1);
        AlertActivity testAlertActivity = alertActivityList.get(alertActivityList.size() - 1);
        assertThat(testAlertActivity.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testAlertActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlertActivity.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAlertActivity.getActionDescription()).isEqualTo(DEFAULT_ACTION_DESCRIPTION);
        assertThat(testAlertActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAlertActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testAlertActivity.getAlertState()).isEqualTo(DEFAULT_ALERT_STATE);
        assertThat(testAlertActivity.getTicketId()).isEqualTo(DEFAULT_TICKET_ID);
        assertThat(testAlertActivity.getTicketName()).isEqualTo(DEFAULT_TICKET_NAME);
        assertThat(testAlertActivity.getTicketUrl()).isEqualTo(DEFAULT_TICKET_URL);
        assertThat(testAlertActivity.getTicketDescription()).isEqualTo(DEFAULT_TICKET_DESCRIPTION);
        assertThat(testAlertActivity.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testAlertActivity.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testAlertActivity.getChangeLog()).isEqualTo(DEFAULT_CHANGE_LOG);
        assertThat(testAlertActivity.getFiredTime()).isEqualTo(DEFAULT_FIRED_TIME);
    }

    @Test
    @Transactional
    public void createAlertActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alertActivityRepository.findAll().size();

        // Create the AlertActivity with an existing ID
//        alertActivity.setId(1L);
        AlertActivityDTO alertActivityDTO = alertActivityMapper.toDto(alertActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertActivityMockMvc.perform(post("/api/alert-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlertActivity in the database
        List<AlertActivity> alertActivityList = alertActivityRepository.findAll();
        assertThat(alertActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAlertActivities() throws Exception {
        // Initialize the database
        alertActivityRepository.saveAndFlush(alertActivity);

        // Get all the alertActivityList
        restAlertActivityMockMvc.perform(get("/api/alert-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(alertActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].actionDescription").value(hasItem(DEFAULT_ACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].alertState").value(hasItem(DEFAULT_ALERT_STATE)))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID.intValue())))
            .andExpect(jsonPath("$.[*].ticketName").value(hasItem(DEFAULT_TICKET_NAME)))
            .andExpect(jsonPath("$.[*].ticketUrl").value(hasItem(DEFAULT_TICKET_URL)))
            .andExpect(jsonPath("$.[*].ticketDescription").value(hasItem(DEFAULT_TICKET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].changeLog").value(hasItem(DEFAULT_CHANGE_LOG)))
            .andExpect(jsonPath("$.[*].firedTime").value(hasItem(DEFAULT_FIRED_TIME.toString())));
    }
    
    
    
    @Test
    @Transactional
    public void getNonExistingAlertActivity() throws Exception {
        // Get the alertActivity
        restAlertActivityMockMvc.perform(get("/api/alert-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }



    @Test
    @Transactional
    public void updateNonExistingAlertActivity() throws Exception {
        int databaseSizeBeforeUpdate = alertActivityRepository.findAll().size();

        // Create the AlertActivity
        AlertActivityDTO alertActivityDTO = alertActivityMapper.toDto(alertActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertActivityMockMvc.perform(put("/api/alert-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alertActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlertActivity in the database
        List<AlertActivity> alertActivityList = alertActivityRepository.findAll();
        assertThat(alertActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    
}
