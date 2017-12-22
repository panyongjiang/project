package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface CertificateService {
	
	public Result addCer(Map<String, Object> paraMap) throws Exception;

	public Result getSite(Map<String, Object> paraMap)throws Exception;

	public Result getSiteSon(Map<String, Object> paraMap)throws Exception;

	public Result getCer(Map<String, Object> paraMap)throws Exception;

	public Result delCer(Map<String, Object> paraMap)throws Exception;

}
