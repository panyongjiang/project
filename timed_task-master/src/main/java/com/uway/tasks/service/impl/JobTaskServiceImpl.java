package com.uway.tasks.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.uway.tasks.domain.ScheduleJob;
import com.uway.tasks.mapper.ScheduleJobMapper;
import com.uway.tasks.service.JobTaskService;
import com.uway.tasks.service.QuartzJobFactory;
import com.uway.tasks.service.QuartzJobFactoryDisallowConcurrentExecution;

/**
 * 
 * @Description: 计划任务管理
 */
@Service
public class JobTaskServiceImpl implements JobTaskService{
	public final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	/**
	 * 添加到数据库中 区别于addJob
	 */
	@Override
	public void addTask(ScheduleJob job) throws Exception{
		saveEntity(job);
		changeStatus(job.getJobId(), "start");
	}
	
	/**
	 * 从数据库中查询job
	 */
	public ScheduleJob getTaskById(Long jobId) throws Exception{
		ScheduleJob job = new ScheduleJob();
		job.setJobId(jobId);
		List<ScheduleJob> jobs = getTasks(job);
		if(jobs == null || jobs.isEmpty()) return null;
		return jobs.get(0);
	}

	/**
	 * 更改任务状态
	 * 
	 * @throws SchedulerException
	 */
	public void changeStatus(Long jobId, String cmd) throws Exception {
		ScheduleJob job = getTaskById(jobId);
		if (job == null) {
			return;
		}
		if ("stop".equalsIgnoreCase(cmd)) {
			stopJob(job);
			job.setJobStatus(ScheduleJob.JOB_STATUS_STOP);
		} else if ("start".equalsIgnoreCase(cmd)) {
			addJob(job);
			job.setJobStatus(ScheduleJob.JOB_STATUS_START);
		}else if("suspend".equalsIgnoreCase(cmd)){
			pauseJob(job);
			job.setJobStatus(ScheduleJob.JOB_STATUS_SUSPEND);
		}else if("resume".equalsIgnoreCase(cmd)){
			resumeJob(job);
			job.setJobStatus(ScheduleJob.JOB_STATUS_START);
		}else if("delete".equalsIgnoreCase(cmd)){
			stopJob(job);
			deleteTask(jobId);
			return;
		}
		updateTask(job);
	}

	/**
	 * 更改任务 cron表达式
	 * 
	 * @throws SchedulerException
	 */
	public void updateCron(Long jobId, String cron) throws Exception {
		ScheduleJob job = getTaskById(jobId);
		if (job == null) {
			return;
		}
		job.setCronExpression(cron);
		if (ScheduleJob.JOB_STATUS_START == job.getJobStatus()) {
			updateJobCron(job);
		}
		updateTask(job);

	}

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void addJob(ScheduleJob job) throws Exception {
		if (job == null || ScheduleJob.JOB_STATUS_SUSPEND == (job.getJobStatus())) {
			return;
		}

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		log.debug(scheduler + ".......................................................................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 不存在，创建一个
		if (null == trigger) {
			@SuppressWarnings("rawtypes")
			Class clazz = ScheduleJob.RUNNING_STATUS_COMPLETED == job.getRunningStatus() ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

			@SuppressWarnings("unchecked")
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

			jobDetail.getJobDataMap().put("scheduleJob", job);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	@PostConstruct
	public void init() throws Exception {

		// 这里获取任务信息数据
		List<ScheduleJob> jobList = getAllActiveTasks();
	
		for (ScheduleJob job : jobList) {
			addJob(job);
		}
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getAllJob() throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				ScheduleJob job = new ScheduleJob();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDescription("触发器:" + trigger.getKey());
//				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//				job.setJobStatus(triggerState.);
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getRunningJob() throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			ScheduleJob job = new ScheduleJob();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			job.setJobName(jobKey.getName());
			job.setJobGroup(jobKey.getGroup());
			job.setDescription("触发器:" + trigger.getKey());
//			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//			job.setJobStatus(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void pauseJob(ScheduleJob scheduleJob) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(ScheduleJob scheduleJob) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 停止一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void stopJob(ScheduleJob scheduleJob) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.deleteJob(jobKey);
	}

	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(ScheduleJob scheduleJob) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.triggerJob(jobKey);
	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void updateJobCron(ScheduleJob scheduleJob) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		scheduler.rescheduleJob(triggerKey, trigger);
	}

	@Override
	public List<ScheduleJob> getTasks(ScheduleJob scheduleJob) throws Exception {
		return scheduleJobMapper.getTasks(scheduleJob);
	}

	@Override
	public void deleteTask(Long jobId) throws Exception {
		scheduleJobMapper.deleteTask(jobId);
	}

	@Override
	public void saveEntity(ScheduleJob scheduleJob) throws Exception {
		scheduleJobMapper.saveEntity(scheduleJob);
	}

	@Override
	public void updateTask(ScheduleJob scheduleJob) throws Exception {
		scheduleJobMapper.updateTask(scheduleJob);
	}

	@Override
	public List<ScheduleJob> getAllActiveTasks() throws Exception {
		return scheduleJobMapper.getAllActiveTasks();
	}

	@Override
	public List<ScheduleJob> checkExistsTrigger(ScheduleJob scheduleJob) throws Exception {
		return scheduleJobMapper.checkExistsTrigger(scheduleJob);
	}
	
}
