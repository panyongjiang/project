package com.uway.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.DeviceService;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.AuthorityUser;
import com.uway.mobile.util.Pagination;

@RestController
@RequestMapping("home")
public class HomePageController {
	
	@Autowired
	private EsService esService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	AuthorityUser au;
	@Autowired
	public UserService userService;
	
	
	/**
	 * 数据统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/data_statistics", method = RequestMethod.POST)
	public Result dataStatistics(HttpServletRequest request){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()==1){
				paramMap.put("role", "1");
			}
			paramMap.put("companyId", String.valueOf(adminUser.getCompanyId()));
			List<Device> deviceList = deviceService.getDevicesByUserId(paramMap);
			if(deviceList==null||deviceList.size()<=0){
				result.setData("");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("未接入设备或设备未激活");
				return result;
			}
			long totalAlerts = esService.getTotalAlert(deviceList);
			Map<String,String> map = new HashMap<String,String>();
			map.put("deviceNum", deviceList.size()+"");
			map.put("totalAlerts", totalAlerts+"");
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 被拦截的URL列表
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_interceptUrl", method = RequestMethod.POST)
	public Result getInterceptUrl(HttpServletRequest request,@RequestBody Map<String, String> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()==1){
				paraMap.put("role", "1");
			}
			paraMap.put("companyId", String.valueOf(adminUser.getCompanyId()));
			paraMap.put("userId", userId);
			List<Device> deviceList = deviceService.getDevicesByUserId(paraMap);
			if(deviceList==null||deviceList.size()<=0){
				result.setData("");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("未接入设备或设备未激活");
				return result;
			}
			Pagination pagination = esService.getInterceptorUrls(paraMap,deviceList);
			if(pagination.getDetails()!=null&&pagination.getDetails().size()>0){
				result.setData(pagination);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("查询成功");
			}else{
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("查询为空");
			}
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * 被拦截的统计图
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_countUrl", method = RequestMethod.POST)
	public Result getCountUrl(HttpServletRequest request,@RequestBody Map<String,Object> paraMap){
		Result result = new Result();
		try {
			String userId = request.getAttribute("userId").toString();
			paraMap.put("userId", userId);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()==1){
				paramMap.put("role", "1");
			}
			paramMap.put("companyId", String.valueOf(adminUser.getCompanyId()));
			List<Device> deviceList = deviceService.getDevicesByUserId(paramMap);
			if(deviceList==null||deviceList.size()<=0){
				result.setData("");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("未接入设备或设备未激活");
				return result;
			}
			result=esService.getCountUrl(paraMap,deviceList);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 最近一个月和流量统计图
	 * @param
	 * @return 
	 */
	@RequestMapping(value="/get_flow", method = RequestMethod.POST)
	public Result getFlow(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		String userId = request.getAttribute("userId").toString();
		paraMap.put("userId", userId);
		try {
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("userId", userId);
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()==1){
				paramMap.put("role", "1");
			}
			paramMap.put("companyId", String.valueOf(adminUser.getCompanyId()));
			List<Device> deviceList = deviceService.getDevicesByUserId(paramMap);
			if(deviceList==null||deviceList.size()<=0){
				result.setData("");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("未接入设备或设备未激活");
				return result;
			}
			paraMap.put("list", deviceList);
			result=deviceService.queryFlow(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设备已离线");
			e.printStackTrace();
		}
		return result;
	}
	
}
