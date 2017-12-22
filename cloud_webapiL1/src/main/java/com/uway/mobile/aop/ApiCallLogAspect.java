package com.uway.mobile.aop;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiCallLogAspect {

	private static final Logger log = Logger.getLogger(ApiCallLogAspect.class);

	ThreadLocal<Long> startTime = new ThreadLocal<Long>();

	/**
	 * ApiServiceImpl所有公用方法
	 */
	@Pointcut("execution(public * com.uway.mobile.service.impl.ApiServiceImpl.*(..))")
	public void apilog() {
	}

	@Before("apilog()")
	public void doBefore(JoinPoint joinpoint) throws Throwable {

		startTime.set(System.currentTimeMillis());
		// 接收到请求，记录请求内容
		StringBuffer sb = new StringBuffer();
		sb.append("方法 : ").append(joinpoint.getTarget().getClass().getName()).append(".")
				.append(joinpoint.getSignature().getName()).append(". \n 参数列表 : ")
				.append(Arrays.toString(joinpoint.getArgs()));

		log.debug(sb.toString());
	}

	@AfterReturning(returning = "ret", pointcut = "apilog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		
		String str = (ret == null ? "null":ret.toString());
		if(str != null && str.length() > 50){
			str = str.substring(0, 46) +"...";
		}
		log.debug("返回值 : " + str + "\n 耗时：" + (System.currentTimeMillis() - startTime.get()) + "ms.");
	}

	@AfterThrowing(pointcut = "apilog()", throwing = "e")
	public void afterThrowing(JoinPoint joinpoint, Throwable e) {
		log.error("异常代码:" + e.getClass().getName());
		log.error("\n 异常信息:" + e.getMessage());
		log.error("\n 异常方法:" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
	}
}
