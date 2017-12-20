package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uway.mobile.domain.Site;

@Mapper
public interface SiteMapper {
	/**
	 * 新增网站结点
	 * @param site
	 * @return
	 * @throws Exception
	 */
	public long insertSite(Site site) throws Exception;
	
	/**
	 * 资产管理，我的站点
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSite(@Param(value = "userId") long userId) throws Exception;
	
	/**
	 * 获取所有安全监控网站结点
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSafeSite(@Param(value = "userId") long userId) throws Exception;
	
	
	/**
	 * 获取当前用户安全监控网站个数
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public long countAllSafeSite(String userId) throws Exception;
	
	/**
	 * 获取所有WAF网站结点
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllWAFSite(@Param(value = "userId") long userId) throws Exception;
	
	/**
	 * 根据网站url得到网站信息
	 * @param siteUrl
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSiteByUrlType(Map<String, Object>paraMap) throws Exception;
	
	
	/**
	 * 根据id获取网站风险信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSiteRiskById(Map<String, Object>paraMap) throws Exception;
	/**
	 * 删除站点
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteSite(String id) throws Exception;
	
	/**
	 * 根据id获取网站
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Site getSiteById(String id) throws Exception;
	/**
	 * 根据id和type获取网站（区分waf和网站监测）
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Site getSiteByIdType(Map<String, Object> paraMap) throws Exception;
	/**
	 * task_id
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getTask(Map<String, Object> paraMap) throws Exception;
	/**
	 * 更新网站(site_id, type)
	 * @param site
	 * @throws Exception
	 */
	public void updSite(Site site) throws Exception;
	
	/**
	 * 获取账号所有网站的漏洞数集合
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSiteHole(String userId) throws Exception;
	
	/**
	 * 得到所有waf网站的id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllWAFId() throws Exception;
	
	/**
	 * 得到所有网站安全监测服务的域名id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSAFEId() throws Exception;
	
	/**
	 * 插入扫描网站和扫描结束时间
	 * @throws Exception
	 */
	public void insertScan(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 得到最近一次扫描时间
	 * @param paraMap
	 * @return 
	 * @throws Exception
	 */
	public Map<String, Object> getLast(Map<String, Object> paraMap) throws Exception;
	/**
	 * 得到网站所有扫描时间
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllLast(Map<String, Object> paraMap) throws Exception;
	/**
	 * 查询扫描信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getScan(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取所有站点报表信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllReport(Map<String, Object> paraMap) throws Exception;
	/**
	 * 根据站点（siteId）获取报表信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getReportBySite(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取所有站点报表条数
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Long countAllReport(Map<String, Object> paraMap) throws Exception;
	/**
	 * 根据站点（siteId）获取报表条数
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Long countReportBySite(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取create_user对应的站点最小create_time
	 * @param paraMap
	 * @return
	 */
	public Map<String, Object> getMinCreateTimeByUser(Map<String, Object> paraMap) throws Exception;

	public Site getDomainById(Map<String, Object> paraMap);
	/**
	 * 目标时间点站点最近的last_end_time（扫描周期）
	 * @param map
	 * @return
	 */
	public Map<String, Object> getLastByEndTime(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据用户ID，获取所有的网站域名
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getSiteDomainByUser(Map<String, Object> paraMap) throws Exception;
	/**
	 * 通过网址模糊查询指定站点type和用户
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> saftSiteByUser(Map<String, Object> paraMap) throws Exception;
	/**
	 * 用户监测详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSafeSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 用户网站域名下的子域名列表
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSonSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 根据域名获取waf站点
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Site getWafSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取所有站点列表
	 * @param paraMap 
	 * @param paraMap
	 * @return
	 */
	public List<Map<String, Object>> getAllSiteList(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取所有站点总条数
	 * @param paraMap 
	 * @param paraMap
	 * @return
	 */
	public Object getAllSiteCount(Map<String, Object> paraMap) throws Exception;
	/**
	 * 通过站点id和用户id获取网站监测信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSiteSafeServiceMsg(Map<String, Object> paraMap) throws Exception;
	/**
	 * 通过站点id和用户id获取网站防护信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSiteWafServiceMsg(Map<String, Object> paraMap) throws Exception;

}
