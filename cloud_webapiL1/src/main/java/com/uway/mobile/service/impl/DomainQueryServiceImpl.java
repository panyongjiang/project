package com.uway.mobile.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.DomainQueryService;
import com.uway.mobile.util.SignUtil;

@Service
public class DomainQueryServiceImpl implements DomainQueryService{
	
	@Autowired
	private ApiService apiService;

	@Override
	public Result selectDomain(Map<String,Object> paraMap) throws Exception {
		Result result = new Result();
		JSONObject strJson=null;
		int page=Integer.parseInt(paraMap.get("page").toString());
		long time = SignUtil.getTime();
		if(""==paraMap.get("domain").toString()||paraMap.get("domain").toString()==null){
			String strs[]=new String[]{"time="+time,"page="+page};
			paraMap.put("paras", strs);
			strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/site?"+SignUtil.sort(strs),paraMap);
		}else{
			String domain=paraMap.get("domain").toString();
			domain = domain.replace("http://", "");
			domain = domain.replace("https://", "");
			domain = domain.replace("www.", "");
			String strs[] = new String[] {"time=" + time,"domain="+domain,"page="+page};
			paraMap.put("paras", strs);
			strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/site?"+SignUtil.sort(strs),paraMap);
		}
		if(strJson.getString("code")==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			return result;
		}
		if(!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null||data.get("sites")==null||data.getString("sites")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		String datas = strJson.getString("data").toString();
		result.setData(datas);
		return result;
	}

}
