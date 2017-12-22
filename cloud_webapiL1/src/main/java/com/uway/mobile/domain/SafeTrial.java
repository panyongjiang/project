package com.uway.mobile.domain;

import java.sql.Timestamp;

public class SafeTrial {
	private String id;
	private long userId;
	private short waf;
	private short app;
	private short site;
	private short expert;
	private short trialStatus;
	private Timestamp createTime;
	private Timestamp modifyTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public short getWaf() {
		return waf;
	}

	public void setWaf(short waf) {
		this.waf = waf;
	}

	public short getApp() {
		return app;
	}

	public void setApp(short app) {
		this.app = app;
	}

	public short getSite() {
		return site;
	}

	public void setSite(short site) {
		this.site = site;
	}

	public short getExpert() {
		return expert;
	}

	public void setExpert(short expert) {
		this.expert = expert;
	}

	public short getTrialStatus() {
		return trialStatus;
	}

	public void setTrialStatus(short trialStatus) {
		this.trialStatus = trialStatus;
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
