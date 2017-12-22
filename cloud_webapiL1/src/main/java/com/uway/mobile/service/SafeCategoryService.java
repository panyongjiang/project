package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SafeCategoryService {

	/**
	 * 获取用户的服务订购信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSafeServiceByUser(String userId) throws Exception;
	
	/**
	 * 修改用户的服务
	 * @param paraMap
	 * @throws Exception
	 */
	public void updSafeService(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 开启服务
	 * @param paraMap
	 * @throws Exception
	 */
	public void startSafeService(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 开通试用服务
	 * @param paraMap
	 * @throws Exception
	 */
	public void trialSafeService(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 关闭服务
	 * @param paraMap
	 * @throws Exception
	 */
	public void offSafeService(Map<String, Object> paraMap) throws Exception;
}
