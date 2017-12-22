package com.uway.mobile.controller;

import java.util.HashMap;
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
import com.uway.mobile.service.WafConfigService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("wafConfig")
public class WafConfigurationController {
	
	@Autowired
	private WafConfigService wafConfigService;
	
	/**
	 * 端口列表
	 * @return
	 */
	@RequestMapping(value="/get_port")
	public Result getPort(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		paraMap.put("user_id", request.getAttribute("userId").toString());
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result=wafConfigService.getPortList(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取列表失败");
		}
		return result;
	}
	
	/**
	 * 端口设置
	 * @param values
	 * @param sid
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/ports_set")
	public Result portsSet(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		paraMap.put("user_id", request.getAttribute("userId").toString());
		try {			
			if(ObjectUtil.isEmpty(paraMap, "sid")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站域名不能为空！");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("子域名不能为空！");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "scheme")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("对外协议不能为空！");
				return result;
			}
			int listenPort=Integer.parseInt(paraMap.get("listenPort").toString());
			if(ObjectUtil.isEmpty(paraMap, "listenPort")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("对外监听端口不能为空！");
				return result;
			}else if(listenPort<=80||listenPort>60000){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("对外监听端口范围必须在:80到60000之间");
				return result;
			}else if(listenPort==443||listenPort==1111||listenPort==873||listenPort==36000||(listenPort>6300&&listenPort<6400)){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("对外监听端口不能设置为:"+listenPort);
				return result;
			}
			int portNum = Integer.parseInt(paraMap.get("port").toString());
			if(ObjectUtil.isEmpty(paraMap, "port")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("源站端口不能为空！");
				return result;
			}else if(portNum<0||portNum>65535){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("源站端口范围必须在:0到65535之间!");
				return result;
			}else if(portNum==80){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("源站端口不能设置为80!");
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "status")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("状态值不能为空！");
				return result;
			}
			paraMap.put("sid", paraMap.get("sid"));
			paraMap.put("id", paraMap.get("id"));
			String ip = "[{\"scheme\":\""+paraMap.get("scheme").toString()+"\",\"listen\":"+paraMap.get("listenPort").toString()+",\"bkd_scheme\":\"http\",\"port\":"+paraMap.get("port").toString()+",\"status\":"+paraMap.get("status").toString()+"}]";
			paraMap.put("values",ip );
			result = wafConfigService.portSet(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置失败");
		}
		return result;
	}
	
	/**
	 * 子域名列表
	 * @return
	 */
	@RequestMapping(value="/get_domain",method = RequestMethod.POST)
	public Result getDomain(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		paraMap.put("user_id", request.getAttribute("userId").toString());
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result=wafConfigService.getDomain(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取列表失败");
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
			result = wafConfigService.switchLock(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 网站防护开关列表
	 * @return
	 */
	@RequestMapping(value="/get_safe",method=RequestMethod.POST)
	public Result getSafeList(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result=wafConfigService.getSafeList(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取安全防护列表失败");
		}
		return result;
	}
	
	/**
	 * 获取拦截信息列表
	 * @return
	 */
	@RequestMapping("/get_intercept_list")
	public Result getInList(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		if(request.getAttribute("userId") !=null){		
			paraMap.put("userId", request.getAttribute("userId").toString());
		}else{
			result.setMsg("请登录！");
			result.setCode(Constance.RESPONSE_USER_ERROR);
			return result;
		}
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result=wafConfigService.InList(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取拦截信息失败");
		}
		return result;
	}
	
	/**
	 * 添加黑白名单
	 * @param sid 网站id
	 * @param id  子域名id
	 * @param ip  添加的ip或url
	 * @param keyword 关键字表明做入黑还是入白操作
	 * @return
	 */
	@RequestMapping(value="/add_ipOrUrl",method=RequestMethod.POST)
	public Result addSource(HttpServletRequest request,
            @RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		paraMap.put("user_id", request.getAttribute("userId").toString());
		try {
			if(paraMap.isEmpty()){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数全部为空值！");
				return result;
			}else if(paraMap.get("status").toString().equals("0")){
				if(paraMap.get("ip").toString().equals("")||paraMap.get("ip").equals(null)){
					result.setCode(Constance.RESPONSE_PARAM_EMPTY);
					result.setMsg("IP不能为空！");
					return result;
				}
			}
			result=wafConfigService.insertIpOrUrl(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("黑白名单处理失败");
		}
		return result;
	}
	

}
