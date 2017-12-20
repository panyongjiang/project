package com.uway.mobile.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.SafeConfigurationService;

@RestController
@RequestMapping("safeConfig")
public class SafeConfigurationController {
	
	@Autowired
	SafeConfigurationService safeConfig;
	
	/**
	 * 设置终端黑名单
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostBlackConfig")
	public Result setHostBlackConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=safeConfig.setHostBlackConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置终端黑名单失败");
		}
		return result;
	}
	
	/**
	 * 设置终端昵称
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostNickConfig")
	public Result setHostNickConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.setHostNickConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置终端昵称失败");
		}
		return result;
	}
	
	/**
	 * 获取恶意网址屏蔽接口
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_ruleSecurityConfig")
	public Result getRuleSecurityConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.getRuleSecurityConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取恶意网址屏蔽接口失败");
		}
		return result;
	}
	
	/**
	 * 获取时区和时间
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_timeConfig")
	public Result getTimeConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.getTimeConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取时区或时间失败");
		}
		return result;
	}
	
	/**
	 * 设置时区和时间
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_timeConfig")
	public Result setTimeConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.setTimeConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置时区或时间失败");
		}
		return result;
	}
	
	/**
	 * 云端历史记录的获取
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_historyCloudConfig")
	public Result getHistoryCloudConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.getHistoryCloudConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("云端历史记录获取失败");
		}
		return result;
	}
	
	/**
	 * 云端历史记录的删除
	 */
	@RequestMapping(value="/set_historyCloudConfig")
	public Result setHistoryCloudConfig(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result=safeConfig.setHistoryCloudConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("云端历史记录获取失败");
		}
		return result;
	}

}
