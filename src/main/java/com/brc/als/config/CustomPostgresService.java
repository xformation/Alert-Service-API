package com.brc.als.config;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.brc.als.AlertserviceApp;
import com.brc.als.domain.Alert;
import com.brc.als.repository.AlertRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class CustomPostgresService {

	private static final Logger logger = LoggerFactory.getLogger(CustomPostgresService.class);

	@Autowired
	private AlertRepository alertRepository;

	public Alert updateAlert(ObjectNode obj) {
		ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
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
	
}
