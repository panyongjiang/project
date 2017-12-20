package com.uway.mobile.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ConfigurationService;

@RestController
@RequestMapping("config")
public class ConfigurationController {
	
	@Autowired
	ConfigurationService config;
	
	/**
	 * 获取网络配置信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_netConfig")
	public Result getNetConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getNetConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取配置信息失败");
		}
		return result;
	}
	
	/**
	 * 获取设备信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_saas")
	public Result getSaas(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getSaasConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取配置信息失败");
		}
		return result;
	}
	
	/**
	 * 设置外网接入方式
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_netConfig")
	public Result setNetConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setNetConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取配置信息失败");
		}
		return result;
	}
	
	/**
	 * 获取wan口的状态信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_wanConfig")
	public Result getWanConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getWanConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取wan口信息失败");
		}
		return result;
	}
	
	/**
	 * 获取PPPOE账户和密码
	 * @param paraMap
	 * @return
	 */
	@RequestMapping("/get_PPPOEconfig")
	public Result getPPPOEconfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getPPPOEconfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取PPPOE账户信息失败");
		}
		return result;
	}
	
	/**
	 * 获取设备wifi的ssid或密码
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_wifiConfig")
	public Result getWifiConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getWifiConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取wifi信息失败");
		}
		return result;
	}
	
	/**
	 * 设置设备wifi的ssid或密码
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_wifiConfig")
	public Result setWifiConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(paraMap.get("passwd").toString().length()<8){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("密码长度不能小于8位");
				return result;
			}
			result=config.setWifiConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置wifi信息失败");
		}		
		return result;
	}
	
	/**
	 * wifi开关
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_wifiLock")
	public Result setwifiLock(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.wifiLock(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置wifi信息失败");
		}
		return result;
	}
	
	
	/**
	 * 设置访客模式
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_guestConfig")
	public Result setGuestConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setGuestConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置访客模式失败");
		}
		return result;
	}
	
	/**
	 * 获取设备DHCP Server信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_DHCPconfig")
	public Result getDHCPconfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getDHCPconfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取DHCP Server信息失败");
		}
		return result;
	}
	
	/**
	 * 设置设备DHCP Server信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_DHCPconfig")
	public Result setDHCPconfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setDHCPconfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置DHCP Server信息失败");
		}
		return result;
	}
	
	/**
	 * 获取终端列表
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/get_appList")
	public Result getAppList(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result = config.getAppList(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("请等待");
		}
		return result;
	}
	
	/**
	 * 获取终端信息
	 */
	@RequestMapping(value="/get_hostApp")
	public Result getHostAppConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getHostAppConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取终端信息失败");
		}
		return result;
	}
	
	/**
	 * 设置终端上网模式
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostModeConfig")
	public Result setHostModeConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setHostModeConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置终端上网模式失败");
		}
		return result;
	}
	
	/**
	 * 设置终端上网的最大速度
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostLsConfig")
	public Result setHostLsConfig(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setHostLsConfig(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置终端上网最大速度失败");
		}
		return result;
	}
	
	
	/**
	 * 设置终端名称
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostName")
	public Result setHostName(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setHostName(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置终端名称失败");
		}
		return result;
	}
	
	
	/**
	 * 主机端口映射
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_hostNat")
	public Result setHostNat(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.setHostNat(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设置映射失败");
		}
		return result;
	}
	
	/**
	 *URL黑白名单
	 *@param  paraMap
	 *@return	
	 */
	@RequestMapping(value="/safe_Url")
	public Result safeInform(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.interceptUrl(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		
		return result;
	}
	
	/**
	 * 获取黑名单
	 */
	@RequestMapping(value="/get_blackUrl")
	public Result getBlackUrl(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=config.getBlackUrl(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		return result;
	}
	
	
	/**
	 * 恢复出厂设置、重启路由
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value="/reboot_route")
	public Result rebootRoute(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result = config.rebootRoute(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("请等待");
		}
		return result;
	}
	
	/**
	 * 获取VPN信息
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value="/get_vpn")
	public Result getVpn(@RequestBody Map<String,Object> paraMap){
		Result result=new Result();
		try {
			result=config.getVpn(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * 设置VPN
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/set_vpn")
	public Result setVpn(@RequestBody Map<String,Object> paraMap){
		Result result=new Result();
		try {
			//校验账号密码
			if(!paraMap.containsKey("act")){
				String username=paraMap.get("user").toString();
				String password=paraMap.get("password").toString();
				String sdomain =paraMap.get("sdomain").toString();
				//校验IP地址
				String serverip=paraMap.get("serverip").toString();
				String ip = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +  
				         "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +  
				         "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +  
				         "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";
				if(!serverip.matches(ip)){
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					result.setMsg("服务器IP格式不正确");
					return result;
				}
				if(username.length()>31||password.length()>31||sdomain.length()>31){
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					result.setMsg("服务器域名或者账号密码长度过长");
					return result;
				}
			}
			result=config.setVpn(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * 网络检测
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/testspeed")
	public Result testSpeed(@RequestBody Map<String,Object> paraMap){
		Result result=new Result();
		try {
			result=config.testSpeed(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("操作失败");
		}
		return result;
	}
	
}
