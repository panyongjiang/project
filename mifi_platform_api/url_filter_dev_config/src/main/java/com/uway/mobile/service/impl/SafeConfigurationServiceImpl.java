package com.uway.mobile.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Result;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.SafeConfigurationService;

@Service
@SuppressWarnings("unused")
public class SafeConfigurationServiceImpl implements SafeConfigurationService{
	
	@Value("${site.safe.prefix.url}")
    public String SAFE_URL;
	@Autowired
	private ApiService apiService;
	
	@Override
	public Result setHostBlackConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		return result;
	}

	@Override
	public Result setHostNickConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		return result;
	}

	@Override
	public Result getRuleSecurityConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		String url = SAFE_URL+"protocol.csp?fname="+paraMap.get("fname")
			     +"&opt="+paraMap.get("opt")
			     +"&function="+paraMap.get("function")
			     +"&sercurity="+paraMap.get("sercurity");
		Map<String, Object> configCode=apiService.doGet(url);
		return result;
	}

	@Override
	public Result getTimeConfig(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String url = SAFE_URL+"protocol.csp?fname="+paraMap.get("fname")
			     +"&opt="+paraMap.get("opt")
			     +"&function="+paraMap.get("function");
	    Map<String, Object> configCode=apiService.doGet(url);
		return result;
	}

	@Override
	public Result setTimeConfig(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		return result;
	}

	@Override
	public Result getHistoryCloudConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result=new Result();
		String url = SAFE_URL+"protocol.csp?fname="+paraMap.get("fname")
			     +"&opt="+paraMap.get("opt")
			     +"&function="+paraMap.get("function")
			     +"&his_type="+paraMap.get("his_type");
	    Map<String, Object> configCode=apiService.doGet(url);
		return result;
	}

	@Override
	public Result setHistoryCloudConfig(Map<String, Object> paraMap)
			throws Exception {
		Result result=new Result();
		String url = SAFE_URL+"protocol.csp?fname="+paraMap.get("fname")
			     +"&opt="+paraMap.get("opt")
			     +"&function="+paraMap.get("function")
			     +"&his_type="+paraMap.get("his_type");
	    Map<String, Object> configCode=apiService.doGet(url);
		return result;
	}
	
	

}
