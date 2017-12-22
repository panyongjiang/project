package com.uway.mobile.service;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface SafeTrialService {
	/**
	 * 获取所有的安全试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAllSafeTrial(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 通过试用申请Id获取试用申请详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSafeTrialById(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 审核试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public int UpdateTrialStatusById(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据id删除试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public int delById (Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取所有试用申请的数目
	 * @param paraMap
	 * @return
	 */
	public long countAllSafeTrial(Map<String, Object> paraMap);
}
