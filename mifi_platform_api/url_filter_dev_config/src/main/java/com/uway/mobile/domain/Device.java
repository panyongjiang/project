package com.uway.mobile.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Device {

	private int id;
	private String mac;//MAC
	private String model;//型号
	private String version;//硬件版本
	private String softVersion; //软件版本
	private String ip;//IP
	private String province;//省份
	private String city;//城市
	private String district;//区
	private Date registeTime;//注册时间
	private Date lastOnTime;//最后在线时间
	private int userId;   //用户ID
	private String deviceId; //设备ID
	private String status;//设备状态 0为离线1为在线
	private Integer companyId; //公司ID
	private String deviceCompanyId; //设备商ID
	private String userName;   //设备绑定的用户
	private String simIccid;  //设sim卡id
	private String operator;  //运营商
	
	

	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd")
	public Date getRegisteTime() {
		return registeTime;
	}

	public void setRegisteTime(Date registeTime) {
		this.registeTime = registeTime;
	}

	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd")
	public Date getLastOnTime() {
		return lastOnTime;
	}

	public void setLastOnTime(Date lastOnTime) {
		this.lastOnTime = lastOnTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDeviceCompanyId() {
		return deviceCompanyId;
	}

	public void setDeviceCompanyId(String deviceCompanyId) {
		this.deviceCompanyId = deviceCompanyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getSimIccid() {
		return simIccid;
	}

	public void setSimIccid(String simIccid) {
		this.simIccid = simIccid;
	}

	public String getSoftVersion() {
		return softVersion;
	}

	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}
	
	
	
	
	
}
