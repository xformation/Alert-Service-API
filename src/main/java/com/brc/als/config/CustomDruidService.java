package com.brc.als.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.brc.als.domain.Alert;

import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.filter.DruidFilter;
import in.zapr.druid.druidry.query.config.Interval;
import in.zapr.druid.druidry.query.scan.DruidScanQuery;

@Service
public class CustomDruidService {
	private static final Logger logger = LoggerFactory.getLogger(CustomDruidService.class);

	@Autowired
	CustomKafkaService customKafkaService;
	
	public List<Map> getRecords(String dataSource, DruidFilter filter){
		List<Map> list=new ArrayList<Map>();
		List<Map> listData=new ArrayList<Map>();

		DruidClient client = null;
		try {
			client = CustomDruidClient.getDruidClient();
		}catch(Exception e) {
			logger.error("Exception in getting druid client connection. Returning empty list: ", e);
			return Collections.emptyList();
		}
		
		try {
			Interval interval = new Interval(Constants.START_TIME, Constants.END_TIME);
			DruidScanQuery query = CustomDruidClient.getDruidScanQuery(dataSource, interval, filter);
			
			client.connect();
			list= client.query(query,Map.class);
			logger.debug("Total records retrived from druid : "+listData.size());
			sortRecords(list, listData);
//			listData.forEach(oneMap -> repalceTimeStamp(oneMap));
			
		}catch (Exception e) {
			logger.error("Exception in getting data from druid. Returning empty list : ", e);
			return Collections.emptyList();
		}finally {
			CustomDruidClient.closeDruidClient(client);
		}
		return listData;
	}
	
//	public void repalceTimeStamp(Map oneMap) {
//		Instant timestamp=Instant.parse(oneMap.get("timestamp").toString());
//		logger.debug("Instant time = "+timestamp);
//		LocalDateTime dateTime = timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime();
//		logger.debug("Instant converted to LocalDateTime: "+dateTime);
//		oneMap.replace("timestamp", dateTime);
//	}
	
	private void sortRecords(List<Map> list, List<Map> listData) {
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
		logger.debug("Sorting records on timestamp");
		Collections.sort(listData, mapComparator);
	}
	
	public void sendAlertResponseTime(ApplicationProperties ap, String guid, Alert alert) throws JSONException {
		
		List<Map> firstRespData = getRecords(ap.getDruidResponseTimeDataSource(), null);
		
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
			customKafkaService.sendMessageToKafka(firstRespJsonObject, ap.getResponseTimeKafkaTopic());
			logger.debug("Alert response time message sent to kafka successful");
		}
	}
	
	public void sendAlertWaitTime(ApplicationProperties ap, String guid, Alert alert) throws JSONException {
		JSONObject waitTimeJsonObject = new JSONObject();
		waitTimeJsonObject.put("guid", guid);
		waitTimeJsonObject.put("name", alert.getName());
		waitTimeJsonObject.put("type", "alert");
		waitTimeJsonObject.put("createdon", alert.getCreatedOn());
		waitTimeJsonObject.put("updatedon", alert.getUpdatedOn());
		waitTimeJsonObject.put("user", "Automated");
		waitTimeJsonObject.put("alert_state", alert.getAlertState());
		customKafkaService.sendMessageToKafka(waitTimeJsonObject, ap.getWaitTimeKafkaTopic());
	}
	
	public void sendAlertActivity(ApplicationProperties applicationProperties, String guid, String alertState, Alert alert) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("guid", guid);
		jsonObject.put("name", alert.getName());
		jsonObject.put("action", "Alert updated");
		jsonObject.put("action_description", "Alert updated. Alert state changed to " + alertState);
		jsonObject.put("action_time", Instant.now());
		jsonObject.put("ticket", "");
		jsonObject.put("ticket_description", "");
		jsonObject.put("user", "Automated");
		customKafkaService.sendMessageToKafka(jsonObject, applicationProperties.getAlertActivityKafaTopic());
	}

}
