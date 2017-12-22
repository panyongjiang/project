package com.uway.tasks.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.uway.tasks.domain.ScheduleJob;


public class TaskUtils {
	public final static Logger log = Logger.getLogger(TaskUtils.class);

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	@SuppressWarnings("unchecked")
	public static void invokMethod(ScheduleJob scheduleJob) {
		Object object = null;
		@SuppressWarnings("rawtypes")
		Class clazz = null;
		String springId = "taskRunning";
		String methodName = "run";
//		if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
			object = SpringUtils.getBean(springId);
//		} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
//			try {
//				clazz = Class.forName(scheduleJob.getBeanClass());
//				object = clazz.newInstance();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
		if (object == null) {
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName,ScheduleJob.class);
		} catch (NoSuchMethodException e) {
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
		} catch (SecurityException e) {
			log.error(e.getMessage(),e);
		}
		if (method != null) {
			try {
				method.invoke(object,scheduleJob);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(),e);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(),e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(),e);
			}catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		log.debug("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
	}
}
