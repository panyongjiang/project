package com.uway.tasks;

import org.apache.log4j.Logger;

public class BaseApplication {
	public static final Logger log = Logger.getLogger("BaseApplication.class");
	
	public void debug(String controllerName, String methodName, String msg){
		log.debug(controllerName + "-" + methodName + "-" + msg);
	}
	
	public void info(String controllerName, String methodName, String msg){
		log.info(controllerName + "-" + methodName + "-" + msg);
	}
	
	public void error(String controllerName, String methodName, String msg,Exception e){
		log.error(controllerName + "-" + methodName + "-" + msg,e);
	}
}
