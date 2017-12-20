package com.uway.mobile.domain;

import java.sql.Timestamp;

public class SafeCategory {
	private String id;
	private short siteSafe;
	private Timestamp siteStartTime;
	private Timestamp siteEndTime;
	private short cloudWaf;
	private Timestamp wafStartTime;
	private Timestamp wafEndTime;
	private short appSafe;
	private Timestamp appStartTime;
	private Timestamp appEndTime;
	private short expertSafe;
	private Timestamp expertStartTime;
	private Timestamp expertEndTime;
	private long userId;
	private Timestamp createTime;
	private Timestamp modifyTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public short getSiteSafe() {
		return siteSafe;
	}

	public void setSiteSafe(short siteSafe) {
		this.siteSafe = siteSafe;
	}

	public Timestamp getSiteStartTime() {
		return siteStartTime;
	}

	public void setSiteStartTime(Timestamp siteStartTime) {
		this.siteStartTime = siteStartTime;
	}

	public Timestamp getSiteEndTime() {
		return siteEndTime;
	}

	public void setSiteEndTime(Timestamp siteEndTime) {
		this.siteEndTime = siteEndTime;
	}

	public short getCloudWaf() {
		return cloudWaf;
	}

	public void setCloudWaf(short cloudWaf) {
		this.cloudWaf = cloudWaf;
	}

	public Timestamp getWafStartTime() {
		return wafStartTime;
	}

	public void setWafStartTime(Timestamp wafStartTime) {
		this.wafStartTime = wafStartTime;
	}

	public Timestamp getWafEndTime() {
		return wafEndTime;
	}

	public void setWafEndTime(Timestamp wafEndTime) {
		this.wafEndTime = wafEndTime;
	}

	public short getAppSafe() {
		return appSafe;
	}

	public void setAppSafe(short appSafe) {
		this.appSafe = appSafe;
	}

	public Timestamp getAppStartTime() {
		return appStartTime;
	}

	public void setAppStartTime(Timestamp appStartTime) {
		this.appStartTime = appStartTime;
	}

	public Timestamp getAppEndTime() {
		return appEndTime;
	}

	public void setAppEndTime(Timestamp appEndTime) {
		this.appEndTime = appEndTime;
	}

	public short getExpertSafe() {
		return expertSafe;
	}

	public void setExpertSafe(short expertSafe) {
		this.expertSafe = expertSafe;
	}

	public Timestamp getExpertStartTime() {
		return expertStartTime;
	}

	public void setExpertStartTime(Timestamp expertStartTime) {
		this.expertStartTime = expertStartTime;
	}

	public Timestamp getExpertEndTime() {
		return expertEndTime;
	}

	public void setExpertEndTime(Timestamp expertEndTime) {
		this.expertEndTime = expertEndTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
