package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.AppCheck;

@Mapper
public interface AppCheckMapper {

	/**
	 * 新增appCheck
	 * @param appCheck
	 * @throws Exception
	 */
	public void insert(AppCheck appCheck) throws Exception;;
	
	/**
	 * 获取appCheck数量
	 * @return
	 * @throws Exception
	 */
	public long countAppCheck(Map<String, Object> sqlMap) throws Exception;; 
	
	/**
	 * 获取AppCheck列表
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listAppCheck(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 上传app检测报告
	 * @param paraMap
	 * @throws Exception
	 */
	public void addAppReport(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据id获取appCheck
	 * @param appCheckId
	 * @return
	 * @throws Exception
	 */
	public AppCheck getAppById(String appCheckId)throws Exception;
}
