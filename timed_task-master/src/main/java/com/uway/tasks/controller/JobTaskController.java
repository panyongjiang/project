package com.uway.tasks.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.tasks.BaseApplication;
import com.uway.tasks.common.Constant;
import com.uway.tasks.common.Result;
import com.uway.tasks.domain.ScheduleJob;
import com.uway.tasks.service.JobTaskService;


@RestController
@RequestMapping("/task")
public class JobTaskController extends BaseApplication{
	@Autowired
	private JobTaskService jobTaskService;
	
	/**
	 * 所有任务列表
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "taskList", method = RequestMethod.POST)
	public Result taskList(HttpServletRequest request ,@RequestBody Map<String, String> paraMap) {
		Result result = new Result();
		try {
			List<ScheduleJob> taskList = jobTaskService.getTasks(new ScheduleJob());
			request.setAttribute("taskList", taskList);
			
			result.setData(taskList);
			result.setCode(Constant.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			return result;
		} catch (Exception e) {
			error("JobTaskController", "taskList", e.getMessage(),e);
			result.setCode(500);
			result.setMsg("内部错误");
			return result;
		}
	}
	
	/**
	 * 修改任务状态
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "changeJobStatus", method = RequestMethod.POST)
	public Result changeJobStatus(HttpServletRequest request ,@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try {
			String jobId = (String)paraMap.get("jobId");
			String cmd = (String)paraMap.get("cmd");
			jobTaskService.changeStatus(Long.valueOf(jobId), cmd);
			
			result.setCode(Constant.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			error("JobTaskController","changeJobStatus",e.getMessage(), e);
			result.setCode(500);
			result.setMsg("内部错误");
			return result;
		}
	}
	
	/**
	 * 修改任务执行计划
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "updateCron", method = RequestMethod.POST)
	public Result updateCron(HttpServletRequest request,@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		String jobId = null;
		String cron = null;
		try {
			cron = (String)paraMap.get("cron");
			if(StringUtils.isEmpty(cron)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("cron表达式不能为空！");
				return result;
			}
			CronScheduleBuilder.cronSchedule(cron);
		} catch (Exception e) {
			result.setCode(Constant.RESPONSE_CRON_ERROR);
			result.setMsg("cron表达式有误，不能被解析！");
			return result;
		}
		try {
			jobId = (String)paraMap.get("jobId");
			if(StringUtils.isEmpty(jobId)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("定时任务ID不能为空！");
				return result;
			}
			jobTaskService.updateCron(Long.valueOf(jobId), cron);
			result.setCode(Constant.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setCode(Constant.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			return result;
		}
	}
	
	/**
	 * 添加新的任务
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "addTask", method = RequestMethod.POST)
	public Result addTask(HttpServletRequest request,@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		String jobName = null;
		String jobGroup = null;
		String cron = null;
		String desc = null;
		String interfaceUrl = null;
		try {
			cron = (String)paraMap.get("cron");
			jobName = (String)paraMap.get("jobName");
			jobGroup = (String)paraMap.get("jobGroup");
			desc = (String)paraMap.get("desc");
			interfaceUrl = (String)paraMap.get("interfaceUrl");
			if(StringUtils.isEmpty(cron)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("cron表达式不能为空！");
				return result;
			}
			if(StringUtils.isEmpty(jobName)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("任务名称不能为空！");
				return result;
			}
			if(StringUtils.isEmpty(jobGroup)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("任务组不能为空！");
				return result;
			}
			if(StringUtils.isEmpty(interfaceUrl)){
				result.setCode(Constant.RESPONSE_PARAM_EMPTY);
				result.setMsg("任务接口不能为空！");
				return result;
			}
			CronScheduleBuilder.cronSchedule(cron);
		} catch (Exception e) {
			result.setCode(Constant.RESPONSE_CRON_ERROR);
			result.setMsg("cron表达式有误，不能被解析！");
			return result;
		}
		
		//检查是否有相同的任务名称和任务组
		ScheduleJob job = new ScheduleJob();
		job.setJobName(jobName);
		job.setJobGroup(jobGroup);
		job.setCronExpression(cron);
		job.setDescription(desc);
		job.setInterfaceUrl(interfaceUrl);
		try {
			List<ScheduleJob> existsjob = jobTaskService.checkExistsTrigger(job);
			if(existsjob != null && !existsjob.isEmpty()){
				result.setCode(Constant.RESPONSE_NAME_EXISTS_ERROR);
				result.setMsg("该任务名称和任务组已经存在！");
				return result;
			}
			jobTaskService.addTask(job);
			result.setCode(Constant.RESPONSE_SUCCESS);
			result.setMsg("添加成功！");
			return result;
		} catch (Exception e) {
			result.setCode(Constant.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			return result;
		}
	}
	
	@RequestMapping(value = "test")
    public Result test() throws Exception {
        Result result = new Result();
        result.setCode(Constant.RESPONSE_SUCCESS);
        result.setMsg("测试成功！");
        return result;
    }
}
