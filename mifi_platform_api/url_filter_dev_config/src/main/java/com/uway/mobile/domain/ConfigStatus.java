package com.uway.mobile.domain;

import java.util.Date;

public class ConfigStatus {
	
	private int id;
	private String deviceId;
	private String commndType;
	private String params;
	private Date optTime;
	private String status;
	private String response;
	private String optSeq;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getCommndType() {
		return commndType;
	}
	public void setCommndType(String commndType) {
		this.commndType = commndType;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Date getOptTime() {
		return optTime;
	}
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getOptSeq() {
		return optSeq;
	}
	public void setOptSeq(String optSeq) {
		this.optSeq = optSeq;
	}
	
	
	

}
