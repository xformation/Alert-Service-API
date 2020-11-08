package com.brc.als.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.brc.als.config.CustomDruidService;

import in.zapr.druid.druidry.filter.DruidFilter;
import in.zapr.druid.druidry.filter.SelectorFilter;

/**
 * REST controller for managing {@link com.brc.als.domain.AlertActivityController}.
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
    
    @GetMapping("/getDataFromAlertActivity")
	public List<Map> getDataFromAlertActivity(){
    	logger.info("Request to get alert activity from druid");
    	
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
    	List<Map> listData = customDruidService.getRecords(ap.getDruidAlertActivityDataSource(), null);
    	
    	logger.info("Request to get alert activity from druid completed");
		return listData;
	}

    @GetMapping("/getDataFromAlertActivity/{guid}")
   	public  List<Map> getDataFromAlertActivityByGuid(@PathVariable String guid){
    	logger.info("Request to get alert activities of an alert from druid. Guid : "+guid);
		
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
    	DruidFilter filter = new SelectorFilter("guid", guid);
		
    	List<Map> listData = customDruidService.getRecords(ap.getDruidAlertActivityDataSource(), filter);
    	logger.info("Request to get alert activity activities of an alert from druid completed");
		return listData;
    }
    
    @GetMapping("/getDataFromFirstResp")
   	public  List<Map> getDataFromFirstResp(){
    	logger.info("Request to get first response data from druid");
    	
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
    	List<Map> listData = customDruidService.getRecords(ap.getDruidResponseTimeDataSource(), null);
    	
    	logger.info("Request to get first response data from druid completed");
		return listData;
    }
    
    @GetMapping("/getAvgResponseTime")
    public Map<String,Object> getAvgResponseTime(){
    	List<Map> firstRespData=getDataFromFirstResp();
    	Map map=getLineGraphData(firstRespData);
		return map;
    }
    
    @GetMapping("/getWaitTimeDataAlertStateClosed")
   	public  List<Map> getWaitTimeDataAlertStateClosed(){
    	logger.info("Request to get wait time data from druid");
    	
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
    	DruidFilter filter = new SelectorFilter("alert_state","Closed");
    	List<Map> listData = customDruidService.getRecords(ap.getDruidWaitTimeDatasource(), filter);
    	
    	logger.info("Request to get wait time data from druid completed");
		return listData;
    }
    
    @GetMapping("/getWaitTimeGraphData")
    public Map<String,Object> getWaitTimeGraphData(){
    	List<Map> mapData=getWaitTimeDataAlertStateClosed();
    	Map map=getLineGraphData(mapData);
    	return map;   	
    }
    
    public Map getLineGraphData(List<Map> mapData) {
    	List<LocalDate> daysList = new ArrayList<LocalDate>();
    	LocalDate today=LocalDate.now();
		daysList.add(today);
		daysList.add(today.minus(1, ChronoUnit.DAYS));
		daysList.add(today.minus(2, ChronoUnit.DAYS));
		daysList.add(today.minus(3, ChronoUnit.DAYS));
		List<Double> totalTimeDurationList = Arrays.asList(0.0, 0.0, 0.0, 0.0);
		List<Integer> recordCountList = Arrays.asList(0, 0, 0, 0);
    	for(Map map:mapData) {
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

}
