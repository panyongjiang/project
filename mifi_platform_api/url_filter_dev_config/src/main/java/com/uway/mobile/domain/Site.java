package com.uway.mobile.domain;

import java.sql.Timestamp;

public class Site {
	private long id;
	private String siteWafId;
	private String siteTitle;
	private String siteDomain;
	private String cdn_type;
	private String siteIp;
	private String siteRepo;
	private String email;
	private short type;
	private String remark;
	private String status;
	private String verify_txt;
	private String taskId;
	private String sweepTime;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getVerify_txt() {
		return verify_txt;
	}

	public void setVerify_txt(String verify_txt) {
		this.verify_txt = verify_txt;
	}

	private long createUser;
	private Timestamp createTime;
	private Timestamp modifyTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSiteWafId() {
		return siteWafId;
	}

	public void setSiteWafId(String siteWafId) {
		this.siteWafId = siteWafId;
	}

	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	public String getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}

	
	public String getCdn_type() {
		return cdn_type;
	}

	public void setCdn_type(String cdn_type) {
		this.cdn_type = cdn_type;
	}

	public String getSiteIp() {
		return siteIp;
	}

	public void setSiteIp(String siteIp) {
		this.siteIp = siteIp;
	}

	public String getSiteRepo() {
		return siteRepo;
	}

	public void setSiteRepo(String siteRepo) {
		this.siteRepo = siteRepo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(long createUser) {
		this.createUser = createUser;
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

	public String getSweepTime() {
		return sweepTime;
	}

	public void setSweepTime(String sweepTime) {
		this.sweepTime = sweepTime;
	}
	
}
