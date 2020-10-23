package com.brc.als.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brc.als.AlertserviceApp;

import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;

@Configuration
public class SynectikDruidClient {

	@Bean
    public DruidClient client() {
		ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
		
		DruidConfiguration config = DruidConfiguration.builder().host(ap.getDruidHost()).port(ap.getDruidPort()).endpoint(ap.getDruidEndPoint()).build();
		DruidClient client = new DruidJerseyClient(config);
		
		return client;
    }
}
