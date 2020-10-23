package com.brc.als.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    
    @GetMapping("/getDataFromAlertActivity/{guid}")
	public List<Object> getDataFromAlertActivity(@PathVariable String guid){
    	ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		List<Object> list=new ArrayList<Object>();
		DruidClient client = synDruidClient.client(); 
		logger.info("Request to get data from druid");
		try {

			DateTime startTime = new DateTime(2020, 10, 19, 0, 0, 0, DateTimeZone.UTC);
			DateTime endTime = new DateTime(2020, 10, 21, 0, 0, 0, DateTimeZone.UTC);
			Interval interval = new Interval(startTime, endTime);

			DruidFilter filter = new SelectorFilter("guid", guid);

			DruidScanQuery query = DruidScanQuery.builder().dataSource(new TableDataSource(ap.getDruidAlertActivityDataSource()))
					.intervals(Collections.singletonList(interval))
					.filter(filter)
					.batchSize(10000).limit(1000L).legacy(true).build();
			ObjectMapper mapper = new ObjectMapper();
			String requiredJson = mapper.writeValueAsString(query);
//			DruidConfiguration config = DruidConfiguration.builder().host("100.64.108.25").port(18888).endpoint("druid/v2/").build();
//			DruidClient client = new DruidJerseyClient(config);
			client.connect();
			list= client.query(query, Object.class);
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
    
}
