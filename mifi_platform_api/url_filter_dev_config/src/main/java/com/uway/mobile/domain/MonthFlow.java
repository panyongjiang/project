package com.uway.mobile.domain;

import java.math.BigDecimal;

public class MonthFlow {
	
	private String deviceId;
	private String month;
	private BigDecimal up_flow;
	private BigDecimal down_flow;
	private BigDecimal upFlowSum;
	private BigDecimal downFlowSum;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public BigDecimal getUp_flow() {
		return up_flow;
	}
	public void setUp_flow(BigDecimal up_flow) {
		this.up_flow = up_flow;
	}
	public BigDecimal getDown_flow() {
		return down_flow;
	}
	public void setDown_flow(BigDecimal down_flow) {
		this.down_flow = down_flow;
	}
	public BigDecimal getUpFlowSum() {
		return upFlowSum;
	}
	public void setUpFlowSum(BigDecimal upFlowSum) {
		this.upFlowSum = upFlowSum;
	}
	public BigDecimal getDownFlowSum() {
		return downFlowSum;
	}
	public void setDownFlowSum(BigDecimal downFlowSum) {
		this.downFlowSum = downFlowSum;
	}
	
	

}
