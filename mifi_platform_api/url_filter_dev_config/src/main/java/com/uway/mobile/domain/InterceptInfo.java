package com.uway.mobile.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InterceptInfo {

	private String id;
	private String deviceid;
	private String mac;
	private String url;
	private int urlLen;
	private String srcIp;
	private String srcFmtIp;
	private int srcPort;
	private String dstIp;
	private String dstFmtIp;
	private int dstPort;
	private int enilType;
	private String createDate;
	private Date createTime;
	private String status;
	private long times;
	private String enilTypeName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getUrlLen() {
		return urlLen;
	}

	public void setUrlLen(int urlLen) {
		this.urlLen = urlLen;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getSrcFmtIp() {
		return srcFmtIp;
	}

	public void setSrcFmtIp(String srcFmtIp) {
		this.srcFmtIp = srcFmtIp;
	}

	public int getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public String getDstFmtIp() {
		return dstFmtIp;
	}

	public void setDstFmtIp(String dstFmtIp) {
		this.dstFmtIp = dstFmtIp;
	}

	public int getDstPort() {
		return dstPort;
	}

	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}

	public int getEnilType() {
		return enilType;
	}

	public void setEnilType(int enilType) {
		this.enilType = enilType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public String getEnilTypeName() {
		return enilTypeName;
	}

	public void setEnilTypeName(String enilTypeName) {
		this.enilTypeName = enilTypeName;
	}
	

}
