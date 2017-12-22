package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface WafConfigService {
	
    public Result getPortList(Map<String, Object> paraMap) throws Exception;
	
	public Result portSet(Map<String, Object> paraMap) throws Exception;

	public Result getDomain(Map<String, Object> paraMap)throws Exception;
	
    public Result switchLock(Map<String, Object> paraMap) throws Exception;
	
	public Result getSafeList(Map<String, Object> paraMap)throws Exception;
	
	public Result InList(Map<String, Object> paraMap)throws Exception;
	
	public Result insertIpOrUrl(Map<String, Object> paraMap)throws Exception;


}
