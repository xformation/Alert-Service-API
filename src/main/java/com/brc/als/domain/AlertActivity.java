package com.brc.als.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A AlertActivity.
 */
@Entity
@Table(name = "alert_activity")
public class AlertActivity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
//    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "name")
    private String name;

    @Column(name = "action")
    private String action;

    @Size(max = 5000)
    @Column(name = "action_description", length = 5000)
    private String actionDescription;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "alert_state")
    private String alertState;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "ticket_name")
    private String ticketName;

    @Size(max = 500)
    @Column(name = "ticket_url", length = 500)
    private String ticketUrl;

    @Size(max = 5000)
    @Column(name = "ticket_description", length = 5000)
    private String ticketDescription;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "event_type")
    private String eventType;

    @Size(max = 10000)
    @Column(name = "change_log", length = 10000)
    private String changeLog;

    @Id
    @Column(name = "fired_time")
    private Instant firedTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getGuid() {
        return guid;
    }

    public AlertActivity guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public AlertActivity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public AlertActivity action(String action) {
        this.action = action;
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public AlertActivity actionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
        return this;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public AlertActivity createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public AlertActivity updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getAlertState() {
        return alertState;
    }

    public AlertActivity alertState(String alertState) {
        this.alertState = alertState;
        return this;
    }

    public void setAlertState(String alertState) {
        this.alertState = alertState;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public AlertActivity ticketId(Long ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public AlertActivity ticketName(String ticketName) {
        this.ticketName = ticketName;
        return this;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public AlertActivity ticketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
        return this;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public AlertActivity ticketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
        return this;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getUserName() {
        return userName;
    }

    public AlertActivity userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventType() {
        return eventType;
    }

    public AlertActivity eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public AlertActivity changeLog(String changeLog) {
        this.changeLog = changeLog;
        return this;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public Instant getFiredTime() {
        return firedTime;
    }

    public AlertActivity firedTime(Instant firedTime) {
        this.firedTime = firedTime;
        return this;
    }

    public void setFiredTime(Instant firedTime) {
        this.firedTime = firedTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertActivity)) {
            return false;
        }
        return firedTime != null && firedTime.equals(((AlertActivity) o).firedTime);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertActivity{" +
//            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", name='" + getName() + "'" +
            ", action='" + getAction() + "'" +
            ", actionDescription='" + getActionDescription() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", alertState='" + getAlertState() + "'" +
            ", ticketId=" + getTicketId() +
            ", ticketName='" + getTicketName() + "'" +
            ", ticketUrl='" + getTicketUrl() + "'" +
            ", ticketDescription='" + getTicketDescription() + "'" +
            ", userName='" + getUserName() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", changeLog='" + getChangeLog() + "'" +
            ", firedTime='" + getFiredTime() + "'" +
            "}";
    }
}
