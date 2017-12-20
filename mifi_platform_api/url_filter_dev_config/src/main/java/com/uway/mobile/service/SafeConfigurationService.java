package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface SafeConfigurationService {
	
	public Result setHostBlackConfig(Map<String, Object> paraMap)throws Exception;

	public Result setHostNickConfig(Map<String, Object> paraMap)throws Exception;

	public Result getRuleSecurityConfig(Map<String, Object> paraMap)throws Exception;

	public Result getTimeConfig(Map<String, Object> paraMap)throws Exception;

	public Result setTimeConfig(Map<String, Object> paraMap)throws Exception;

	public Result getHistoryCloudConfig(Map<String, Object> paraMap)throws Exception;

	public Result setHistoryCloudConfig(Map<String, Object> paraMap)throws Exception;

}
