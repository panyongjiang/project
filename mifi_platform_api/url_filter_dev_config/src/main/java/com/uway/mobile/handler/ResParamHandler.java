package com.uway.mobile.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.uway.mobile.common.Result;

/**
 * 责任链处理类
 * 针对返回参数进行处理
 * 依照条件为 CommandType
 * @author java_ztx
 *
 */
@Component
public abstract class ResParamHandler {
	
	private ResParamHandler nextHandler;

	public ResParamHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(ResParamHandler nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	public abstract Result doHandler(Map<String,Object> paraMap);

}
