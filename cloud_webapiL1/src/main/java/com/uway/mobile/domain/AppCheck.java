package com.uway.mobile.domain;

import java.sql.Timestamp;

public class AppCheck {
	private String id;
	private String appName;
	private short appStatus;
	private String appUrl;
	private long createUser;
	private String appChkUrl;
	private String appChkUser;
	private String remark;
	private Timestamp createTime;
	private Timestamp modifyTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public short getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(short appStatus) {
		this.appStatus = appStatus;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}
	public String getAppChkUrl() {
		return appChkUrl;
	}
	public void setAppChkUrl(String appChkUrl) {
		this.appChkUrl = appChkUrl;
	}
	public String getAppChkUser() {
		return appChkUser;
	}
	public void setAppChkUser(String appChkUser) {
		this.appChkUser = appChkUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

}
