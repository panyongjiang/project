package com.uway.mobile.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.InterceptUrl;
import com.uway.mobile.domain.Port;
import com.uway.mobile.domain.SafeManagement;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.SubDomainList;
import com.uway.mobile.mapper.WafConfigMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.WafConfigService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.SignUtil;

@Service
public class WafConfigServiceImpl implements WafConfigService{
	
	@Autowired
	private ApiService apiService;
	@Autowired
	private WafConfigMapper wafConfigMapper;
	
	@Override
	public Result getPortList(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Long totalNum=wafConfigMapper.countPort(paraMap);
		List<Port> port = wafConfigMapper.getPort(paraMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		map.put("value", port);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("列表获取成功");
		return result;
	}
	
	@Override
	public Result portSet(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		//端口名称去重复	
		Map<String,Object> resultMap = wafConfigMapper.SelectPortNameById(paraMap);
		if(resultMap!=null){
			result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
			result.setMsg("端口名称已存在！");
			return result;
		}
		//端口信息设置去重
		Map<String,Object> resultMap2 = wafConfigMapper.SelectPortInfoById(paraMap);
		if(resultMap2!=null){
			result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
			result.setMsg("端口信息重复！");
			return result;
		}
		long time = SignUtil.getTime();
		paraMap.put("time", time);
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("子域名ID不能为空！");
			return result;
		}
		Site site = wafConfigMapper.getSiteById(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		if(site.getTaskId()==null||site.getTaskId()==""){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未激活！");
			return result;
		}
		String paras[] = new String[] {"time="+time,"sid="+paraMap.get("sid").toString(),"id="+paraMap.get("id").toString(),"values="+paraMap.get("values")};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/ports_set", paraMap);
		
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_HOST_ERROR);
			result.setMsg(strJson.getString("message"));
		}
		
		if(strJson.getString("status").equals("success")){
			wafConfigMapper.insertPort(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("操作成功！");
		}
		return result;
	}

	@Override
	public Result getDomain(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Long totalNum = wafConfigMapper.countSiteSon(paraMap);
		List<SubDomainList> list = wafConfigMapper.getDomainList(paraMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		map.put("value", list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("列表获取成功");
		return result;
	}
	
	@Override
	public Result switchLock(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("time", time);
		if(ObjectUtil.isEmpty(paraMap, "sid")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("主站ID不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("子域名ID不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "value")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("状态不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "keyword")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("开关字段不能为空！");
			return result;
		}
		if(!"0".equals(paraMap.get("value").toString()) && !"1".equals(paraMap.get("value").toString())){
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("开关字段不正确！");
			return result;
		}
		
		Site site = wafConfigMapper.getSiteById(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		if(site.getTaskId()==null||site.getTaskId()==""){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未激活！");
			return result;
		}
		
		String paras[] = new String[] {"time="+time,"sid="+paraMap.get("sid").toString(),"id="+paraMap.get("id"),"value="+paraMap.get("value"),"keyword="+paraMap.get("keyword")};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/switch", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_HOST_ERROR);
			result.setMsg(strJson.getString("message"));
		}
		if(strJson.getString("status").equals("success")){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("操作成功");
		}
		return result;
	}

	@Override
	public Result getSafeList(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Long totalNum = wafConfigMapper.countSafeManagement();
		List<SafeManagement> list = wafConfigMapper.getSafeList(paraMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		map.put("value", list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("列表获取成功");
		
		return result;
	}
	
	@Override
	public Result InList(Map<String, Object> paraMap) throws Exception {
	    Result result = new Result();
	    if (!ObjectUtil.isEmpty(paraMap, "i_name")) {
			paraMap.put("iName", "%"
					+ paraMap.get("i_name").toString() + "%");
		}
	    if (!ObjectUtil.isEmpty(paraMap, "i_type")) {
			paraMap.put("iType", paraMap.get("i_type").toString());
		}
	    if (!ObjectUtil.isEmpty(paraMap, "ip")) {
			paraMap.put("ip", "%"
					+ paraMap.get("ip").toString() + "%");
		}
	    if (!ObjectUtil.isEmpty(paraMap, "start_time")) {
			paraMap.put("startTime", paraMap.get("start_time").toString());
		}
	    if (!ObjectUtil.isEmpty(paraMap, "end_time")) {
			paraMap.put("endTime", paraMap.get("end_time").toString());
		}
	    Long totalNum = wafConfigMapper.countInList(paraMap);
	    List<Map<String,Object>> list = wafConfigMapper.getAllList(paraMap);
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		map.put("value", list);
	    result.setData(map);
	    result.setCode(Constance.RESPONSE_SUCCESS);
	    result.setMsg("获取列表成功");
		return result;
	}
	
	@Override
	public Result insertIpOrUrl(Map<String, Object> paraMap) throws Exception {
		    Result result = new Result();
		    long time = SignUtil.getTime();
		    JSONObject strJson = new JSONObject();
		    paraMap.put("time", time);
		    paraMap.put("sid", paraMap.get("sid").toString());
		    Site site = wafConfigMapper.getSiteById(paraMap);
			if(site==null){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的网站");
				return result;
			}
			if(site.getTaskId()==null||site.getTaskId()==""){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站未激活！");
				return result;
			}
			//查询黑白名单的个数
			List<InterceptUrl> list=wafConfigMapper.getLen(paraMap);
			String value="[";
			//添加操作
			if(paraMap.get("status").toString().equals("0")){
				if(list.size()>=5){
					result.setMsg("每个子域名最多绑定5个"+paraMap.get("i_type").toString()+",请先删除!");
					result.setCode(Constance.RESPONSE_PARAM_EMPTY);
					result.setData(null);
					return result;
				}else{
					for(int i=0;i<list.size();i++){
						value+= "\""+list.get(i).getIp().toString()+"\",";
					}
				}
			//删除操作	
			}else if(paraMap.get("status").toString().equals("1")){
				for(int i=0;i<list.size();i++){
					if(paraMap.get("ip").toString().equals(list.get(i).getIp())){
						continue;
					}else{
						value+= "\""+list.get(i).getIp().toString()+"\",";
					}
				}
			//修改操作	
			}else if(paraMap.get("status").toString().equals("2")){
				wafConfigMapper.updateIpOrUrl(paraMap);
				result.setMsg("修改名称成功");
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setData(null);
				return result;
			}
			value+="\""+paraMap.get("ip").toString()+"\"";
			value+="]";
			paraMap.put("values", value);
			String paras[] = new String[] {"time="+time,"sid="+paraMap.get("sid").toString(),"id="+paraMap.get("id").toString(),"values="+value,"keyword="+paraMap.get("keyword").toString()};
			paraMap.put("paras", paras);
			//url入白
			if(paraMap.get("keyword").toString().equals("attack_whitelist")){
				strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/url_white_black_list", paraMap);
			//ip入黑白
			}else if(paraMap.get("keyword").toString().equals("ip_whitelist")||paraMap.get("keyword").toString().equals("ip_blacklist")){ 
				strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/ip_white_black_list", paraMap);
			}else{
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("无效的操作");
				return result;
			}
			
			
			if(strJson.getString("status").equals("success")){
				//存入到本地数据库
				paraMap.put("date", new Date());
				if(paraMap.get("status").toString().equals("0")){
					wafConfigMapper.insertIpOrUrl(paraMap);
				}else{
					wafConfigMapper.deleteIpOrUrl(paraMap);
				}
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("操作成功");
			}
			
			if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
				result.setCode(Constance.RESPONSE_HOST_ERROR);
				result.setMsg(strJson.getString("message"));
			}
			
		return result;
	}

}
