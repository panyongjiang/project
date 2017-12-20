package com.uway.mobile.domain;

import java.util.Date;

public class Excel {

	private Integer id;

	private String phone;

	private String city;

	private String name;

	private String type;

	private String attribute;

	private String ostype;

	private Date gettime;

	private String url;

	private String md5;

	public Excel(String phone, String city, String name, String type, String attribute, String ostype, Date gettime,
			String url, String md5) {
		super();
		this.phone = phone;
		this.city = city;
		this.name = name;
		this.type = type;
		this.attribute = attribute;
		this.ostype = ostype;
		this.gettime = gettime;
		this.url = url;
		this.md5 = md5;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public Date getGettime() {
		return gettime;
	}

	public void setGettime(Date gettime) {
		this.gettime = gettime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return "Excel [phone=" + phone + ", city=" + city + ", name=" + name + ", type=" + type + ", attribute="
				+ attribute + ", ostype=" + ostype + ", gettime=" + gettime + ", url=" + url + ", md5=" + md5 + "]";
	}

}