package com.uway.mobile.domain;

public class SafeManagement {
	//网站id
	private String id;
	//网站标题
	private String site_title;
	//子域名id
	private String ssid;
	//子域名指向
	private String site_son_point;
	//开关值
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSite_title() {
		return site_title;
	}
	public void setSite_title(String site_title) {
		this.site_title = site_title;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getSite_son_point() {
		return site_son_point;
	}
	public void setSite_son_point(String site_son_point) {
		this.site_son_point = site_son_point;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	

}
