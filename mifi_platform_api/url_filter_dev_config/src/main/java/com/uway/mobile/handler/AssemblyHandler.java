package com.uway.mobile.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uway.mobile.common.Result;

/**
 * 责任链组装类
 * 入参模板，格式固定
 * 返回参数处理类
 * @author java_ztx
 *
 */

@Component
public class AssemblyHandler {
	
	@Autowired
	ReqParamChuYunHandler rpch;
	@Autowired
	ReqParamHengtongHandler rpht;
	@Autowired
	ResHengtongHandler rsht;
	
	/*
	 * 入参模板转换
	 */
	public Map<String,Object> assemblyReqHandler(Map<String, Object> paraMap){
		Map<String,Object> paras = new HashMap<String,Object>();
		//组装参数模板链
		/*ReqParamHandler hengtong = new ReqParamHengtongHandler();
		ReqParamHandler chuyun = new ReqParamChuYunHandler();
		
		hengtong.setNextHandler(chuyun);
		paras=hengtong.doHandler(paraMap);*/
		rpht.setNextHandler(rpch);
		paras=rpht.doHandler(paraMap);
		return paras;
	}
	
	/*
	 * 返回参数转换
	 */
	public Result assemblyResHandler(Map<String, Object> paraMap){
		Result result = new Result();
		//组装参数模板链
		result=rsht.doHandler(paraMap);
		return result;
	}
	
}
