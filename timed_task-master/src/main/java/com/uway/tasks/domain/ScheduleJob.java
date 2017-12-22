package com.uway.tasks.domain;

/**
 * 
 * @Description: 计划任务信息
 */
public class ScheduleJob {

	public static final int JOB_STATUS_STOP = 0;
	public static final int JOB_STATUS_START = 1;
	public static final int JOB_STATUS_SUSPEND = 2;
	public static final int RUNNING_STATUS_NOT_START = 0;
	public static final int RUNNING_STATUS_IN_PROGRESS = 1;
	public static final int RUNNING_STATUS_COMPLETED = 2;

	private Long jobId;
	private String jobName;
	private String jobGroup;
	private int jobStatus;
	private String cronExpression;
	private String description;
	private String interfaceUrl;
	private int runningStatus;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public int getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public int getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(int runningStatus) {
		this.runningStatus = runningStatus;
	}

	public String toString() {
		return "ScheduleJob[jobId=" + jobId + ",jobName" + jobName + ",jobGroup=" + jobGroup + "]";
	}
}