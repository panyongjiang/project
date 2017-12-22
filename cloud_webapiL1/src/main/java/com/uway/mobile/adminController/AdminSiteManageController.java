package com.uway.mobile.adminController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("admin_site")
public class AdminSiteManageController {
	@Autowired
	public SiteService siteService;
	@Autowired
	public UserService userService;
	@Autowired
    private EsService esService;
	/**
	 * 网站管理
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/site_list_manage",method = RequestMethod.POST)
	public Result getSiteList(@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
			result = siteService.getAllSiteList(paraMap);
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
	public Result safeSiteDetail(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}		
		paraMap.put("userId", paraMap.get("create_user").toString());
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
	public Result wafSiteDetail(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}		
		paraMap.put("userId", paraMap.get("create_user").toString());
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
	 * 查看网站域名信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/get_msg_detail")
	public Result getMsgDetail(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		Map<String,Object> data = new HashMap<String,Object>();
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("域名不能为空！");
			return result;
		}
		String userId = paraMap.get("create_user").toString();
		paraMap.put("userId", userId);
		try {
			Map<String,Object> userMsg = new HashMap<String,Object>();
			User user = userService.getUserById(userId);
			if(user == null){
				data.put("userMsg", "用户不存在！");
			}
			userMsg.put("userName",user.getUserName() == null ?"": user.getUserName().toString());
			userMsg.put("person",user.getPerson() == null ?"": user.getPerson().toString());
			userMsg.put("phone",user.getPhone() == null ?"": user.getPhone().toString());
			data.put("userMsg", userMsg);	
			Map<String, Object> siteServiceMsg = siteService.getSiteServiceMsg(paraMap);
			data.put("siteServiceMsg", siteServiceMsg);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
		result.setData(data);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;	
	}
	
	/**
	 * 漏洞等级分布
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/Vul_level_distribution")
	public Result getHoles(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getVulLevel(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 漏洞检测结果
	 * @param paraMap
	 * @return
	 */	
	@RequestMapping(value = "/get_hole")
	public Result getHole(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (Integer.parseInt(paraMap.get("page_num").toString()) < 1){
				result.setMsg("页码错误！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "page_size")) {
				result.setMsg("每页显示条数不能为空不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			result = esService.getHole(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 获取安全监测趋势
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_risk_trend")
	public Result getRiskTrend(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getRiskTrend(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取所有高危漏洞
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_vuls")
	public Result getVuls(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getVuls(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 暂未使用
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/view_site_risk")
	public Result viewSiteRisk(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try{
			result = esService.getSiteRisk(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		
		return result;
	}
	/**
	 * 判断是否为waf站点
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/is_waf", method = RequestMethod.POST)
	public Result isWafSite(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setData(0);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}else{
				result.setData(1);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("有防护任务！");
				return result;
			}		
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	/**
	 * 获取网站每日数据
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_day", method = RequestMethod.POST)
	public Result getDay(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getDay(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	/**
	 * 获取每日web流量数
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_web", method = RequestMethod.POST)
	public Result getWeb(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getWeb(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 获取每日cc攻击信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_cc", method = RequestMethod.POST)
	public Result getCC(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getWebcc(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 根据网站id和时间获取攻击归属地信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_top", method = RequestMethod.POST)
	public Result getAttackTop(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getAttackTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 获取网站攻击防御信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend", method = RequestMethod.POST)
	public Result getAttackTrend(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getAttackTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
		
	/**
	 * 攻击类型分类汇总统计
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend_condition", method = RequestMethod.POST)
	public Result getAttackTrendWithCondition(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getAttackTrendWithCondition(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
		
	/**
	 * 攻击者来源统计
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_count", method = RequestMethod.POST)
	public Result getAttackCount(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getAttackCount(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 获取网站攻击防御趋势
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_trend", method = RequestMethod.POST)
	public Result getTrand(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	/**
	 * 获取王网站攻击详情
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_detail", method = RequestMethod.POST)
	public Result getAttackDetail(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "site_domain")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "create_user")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("客户id不能为空！");
			return result;
		}
		try {
			String siteId = siteService.getsiteIdBysiteDomain(paraMap);
			if(siteId == null){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("该域名没有防护任务！");
				return result;
			}
			paraMap.put("siteId", siteId);
			result = esService.getAttackDetail(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 获取防护网站今日和昨日攻击次数
	 * @param paraMap
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年10月17日上午11:08:47
	 */
	@RequestMapping(value = "/get_waf_day_data", method = RequestMethod.POST)
	public Result getTwoDayWafDataList(@RequestBody Map<String, Object>paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			paraMap.put("pageNum", pageNum);
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
				paraMap.put("pageSize", pageSize);
			}			
			if (Integer.parseInt(paraMap.get("pageNum").toString()) < 0
					|| Integer.parseInt(paraMap.get("pageSize").toString()) <= 0) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			return esService.getTwoDayWafData(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}
	@RequestMapping(value = "/get_safe_monitor_total", method = RequestMethod.POST)
	public Result getSafeMonitorTotal(@RequestBody Map<String, Object>paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			paraMap.put("pageNum", pageNum);
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
				paraMap.put("pageSize", pageSize);
			}			
			if (Integer.parseInt(paraMap.get("pageNum").toString()) < 0
					|| Integer.parseInt(paraMap.get("pageSize").toString()) <= 0) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			return esService.getSafeMonitorTotal(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}
	
}
