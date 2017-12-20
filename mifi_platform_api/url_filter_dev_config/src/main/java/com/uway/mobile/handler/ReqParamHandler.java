package com.uway.mobile.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 责任链处理类
 * 针对不同企业模板进行参数处理
 * @author java_ztx
 *
 */
@Component
public abstract class ReqParamHandler {
	
	private ReqParamHandler nextHandler;
	
	public ReqParamHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(ReqParamHandler nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	public abstract Map<String, Object> doHandler(Map<String,Object> paraMap);

}
