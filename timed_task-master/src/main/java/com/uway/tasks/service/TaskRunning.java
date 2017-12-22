package com.uway.tasks.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.uway.tasks.domain.ScheduleJob;
import com.uway.tasks.util.HttpUtil;

@Component
public class TaskRunning {
	public final Logger log = Logger.getLogger(this.getClass());
 
	public void run(ScheduleJob scheduleJob) {
		log.debug(" run........." +  scheduleJob.toString() + "............................." + (new Date()));
		HttpUtil.sendPost(scheduleJob.getInterfaceUrl(), null);
	}
}
