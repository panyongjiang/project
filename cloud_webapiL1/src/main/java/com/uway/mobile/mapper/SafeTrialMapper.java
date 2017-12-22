package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.SafeTrial;

@Mapper
public interface SafeTrialMapper {
	/**
	 * 获取所有的试用申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSafeTrial(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 通过试用申请Id获取试用申请详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSafeTrialById(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 审核申请
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public int updateTrialStatus(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 根据使用申请id删除申请
	 * @param paraMap
	 * @throws Exception
	 */
	public int delSafeTrialById(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 获取所有试用申请的数目
	 * @param sqlMap
	 * @return
	 */
	public long countAllSafeTrial(Map<String, Object> sqlMap);
	
	/**
	 * 试用申请
	 * @param st
	 * @throws Exception
	 */
	public void insertSafeTrial(SafeTrial st) throws Exception;
}
