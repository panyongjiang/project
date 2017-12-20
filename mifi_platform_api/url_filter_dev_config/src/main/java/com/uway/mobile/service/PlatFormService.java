package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface PlatFormService {

	public Result setConfig(Map<String, Object> paraMap)throws Exception;

	public Result getConfig(Map<String, Object> paraMap)throws Exception;

	public Result checkUpdat(Map<String, Object> paraMap)throws Exception;

	public Result uploadFile(Map<String, Object> paraMap)throws Exception;
	
	

}
