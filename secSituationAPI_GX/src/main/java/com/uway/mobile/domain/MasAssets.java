package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class MasAssets implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;

	private String name;

	private String ipAddress;

	private String department;

	private String productModel;

	private String operateSystemVersion;

	private String applicationVersion;

	private String storeBussinessData;

	private String bussinessType;

	private String deviceActualAddress;

	private String applicationExplain;

	private String administratorContact;

	private String url;

	private String effect;

	private String comment;

	private String ip;

	private String port;

	private Date time;

	private String province;

	public MasAssets(String name, String ipAddress, String department, String productModel, String operateSystemVersion,
			String applicationVersion, String bussinessType, String url) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
		this.department = department;
		this.productModel = productModel;
		this.operateSystemVersion = operateSystemVersion;
		this.applicationVersion = applicationVersion;
		this.bussinessType = bussinessType;
		this.url = url;
	}

	public MasAssets(String name, String ipAddress, String department, String productModel, String operateSystemVersion,
			String applicationVersion, String storeBussinessData, String bussinessType, String deviceActualAddress,
			String applicationExplain, String administratorContact, String url, String effect, String comment,
			String ip, String port, Date time, String province) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
		this.department = department;
		this.productModel = productModel;
		this.operateSystemVersion = operateSystemVersion;
		this.applicationVersion = applicationVersion;
		this.storeBussinessData = storeBussinessData;
		this.bussinessType = bussinessType;
		this.deviceActualAddress = deviceActualAddress;
		this.applicationExplain = applicationExplain;
		this.administratorContact = administratorContact;
		this.url = url;
		this.effect = effect;
		this.comment = comment;
		this.ip = ip;
		this.port = port;
		this.time = time;
		this.province = province;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getOperateSystemVersion() {
		return operateSystemVersion;
	}

	public void setOperateSystemVersion(String operateSystemVersion) {
		this.operateSystemVersion = operateSystemVersion;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public String getStoreBussinessData() {
		return storeBussinessData;
	}

	public void setStoreBussinessData(String storeBussinessData) {
		this.storeBussinessData = storeBussinessData;
	}

	public String getBussinessType() {
		return bussinessType;
	}

	public void setBussinessType(String bussinessType) {
		this.bussinessType = bussinessType;
	}

	public String getDeviceActualAddress() {
		return deviceActualAddress;
	}

	public void setDeviceActualAddress(String deviceActualAddress) {
		this.deviceActualAddress = deviceActualAddress;
	}

	public String getApplicationExplain() {
		return applicationExplain;
	}

	public void setApplicationExplain(String applicationExplain) {
		this.applicationExplain = applicationExplain;
	}

	public String getAdministratorContact() {
		return administratorContact;
	}

	public void setAdministratorContact(String administratorContact) {
		this.administratorContact = administratorContact;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}