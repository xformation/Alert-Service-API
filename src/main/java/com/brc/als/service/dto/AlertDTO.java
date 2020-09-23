package com.brc.als.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.brc.als.domain.Alert} entity.
 */
public class AlertDTO implements Serializable {
    
    private Long id;

    private String guid;

    private String name;

    private String severity;

    private String monitorcondition;

    private String affectedresource;

    private String monitorservice;

    private String signaltype;

    private String brcsubscription;

    private String suppressionstate;

    private String resourcegroup;

    private String resources;

    private String firedtime;

    private Instant createdOn;

    private Instant updatedOn;

    private String alertState;

    private String client;

    private String clientUrl;

    @Size(max = 5000)
    private String description;

    private String details;

    private String incidentKey;

    
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMonitorcondition() {
        return monitorcondition;
    }

    public void setMonitorcondition(String monitorcondition) {
        this.monitorcondition = monitorcondition;
    }

    public String getAffectedresource() {
        return affectedresource;
    }

    public void setAffectedresource(String affectedresource) {
        this.affectedresource = affectedresource;
    }

    public String getMonitorservice() {
        return monitorservice;
    }

    public void setMonitorservice(String monitorservice) {
        this.monitorservice = monitorservice;
    }

    public String getSignaltype() {
        return signaltype;
    }

    public void setSignaltype(String signaltype) {
        this.signaltype = signaltype;
    }

    public String getBrcsubscription() {
        return brcsubscription;
    }

    public void setBrcsubscription(String brcsubscription) {
        this.brcsubscription = brcsubscription;
    }

    public String getSuppressionstate() {
        return suppressionstate;
    }

    public void setSuppressionstate(String suppressionstate) {
        this.suppressionstate = suppressionstate;
    }

    public String getResourcegroup() {
        return resourcegroup;
    }

    public void setResourcegroup(String resourcegroup) {
        this.resourcegroup = resourcegroup;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getFiredtime() {
        return firedtime;
    }

    public void setFiredtime(String firedtime) {
        this.firedtime = firedtime;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(String incidentKey) {
        this.incidentKey = incidentKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDTO)) {
            return false;
        }

        return id != null && id.equals(((AlertDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", name='" + getName() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", monitorcondition='" + getMonitorcondition() + "'" +
            ", affectedresource='" + getAffectedresource() + "'" +
            ", monitorservice='" + getMonitorservice() + "'" +
            ", signaltype='" + getSignaltype() + "'" +
            ", brcsubscription='" + getBrcsubscription() + "'" +
            ", suppressionstate='" + getSuppressionstate() + "'" +
            ", resourcegroup='" + getResourcegroup() + "'" +
            ", resources='" + getResources() + "'" +
            ", firedtime='" + getFiredtime() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", alertState='" + getAlertState() + "'" +
            ", client='" + getClient() + "'" +
            ", clientUrl='" + getClientUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", details='" + getDetails() + "'" +
            ", incidentKey='" + getIncidentKey() + "'" +
            "}";
    }
}
