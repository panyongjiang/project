package com.uway.mobile.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;

@Configuration
public class JPushConfig {
	@Value("${jpush.master.secret}")
	private String MASTER_SECRET;
	@Value("${jpush.app.key}")
	private String APP_KEY;

	@Bean(name = "jPushClient")
	public JPushClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		return new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
	}

}
