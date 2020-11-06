package com.brc.als.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.client.exception.ConnectionException;
import in.zapr.druid.druidry.dataSource.TableDataSource;
import in.zapr.druid.druidry.filter.DruidFilter;
import in.zapr.druid.druidry.filter.SelectorFilter;
import in.zapr.druid.druidry.query.config.Interval;
import in.zapr.druid.druidry.query.scan.DruidScanQuery;

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

	@Autowired
	AlertActivityController alertActivityController;

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
				alert.setUpdatedOn(Instant.now());
				alert = alertRepository.save(alert);
				logger.info("Alert updated in db successfully");

				obj.put("type", "alert");
				obj.put("index", "alert");
				obj.put("searchKey", "guid");
				obj.put("searchValue", guid);
				obj.put("updateKey", "alert_state");
				obj.put("updateValue", alertState);
				try {
					list = restTemplate.postForObject(
							applicationProperties.getSearchSrvUrl() + "/search/updateWithQuery", obj, List.class);
					logger.info("Alert updated in elasticsearch successfully");
				} catch (Exception e) {
					// TODO: handle exception

				}
				/* first updated code */
				AlertActivityController alertActivityController = AlertserviceApp
						.getBean(AlertActivityController.class);
				List<Map> firstRespData = list = alertActivityController.getDataFromFirstResp();
				boolean guidAvailableInFirstRespFlag = false;
				for (Map map : firstRespData) {
					String mapGuid = (String) map.get("guid");
					if (mapGuid.equalsIgnoreCase(guid)) {
						guidAvailableInFirstRespFlag = true;
					}
				}
				if (!guidAvailableInFirstRespFlag) {
					JSONObject firstRespJsonObject = new JSONObject();
					firstRespJsonObject.put("guid", guid);
					firstRespJsonObject.put("name", alert.getName());
					firstRespJsonObject.put("type", "alert");
					firstRespJsonObject.put("createdon", alert.getCreatedOn());
					firstRespJsonObject.put("updatedon", alert.getUpdatedOn());
					firstRespJsonObject.put("user", "Automated");
					UriComponentsBuilder builder = UriComponentsBuilder
							.fromUriString(applicationProperties.getKafkaQueueUrl())
							.queryParam("topic", applicationProperties.getResponseTimeKafkaTopic())
							.queryParam("msg", firstRespJsonObject.toString());
					logger.debug("Kafka URI for  first response :" + builder.toUriString());
					String res = restTemplate.getForObject(builder.toUriString(), String.class);
					logger.debug("Alert update sent to separate kafka topic - ."
							+ applicationProperties.getResponseTimeKafkaTopic() + " Response : " + res);
				}
				/* first updated code */

				/* wait time code */
				{
					JSONObject waitTimeJsonObject = new JSONObject();
					waitTimeJsonObject.put("guid", guid);
					waitTimeJsonObject.put("name", alert.getName());
					waitTimeJsonObject.put("type", "alert");
					waitTimeJsonObject.put("createdon", alert.getCreatedOn());
					waitTimeJsonObject.put("updatedon", alert.getUpdatedOn());
					waitTimeJsonObject.put("user", "Automated");
					waitTimeJsonObject.put("alert_state", alert.getAlertState());
					UriComponentsBuilder builder = UriComponentsBuilder
							.fromUriString(applicationProperties.getKafkaQueueUrl())
							.queryParam("topic", applicationProperties.getWaitTimeKafkaTopic())
							.queryParam("msg", waitTimeJsonObject.toString());
					logger.debug("Kafka URI for  first response :" + builder.toUriString());
					String res = restTemplate.getForObject(builder.toUriString(), String.class);
					logger.debug("Alert update sent to separate kafka topic - ."
							+ applicationProperties.getWaitTimeKafkaTopic() + " Response : " + res);

				}
				/* wait time code */

				/* send data to alert_activity kafka topic code */
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("guid", guid);
				jsonObject.put("name", alert.getName());
				jsonObject.put("action", "Alert updated");
				jsonObject.put("action_description", "Alert updated. Alert state changed to " + alertState);
				jsonObject.put("action_time", Instant.now());
				jsonObject.put("ticket", "");
				jsonObject.put("ticket_description", "");
				jsonObject.put("user", "Automated");
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
				UriComponentsBuilder builder = UriComponentsBuilder
						.fromUriString(applicationProperties.getKafkaQueueUrl())
						.queryParam("topic", applicationProperties.getAlertActivityKafaTopic())
						.queryParam("msg", jsonObject.toString());
