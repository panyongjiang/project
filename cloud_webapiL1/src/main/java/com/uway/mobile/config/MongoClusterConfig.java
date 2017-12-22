package com.uway.mobile.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
@Configuration
public class MongoClusterConfig {
	@Value("${spring.data.mongodb.clusterNodes}")
	private String clusterNodes;
	/*
	 * mongodb集群配置
	 */
	@Bean(name = "mongoClient")
	public MongoClient getMongoDB() {
		MongoClient mongoDB = null;
        if (mongoDB == null) {
			String[] nodes = clusterNodes.split(",");
			try {
				List<ServerAddress> sends = new ArrayList<ServerAddress>();
				for (String node : nodes) {
					String[] table = node.split(":");
					int port = Integer.valueOf(table[1]);
					ServerAddress sa = new ServerAddress(table[0], port);
					sends.add(sa);
				}
				List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
				//mongoCredentialList.add(MongoCredential.createMongoCRCredential("test", "test", "test".toCharArray()));
				mongoDB = new MongoClient(sends, mongoCredentialList);
			} catch (Exception e) {
				throw new RuntimeException("连接MongoDB数据库错误", e);
			}
		}
		return mongoDB;
	}
}
