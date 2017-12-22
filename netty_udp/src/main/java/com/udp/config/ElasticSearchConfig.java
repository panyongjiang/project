package com.udp.config;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchConfig {

	public static String ES_ADDRESS="192.168.8.252:9300";	
	
	public static TransportClient getInstance() throws Exception {
		TransportClient client = TransportClient.builder().build();
		String[] nodes = ES_ADDRESS.split(",");
		for (String node : nodes) {
			if (node.length() > 0) {//跳过为空的node
				String[] hostPort = node.split(":");
				client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]), Integer.parseInt(hostPort[1])));
			}
		}
		return client;
	}
}
