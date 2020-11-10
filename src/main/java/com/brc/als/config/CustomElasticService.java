package com.brc.als.config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brc.als.domain.Alert;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class CustomElasticService {
	private static final Logger logger = LoggerFactory.getLogger(CustomElasticService.class);

	@Autowired
	RestTemplate restTemplate;

	public List updateAlert(ObjectNode obj, ApplicationProperties ap, List list, String guid, String alertState) {
		obj.put("type", "alert");
		obj.put("index", "alert");
		obj.put("searchKey", "guid");
		obj.put("searchValue", guid);
		obj.put("updateKey", "alert_state");
		obj.put("updateValue", alertState);
		list = restTemplate.postForObject(ap.getSearchSrvUrl() + "/search/updateWithQuery", obj, List.class);
		logger.debug("Alert updated in elasticsearch successfully");
		return list;
	}
	
	public Alert getAlert(ApplicationProperties ap, String guid) throws JSONException {
		String url = ap.getSearchSrvUrl()+"/search/searchWithQuery?type=alert&index=alert&searchKey=guid&searchValue="+guid; 
		String str = restTemplate.getForObject(url, String.class);
		JSONObject res = new JSONObject(str);
		logger.debug("Alert found in elasticsearch : "+res.toString());
		Alert alert = new Alert();
		try {
			createAlertFromJson(res, alert);
		}catch(Exception e) {
			logger.error("Exeption in converting JSONObject to alert: ", e);
			return null;
		}
		return alert;
	}
	
	public List getAllAlerts(ApplicationProperties ap) {
		String url = ap.getSearchSrvUrl() + "/search/searchWithIndexAndType?type=alert&index=alert";
		List res = restTemplate.getForObject(url, List.class);
		return res;
	}
	
	public List<Alert> convertStringToAlertList(List ls){
		List<Alert> allAlList = new ArrayList<>();
		for(Object s: ls) {
			try {
				JSONObject res = new JSONObject((String)s);
				Alert alert = new Alert();
				createAlertFromJson(res, alert);
				allAlList.add(alert);
			}catch(Exception e) {
				logger.error("Exeption in converting JSONObject to alert: "+ e.getMessage());
			}
		}
		return allAlList;
	}

	private void createAlertFromJson(JSONObject res, Alert alert) throws JSONException {
		try {
			alert.setGuid(res.getString("guid"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setName(res.getString("name"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setAlertState(res.getString("alert_state"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setClient(res.getString("client"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setClientUrl(res.getString("client_url"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setDescription(res.getString("description"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setFiredtime(res.getString("firedtime"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setIncidentKey(res.getString("incident_key"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		
		try {
			alert.setDetails(res.getString("details"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setSeverity(res.getString("severity"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setMonitorcondition(res.getString("monitorcondition"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setAffectedresource(res.getString("affectedresource"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setMonitorservice(res.getString("monitorservice"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setSignaltype(res.getString("signaltype"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setBrcsubscription(res.getString("brcsubscription"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setSuppressionstate(res.getString("suppressionstate"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setResourcegroup(res.getString("resourcegroup"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setResources(res.getString("resources"));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setCreatedOn(Instant.ofEpochMilli(Long.parseLong(res.getString("created_on"))));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		try {
			alert.setUpdatedOn(Instant.ofEpochMilli(Long.parseLong(res.getString("updated_on"))));
		}catch(Exception e) {
			logger.error("Exception. Tag not found. "+e.getMessage());
		}
		
	}
}