//				restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
				logger.debug("Kafka URI for alert activity :" + builder.toUriString());
				String res = restTemplate.getForObject(builder.toUriString(), String.class);
				logger.debug("Alert activity sent to separate kafka topic - ."
						+ applicationProperties.getAlertActivityKafaTopic() + " Response : " + res);
				/* send data to alert_activity kafka topic code */
			} else {
				logger.warn("No alert found in database. Guid : " + guid);
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

	@GetMapping("/topAlertToday")
	public List<Map<String, Object>> topAlertToday() {
		logger.debug("Request to get top alerts");
		List<Alert> selectedAlList = new ArrayList<>();
		List<Alert> list = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate today = LocalDate.parse(LocalDate.now().format(formatter), formatter);
		logger.debug("Today : " + today);

		List<Alert> allAlList = alertRepository.findAll(Sort.by(Direction.DESC, "updatedOn"));
		for (Alert al : allAlList) {
			LocalDate laDate = LocalDateTime.ofInstant(al.getUpdatedOn(), ZoneOffset.UTC).toLocalDate();
			logger.debug("Alert date : " + laDate);
			if (laDate.equals(today)) {
				list.add(al);
			}
		}
		for (Alert al : list) {
			if (al.getSeverity().equalsIgnoreCase("Urgent")) {
				selectedAlList.add(al);
				break;
			}
		}
		for (Alert al : list) {
			if (al.getSeverity().equalsIgnoreCase("Critical")) {
				selectedAlList.add(al);
				break;
			}
		}
		for (Alert al : list) {
			if (al.getSeverity().equalsIgnoreCase("High")) {
				selectedAlList.add(al);
				break;
			}
		}
		for (Alert al : list) {
			if (al.getSeverity().equalsIgnoreCase("Medium")) {
				selectedAlList.add(al);
				break;
			}
		}
		for (Alert al : list) {
			if (al.getSeverity().equalsIgnoreCase("Low")) {
				selectedAlList.add(al);
				break;
			}
		}
		Comparator<Alert> comparator = new Comparator<Alert>() {
			@Override
			public int compare(Alert a1, Alert a2) {
				// TODO Auto-generated method stub
				Instant updatedOnDate1 = a1.getUpdatedOn();
				Instant updatedOnDate2 = a2.getUpdatedOn();
				return updatedOnDate2.compareTo(updatedOnDate1);
			}
		};
		Collections.sort(selectedAlList, comparator);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (Alert alert : selectedAlList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", alert.getName());
			map.put("severity", alert.getSeverity());
			map.put("time", ChronoUnit.MINUTES.between(alert.getUpdatedOn(), Instant.now()));
			mapList.add(map);
		}
		return mapList;
	}

	@GetMapping("/getAlertVolumeData")
	public Map<String, Object> getAlertVolumeData() {
		logger.debug("Request to get top alerts");
		List<Alert> allAlList = alertRepository.findAll(Sort.by(Direction.DESC, "createdOn"));
		List<Alert> last6DayAlerts = new ArrayList<Alert>();
		for (Alert alert : allAlList) {
			if (alert.getCreatedOn().isAfter(Instant.now().minus(5, ChronoUnit.DAYS))) {
				last6DayAlerts.add(alert);
			} else {
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int totalClosedAlert = 0;
		int totalNewAlert = 0;
		List<Integer> dailyNewAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		List<Integer> dailyClosedAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		LocalDate today = LocalDate.now();
		List<LocalDate> daysList = new ArrayList<LocalDate>();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		daysList.add(today.minus(4, ChronoUnit.DAYS));
		daysList.add(today.minus(5, ChronoUnit.DAYS));
		for (Alert alert : last6DayAlerts) {
			LocalDate createDate = alert.getCreatedOn().atZone(ZoneId.systemDefault()).toLocalDate();
			if (createDate.equals(today)) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(0, dailyNewAlertList.get(0) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(0, dailyClosedAlertList.get(0) + 1);
					totalClosedAlert++;
				}
			} else if (createDate.equals(today.minus(1, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(1, dailyNewAlertList.get(1) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(1, dailyClosedAlertList.get(1) + 1);
					totalClosedAlert++;
				}
			} else if (createDate.equals(today.minus(2, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(2, dailyNewAlertList.get(2) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(2, dailyClosedAlertList.get(2) + 1);
					totalClosedAlert++;
				}
			} else if (createDate.equals(today.minus(3, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(3, dailyNewAlertList.get(3) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(3, dailyClosedAlertList.get(3) + 1);
					totalClosedAlert++;
				}
			} else if (createDate.equals(today.minus(4, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(4, dailyNewAlertList.get(4) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(4, dailyClosedAlertList.get(4) + 1);
					totalClosedAlert++;
				}
			} else if (createDate.equals(today.minus(5, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(5, dailyNewAlertList.get(5) + 1);
					totalNewAlert++;
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(5, dailyClosedAlertList.get(5) + 1);
					totalClosedAlert++;
				}
			}
		}
		map.put("totalNewAlert", totalNewAlert);
		map.put("totalClosedAlert", totalClosedAlert);
		map.put("newAlertList", dailyNewAlertList);
		map.put("closedAlertList", dailyClosedAlertList);
		map.put("daysList", daysList);
		return map;
	}

	@GetMapping("/getAverageResponseTimeGraphData")
	public Map<String, Object> getAverageResponseTimeGraphData() {
		logger.debug("Request to get Average Response Time graph data");
		List<Alert> allAlList = alertRepository.findAll(Sort.by(Direction.DESC, "createdOn"));
		List<Alert> last6DayAlerts = new ArrayList<Alert>();
		for (Alert alert : allAlList) {
			if (alert.getCreatedOn().isAfter(Instant.now().minus(5, ChronoUnit.DAYS))) {
				last6DayAlerts.add(alert);
			} else {
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();

		List<Integer> dailyNewAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		List<Integer> dailyClosedAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		List<Integer> dailyInProgressAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);

		List<Float> graphDataList = new ArrayList<Float>();
		LocalDate today = LocalDate.now();
		List<LocalDate> daysList = new ArrayList<LocalDate>();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		daysList.add(today.minus(4, ChronoUnit.DAYS));
		daysList.add(today.minus(5, ChronoUnit.DAYS));
		List<Double> totalTimeDurationList = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		List<Integer> recordCountList = Arrays.asList(0, 0, 0, 0, 0, 0);
		for (Alert alert : last6DayAlerts) {
			LocalDate createDate = alert.getCreatedOn().atZone(ZoneId.systemDefault()).toLocalDate();
			Duration timeDuration = Duration.between(alert.getCreatedOn(), alert.getUpdatedOn());
			if (createDate.equals(today)) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(0, totalTimeDurationList.get(0) + timeDurationInHours);
				recordCountList.set(0, recordCountList.get(0) + 1);
			} else if (createDate.equals(today.minus(1, ChronoUnit.DAYS))) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(1, totalTimeDurationList.get(1) + timeDurationInHours);
				recordCountList.set(1, recordCountList.get(1) + 1);
			} else if (createDate.equals(today.minus(2, ChronoUnit.DAYS))) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(2, totalTimeDurationList.get(2) + timeDurationInHours);
				recordCountList.set(2, recordCountList.get(2) + 1);
			} else if (createDate.equals(today.minus(3, ChronoUnit.DAYS))) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(3, totalTimeDurationList.get(3) + timeDurationInHours);
				recordCountList.set(3, recordCountList.get(3) + 1);
			} else if (createDate.equals(today.minus(4, ChronoUnit.DAYS))) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(4, totalTimeDurationList.get(4) + timeDurationInHours);
				recordCountList.set(4, recordCountList.get(4) + 1);
			} else if (createDate.equals(today.minus(5, ChronoUnit.DAYS))) {
				double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
				totalTimeDurationList.set(5, totalTimeDurationList.get(5) + timeDurationInHours);
				recordCountList.set(5, recordCountList.get(5) + 1);
			}
		}
		List<Double> timeDurationAvgList = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		if (recordCountList.get(0) > 0) {
			timeDurationAvgList.set(0, (totalTimeDurationList.get(0) / recordCountList.get(0)));
		}
		if (recordCountList.get(1) > 0) {
			timeDurationAvgList.set(1, (totalTimeDurationList.get(1) / recordCountList.get(1)));
		}
		if (recordCountList.get(2) > 0) {
			timeDurationAvgList.set(2, (totalTimeDurationList.get(2) / recordCountList.get(2)));
		}
		if (recordCountList.get(3) > 0) {
			timeDurationAvgList.set(3, (totalTimeDurationList.get(3) / recordCountList.get(3)));
		}
		if (recordCountList.get(4) > 0) {
			timeDurationAvgList.set(4, (totalTimeDurationList.get(4) / recordCountList.get(4)));
		}
		if (recordCountList.get(5) > 0) {
			timeDurationAvgList.set(5, (totalTimeDurationList.get(5) / recordCountList.get(5)));
		}
		map.put("timeDurationAvgList", timeDurationAvgList);
		map.put("daysList", daysList);
		return map;
	}

	@GetMapping("/getAlertVolumeByStatus")
	public Map<String, Object> getAlertVolumeByStatus() {
		logger.debug("Request to get alert Volume by status");
		List<Alert> allAlList = alertRepository.findAll(Sort.by(Direction.DESC, "createdOn"));
		List<Alert> last6DayAlerts = new ArrayList<Alert>();
		for (Alert alert : allAlList) {
			if (alert.getCreatedOn().isAfter(Instant.now().minus(5, ChronoUnit.DAYS))) {
				last6DayAlerts.add(alert);
			} else {
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();

		List<Integer> dailyNewAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		List<Integer> dailyClosedAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		List<Integer> dailyInProgressAlertList = Arrays.asList(0, 0, 0, 0, 0, 0);
		LocalDate today = LocalDate.now();
		List<LocalDate> daysList = new ArrayList<LocalDate>();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		daysList.add(today.minus(4, ChronoUnit.DAYS));
		daysList.add(today.minus(5, ChronoUnit.DAYS));
		for (Alert alert : last6DayAlerts) {
			LocalDate createDate = alert.getCreatedOn().atZone(ZoneId.systemDefault()).toLocalDate();
			if (createDate.equals(today)) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(0, dailyNewAlertList.get(0) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(0, dailyClosedAlertList.get(0) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(0, dailyInProgressAlertList.get(0) + 1);
				}
			} else if (createDate.equals(today.minus(1, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(1, dailyNewAlertList.get(1) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(1, dailyClosedAlertList.get(1) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(1, dailyInProgressAlertList.get(1) + 1);
				}
			} else if (createDate.equals(today.minus(2, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(2, dailyNewAlertList.get(2) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(2, dailyClosedAlertList.get(2) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(2, dailyInProgressAlertList.get(2) + 1);
				}
			} else if (createDate.equals(today.minus(3, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(3, dailyNewAlertList.get(3) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(3, dailyClosedAlertList.get(3) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(3, dailyInProgressAlertList.get(3) + 1);
				}
			} else if (createDate.equals(today.minus(4, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(4, dailyNewAlertList.get(4) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(4, dailyClosedAlertList.get(4) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(4, dailyInProgressAlertList.get(4) + 1);
				}
			} else if (createDate.equals(today.minus(5, ChronoUnit.DAYS))) {
				if (alert.getAlertState().equalsIgnoreCase("New")) {
					dailyNewAlertList.set(5, dailyNewAlertList.get(5) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("Closed")) {
					dailyClosedAlertList.set(5, dailyClosedAlertList.get(5) + 1);
				} else if (alert.getAlertState().equalsIgnoreCase("InProgress")) {
					dailyInProgressAlertList.set(5, dailyInProgressAlertList.get(5) + 1);
				}
			}
		}
		map.put("inProgressList", dailyInProgressAlertList);
		map.put("newAlertList", dailyNewAlertList);
		map.put("closedAlertList", dailyClosedAlertList);
		map.put("daysList", daysList);
		return map;
	}
}
