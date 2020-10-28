package com.brc.als.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.brc.als.AlertserviceApp;
import com.brc.als.config.ApplicationProperties;
import com.brc.als.domain.Alert;
import com.brc.als.repository.AlertRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST controller for managing {@link com.brc.als.domain.Alert}.
 */
@RestController
@RequestMapping("/api")
public class AlertController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String ENTITY_NAME = "alert";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * {@code POST  /updateAlert} : update an entry in alert.
	 *
	 * @param guid: alert's guid.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         in updated alert, or with status {@code 417 Exception_Failed} if guid
	 *         is null,
	 */
	@PostMapping("/updateAlert")
	public ResponseEntity<Object> updateAlert(@RequestBody ObjectNode obj) {
		logger.info("Request to update alert. Request object : " + obj);
		ApplicationProperties applicationProperties = AlertserviceApp.getBean(ApplicationProperties.class);
		List list = null;
		try {
			String guid = obj.get("guid").asText();
			Alert al = new Alert();
			al.setGuid(guid);
			Optional<Alert> oa = alertRepository.findOne(Example.of(al));
			String alertState = obj.get("alertState").asText();
			if (oa.isPresent()) {
				Alert alert = oa.get();
				alert.setAlertState(alertState);
				alert = alertRepository.save(alert);
				logger.info("Alert updated in db successfully");

				obj.put("type", "alert");
				obj.put("index", "alert");
				obj.put("searchKey", "guid");
				obj.put("searchValue", guid);
				obj.put("updateKey", "alert_state");
				obj.put("updateValue", alertState);
				list = restTemplate.postForObject(applicationProperties.getSearchSrvUrl() + "/search/updateWithQuery",
						obj, List.class);
				logger.info("Alert updated in elasticsearch successfully");
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("guid", guid);
				jsonObject.put("name", alert.getName());
				jsonObject.put("action","Alert Updated");
				jsonObject.put("action_description", "Alert updated. Alert state changed to "+alertState);
				jsonObject.put("action_time", Instant.now());
				jsonObject.put("ticket", "");
				jsonObject.put("ticket_description", "");
				jsonObject.put("user", "Admin");
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
				UriComponentsBuilder builder = UriComponentsBuilder
						.fromUriString(applicationProperties.getKafkaQueueUrl())
						.queryParam("topic", applicationProperties.getAlertActivityKafaTopic()).queryParam("msg", jsonObject.toString());
//				restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
				logger.debug("Kafka URI for alert activity :"+builder.toUriString());
				String res = restTemplate.getForObject(builder.toUriString(), String.class);
				logger.debug("Alert activity sent to separate kafka topic - ."+applicationProperties.getAlertActivityKafaTopic()+" Response : "+res);

			}else {
				logger.warn("No alert found in database. Guid : "+guid);
			}

		} catch (Exception e) {
			logger.error("Error in updating alert: ", e);
			list = Collections.emptyList();
			return new ResponseEntity<>(list, HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAlert/{guid}")
	public ResponseEntity<Object> deleteAlert(@PathVariable String guid) {
		logger.info("Request to delete alert. Guid : " + guid);
		ApplicationProperties applicationProperties = AlertserviceApp.getBean(ApplicationProperties.class);
		List list = null;
		try {
			Alert al = new Alert();
			al.setGuid(guid);
			Optional<Alert> oa = alertRepository.findOne(Example.of(al));
			if (oa.isPresent()) {
				Alert alert = oa.get();
				alertRepository.delete(alert);
				logger.debug("Alert deleted from db successfully");
				Map<String, String> obj = new HashMap<>();
				obj.put("type", "alert");
				obj.put("index", "alert");
				obj.put("searchKey", "guid");
				obj.put("searchValue", guid);
				list = restTemplate.postForObject(applicationProperties.getSearchSrvUrl() + "/search/deleteWithQuery",
						obj, List.class);
				if (list == null) {
					list = Collections.emptyList();
				}
				logger.debug("Alert deleted from elasticsearch successfully");

			}

		} catch (Exception e) {
			logger.error("Error in updating alert: ", e);
			list = Collections.emptyList();
			return new ResponseEntity<>(list, HttpStatus.PRECONDITION_FAILED);
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	static Object getFailedResponse(String msg) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("STATUS", "FAILED");
			obj.put("CAUSE", msg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return obj;
	}

	@GetMapping("/listAlert")
	public List<Alert> getAllAlert() {
		logger.debug("Request to get all alerts");
		List<Alert> list = alertRepository.findAll(Sort.by(Direction.DESC, "id"));
		return list;
	}

}
