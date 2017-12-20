package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class SrvResource implements Serializable {
	/**
	 * uuid
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String department;

	private String ip;

	private String port;

	private String service;

	private String softwareinfo;

	private Date createtime;

	private Date updatetime;

	private String state;

	public SrvResource() {
		super();
	}

	public SrvResource(String department, String ip, String port, String service, String softwareinfo, Date createtime,
			Date updatetime, String state) {
		super();
		this.department = department;
		this.ip = ip;
		this.port = port;
		this.service = service;
		this.softwareinfo = softwareinfo;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department == null ? null : department.trim();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port == null ? null : port.trim();
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service == null ? null : service.trim();
	}

	public String getSoftwareinfo() {
		return softwareinfo;
	}

	public void setSoftwareinfo(String softwareinfo) {
		this.softwareinfo = softwareinfo == null ? null : softwareinfo.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	@Override
	public String toString() {
		return "SrvResource [id=" + id + ", department=" + department + ", ip=" + ip + ", port=" + port + ", service="
				+ service + ", softwareinfo=" + softwareinfo + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", state=" + state + "]";
	}

}