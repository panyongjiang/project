package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.SafeCategory;

@Mapper
public interface SafeCategoryMapper {
	/**
	 * 为用户添加相应的服务信息
	 * @param safeCategory
	 * @return
	 * @throws Exception
	 */
	public void insertSafeCategory(long userId) throws Exception;
	
	/**
	 * 获取用户服务订购状态
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSafeServiceByUser(String userId) throws Exception;
	
	/**
	 * 据用户获取对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public SafeCategory getSafeService(String userId) throws Exception;
	
	/**
	 * 修改相应的状态
	 * @param sc
	 * @throws Exeption
	 */
	public void updSafeServiceTime(SafeCategory sc) throws Exception;
	
	/**
	 * 开启服务
	 * @param sqlMap
	 * @throws Exception
	 */
	public void startService(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 关闭服务
	 * @param sqlMap
	 * @throws Exception
	 */
	public void offService(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 关闭服务
	 * @param sqlMap
	 * @throws Exception
	 */
	public void offService1(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 服务试用开启
	 * @param sqlMap
	 * @throws Exception
	 */
	public void trialService(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 获取所有SafeCategory列表
	 * @return
	 * @throws Exception
	 */
	public List<SafeCategory> getAllSafeService() throws Exception;
}
