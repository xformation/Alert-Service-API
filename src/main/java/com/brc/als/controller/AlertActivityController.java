package com.brc.als.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "collector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
//    @Autowired
//    private AlertRepository alertRepository;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    SynectikDruidClient synDruidClient;
    
    @GetMapping("/getDataFromAlertActivity2/{guid}")
	public List<Object> getDataFromAlertActivity(@PathVariable String guid){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Object> list=new ArrayList<Object>();
//		DruidClient client = synDruidClient.client(); 
		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get data from druid");
		try {
			LocalDate startDate=LocalDate.now().minus(10,ChronoUnit.DAYS);
			LocalDate endDate=LocalDate.now().plus(1,ChronoUnit.DAYS);
			DateTime startTime = new DateTime(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(endDate.getYear(),endDate.getMonthValue(), endDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);
			DruidFilter filter = new SelectorFilter("guid", guid);
			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidAlertActivityDataSource()))
					.intervals(Collections.singletonList(interval))
					.filter(filter)
					.batchSize(10000).limit(1000L).legacy(true).build();
			ObjectMapper mapper = new ObjectMapper();
			String requiredJson = mapper.writeValueAsString(query);
			client.connect();
			list= client.query(query, Object.class);
//			for(JSONObject jsonObject: list) {
//				List<JSONObject> listData=(List<JSONObject>) jsonObject.get("events");
//			}
			logger.debug("Record list retrive from druid : ",list);
			
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
			return Collections.emptyList();
		}finally {
			try {
				client.close();
			}catch(ConnectionException ce) {
				logger.error("Exception in closing druid client connection: ", ce);
			}
		}
		return list;
	}
    @GetMapping("/getDataFromAlertActivity")
	public List<Map> getDataFromAlertActivity(){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();
//		DruidClient client = synDruidClient.client(); 
		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get data from druid");
		try {
			LocalDate startDate=LocalDate.now().minus(10,ChronoUnit.DAYS);
			LocalDate endDate=LocalDate.now().plus(1,ChronoUnit.DAYS);
			DateTime startTime = new DateTime(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(endDate.getYear(),endDate.getMonthValue(), endDate.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);
			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidAlertActivityDataSource()))
					.intervals(Collections.singletonList(interval))
					.batchSize(10000).limit(1000L).legacy(true).build();
			client.connect();
			List lst=client.query(query,Object.class);
			System.out.println(lst);
			list= client.query(query,Map.class);
			for(Map map: list) {
				 listData=(List<Map>) map.get("events");
			}
			logger.debug("Record list retrive from druid : ",list);
			
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
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
   	public  List<Map> getDataFromAlertActivity2(@PathVariable String guid){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();
//		DruidClient client = synDruidClient.client(); 
		DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
		DruidClient client = new DruidJerseyClient(config);
		logger.info("Request to get data from druid");
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
			List lst=client.query(query,Object.class);
			System.out.println(lst);
			list= client.query(query,Map.class);
			for(Map map: list) {
				List<Map> eventList= (List<Map>) map.get("events");
				 listData.addAll(eventList);
			}
			logger.debug("Record list retrive from druid : ",list);
			
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
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
    
}
