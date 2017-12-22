package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface SiteService {
	/**
	 * 新增网站
	 * @param paraMap
	 * @throws Exception
	 */
    public Result addSite(Map<String, Object> paraMap) throws Exception;
    
    /**
     * 新增waf服务网站
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result addWafSite(Map<String, Object> paraMap) throws Exception;
    /**
     * 获取所有的安全监控网站
     * @param userId
     * @return
     * @throws Exception
     */
    public Result getAllSaftSite(String userId) throws Exception;
    
    /**
     * 获取所有waf网站
     * @param userId
     * @return
     * @throws Exception
     */
    public Result getAllWafSite(String userId) throws Exception;
    
    /**
     * 根据网站url得到网站信息
     * @param siteUrl
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSiteByUrlType(Map<String, Object>paraMap) throws Exception;
    
        /**
     * 删除站点
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result deleteSite(Map<String, Object>paraMap) throws Exception;
    /**
     * 删除waf站点
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result deleteWafSite(Map<String, Object>paraMap) throws Exception;
    
    /**
     * 开启网站监测
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result startCheck(String id) throws Exception;
    
    /**
     * 关闭网站监控
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result stopCheck(String id) throws Exception;
       
    /**
     * 接收机器发送的告警报告
     * @throws Exception
     */
    public void receiveXml(Map<String, Object> paraMap) throws Exception;
    /**
     * waf激活域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result activeSite(Map<String, Object>paraMap) throws Exception;
    /**
     * 查询网站信息
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result viewSite(Map<String, Object>paraMap) throws Exception;
    /**
     * waf激活域名（cname方式）激活子域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result activeCname(Map<String, Object>paraMap) throws Exception;
    
    /**
     * waf域名激活，激活域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result cnameActive(Map<String, Object> paraMap) throws Exception;
    /**
     * 查看激活任务
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result viewTask(Map<String, Object>paraMap) throws Exception;
    
    /**
     * 激活子域名（同上，子域名列表改为单个子域名）
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result activeSon(Map<String, Object> paraMap) throws Exception;
   
    /**
     * 增加子域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result addSonSite(Map<String, Object>paraMap) throws Exception;
    /**
     * 查询子域名信息
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result viewSonSite(Map<String, Object>paraMap) throws Exception;
    /**
     * 删除子域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result deleteSonSite(Map<String, Object>paraMap) throws Exception;
    /**
     * 子域名获取
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result listSonSite(Map<String, Object> paraMap) throws Exception;
    /**
     * 修改子域名
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result updSonSite(Map<String, Object> paraMap) throws Exception;
    
    /**
     * 整站锁是否开启
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result switchLock(Map<String, Object> paraMap) throws Exception;
    

    
    public Result getAll() throws Exception;
    
    /**
     * 添加相应的IP端口映射
     * @param paraMap
     * @return
     * @throws Exception
     */
    public Result portSet(Map<String, Object> paraMap) throws Exception;

	
	/**
	 * 获取用户网站域名列表
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getSiteDomainByUser(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取用户网站安全监测站点
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getSaftSiteByUser(Map<String, Object> paraMap) throws Exception;
	/**
	 * 监控详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getSafeSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 防护详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getWafSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 删除域名
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result deleteWafSiteByDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 网站管理列表（管理员用户）
	 * @param paraMap 
	 * @param paraMap
	 * @return
	 */
	public Result getAllSiteList(Map<String, Object> paraMap) throws Exception;
	/**
	 * cname激活域名
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result cnameActiveDomain(Map<String, Object> paraMap) throws Exception;
	/**
	 * 服务信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSiteServiceMsg(Map<String, Object> paraMap) throws Exception;

	public String getsiteIdBysiteDomain(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据用户ID和类型查找站点
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getSitesByUserAndType(Map<String, Object> paraMap) throws Exception;
	
}
