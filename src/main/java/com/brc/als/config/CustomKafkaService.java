package com.brc.als.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.brc.als.AlertserviceApp;

@Service
public class CustomKafkaService {
	private static final Logger logger = LoggerFactory.getLogger(CustomKafkaService.class);

	@Autowired
	RestTemplate restTemplate;

	public void sendMessageToKafka(JSONObject jsonObject, String kafkaTopic) {
		logger.info("Begin sending message to kafka queue");
		ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(ap.getKafkaQueueUrl())
				.queryParam("topic", kafkaTopic)
				.queryParam("msg", jsonObject.toString());
		logger.debug("Kafka URI :" + builder.toUriString());
		String res = restTemplate.getForObject(builder.toUriString(), String.class);
		logger.debug("Message sent to kafka topic : "+ kafkaTopic + ". Response : " + res);
		logger.info("End sending message to kafka queue");
	}
}
