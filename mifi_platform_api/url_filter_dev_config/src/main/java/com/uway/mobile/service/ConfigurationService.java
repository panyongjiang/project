package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface ConfigurationService {

	public Result getNetConfig(Map<String, Object> paraMap)throws Exception;
	
	public Result getSaasConfig(Map<String, Object> paraMap)throws Exception;

	public Result setNetConfig(Map<String, Object> paraMap)throws Exception;

	public Result getWanConfig(Map<String, Object> paraMap)throws Exception;

	public Result getPPPOEconfig(Map<String, Object> paraMap)throws Exception;

	public Result getWifiConfig(Map<String, Object> paraMap)throws Exception;

	public Result setWifiConfig(Map<String, Object> paraMap)throws Exception;

	public Result setGuestConfig(Map<String, Object> paraMap)throws Exception;

	public Result getDHCPconfig(Map<String, Object> paraMap)throws Exception;

	public Result setDHCPconfig(Map<String, Object> paraMap)throws Exception;
	
	public Result getAppList(Map<String, Object> paraMap)throws Exception;
	
	public Result getHostAppConfig(Map<String, Object> paraMap)throws Exception;

	public Result setHostModeConfig(Map<String, Object> paraMap)throws Exception;

	public Result setHostLsConfig(Map<String, Object> paraMap)throws Exception;
	
	public Result setHostName(Map<String, Object> paraMap)throws Exception;
	
	public Result wifiLock(Map<String, Object> paraMap)throws Exception;
	
	public Result setHostNat(Map<String, Object> paraMap)throws Exception;
	
	public Result interceptUrl(Map<String, Object> paraMap)throws Exception;

	public Result rebootRoute(Map<String, Object> paraMap)throws Exception;

	public Result getBlackUrl(Map<String, Object> paraMap)throws Exception;

	public Result getVpn(Map<String, Object> paraMap)throws Exception;

	public Result setVpn(Map<String, Object> paraMap)throws Exception;

	public Result testSpeed(Map<String, Object> paraMap)throws Exception;


}
