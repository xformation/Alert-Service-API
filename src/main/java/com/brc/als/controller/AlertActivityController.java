package com.brc.als.controller;

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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brc.als.AlertserviceApp;
import com.brc.als.config.ApplicationProperties;
import com.brc.als.config.SynectikDruidClient;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class AlertActivityController {

    private static final Logger logger = LoggerFactory.getLogger(AlertActivityController.class);

    private static final String ENTITY_NAME = "AlertActivityController";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
       
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    SynectikDruidClient synDruidClient;
    @GetMapping("/getDataFromAlertActivity")
	public List<Map> getDataFromAlertActivity(){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();
//		DruidClient client = synDruidClient.client(); 
		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get alert activity from druid");
		try {
			LocalDate startDate=LocalDate.now().minus(2,ChronoUnit.YEARS);
			LocalDate endDate=LocalDate.now().plus(1,ChronoUnit.YEARS);
			DateTime startTime = new DateTime(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(endDate.getYear(),endDate.getMonthValue(), endDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);
			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidAlertActivityDataSource()))
					.intervals(Collections.singletonList(interval))
					.batchSize(10000).limit(1000L).legacy(true).build();
			client.connect();
			list= client.query(query,Map.class);
			for(Map map: list) {
				List<Map> eventList= (List<Map>) map.get("events");
				listData.addAll(eventList);
			}
			Comparator<Map> mapComparator = new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					Instant val1 = Instant.parse(m1.get("timestamp").toString());
					Instant val2 = Instant.parse(m2.get("timestamp").toString());
					return val2.compareTo(val1);
				}
			};
			Collections.sort(listData, mapComparator);
			listData.forEach(oneMap -> repalceTimeStamp(oneMap));
			logger.debug("Total records retrived from druid : "+listData.size());
		}catch (Exception e) {
			logger.error("Exception in getting alert activity from druid. Returning empty list : ", e);
			return Collections.emptyList();
		}finally {
			try {
				client.close();
			}catch(ConnectionException ce) {
				logger.error("Exception in closing druid client connection: ", ce);
			}
		}
		return listData;
	}
    
    @GetMapping("/getDataFromAlertActivity/{guid}")
   	public  List<Map> getDataFromAlertActivityByGuid(@PathVariable String guid){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();
		DruidClient client = synDruidClient.client(ap); 
//		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
//		DruidConfiguration config = DruidConfiguration.builder().host("localhost").port(8888).endpoint("druid/v2/").build();
//		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get alert activity from druid. Guid : "+guid);
		try {
			LocalDate startDate=LocalDate.now().minus(2,ChronoUnit.YEARS);
			LocalDate endDate=LocalDate.now().plus(1,ChronoUnit.DAYS);
			DateTime startTime = new DateTime(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(endDate.getYear(),endDate.getMonthValue(), endDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);
			DruidFilter filter = new SelectorFilter("guid", guid);
			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidAlertActivityDataSource()))
					.intervals(Collections.singletonList(interval))
					.filter(filter)
					.batchSize(10000).limit(1000L).legacy(true).build();
			client.connect();
			list= client.query(query,Map.class);
			for(Map map: list) {
				List<Map> eventList= (List<Map>) map.get("events");
				listData.addAll(eventList);
			}
			Comparator<Map> mapComparator = new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					Instant val1 = Instant.parse(m1.get("timestamp").toString());
					Instant val2 = Instant.parse(m2.get("timestamp").toString());
					return val2.compareTo(val1);
				}
			};
			Collections.sort(listData, mapComparator);
			listData.forEach(oneMap -> repalceTimeStamp(oneMap));
			logger.debug("Total record retrived from druid : "+list.size());
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
			return Collections.emptyList();
		}
//		finally {
//			try {
//				client.close();
//			}catch(ConnectionException ce) {
//				logger.error("Exception in closing druid client connection: ", ce);
//			}
//		}
		return listData;
    }
    @GetMapping("/getDataFromFirstResp")
   	public  List<Map> getDataFromFirstResp(){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();
		DruidClient client = synDruidClient.client(ap); 
//		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
//		DruidConfiguration config = DruidConfiguration.builder().host("localhost").port(8888).endpoint("druid/v2/").build();
//		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get first_response data from druid. : ");
		try {
			LocalDate startDate=LocalDate.now().minus(2,ChronoUnit.YEARS);
			LocalDate endDate=LocalDate.now().plus(1,ChronoUnit.DAYS);
			DateTime startTime = new DateTime(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(endDate.getYear(),endDate.getMonthValue(), endDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);
			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidResponseTimeDataSource()))
					.intervals(Collections.singletonList(interval))
					.batchSize(10000).limit(1000L).legacy(true).build();
			client.connect();
			list= client.query(query,Map.class);
			for(Map map: list) {
				List<Map> eventList= (List<Map>) map.get("events");
				listData.addAll(eventList);
			}
			Comparator<Map> mapComparator = new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					Instant val1 = Instant.parse(m1.get("timestamp").toString());
					Instant val2 = Instant.parse(m2.get("timestamp").toString());
					return val2.compareTo(val1);
				}
			};
			Collections.sort(listData, mapComparator);
//			listData.forEach(oneMap -> repalceTimeStamp(oneMap));
			logger.debug("Total record retrived from druid : "+list.size());
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
			return Collections.emptyList();
		}
//		finally {
//			try {
//				client.close();
//			}catch(ConnectionException ce) {
//				logger.error("Exception in closing druid client connection: ", ce);
//			}
//		}
		return listData;
    }
    @GetMapping("/getAvgResponseTime")
    public Map<String,Object> getAvgResponseTime(){
    	List<Map> firstRespData=getDataFromFirstResp();
    	LocalDate today=LocalDate.now();
    	List<LocalDate> daysList = new ArrayList<LocalDate>();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		List<Double> totalTimeDurationList = Arrays.asList(0.0, 0.0, 0.0, 0.0);
		List<Integer> recordCountList = Arrays.asList(0, 0, 0, 0);
    	for(Map map:firstRespData) {
    		Instant timestamp=Instant.parse(map.get("timestamp").toString());
    		Instant createdTimestamp=Instant.parse(map.get("createdon").toString());
    		LocalDate updatedDate=timestamp.atZone(ZoneId.systemDefault()).toLocalDate();
    		LocalDate createDate=createdTimestamp.atZone(ZoneId.systemDefault()).toLocalDate();
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
		if (recordCountList.get(0)>0) {
			timeDurationAvgList.set(0, (int)(totalTimeDurationList.get(0) / recordCountList.get(0)));
		}
		if (recordCountList.get(1)>0) {
			timeDurationAvgList.set(1, (int)(totalTimeDurationList.get(1) / recordCountList.get(1)));
		}
		if (recordCountList.get(2)>0) {
			timeDurationAvgList.set(2, (int)(totalTimeDurationList.get(2) / recordCountList.get(2)));
		}
		if (recordCountList.get(3)>0) {
			timeDurationAvgList.set(3, (int)(totalTimeDurationList.get(3) / recordCountList.get(3)));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lineDataSetList", timeDurationAvgList);
		map.put("daysList", daysList);
		return map;
    	
    }
    
	public static void repalceTimeStamp(Map oneMap) {
		Instant timestamp=Instant.parse(oneMap.get("timestamp").toString());
		logger.debug("Instant time = "+timestamp);
		LocalDateTime dateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
		logger.debug("Instant converted to LocalDateTime: "+dateTime);
		oneMap.replace("timestamp", dateTime);
	}
    
}
