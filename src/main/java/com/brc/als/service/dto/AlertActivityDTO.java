package com.brc.als.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.brc.als.domain.AlertActivity} entity.
 */
public class AlertActivityDTO implements Serializable {
    
    private Long id;

    private String guid;

    private String name;

    private String action;

    @Size(max = 5000)
    private String actionDescription;

    private Instant createdOn;

    private Instant updatedOn;

    private String alertState;

    private Long ticketId;

    private String ticketName;

    @Size(max = 500)
    private String ticketUrl;

    @Size(max = 5000)
    private String ticketDescription;

    private String userName;

    private String eventType;

    @Size(max = 10000)
    private String changeLog;

    private Instant firedTime;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getAlertState() {
        return alertState;
    }

    public void setAlertState(String alertState) {
        this.alertState = alertState;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public Instant getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(Instant firedTime) {
        this.firedTime = firedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertActivityDTO)) {
            return false;
        }

        return id != null && id.equals(((AlertActivityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertActivityDTO{" +
            "id=" + getId() +
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
