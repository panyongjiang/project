package com.uway.tasks.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostStateCheckingJob {
	
	Logger logger = LoggerFactory.getLogger(HostStateCheckingJob.class.getName());
	
	public void hostStateCheck() throws Exception{
		logger.info("定时任务执行！");
	}
}
