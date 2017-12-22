package com.uway.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("site")
public class SiteController {
	@Autowired
	public SiteService siteService;
	@Autowired
	private RedisUtil redisUtil;

	private static final Logger log = Logger.getLogger(SiteController.class);
	
	/**
	 * 新增站点
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_safe_site")
	public Result addSafeSite(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
			String createUser = request.getAttribute("userId").toString();
			if(ObjectUtil.isEmpty(paraMap, "site_title")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网址不能为空！");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "site_domain")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站域名不能为空！");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "site_head")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站域名不能为空！");
				return result;
			}
			/*if(ObjectUtil.isEmpty(paraMap, "site_ip")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站ip不能为空！");
				return result;
			}
			*/
			
			if(ObjectUtil.isEmpty(paraMap, "week")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("扫描日期不能为空！");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "hour")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("扫描时间不能为空！");
				return result;
			}
			
			paraMap.put("type", (short) 0);
			List<Map<String, Object>> list = siteService.getSiteByUrlType(paraMap);
			if(list!=null&&list.size()>0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("网址已存在");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = siteService.addSite(paraMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
		}
		return result;
	}
	
	/**
	 *列出监测系统网站
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_safe_site")
	public Result listAllSafeSite(HttpServletRequest request) throws Exception {
		Result result = new Result();
		try {
			return siteService.getAllSaftSite(request.getAttribute("userId").toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	
	/**
	 * 删除站点
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/delete_safe_site")
	public Result deleteSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要删除的站点");
				return result;
			}
			result = siteService.deleteSite(paraMap);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	
	/**
	 * 开启漏扫监测
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/start_check")
	public Result startCheck(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择站点");
				return result;
			}
			//result = siteService.startCheck(paraMap);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	
	/**
	 * 关闭漏扫
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/stop_check")
	public Result stopCheck(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择站点");
				return result;
			}
			//result = siteService.stopCheck(paraMap);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	
	/*@RequestMapping(value = "/get_report_xml")
	public Result getReportXml(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择站点");
			return result;
		}
		
		try {
			//paraMap.put("siteId", value)
			list.add(paraMap);
			siteService.getReportXml(list);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			e.printStackTrace();
		}
		return result;
	}*/
	
	//@SuppressWarnings("unused")
	@RequestMapping(value = "/receive_xml", method = RequestMethod.POST)
	public void receiveXml(HttpServletRequest request){
		     
        try {
        	//DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();     
            //DocumentBuilder db = dbf.newDocumentBuilder();
			//Document doc = db.parse(request.getInputStream());
            byte[] bytes = new byte[1024 * 1024];
            int nRead = 1;  
            int nTotalRead = 0;  
            while (nRead > 0) {  
                nRead = request.getInputStream().read(bytes, nTotalRead, bytes.length - nTotalRead);  
                if (nRead > 0)  
                    nTotalRead = nTotalRead + nRead;  
            }  
            String str = new String(bytes, 0, nTotalRead, "utf-8");  
            log.debug("receive report		"+str);
			
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 用户域名列表
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_user_domain")
	public Result listUserDomain(HttpServletRequest request) throws Exception {
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if(createUser == null || "".equals(createUser)){
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("请登陆！");
				return result;
			}
			paraMap.put("createUser", createUser);
			return siteService.getSiteDomainByUser(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	/**
	 * 用户站点模糊匹配
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/site_list_user")
	public Result getSiteListByUser(HttpServletRequest request,@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "type")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择站点类型");
			return result;
		}
		String create_user = request.getAttribute("userId").toString();
		if(create_user == null || "".equals(create_user)){
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			return result;
		}
		paraMap.put("userId", create_user);
		try {			
			result = siteService.getSaftSiteByUser(paraMap);			
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
		return result;		
	}
	/**
	 * 监控详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/safe_site_detail")
	public Result safeSiteDetail(HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		String create_user = request.getAttribute("userId").toString();
		if(create_user == null || "".equals(create_user)){
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			return result;
		}
		paraMap.put("userId", create_user);
		try {
			result = siteService.getSafeSiteByDomain(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
		return result;		
	}
	
	/**
	 * 防护详情
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/waf_site_detail")
	public Result wafSiteDetail(HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		String create_user = request.getAttribute("userId").toString();
		if(create_user == null || "".equals(create_user)){
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			return result;
		}
		paraMap.put("userId", create_user);
		try {
			result = siteService.getWafSiteByDomain(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
		return result;		
	}
	/**
	 * 删除网站域名
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delete_site_domain")
	public Result deleteSiteDomain(HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		String create_user = request.getAttribute("userId").toString();
		if(create_user == null || "".equals(create_user)){
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			return result;
		}
		paraMap.put("userId", create_user);		
		try {
			result = siteService.getSafeSiteByDomain(paraMap);
			if(result.getData() != null){
				result.setMsg("此域名有监测任务，无法删除！");
				return result;
			}
			result = siteService.getWafSiteByDomain(paraMap);
			if(result.getData() != null){
				result.setMsg("此域名有防护任务，无法删除！");
				return result;
			}
			result = siteService.deleteWafSiteByDomain(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
		return result;		
	}
	
	/**
	 * 监测与防护切换站点下拉框
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_sites_user_type")
	public Result getSitesByUserAndType(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			String sid = request.getHeader("sid");
			String sessionId = Constance.REDIS_USER_PRE + sid;
			String userId = redisUtil.get(sessionId).toString();
			paraMap.put("userId", userId);
			String type2 = paraMap.get("type2").toString();
			String type = "1".equals(type2)?"1":"0";
			paraMap.put("type",type);
			List<Map<String,Object>> sites = siteService.getSitesByUserAndType(paraMap);
			result.setData(sites);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
		}
		return result;
	}
	
}
