package com.uway.mobile.domain;

import java.sql.Timestamp;

public class Port {
	//id
	private Integer id;
	//端口名称
	private String portName;
	//网站id
	private Integer sid;
	//子域名id
	private String ssid;
	//子域名
	private String siteSon;
	//对外协议
	private String scheme;
	//对外监听端口
	private String listenPort;
	//源站端口
	private String port;
	//状态
	private String status;
	//当前时间
	private Timestamp time;
	
	
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getPort() {
		return port;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getListenPort() {
		return listenPort;
	}
	public void setListenPort(String listenPort) {
		this.listenPort = listenPort;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getSiteSon() {
		return siteSon;
	}
	public void setSiteSon(String siteSon) {
		this.siteSon = siteSon;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
