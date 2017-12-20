package com.uway.mobile.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.PlatFormService;

@RestController
@RequestMapping("plat")
public class PlatFormController extends BaseApplication{
	
	@Autowired
	public PlatFormService pfs;
	
	/**
	 * 设备设置
	 * 统一参数转化
	 * @param paraMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/set_config", method = RequestMethod.POST)
	public Result setConfig(@RequestBody Map<String,Object> paraMap,HttpServletRequest request){
		
		Result result = new Result();
		String userId = request.getAttribute("userId").toString();
		paraMap.put("userId", userId);
		//查询设备商
		try {
			if(!paraMap.containsKey("deviceCompanyId")||!paraMap.containsKey("uwayStatus")||!paraMap.containsKey("CommandType")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数为空");
				return result;
			}
		   result=pfs.setConfig(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	@RequestMapping(value="get_config", method = RequestMethod.POST)
	public Result getConfig(@RequestBody Map<String,Object> paraMap,HttpServletRequest request){
		Result result = new Result();
		try {
			result=pfs.getConfig(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	@RequestMapping(value="upload", method = RequestMethod.POST)
	public Result upLoad(@RequestBody Map<String,Object> paraMap,HttpServletRequest request){
		Result result = new Result();
		try {
			result=pfs.uploadFile(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			log.debug("upload", e);
			return result;
		}
		return result;
	}
	
	/**
	 * 检测固件升级
	 * 是否有差异包
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="check_update",method=RequestMethod.POST)
	public Result checkUpdate(@RequestBody Map<String,Object> paraMap){
		Result result = new Result();
		try {
			result=pfs.checkUpdat(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			log.debug("check_update", e);
			return result;
		}
		return result;
	}

}
