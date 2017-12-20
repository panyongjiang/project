package com.uway.mobile.config;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
	@Value("${elasticsearch.address}")
	private String ES_ADDRESS;

	@Bean(name = "esClient")
	// @Scope("prototype")
	public TransportClient getInstance() throws Exception {
		TransportClient client = TransportClient.builder().build();
		String[] nodes = ES_ADDRESS.split(",");
		for (String node : nodes) {
			if (node.length() > 0) {// 跳过为空的node（当开头、结尾有逗号或多个连续逗号时会出现空node）
				String[] hostPort = node.split(":");
				client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]),
						Integer.parseInt(hostPort[1])));
			}
		}
		return client;
	}
}
