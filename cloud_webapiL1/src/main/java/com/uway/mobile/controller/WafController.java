package com.uway.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("waf")
public class WafController {

	@Autowired
    private SiteService siteService;
	@Autowired
	private ApiService apiService;
    
	/**
	 * waf新增网站
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/add_site", method = RequestMethod.POST)
	public Result addSite(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		String createUser = request.getAttribute("userId").toString();
		try {
			
			if(ObjectUtil.isEmpty(paraMap, "site_domain")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站域名不能为空！");
				return result;
			}
			
			paraMap.put("type", (short) 1);
			List<Map<String, Object>> list = siteService.getSiteByUrlType(paraMap);
			if(list!=null&&list.size()>0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("网站域名已存在");
				return result;
			}
			
			paraMap.put("createUser", createUser);
			result = siteService.addWafSite(paraMap);
			

		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增失败");
		}
		return result;
	}
	
	/**
	 * 获取waf网站列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list_site", method = RequestMethod.POST)
	public Result listSite(HttpServletRequest request){
		Result result = new Result();
		try{
			
			String userId = request.getAttribute("userId").toString();
			result = siteService.getAllWafSite(userId);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/view_site", method = RequestMethod.POST)
	public Result viewSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的网站");
				return result;
			}
			result = siteService.viewSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}
	/**
	 * 根据id删除网站
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/delete_site", method = RequestMethod.POST)
	public Result deleteSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要删除的站点！");
				return result;
			};
			
			result = siteService.deleteWafSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("删除失败");
		}
		return result;
	}
	/**
	 * 激活网站
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/active_site", method = RequestMethod.POST)
	public Result activeSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要激活的网站");
				return result;
			}
			result = siteService.activeSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	/**
	 * 新增子域名
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/add_son_site", method = RequestMethod.POST)
	public Result addSonSite(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择域名");
			return result;
			}
			
			if(ObjectUtil.isEmpty(paraMap, "point")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("指向不能为空");
				return result;
			}
			
			paraMap.put("userId", userId);
			result = siteService.addSonSite(paraMap);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增失败");
		}
		
		return result;
	}
	@RequestMapping(value = "/view_son_site", method = RequestMethod.POST)
	public Result viewSonSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "sid")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = siteService.viewSonSite(paraMap);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		
		return result;
	}
	/**
	 * 删除子域名
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/delete_son_site", method = RequestMethod.POST)
	public Result deleteSonSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "siteSonId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要删除的网站");
				return result;
			}
			result = siteService.deleteSonSite(paraMap);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("删除失败");
		}
		return result;
	}
	
	/**
	 * 获取子域名列表
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/list_son_site", method = RequestMethod.POST)
	public Result listSonSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "siteId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = siteService.listSonSite(paraMap);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	/**
	 * cname方式激活子域名
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/active_cname", method = RequestMethod.POST)
	public Result activeCname(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要激活的网站");
				return result;
			}
			result = siteService.activeCname(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	/**
	 * cname方式激活域名
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/cname_active", method = RequestMethod.POST)
	public Result cnameActive(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要激活的网站");
				return result;
			}
			result = siteService.cnameActive(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	/**
	 * cname方式激活域名
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/view_task", method = RequestMethod.POST)
	public Result viewTask(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要查看的网站");
				return result;
			}
			result = siteService.viewTask(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	
	/**
	 * cname方式激活
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/active_son", method = RequestMethod.POST)
	public Result activeSon(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "siteSonId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要激活的子域名");
				return result;
			}
			result = siteService.activeSon(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	
	@RequestMapping(value = "/upd_son_site", method = RequestMethod.POST)
	public Result updSonSite(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要修改的子域名");
				return result;
			}
			result = siteService.updSonSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/open_anti_theft", method = RequestMethod.POST)
	public Result openAntiTheft(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的域名或子域名");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "value")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的操作");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "keyword")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("开关字段不能为空");
				return result;
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	@RequestMapping(value = "/open_wall", method = RequestMethod.POST)
	public Result openWall(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的域名或子域名");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "value")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的操作");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "keyword")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("开关字段不能为空");
				return result;
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 开关网站整站锁
	 * @param value 0关闭，1开启
	 * @param sid   网站id
	 * @param id    子域名id
	 * @param keyword 关键字
	 * @return
	 */
	@RequestMapping(value = "/switch_lock", method = RequestMethod.GET)
	public Result switchLock(@RequestParam(value = "value", required = true) String value,
			@RequestParam(value = "sid", required = true) String sid,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "keyword", required = true) String keyword){
		Result result = new Result();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("sid", sid);
		paraMap.put("id", id);
		paraMap.put("value", Integer.parseInt(value));
		paraMap.put("keyword", keyword);
		paraMap.put("type", 1);
		try{
			result = siteService.switchLock(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 获取所有的waf攻击
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_all_waf_attack")
	public Result getAll() throws Exception{
		return siteService.getAll();
	}
	
	
	
	
	/**
	 * cname激活域名
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/cname_active_domain", method = RequestMethod.POST)
	public Result cnameActiveDomain(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "site_domain")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请选择要激活的网站域名");
				return result;
			}
			String create_user = request.getAttribute("userId").toString();
			if(create_user == null || "".equals(create_user)){
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("请登陆！");
				return result;
			}
			paraMap.put("userId", create_user);
			result = siteService.cnameActiveDomain(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}

}
