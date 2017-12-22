package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Certificate;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.SubDomainList;
import com.uway.mobile.mapper.CertificateMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.CertificateService;
import com.uway.mobile.util.SignUtil;

@Service
public class CertificateServiceImpl implements CertificateService{
	
	@Autowired
	CertificateMapper cerMapper;
	@Autowired
	private ApiService apiService;
	
	
	@Override
	public Result getSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<Site> site = cerMapper.getSite(paraMap);
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("value", site);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("列表获取成功");
		return result;
	}
	
	@Override
	public Result getSiteSon(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<SubDomainList> sl= cerMapper.getSiteSonById(paraMap);
		if(sl.isEmpty()){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("该站点没有绑定子域名");
			result.setData(null);
		}else{
			result.setData(sl);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取子域名列表成功");
		}
		return result;
	}
	
	@Override
	public Result addCer(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Certificate cer=new Certificate();
		long time = SignUtil.getTime();
		paraMap.put("time", time); 
		//查询域名sitedomain和子域名point
		SubDomainList sdl = cerMapper.getDomainById(paraMap);
		String paras[] = new String[] {"time="+time,"domain="+sdl.getsDomain(),"pem="+paraMap.get("pem").toString(),"subdomains="+"[\""+sdl.getPoint()+"\"]" };
		//String paras[] = new String[] {"time="+time,"domain=ynicity.cn","pem="+paraMap.get("pem").toString(),"subdomains=[\"passport.ynicity.cn\"]" };
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/ssl", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			result.setData(null);
			return result;
		}
		
		if(strJson.getString("status").equals("success")){
			String cer_name = paraMap.get("cer_name").toString();
			String cer_url = paraMap.get("cer_url").toString();
			String siteSonId = paraMap.get("ssid").toString();
			cer.setCerName(cer_name);
			cer.setCerUrl(cer_url);
			cer.setSiteSonId(siteSonId);
			cer.setCreateUser(Integer.parseInt(paraMap.get("createUser")
				.toString()));
			cerMapper.insertCer(cer);
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("操作成功！");
		}
		return result;
	}

	@Override
	public Result getCer(Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("time", time); 
		String paras[] = new String[] {"time="+time,"domain="+paraMap.get("domain").toString()};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/ssl", paraMap);
		if(strJson.getString("status").equals("success")){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(strJson.getString("data"));
			result.setMsg("获取列表成功！");
		}else{
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
		}
		return result;
	}

	@Override
	public Result delCer(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("time", time); 
		String paras[] = new String[] {"time="+time,"id="+paraMap.get("cer_id").toString()};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafDelete("https://www.yunaq.com/api/v3/ssl", paraMap);
		if(strJson.getString("status").equals("success")){
			//查询要删除证书的url
			
			//删除mongodb中的证书
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(strJson.getString("data"));
			result.setMsg("删除证书成功！");
		}else{
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
		}
		return result;
	}

	

	

}
