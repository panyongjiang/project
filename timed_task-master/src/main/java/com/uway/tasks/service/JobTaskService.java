package com.uway.tasks.service;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.transaction.annotation.Transactional;

import com.uway.tasks.domain.ScheduleJob;

/**
 * 
 * @Description: 计划任务管理
 */
@Transactional
public interface JobTaskService {

	/**
	 * 添加到数据库中 区别于addJob
	 */
	public void addTask(ScheduleJob job) throws Exception;

	/**
	 * 从数据库中查询job
	 */
	public ScheduleJob getTaskById(Long jobId) throws Exception;

	/**
	 * 更改任务状态
	 * 
	 * @throws SchedulerException
	 */
	public void changeStatus(Long jobId, String cmd)throws Exception;

	/**
	 * 更改任务 cron表达式
	 * 
	 * @throws SchedulerException
	 */
	public void updateCron(Long jobId, String cron) throws Exception;

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void addJob(ScheduleJob job) throws Exception;

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getAllJob() throws Exception;

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getRunningJob() throws Exception;

	/**
	 * 暂停一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void pauseJob(ScheduleJob scheduleJob) throws Exception;

	/**
	 * 恢复一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(ScheduleJob scheduleJob) throws Exception;

	/**
	 * 停止一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void stopJob(ScheduleJob scheduleJob) throws Exception;

	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(ScheduleJob scheduleJob) throws Exception;

	/**
	 * 更新job时间表达式
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void updateJobCron(ScheduleJob scheduleJob) throws Exception;
	
	/**
	 * 取得任务
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> getTasks(ScheduleJob scheduleJob) throws Exception;
	
	/**
	 * 删除任务
	 * @param jobId
	 * @throws Exception
	 */
	public void deleteTask(Long jobId) throws Exception;
	
	/**
	 * 新增任务
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void saveEntity(ScheduleJob scheduleJob) throws Exception;
	
	/**
	 * 更新任务
	 * @param jobId
	 * @throws Exception
	 */
	public void updateTask(ScheduleJob scheduleJob) throws Exception;
	
	/**
	 * 取得所有启用的任务
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> getAllActiveTasks() throws Exception;
	
	/**
	 * 检查是否有相同的任务名称和任务组
	 * @param scheduleJob
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> checkExistsTrigger(ScheduleJob scheduleJob) throws Exception;

}
