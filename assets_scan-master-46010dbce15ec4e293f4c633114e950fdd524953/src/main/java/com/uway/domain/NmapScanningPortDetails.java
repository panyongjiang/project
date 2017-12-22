package com.uway.domain;

import java.sql.Timestamp;

public class NmapScanningPortDetails {
	private long portDetailsId;
	private long scanResultId;
	private int port;
	private String protocol;
	private String state;
	private String serviceName;
	private String serviceProduct;
	private Timestamp createTime;
	private Timestamp modifyTime;
	
	public long getPortDetailsId() {
		return portDetailsId;
	}

	public void setPortDetailsId(long portDetailsId) {
		this.portDetailsId = portDetailsId;
	}
	

	public long getScanResultId() {
		return scanResultId;
	}

	public void setScanResultId(long scanResultId) {
		this.scanResultId = scanResultId;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceProduct() {
		return serviceProduct;
	}

	public void setServiceProduct(String serviceProduct) {
		this.serviceProduct = serviceProduct;
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
