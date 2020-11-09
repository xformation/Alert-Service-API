package com.brc.als.config;

import java.time.Instant;
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
			alert.setGuid(guid);
			alert.setName(res.getString("name"));
			alert.setAlertState("alert_state");
			alert.setClient("client");
			alert.setClientUrl("client_url");
			alert.setDescription("description");
			alert.setFiredtime("firedtime");
			alert.setIncidentKey("incident_key");
			alert.setSeverity("severity");
			long l = Long.parseLong(res.getString("created_on"));
			alert.setCreatedOn(Instant.ofEpochMilli(l));
			l = Long.parseLong(res.getString("updated_on"));
			alert.setUpdatedOn(Instant.ofEpochMilli(l));
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
	
}
