package com.uway.mobile.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DayFlow {
	
	private String deviceId;
	private Date  date;
	private BigDecimal upFlow;
	private BigDecimal downFlow;
	private String model;
	private String softVersion;
	private String hardVersion;
	private BigDecimal totalUpFlow;
	private BigDecimal totalDownFlow;
	
	public BigDecimal getFlow() {
		return upFlow;
	}
	public void setFlow(BigDecimal flow) {
		this.upFlow = flow;
	}
	@JsonFormat(pattern = "yyyyMMdd")
	public Date  getDate() {
		return date;
	}
	public void setDate(Date  date) {
		this.date = date;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSoftVersion() {
		return softVersion;
	}
	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}
	public String getHardVersion() {
		return hardVersion;
	}
	public void setHardVersion(String hardVersion) {
		this.hardVersion = hardVersion;
	}
	public BigDecimal getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(BigDecimal upFlow) {
		this.upFlow = upFlow;
	}
	public BigDecimal getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(BigDecimal downFlow) {
		this.downFlow = downFlow;
	}
	public BigDecimal getTotalUpFlow() {
		return totalUpFlow;
	}
	public void setTotalUpFlow(BigDecimal totalUpFlow) {
		this.totalUpFlow = totalUpFlow;
	}
	public BigDecimal getTotalDownFlow() {
		return totalDownFlow;
	}
	public void setTotalDownFlow(BigDecimal totalDownFlow) {
		this.totalDownFlow = totalDownFlow;
	}
	
	
	
}
