package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface CodeService {

	public Result addCodes(String userId) throws Exception;
	
	public Result listCodes(Map<String, Object> paraMap) throws Exception;
}
