package com.brc.als.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Alertservice.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	private String searchSrvUrl;
	private String druidHost;
	private int druidPort;
	private String druidEndPoint;
	private String druidAlertActivityDataSource; 
	private String alertActivityKafaTopic;

	public String getSearchSrvUrl() {
		return searchSrvUrl;
	}

	public void setSearchSrvUrl(String searchSrvUrl) {
		this.searchSrvUrl = searchSrvUrl;
	}

	public String getDruidHost() {
		return druidHost;
	}

	public void setDruidHost(String druidHost) {
		this.druidHost = druidHost;
	}

	public int getDruidPort() {
		return druidPort;
	}

	public void setDruidPort(int druidPort) {
		this.druidPort = druidPort;
	}

	public String getDruidEndPoint() {
		return druidEndPoint;
	}

	public void setDruidEndPoint(String druidEndPoint) {
		this.druidEndPoint = druidEndPoint;
	}

	public String getDruidAlertActivityDataSource() {
		return druidAlertActivityDataSource;
	}

	public void setDruidAlertActivityDataSource(String druidAlertActivityDataSource) {
		this.druidAlertActivityDataSource = druidAlertActivityDataSource;
	}

	public String getAlertActivityKafaTopic() {
		return alertActivityKafaTopic;
	}

	public void setAlertActivityKafaTopic(String alertActivityKafaTopic) {
		this.alertActivityKafaTopic = alertActivityKafaTopic;
	}

	
	
}


