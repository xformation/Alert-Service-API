package com.brc.als.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A Alert.
 */
@Entity
@Table(name = "alert")
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "name")
    private String name;

    @Column(name = "severity")
    private String severity;

    @Column(name = "monitorcondition")
    private String monitorcondition;

    @Column(name = "alertstate")
    private String alertstate;

    @Column(name = "affectedresource")
    private String affectedresource;

    @Column(name = "monitorservice")
    private String monitorservice;

    @Column(name = "signaltype")
    private String signaltype;

    @Column(name = "brcsubscription")
    private String brcsubscription;

    @Column(name = "suppressionstate")
    private String suppressionstate;

    @Column(name = "resourcegroup")
    private String resourcegroup;

    @Column(name = "resources")
    private String resources;

    @Column(name = "firedtime")
    private String firedtime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public Alert guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public Alert name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public Alert severity(String severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMonitorcondition() {
        return monitorcondition;
    }

    public Alert monitorcondition(String monitorcondition) {
        this.monitorcondition = monitorcondition;
        return this;
    }

    public void setMonitorcondition(String monitorcondition) {
        this.monitorcondition = monitorcondition;
    }

    public String getAlertstate() {
        return alertstate;
    }

    public Alert alertstate(String alertstate) {
        this.alertstate = alertstate;
        return this;
    }

    public void setAlertstate(String alertstate) {
        this.alertstate = alertstate;
    }

    public String getAffectedresource() {
        return affectedresource;
    }

    public Alert affectedresource(String affectedresource) {
        this.affectedresource = affectedresource;
        return this;
    }

    public void setAffectedresource(String affectedresource) {
        this.affectedresource = affectedresource;
    }

    public String getMonitorservice() {
        return monitorservice;
    }

    public Alert monitorservice(String monitorservice) {
        this.monitorservice = monitorservice;
        return this;
    }

    public void setMonitorservice(String monitorservice) {
        this.monitorservice = monitorservice;
    }

    public String getSignaltype() {
        return signaltype;
    }

    public Alert signaltype(String signaltype) {
        this.signaltype = signaltype;
        return this;
    }

    public void setSignaltype(String signaltype) {
        this.signaltype = signaltype;
    }

    public String getBrcsubscription() {
        return brcsubscription;
    }

    public Alert brcsubscription(String brcsubscription) {
        this.brcsubscription = brcsubscription;
        return this;
    }

    public void setBrcsubscription(String brcsubscription) {
        this.brcsubscription = brcsubscription;
    }

    public String getSuppressionstate() {
        return suppressionstate;
    }

    public Alert suppressionstate(String suppressionstate) {
        this.suppressionstate = suppressionstate;
        return this;
    }

    public void setSuppressionstate(String suppressionstate) {
        this.suppressionstate = suppressionstate;
    }

    public String getResourcegroup() {
        return resourcegroup;
    }

    public Alert resourcegroup(String resourcegroup) {
        this.resourcegroup = resourcegroup;
        return this;
    }

    public void setResourcegroup(String resourcegroup) {
        this.resourcegroup = resourcegroup;
    }

    public String getResources() {
        return resources;
    }

    public Alert resources(String resources) {
        this.resources = resources;
        return this;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getFiredtime() {
        return firedtime;
    }

    public Alert firedtime(String firedtime) {
        this.firedtime = firedtime;
        return this;
    }

    public void setFiredtime(String firedtime) {
        this.firedtime = firedtime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alert)) {
            return false;
        }
        return id != null && id.equals(((Alert) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alert{" +
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
