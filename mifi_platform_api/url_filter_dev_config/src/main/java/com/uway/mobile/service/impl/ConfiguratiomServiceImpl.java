package com.uway.mobile.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.ConfigurationService;
import com.uway.mobile.util.TraversalMapUtil;

@Service
public class ConfiguratiomServiceImpl implements ConfigurationService{
	
	@Value("${site.safe.prefix.url}")
    public String SAFE_URL;
	@Autowired
	private ApiService apiService;
	@Autowired
	public TraversalMapUtil tsm;

	@Override
	public Result getNetConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","net");
		paraMap.put("opt", "wan_conf");
		paraMap.put("function","get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result getSaasConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","sys");
		paraMap.put("opt", "saas");
		paraMap.put("function","get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setNetConfig(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "wan_conf");
		paraMap.put("function", "set");
		//PPPOE外网接入方式
		if(paraMap.get("mode").equals("2")
				||paraMap.get("mode").equals("1")
				||paraMap.get("mode").equals("3")
				||paraMap.get("mode").equals("4")
				||paraMap.get("mode").equals("5")){
			
			String url = tsm.transfer(paraMap);
			Map<String, Object> configCode=apiService.doGet(url);
			if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
				 result.setCode(Constance.RESPONSE_INNER_ERROR);
		    	 result.setMsg("设备已离线");
		    	 return result;
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(configCode.get("res"));
		
		/*//DHCP外网接入方式
		}else if(paraMap.get("mode").equals("1")){
			
		//STATIC外网接入方式
		}else if(paraMap.get("mode").equals("3")){
			
		//WISP外网接入方式
		}else if(paraMap.get("mode").equals("WISP")){
			
		//3G外网接入方式
		}else if(paraMap.get("mode").equals("5")){*/
			
		//无效的设置
		}else{
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("无效的配置");
			return result;
		}
		return result;
	}

	@Override
	public Result getWanConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result getPPPOEconfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result getWifiConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","net");
		paraMap.put("opt", "wifi_ap");
		paraMap.put("function","get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setWifiConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","net");
		paraMap.put("opt", "wifi_ap");
		paraMap.put("function","set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setGuestConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result getDHCPconfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "dhcpd");
		paraMap.put("function", "get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setDHCPconfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "dhcpd");
		paraMap.put("function", "get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result getAppList(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","system");
		paraMap.put("opt", "main");
		paraMap.put("function","get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result getHostAppConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result= new Result();
		paraMap.put("fname","system");
		paraMap.put("opt", "host_app");
		paraMap.put("function","get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setHostModeConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result= new Result();
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result setHostLsConfig(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "host_ls");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result setHostName(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "host_nick");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result wifiLock(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "wifi_lt");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public Result setHostNat(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "host_nat");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		System.out.println(((Map<String,Object>)JSON.parse(configCode.get("res").toString())).get("error"));
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		if(((Map<String,Object>)JSON.parse(configCode.get("res").toString())).get("error").equals("10004")){
			result.setMsg("添加的条目已存在");
		}else if(((Map<String,Object>)JSON.parse(configCode.get("res").toString())).get("error").equals("10005")){
			result.setMsg("删除的条目不存在");
		}else if(((Map<String,Object>)JSON.parse(configCode.get("res").toString())).get("error").equals("10006")){
			result.setMsg("添加条目已满");
		}else if(((Map<String,Object>)JSON.parse(configCode.get("res").toString())).get("error").equals("10007")){
			result.setMsg("请重新登录");
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}
	
	@Override
	public Result interceptUrl(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "plug_cgi");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		result.setMsg("黑名单操作成功");
		return result;
	}
	
	@Override
	public Result getBlackUrl(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "plug_cgi");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		result.setMsg("获取黑名单成功");
		return result;
	}

	@Override
	public Result rebootRoute(Map<String, Object> paraMap) throws Exception {
		Result result= new Result();
		paraMap.put("fname","system");
		paraMap.put("opt", "setting");
		paraMap.put("function","set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	@Override
	public Result getVpn(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		paraMap.put("fname", "net");
		paraMap.put("opt", "tunnel");
		paraMap.put("function", "get");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		result.setMsg("获取VPN成功");
		return result;
	}

	@Override
	public Result setVpn(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		if(!paraMap.containsKey("act")){
			if(paraMap.get("index").toString()!=""&&paraMap.get("index").toString()!=null){
				paraMap.put("act", "mod");
			}else{
				paraMap.put("act", "add");
			}
		}
		paraMap.put("fname", "net");
		paraMap.put("opt", "tunnel");
		paraMap.put("function", "set");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		result.setMsg("设置VPN成功");
		return result;
	}

	@Override
	public Result testSpeed(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("fname","net");
		paraMap.put("opt", "testspeed");
		String url = tsm.transfer(paraMap);
		Map<String, Object> configCode=apiService.doGet(url);
		if(configCode==null||Integer.parseInt(configCode.get("code").toString())!=0){
			 result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	 result.setMsg("设备已离线");
	    	 return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(configCode.get("res"));
		return result;
	}

	

	


}
