package com.brc.als.config;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.brc.als.domain.Alert;
import com.brc.als.domain.AlertActivity;
import com.brc.als.repository.AlertActivityRepository;
import com.brc.als.repository.AlertRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class CustomPostgresService {

	private static final Logger logger = LoggerFactory.getLogger(CustomPostgresService.class);

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	CustomKafkaService customKafkaService;
	
	@Autowired
	private AlertActivityRepository alertActivityRepository;

	public Alert updateAlert(ObjectNode obj) {
		Alert alert = null;
		String guid = obj.get("guid").asText();
		Alert al = new Alert();
		al.setGuid(guid);
		Optional<Alert> oa = alertRepository.findOne(Example.of(al));
		String alertState = obj.get("alertState").asText();
		if (oa.isPresent()) {
			alert = oa.get();
			alert.setAlertState(alertState);
			alert.setUpdatedOn(Instant.now());
			alert = alertRepository.save(alert);
			logger.debug("Alert updated in db successfully");
		} else {
			logger.warn("No alert found in database. Guid : " + guid);
		}
		return alert;
	}
	
	public void sendAlertActivity(ApplicationProperties applicationProperties, String guid, String alertState, Alert alert) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("guid", guid);
		jsonObject.put("name", alert.getName());
		jsonObject.put("action", "Alert updated");
		jsonObject.put("action_description", "Alert updated. Alert state changed to " + alertState);
		jsonObject.put("created_on", alert.getCreatedOn().toEpochMilli());
		jsonObject.put("updated_on", Instant.now().toEpochMilli());
		jsonObject.put("alert_state", alertState);
		jsonObject.put("ticket_id", null);
		jsonObject.put("ticket_name", null);
		jsonObject.put("ticket_url", null);
		jsonObject.put("ticket_description", null);
		jsonObject.put("user_name", "Automated");
		jsonObject.put("event_type", "update");
		jsonObject.put("change_log", null);
		jsonObject.put("fired_time", Instant.now().toEpochMilli());
		
		customKafkaService.sendMessageToKafka(jsonObject, applicationProperties.getAlertActivityKafaTopic());
	}
	
	public List<AlertActivity> getAlertActivity(String guid){
		if(!StringUtils.isBlank(guid)) {
			logger.debug("Getting alert_activity for guid : "+guid);
			AlertActivity al = new AlertActivity();
			al.setGuid(guid);
			List<AlertActivity> list = alertActivityRepository.findAll(Example.of(al), Sort.by(Direction.DESC, "updatedOn"));
			return list;
		}
		logger.debug("Getting all alert_activity data");
		return alertActivityRepository.findAll(Sort.by(Direction.DESC, "updatedOn"));
	}
}
