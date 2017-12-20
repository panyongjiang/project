package com.uway.mobile.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.uway.mobile.util.HttpClientUtil;

@Configuration
public class HttpClientConfig {
	@Value("${http.socketTimeout}")
	private int socketTimeout;
	@Value("${http.connectTimeout}")
	private int connectTimeout;
	@Value("${http.connectionRequestTimeout}")
	private int connectionRequestTimeout;

	@Bean(name = "httpClientObj", destroyMethod = "close")
	@Scope("prototype")
	public CloseableHttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		return HttpClientUtil.getInstance();
	}

	@Bean(name = "httpClientRequestConfig")
	public RequestConfig getConfig() {
		return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout).build();// 设置请求和传输超
	}
}
