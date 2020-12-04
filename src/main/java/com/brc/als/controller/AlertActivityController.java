package com.brc.als.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brc.als.AlertserviceApp;
import com.brc.als.config.ApplicationProperties;
import com.brc.als.config.CustomDruidService;
import com.brc.als.config.CustomPostgresService;
import com.brc.als.domain.AlertActivity;
import com.brc.als.repository.AlertActivityRepository;

import in.zapr.druid.druidry.filter.DruidFilter;
import in.zapr.druid.druidry.filter.SelectorFilter;
import liquibase.pro.packaged.al;

/**
 * REST controller for managing
 * {@link com.brc.als.domain.AlertActivityController}.
 */
@RestController
@RequestMapping("/api")
public class AlertActivityController {

	private static final Logger logger = LoggerFactory.getLogger(AlertActivityController.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CustomDruidService customDruidService;

	@Autowired
	CustomPostgresService customPostgresService;

	@Autowired
	private AlertActivityRepository alertActivityRepository;

	@GetMapping("/getDataFromAlertActivity")
	public List<AlertActivity> getDataFromAlertActivity() {
		logger.info("Request to get alert activity from db");

//    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<AlertActivity> listData = customPostgresService.getAlertActivity(null);
//    	listData.forEach(oneMap -> repalceTimeStamp(oneMap));
		logger.info("Request to get alert activity from db completed");
		return listData;
	}

	@GetMapping("/getDataFromAlertActivity/{guid}")
	public List<AlertActivity> getDataFromAlertActivityByGuid(@PathVariable String guid) {
		logger.info("Request to get alert activities of an alert from db. Guid : " + guid);

//    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
//    	DruidFilter filter = new SelectorFilter("guid", guid);

		List<AlertActivity> listData = customPostgresService.getAlertActivity(guid);
//    	listData.forEach(oneMap -> repalceTimeStamp(oneMap));
		logger.info("Request to get alert activity activities of an alert from db completed");
		return listData;
	}

	@GetMapping("/getDataFromFirstResp")
	public List<Map> getDataFromFirstResp() {
		logger.info("Request to get first response data from druid");

		ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> listData = customDruidService.getRecords(ap.getDruidResponseTimeDataSource(), null);

		logger.info("Request to get first response data from druid completed");
		return listData;
	}

	@GetMapping("/getAvgResponseTime")
	public Map<String, Object> getAvgResponseTime() {
		List<Map> firstRespData = getDataFromFirstResp();
		Map map = getLineGraphData(firstRespData);
		return map;
	}

	@GetMapping("/getWaitTimeDataAlertStateClosed")
	public List<Map> getWaitTimeDataAlertStateClosed() {
		logger.info("Request to get wait time data from druid");

		ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		DruidFilter filter = new SelectorFilter("alert_state", "Closed");
		List<Map> listData = customDruidService.getRecords(ap.getDruidWaitTimeDatasource(), filter);

		logger.info("Request to get wait time data from druid completed");
		return listData;
	}

	@GetMapping("/getWaitTimeGraphData")
	public Map<String, Object> getWaitTimeGraphData() {
		List<Map> mapData = getWaitTimeDataAlertStateClosed();
		Map map = getLineGraphData(mapData);
		return map;
	}

	public Map getLineGraphData(List<Map> mapData) {

		List<LocalDate> daysList = new ArrayList<LocalDate>();
		LocalDate today = LocalDate.now();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		List<Double> totalTimeDurationList = Arrays.asList(0.0, 0.0, 0.0, 0.0);
		List<Integer> recordCountList = Arrays.asList(0, 0, 0, 0);
		for (Map map : mapData) {
			Instant timestamp = Instant.parse(map.get("timestamp").toString());
			Instant createdTimestamp = Instant.parse(map.get("createdon").toString());
			LocalDate updatedDate = timestamp.atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate createDate = createdTimestamp.atZone(ZoneId.systemDefault()).toLocalDate();
			Duration timeDuration = Duration.between(createdTimestamp, timestamp);
			double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
			if (updatedDate.equals(today)) {
				totalTimeDurationList.set(0, totalTimeDurationList.get(0) + timeDurationInHours);
				recordCountList.set(0, recordCountList.get(0) + 1);
			} else if (updatedDate.equals(today.minus(1, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(1, totalTimeDurationList.get(1) + timeDurationInHours);
				recordCountList.set(1, recordCountList.get(1) + 1);
			} else if (updatedDate.equals(today.minus(2, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(2, totalTimeDurationList.get(2) + timeDurationInHours);
				recordCountList.set(2, recordCountList.get(2) + 1);
			} else if (updatedDate.equals(today.minus(3, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(3, totalTimeDurationList.get(3) + timeDurationInHours);
				recordCountList.set(3, recordCountList.get(3) + 1);
			}
		}
		List<Integer> timeDurationAvgList = Arrays.asList(0, 0, 0, 0);
		if (recordCountList.get(0) > 0) {
			timeDurationAvgList.set(0, (int) (totalTimeDurationList.get(0) / recordCountList.get(0)));
		}
		if (recordCountList.get(1) > 0) {
			timeDurationAvgList.set(1, (int) (totalTimeDurationList.get(1) / recordCountList.get(1)));
		}
		if (recordCountList.get(2) > 0) {
			timeDurationAvgList.set(2, (int) (totalTimeDurationList.get(2) / recordCountList.get(2)));
		}
		if (recordCountList.get(3) > 0) {
			timeDurationAvgList.set(3, (int) (totalTimeDurationList.get(3) / recordCountList.get(3)));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lineDataSetList", timeDurationAvgList);
		map.put("daysList", daysList);
		return map;
	}

	public void repalceTimeStamp(Map oneMap) {
		Instant timestamp = Instant.parse(oneMap.get("timestamp").toString());
		logger.debug("Instant time = " + timestamp);
		LocalDateTime dateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
		logger.debug("Instant converted to LocalDateTime: " + dateTime);
		oneMap.replace("timestamp", dateTime);
	}

	@PostMapping("/insertAlertActivityInDb")
	public AlertActivity insertDataIntoPostgresAlertActivity(@RequestParam(required = false) String guid,
			@RequestParam String name, @RequestParam String action, @RequestParam String actionDescription,
			@RequestParam Instant createdOn, @RequestParam Instant updatedOn, @RequestParam String alertState,
			@RequestParam(required = false) Long ticketId, @RequestParam(required = false) String ticketName,
			@RequestParam(required = false) String ticketUrl, @RequestParam(required = false) String ticketDescription,
			@RequestParam(defaultValue = "Automated") String userName,
			@RequestParam(required = false) String eventType) {
		AlertActivity alertActivity = new AlertActivity();
		if (!StringUtils.isBlank(guid)) {
			alertActivity.setGuid(guid);
		} else {
			UUID uuid = UUID.randomUUID();
			alertActivity.setGuid(uuid.toString());
		}
		alertActivity.setName(name);
		alertActivity.setAction(action);
		alertActivity.setActionDescription(actionDescription);
		alertActivity.setAlertState(alertState);
		alertActivity.setCreatedOn(createdOn);
		alertActivity.setUpdatedOn(updatedOn);
		alertActivity.setTicketId(ticketId);
		alertActivity.setTicketName(ticketName);
		alertActivity.setTicketDescription(ticketDescription);
		alertActivity.setTicketUrl(ticketUrl);
		alertActivity.setFiredTime(Instant.now());
		try {
			alertActivityRepository.save(alertActivity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return alertActivity;
	}

	@GetMapping("/getDataFromPostgresAlertActivity")
	public List<AlertActivity> getDataFromPostgresAlertActivity() {
		List<AlertActivity> alertActivities = alertActivityRepository.findAll();

		Comparator<AlertActivity> mapComparator = new Comparator<AlertActivity>() {
			@Override
			public int compare(AlertActivity alertActivity1, AlertActivity alertActivity2) {
				Instant val1 = alertActivity1.getUpdatedOn();
				Instant val2 = alertActivity2.getUpdatedOn();
				return val2.compareTo(val1);
			}
		};
		logger.debug("Sorting records on timestamp");
		Collections.sort(alertActivities, mapComparator);
		return alertActivities;
	}

	@GetMapping("/getAvgResponseTimeGraphDataFromDb")
	public Map<String, Object> getAvgResponseTimePostgresTable() {
		List<AlertActivity> alertActivities = getDataFromPostgresAlertActivity();
		List<AlertActivity> last6DaysUniqueData = alertActivities.stream()
				.filter((distinctByKey(AlertActivity::getGuid)))
				.filter(a -> a.getUpdatedOn().isAfter(Instant.now().minus(6, ChronoUnit.DAYS)))
				.filter(a -> a.getAction().toLowerCase().contains("updated")).collect(Collectors.toList());
//		System.out.println(lst2);
//		Map<String, List<AlertActivity>> map = new HashMap<String, List<AlertActivity>>();
//		List<AlertActivity> last6DaysData = alertActivities.stream()
//				.filter(a -> a.getUpdatedOn().isAfter(Instant.now().minus(6, ChronoUnit.DAYS)))
//				.collect(Collectors.toList());
		List<AlertActivity> finalList = new ArrayList<AlertActivity>();
		for (AlertActivity activity : last6DaysUniqueData) {
			List<AlertActivity> lst = alertActivities.stream()
					.filter(a -> a.getGuid().equalsIgnoreCase(activity.getGuid())
							&& a.getAction().toLowerCase().contains("updated"))
					.filter(a -> a.getUpdatedOn().isAfter(Instant.now().minus(6, ChronoUnit.DAYS)))
					.collect(Collectors.toList());
			if (lst.size() > 0) {
				AlertActivity al = lst.get((lst.size() - 1));
				finalList.add(al);
			}
		}

//		System.out.println(lst);
		return getLineGraphDataForPostgresData(finalList);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();

		return t -> seen.add(keyExtractor.apply(t));
	}

	@GetMapping("/getWaitTimeGraphDataFromDb")
	public Map<String, Object> getWaitTimeGraphData1() {
		List<AlertActivity> alertActivities = getDataFromPostgresAlertActivity();
		List<AlertActivity> filterByAlertSateClosed = alertActivities.stream()
				.filter(a -> a.getAlertState().equalsIgnoreCase("Closed")).collect(Collectors.toList());
		return getLineGraphDataForPostgresData(filterByAlertSateClosed);
	}

	public Map getLineGraphDataForPostgresData(List<AlertActivity> alertActivities) {

		List<LocalDate> daysList = new ArrayList<LocalDate>();
		LocalDate today = LocalDate.now();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		daysList.add(today.minus(4, ChronoUnit.DAYS));
		daysList.add(today.minus(5, ChronoUnit.DAYS));
		List<Double> totalTimeDurationList = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		List<Integer> recordCountList = Arrays.asList(0, 0, 0, 0, 0, 0);
		for (AlertActivity alertActivity : alertActivities) {
			Instant updatedOn = alertActivity.getUpdatedOn();
			Instant createdOn = alertActivity.getCreatedOn();
			LocalDate updatedDate = updatedOn.atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate createDate = createdOn.atZone(ZoneId.systemDefault()).toLocalDate();
			Duration timeDuration = Duration.between(createdOn, updatedOn);
			double timeDurationInHours = (double) (timeDuration.getSeconds()) / (60 * 60);
			if (updatedDate.equals(today)) {
				totalTimeDurationList.set(0, totalTimeDurationList.get(0) + timeDurationInHours);
				recordCountList.set(0, recordCountList.get(0) + 1);
			} else if (updatedDate.equals(today.minus(1, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(1, totalTimeDurationList.get(1) + timeDurationInHours);
				recordCountList.set(1, recordCountList.get(1) + 1);
			} else if (updatedDate.equals(today.minus(2, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(2, totalTimeDurationList.get(2) + timeDurationInHours);
				recordCountList.set(2, recordCountList.get(2) + 1);
			} else if (updatedDate.equals(today.minus(3, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(3, totalTimeDurationList.get(3) + timeDurationInHours);
				recordCountList.set(3, recordCountList.get(3) + 1);
			} else if (updatedDate.equals(today.minus(4, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(4, totalTimeDurationList.get(4) + timeDurationInHours);
				recordCountList.set(4, recordCountList.get(4) + 1);
			} else if (updatedDate.equals(today.minus(5, ChronoUnit.DAYS))) {
				totalTimeDurationList.set(5, totalTimeDurationList.get(5) + timeDurationInHours);
				recordCountList.set(5, recordCountList.get(5) + 1);
			}
		}
		List<Integer> timeDurationAvgList = Arrays.asList(0, 0, 0, 0, 0, 0);
		if (recordCountList.get(0) > 0) {
			timeDurationAvgList.set(0, (int) (totalTimeDurationList.get(0) / recordCountList.get(0)));
		}
		if (recordCountList.get(1) > 0) {
			timeDurationAvgList.set(1, (int) (totalTimeDurationList.get(1) / recordCountList.get(1)));
		}
		if (recordCountList.get(2) > 0) {
			timeDurationAvgList.set(2, (int) (totalTimeDurationList.get(2) / recordCountList.get(2)));
		}
		if (recordCountList.get(3) > 0) {
			timeDurationAvgList.set(3, (int) (totalTimeDurationList.get(3) / recordCountList.get(3)));
		}
		if (recordCountList.get(4) > 0) {
			timeDurationAvgList.set(4, (int) (totalTimeDurationList.get(4) / recordCountList.get(4)));
		}
		if (recordCountList.get(5) > 0) {
			timeDurationAvgList.set(5, (int) (totalTimeDurationList.get(5) / recordCountList.get(5)));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lineDataSetList", timeDurationAvgList);
		map.put("daysList", daysList);
		return map;
	}
}
