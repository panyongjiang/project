package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Terminal implements Serializable {

	private Integer id;

	private String user;

	private String accounts;

	private String pc_name;

	private String ip;

	private String mac;

	private String city;

	private Integer untreated;

	private String status;

	private String remarks;

	private Date entrytime;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getUntreated() {
		return untreated;
	}

	public void setUntreated(Integer untreated) {
		this.untreated = untreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public Terminal(String user, String accounts, String pc_name, String ip, String mac, String city, Integer untreated,
			String status, String remarks, Date entrytime) {
		super();
		this.user = user;
		this.accounts = accounts;
		this.pc_name = pc_name;
		this.ip = ip;
		this.mac = mac;
		this.city = city;
		this.untreated = untreated;
		this.status = status;
		this.remarks = remarks;
		this.entrytime = entrytime;
	}

	@Override
	public String toString() {
		return "Terminal [id=" + id + ", user=" + user + ", accounts=" + accounts + ", pc_name=" + pc_name + ", ip="
				+ ip + ", mac=" + mac + ", city=" + city + ", untreated=" + untreated + ", status=" + status
				+ ", remarks=" + remarks + ", entrytime=" + entrytime + "]";
	}

}
