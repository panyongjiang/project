package com.uway.tasks.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uway.tasks.domain.ScheduleJob;

@Mapper
public interface ScheduleJobMapper {
	
	/**
	 * 取得任务
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleJob> getTasks(ScheduleJob scheduleJob) throws Exception;
	
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
	
	/**
	 * 删除任务
	 * @param jobId
	 * @throws Exception
	 */
	public void deleteTask(@Param(value = "jobId") long jobId) throws Exception;
	
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
	
}