package com.brc.als.service.dto;

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

    private String alertstate;

    private String affectedresource;

    private String monitorservice;

    private String signaltype;

    private String brcsubscription;

    private String suppressionstate;

    private String resourcegroup;

    private String resources;

    private String firedtime;

    
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

    public String getAlertstate() {
        return alertstate;
    }

    public void setAlertstate(String alertstate) {
        this.alertstate = alertstate;
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
            ", alertstate='" + getAlertstate() + "'" +
            ", affectedresource='" + getAffectedresource() + "'" +
            ", monitorservice='" + getMonitorservice() + "'" +
            ", signaltype='" + getSignaltype() + "'" +
            ", brcsubscription='" + getBrcsubscription() + "'" +
            ", suppressionstate='" + getSuppressionstate() + "'" +
            ", resourcegroup='" + getResourcegroup() + "'" +
            ", resources='" + getResources() + "'" +
            ", firedtime='" + getFiredtime() + "'" +
            "}";
    }
}
