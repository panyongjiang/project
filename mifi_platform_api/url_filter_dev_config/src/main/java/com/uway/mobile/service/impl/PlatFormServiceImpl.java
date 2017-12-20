package com.uway.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.HengtongConstance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.ConfigStatus;
import com.uway.mobile.handler.AssemblyHandler;
import com.uway.mobile.mapper.PlatFormMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.PlatFormService;

@Service
public class PlatFormServiceImpl extends BaseApplication implements PlatFormService{
	
	@Autowired
	private PlatFormMapper pfm;
	@Autowired
	private ApiService apiService;
	@Autowired
	private AssemblyHandler ah;
	
	@Value("${site.safe.prefix.url_hengtong}")
	public String URL_TCP;

	@SuppressWarnings("unchecked")
	@Override
	public Result setConfig(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		int logStatus=0;
		Calendar calendar = Calendar.getInstance();
		Map<String,Object> paras=ah.assemblyReqHandler(paraMap);
		if(paras==null||!paras.containsKey("RouterUUID")||!paras.containsKey("CommandType")||!paras.containsKey("Command")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数为空");
			return result;
		}
		if(HengtongConstance.SET_DEVICE_URL_LISTS.equals(paras.get("CommandType").toString())){
			Map<String,Object> urlMap = (Map<String,Object>)JSON.parse(paras.get("Command").toString());
			String urls=urlMap.get("Url_Lists").toString();
			if(urls.length()<1){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("添加黑名单不能为空");
				return result;
			}
			String[] str=urls.split(";");
			for(String url:str){
				String URL_TEST ="^(?:https?://)?[\\w]{1,}(?:\\.?[\\w]{1,})+$";
				//正则表达式的模式
		        Pattern p1 = Pattern.compile(URL_TEST);
		        //正则表达式的匹配器
		        Matcher m1 = p1.matcher(url);
		        if(!m1.matches()){
		        	result.setMsg("URL格式不正确!请检查url或者是否以分号进行分割");
					result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
					return result;
		        }
			}
		}
		//生成与TCP端对应的序列号
		if(!paraMap.containsKey("optSeq")){
			SimpleDateFormat formatterString = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String dateString = formatterString.format(calendar.getTime());
			paras.put("optSeq", dateString+"_"+paraMap.get("userId").toString());
		}else{
			paras.put("optSeq", paraMap.get("optSeq"));
			logStatus=1;
		}
		if(!paraMap.containsKey("optTime")){
			SimpleDateFormat formatterTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = formatterTime.format(calendar.getTime());
			paras.put("optTime", dateTime);
		}else{
			paras.put("optTime", paraMap.get("optTime"));
		}
		if(!paraMap.containsKey("optType")){
			paras.put("optType", 0);
		}else{
			paras.put("optType", paraMap.get("optType"));
		}
		Map<String,Object> map = apiService.doPost(URL_TCP,paras);
		if(map==null||map.containsKey("result")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设备已离线");
			return result;
		}
		if(map.containsKey("HttpException")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("连接超时");
			return result;
		}
		map.put("deviceCompanyId", paraMap.get("deviceCompanyId"));
		map.put("uwayStatus", paraMap.get("uwayStatus"));
		map.put("seqNum", paras.get("optSeq"));
		map.put("userId", paraMap.get("userId"));
		map.put("logStatus", logStatus);
		map.put("optType", paras.get("optType"));
		if(!map.containsKey("CommandType")){
			map.put("CommandType",paras.get("CommandType"));
		}
		result=ah.assemblyResHandler(map);
		return result;
	}

	@Override
	public Result getConfig(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<ConfigStatus> lists = pfm.getConfig(paraMap);
		List<Map<String,Object>> relist=new ArrayList<Map<String,Object>>();
		//list集合 判断Null与长度
		if(lists!=null&&lists.size()>0){
			for(ConfigStatus cs:lists){
				Map<String,Object> para=new HashMap<String,Object>();
				para.put("status", cs.getStatus());
				para.put("deviceId", cs.getDeviceId());
				para.put("response", cs.getResponse());
				relist.add(para);
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(relist);
		}else{
			Map<String,Object> para=new HashMap<String,Object>();
			para.put("status", "");
			para.put("deviceId", "");
			para.put("response", "");
			relist.add(para);
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setData(relist);
			result.setMsg("操作频繁，请稍等!");
			return result;
		}
		
		result.setMsg("查看日志");
		return result;
	}

	@Override
	public Result checkUpdat(Map<String, Object> paraMap) throws Exception {
		Result result =new Result();
		List<Map<String,Object>> checkVList = pfm.getVersion(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		if(checkVList.size()<1){
			Map<String,Object> par=new HashMap<String,Object>();
			par.put("update_status", 1);
			result.setData(par);
			result.setMsg("没有更新的版本");
			return result;
		}
		Map<String,Object> checkV=checkVList.get(0);
		checkV.put("update_status", 0);
		result.setData(checkV);
		result.setMsg("有可更新的版本");
		return result;
	}

	@Override
	public Result uploadFile(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		MultipartFile file = (MultipartFile) paraMap.get("file");
		String fileName=file.getName();
		String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
		prefix=prefix.toLowerCase();
		if(!"package".equals(prefix)){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("上传文件格式错误");
			return result;
		}
		paraMap.put("file", file);
		Map<String,Object> par = apiService.doPost("http://114.115.138.117:9090/ROUTER_FLOW/file/upload", paraMap);
		if(par.containsKey("id")){
			paraMap.put("mongid", par.get("id"));
			paraMap.put("deviceCompanyId", 2);
			paraMap.put("model", "MF910W");
			paraMap.put("MD5", "69ee9a9d45aa9684f5acc22926d8631e");
			paraMap.put("size", 2766333);
			paraMap.put("createTime", new Date());
			paraMap.put("uploadTime", new Date());
			paraMap.put("remarks", "软件测试");
			paraMap.put("srcVersion", "ZMPSDK7520V2V1.0.4B11_08-V1.8");
			paraMap.put("destVersion", "ZMPSDK7520V2V1.0.4B11_08-V1.8");
			pfm.insertVersion(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("上传成功");
		}else{
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("上传失败");
		}
		return result;
	}
	
}
