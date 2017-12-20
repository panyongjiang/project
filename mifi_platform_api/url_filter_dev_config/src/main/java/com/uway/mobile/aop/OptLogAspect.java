package com.uway.mobile.aop;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Log;
import com.uway.mobile.service.LogService;

@Aspect
@Component
public class OptLogAspect {
	private static final Logger log = Logger.getLogger(OptLogAspect.class);
	
	@Autowired
	private LogService logService;
	
	private Log optLog;
	
	@Pointcut("execution(public * com.uway.mobile.controller.StrategyController.*(..))")
	public void optLog(){}
	
	@Before("optLog()")
	public void doBefore(JoinPoint joinpoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		StringBuffer sb = new StringBuffer();
		// 记录下请求内容
		sb.append(" URL : [").append(request.getMethod()).append("]").append(request.getRequestURL().toString())
				.append(". \n 请求IP : ").append(request.getRemoteAddr()).append(". \n 方法 : ")
				.append(joinpoint.getTarget().getClass().getName()).append(".")
				.append(joinpoint.getSignature().getName()).append(". \n 参数列表 : ")
				.append(Arrays.toString(joinpoint.getArgs()));

		log.debug(sb.toString());
		
		String userId = request.getAttribute("userId")==null?"":request.getAttribute("userId").toString();
		String method = joinpoint.getSignature().getName();
		if(method.endsWith("addStrategy")){
			optLog = new Log();
			optLog.setOptUser(userId);
			optLog.setOperation("新增策略");
		}else if(method.endsWith("editStrategy")){
			optLog = new Log();
			optLog.setOptUser(userId);
			optLog.setOperation("编辑策略");
		}else if(method.endsWith("deleteStrategy")){
			optLog = new Log();
			optLog.setOptUser(userId);
			optLog.setOperation("删除策略");
		}else if(method.endsWith("order")){
			optLog = new Log();
			optLog.setOptUser(userId);
			optLog.setOperation("策略下发");
		}
	}

	@SuppressWarnings("unchecked")
	@AfterReturning(returning = "ret", pointcut = "optLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		if(optLog!=null){
			Result result =(Result) ret;
			Object data=result.getData();
			Map<String,Object> map=(Map<String,Object>)data;
			String seqNum="";
			if(map.containsKey("seqNum")){
				seqNum=map.get("seqNum").toString();
			}
			
			optLog.setSeqNum(seqNum);
			logService.insertLog(optLog);
		}
	}

	@AfterThrowing(pointcut = "optLog()", throwing = "e")
	public void afterThrowing(JoinPoint joinpoint, Throwable e) {
		log.error("异常代码:" + e.getClass().getName());
		log.error("\n 异常信息:" + e.getMessage());
		log.error("\n 异常方法:" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
	}
	
}
