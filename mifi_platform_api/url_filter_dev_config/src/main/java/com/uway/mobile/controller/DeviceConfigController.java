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
import org.springframework.web.multipart.MultipartFile;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.SecSetting;
import com.uway.mobile.domain.User;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.ConfigurationService;
import com.uway.mobile.service.DeviceConfigService;
import com.uway.mobile.util.AuthorityUser;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("device")
public class DeviceConfigController {
	
	@Autowired
	private DeviceConfigService dcs;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	public ConfigurationService cs;
	@Autowired
	public AuthorityUser au;
	
	
	/**
	 * 搜索设备
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/select_route", method = RequestMethod.POST)
	public Result selectRoute(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(!paraMap.containsKey("userId")||paraMap.get("userId")==null||"".equals(paraMap.get("userId"))){
				String userId = request.getAttribute("userId").toString();
				paraMap.put("userId", userId);
			}
			result = dcs.selectDevice(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("搜索失败");
		}
		return result;
		
	}
	
	/**
	 * 获取设备型号与版本
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_deviceInfo", method = RequestMethod.POST)
	public Result getDeviceInfo(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result = dcs.getDeviceInfo(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设备版本型号获取失败");
		}
		return result;
	}
	
	
	/**
	 * 根据设备ID查询安全设置
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/findSecSettingByDeviceId", method = RequestMethod.POST)
	public Result findSecSettingByDeviceId(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			String deviceId = paraMap.get("deviceId")==null?"":paraMap.get("deviceId").toString();
			String userId = request.getAttribute("userId").toString();
			SecSetting secSetting = dcs.findSecSettingByDeviceId(deviceId,userId);
			result.setData(secSetting);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}
	
	/**
	 * 安全设置的保存功能
	 * @param request
	 * @param secSetting
	 * @return
	 */
	@RequestMapping(value="/saveSecSetting", method = RequestMethod.POST)
	public Result saveSecSetting(HttpServletRequest request,@RequestBody SecSetting secSetting){
		Result result = new Result();
		try {
			dcs.saveSecSetting(secSetting);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("保存失败");
		}
		return result;
	}
	
	/**
	 * 设备批量导入
	 */
	@RequestMapping(value="/insertRouteLists", method = RequestMethod.POST)
	public Result insertRoute(HttpServletRequest request
		,@RequestParam(value = "file", required = true) MultipartFile file){
		Result result = new Result();
		String deviceCompanyId=request.getHeader("deviceCompanyId");
		String companyId=request.getHeader("companyId");
		Map<String,Object> paraMap=new HashMap<String,Object>();
		try {
			String userId = request.getAttribute("userId").toString();
			paraMap.put("userId", userId);
			paraMap.put("deviceCompanyId", deviceCompanyId);
			paraMap.put("companyId", companyId);
			result = dcs.insertRoute(paraMap,file);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取用户列表
	 */
	@RequestMapping(value="/getUserLists", method = RequestMethod.POST)
	public Result getUser(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			String userId=request.getAttribute("userId").toString();
			paraMap.put("userId", userId);
			List<User> lists=au.getUsers(paraMap);
			result.setData(lists);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取用户列表成功");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 单个导入设备
	 */
	@RequestMapping(value="/insertOneRoute", method = RequestMethod.POST)
	public Result insertOneRoute(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=dcs.insertOneRoute(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 绑定用户
	 */
	@RequestMapping(value="/bindRoute", method = RequestMethod.POST)
	public Result bindRoute(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=dcs.bindRoute(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 未绑定过的设备查询
	 */
	@RequestMapping(value="fBindRoute", method = RequestMethod.POST)
	public Result fBindRoute(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
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
			result=dcs.fBindRoute(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
		
	}
	
	/**
	 * 激活设备数
	 */
	@RequestMapping(value="/get_macNum", method = RequestMethod.POST)
	public Result getMacNum(HttpServletRequest request,@RequestBody Map<String,Object> paraMap){
		Result result = new Result();
		try {
			String userId=request.getAttribute("userId").toString(); 
			paraMap.put("userId", userId);
			result=dcs.getMacNum(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
		
	}
	
	/**
	 * 删除设备
	 */
	@RequestMapping(value="/delete_device", method = RequestMethod.POST)
	public Result delDevice(@RequestBody Map<String,Object> paraMap){
		Result result = new Result();
		try {
			result=dcs.delDevice(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 更新是设备
	 */
	@RequestMapping(value="update_device",method=RequestMethod.POST)
	public Result updateDevice(@RequestBody Map<String,Object>paraMap){
		Result result=new Result();
		try {
			result=dcs.updateDevice(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	

}
