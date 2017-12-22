package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SiteSonMapper {

	/**
	 * 新增子域名
	 * @param paraMap
	 */
	public void insertSiteSon(Map<String, Object>paraMap)throws Exception;
	
	/**
	 * 根据id获取子域名信息
	 * @param paraMap
	 * @throws Exception
	 */
	public Map<String, Object> getSiteSonById(Map<String, Object>paraMap)throws Exception;
	
	/**
	 * 删除子域名
	 * @throws Exception
	 */
	public int deleteSon(String id)throws Exception;
	/**
	 * 根据网站id删除所有子域名
	 * @return
	 */
	public int deleteSonBySite(String siteId) throws Exception;
	/**
	 * 根据域名id得到其所有子域名的id
	 * @param siteWafId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSons(String siteWafId) throws Exception;
	/**
	 * 根据域名id获取其所有子域名
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listSon(Map<String, Object> paraMap) throws Exception;
	/**
	 * 修改子域名信息
	 * @param paraMap
	 * @throws Exception
	 */
	public void updSonSite(Map<String, Object> paraMap) throws Exception;
}
