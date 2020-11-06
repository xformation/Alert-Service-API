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
	private String kafkaQueueUrl;
	private String responseTimeKafkaTopic;
	private String druidResponseTimeDataSource;
	private String waitTimeKafkaTopic;
	private String druidWaitTimeDatasource;
	
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

	public String getKafkaQueueUrl() {
		return kafkaQueueUrl;
	}

	public void setKafkaQueueUrl(String kafkaQueueUrl) {
		this.kafkaQueueUrl = kafkaQueueUrl;
	}

	public String getResponseTimeKafkaTopic() {
		return responseTimeKafkaTopic;
	}

	public void setResponseTimeKafkaTopic(String responseTimeKafkaTopic) {
		this.responseTimeKafkaTopic = responseTimeKafkaTopic;
	}

	public String getDruidResponseTimeDataSource() {
		return druidResponseTimeDataSource;
	}

	public void setDruidResponseTimeDataSource(String druidResponseTimeDataSource) {
		this.druidResponseTimeDataSource = druidResponseTimeDataSource;
	}

	public String getWaitTimeKafkaTopic() {
		return waitTimeKafkaTopic;
	}

	public void setWaitTimeKafkaTopic(String waitTimeKafkaTopic) {
		this.waitTimeKafkaTopic = waitTimeKafkaTopic;
	}

	public String getDruidWaitTimeDatasource() {
		return druidWaitTimeDatasource;
	}

	public void setDruidWaitTimeDatasource(String druidWaitTimeDatasource) {
		this.druidWaitTimeDatasource = druidWaitTimeDatasource;
	}
	
	
	
}


