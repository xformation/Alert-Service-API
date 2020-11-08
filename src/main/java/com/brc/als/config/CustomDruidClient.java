package com.brc.als.config;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brc.als.AlertserviceApp;

import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.client.exception.ConnectionException;
import in.zapr.druid.druidry.dataSource.TableDataSource;
import in.zapr.druid.druidry.filter.DruidFilter;
import in.zapr.druid.druidry.query.config.Interval;
import in.zapr.druid.druidry.query.scan.DruidScanQuery;
import in.zapr.druid.druidry.query.scan.DruidScanQuery.DruidScanQueryBuilder;

public class CustomDruidClient {

	private static final Logger logger = LoggerFactory.getLogger(CustomDruidClient.class);
	private static ApplicationProperties ap = AlertserviceApp.getBean(ApplicationProperties.class);
	private static DruidConfiguration config = DruidConfiguration.builder().host(ap.getDruidHost()).port(ap.getDruidPort())
			.endpoint(ap.getDruidEndPoint()).build();
	private static int BATCH_SIZE = 10000;
	private static long LIMIT = 1000L;
	
	public static DruidClient getDruidClient() {
		logger.debug("Getting durid client");
		return new DruidJerseyClient(config);
	}
	
	public static void closeDruidClient(DruidClient client) {
		logger.debug("Closing durid client");
		try {
			if(client != null) {
				client.close();
			}
		}catch(ConnectionException e) {
			logger.error("ConnectionException in closing druid client : "+e.getMessage());
		}
	}
	
	public static DruidScanQuery getDruidScanQuery(String dataSource, Interval interval, DruidFilter filter) {
		logger.debug("Getting durid query");
		DruidScanQueryBuilder dsb = DruidScanQuery.builder()
				.dataSource(new TableDataSource(dataSource))
				.intervals(Collections.singletonList(interval))
				.batchSize(BATCH_SIZE).limit(LIMIT)
				.legacy(true);
		if(filter != null) {
			dsb.filter(filter);
		}
		return dsb.build();
	}
}
