package com.uway.mobile.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uway.mobile.util.TraversalMapUtil;

@Component
public class ReqParamChuYunHandler extends ReqParamHandler{
	@Autowired
	public TraversalMapUtil tm;
	
	@Override
	public Map<String, Object> doHandler(Map<String, Object> paraMap) {
		Map<String,Object> mapParas = new HashMap<String,Object>();
		//根据设备商ID查找对应模板
		if(paraMap.get("deviceCompanyId").toString().equals("1")){
			mapParas=tm.changeReqParas(paraMap);
		}else{
			if(getNextHandler() != null){
				getNextHandler().doHandler(paraMap);
			}
		}
		return mapParas;
	}
	

}
