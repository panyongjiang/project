package com.uway.mobile.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.IccidParam;
import com.uway.mobile.domain.IccidParam.Attribution;
import com.uway.mobile.domain.IccidParam.Operator;
import com.uway.mobile.domain.Params;
import com.uway.mobile.mapper.ParamsMapper;

/**
 * 遍历map，取key=value
 * @author java_ztx
 *
 */
@Component
public class TraversalMapUtil {
	
	@Value("${site.safe.prefix.url}")
    public String SAFE_URL;
	@Autowired
	public ParamsMapper pcs;
	
	public TraversalMapUtil(){
		
	}
	
	@SuppressWarnings("deprecation")
	public String transfer(Map<String,Object> paraMap){
		String transfer="";
		for(String key:paraMap.keySet()){
			if(key.equals("deviceid")){
				continue;
			}
			transfer+=key+"="+paraMap.get(key)+"&";
		}
		String url = SAFE_URL+"?act=recMsg"+"&deviceid="+paraMap.get("deviceid")+"&transfer="+URLEncoder.encode(transfer.substring(0,(transfer.length()-1)));
		return url;
	}
	
	public Map<String,Object> changeResParas(Map<String,Object> paraMap){
		Map<String,Object> resParaMap=new HashMap<String,Object>();
		try {
			List<Params> params=pcs.getById(paraMap.get("deviceCompanyId").toString());
			if(params!=null&params.size()>0){
				for(String key:paraMap.keySet()){
					for(Params para:params){
						if(key.equals(para.getDestParam())){
							resParaMap.put(para.getSrcParam(), paraMap.get(key));
						}
					}
				}
			}
		} catch (Exception e) {
			Log.debug(e);
		}
		return resParaMap;
	}
	
	public Map<String,Object> changeReqParas(Map<String,Object> paraMap){
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> mapParas=new HashMap<String,Object>();
		try {
			List<Params> params=pcs.getById(paraMap.get("deviceCompanyId").toString());
			if(params!=null&&params.size()>0){
				for(String keyMap:paraMap.keySet()){
					for(Params keyParam:params){
						if(keyMap.equals(keyParam.getSrcParam())){
							if("RouterUUID".equals(keyParam.getDestParam())||"CommandType".equals(keyParam.getDestParam())){
								mapParas.put(keyParam.getDestParam(), paraMap.get(keyMap));
							}else if(!"deviceCompanyId".equals(keyMap)){
								map.put(keyParam.getDestParam(), paraMap.get(keyMap));
							}
						}
					}
				}
			}
			mapParas.put("Command", JSONObject.toJSON(map));
			mapParas.put("deviceCompanyId", paraMap.get("deviceCompanyId"));
		} catch (Exception e) {
			Log.debug(e);
		}
		return mapParas;
	}
	
	public String getSeqNum(HttpServletRequest request){
		SimpleDateFormat formatterString = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar calendar = Calendar.getInstance();
		String time = formatterString.format(calendar.getTime());
		String userId = request.getAttribute("userId").toString();
		String seqNum=time+"_"+userId;
		return seqNum;
		
	}
	
	public void getICCID(List<Device> device){
		for(Device di:device){
			String str=di.getSimIccid();
			String operator=str.substring(0,6);
			String attribution=str.substring(8,10);
			Boolean flagPro=false;
			for(Attribution e:IccidParam.Attribution.values()){
				String abution=e.toString();
				String abt=abution.substring(abution.length()-2,abution.length());
				if(abt.equals(attribution)){
					String province=abution.substring(0,abution.length()-2);
					di.setProvince(province);
					flagPro=true;
					break;
				}
			}
			if(!flagPro){
				di.setProvince("未知归属地");
			}
			Boolean flagOpe=false;
			for(Operator o:IccidParam.Operator.values()){
				String op=o.toString();
				String operat=op.substring(op.length()-6,op.length());
				if(operat.equals(operator)){
					operator=op.substring(0,2);
					di.setOperator(operator);
					flagOpe=true;
					break;
				}
			}
			if(!flagOpe){
				di.setOperator("未知运营商");
			}
		}
	}
	
}
