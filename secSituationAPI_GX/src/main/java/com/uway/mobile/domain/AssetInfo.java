package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class AssetInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String province;

	private String department;

	private String ip;

	private String port;

	private String weburl;

	private String webname;

	private String category;

	private String subcategory;

	private String servicetype;

	private String softwarename;

	private String softwareversion;

	private String manufacturer;

	private String unittype;

	private String os;

	private String hasweb;

	private String comment;
	private String isreport;

	private Date registdate;

	public String getIsreport() {
		return isreport;
	}

	public void setIsreport(String isreport) {
		this.isreport = isreport;
	}

	public AssetInfo(String department, String ip, String port, String weburl, String webname, String servicetype,
			String os, String hasweb, String comment) {
		super();
		this.department = department;
		this.ip = ip;
		this.port = port;
		this.weburl = weburl;
		this.webname = webname;
		this.servicetype = servicetype;
		this.os = os;
		this.hasweb = hasweb;
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
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

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl == null ? null : weburl.trim();
	}

	public String getWebname() {
		return webname;
	}

	public void setWebname(String webname) {
		this.webname = webname == null ? null : webname.trim();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category == null ? null : category.trim();
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory == null ? null : subcategory.trim();
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype == null ? null : servicetype.trim();
	}

	public String getSoftwarename() {
		return softwarename;
	}

	public void setSoftwarename(String softwarename) {
		this.softwarename = softwarename == null ? null : softwarename.trim();
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion == null ? null : softwareversion.trim();
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer == null ? null : manufacturer.trim();
	}

	public String getUnittype() {
		return unittype;
	}

	public void setUnittype(String unittype) {
		this.unittype = unittype == null ? null : unittype.trim();
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os == null ? null : os.trim();
	}

	public String getHasweb() {
		return hasweb;
	}

	public void setHasweb(String hasweb) {
		this.hasweb = hasweb == null ? null : hasweb.trim();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment == null ? null : comment.trim();
	}

	public Date getRegistdate() {
		return registdate;
	}

	public void setRegistdate(Date registdate) {
		this.registdate = registdate;
	}

	public AssetInfo(String province, String department, String ip, String port, String weburl, String webname,
			String category, String subcategory, String servicetype, String softwarename, String softwareversion,
			String manufacturer, String unittype, String os, String hasweb, String comment, Date registdate,
			String isreport) {
		super();
		this.province = province;
		this.department = department;
		this.ip = ip;
		this.port = port;
		this.weburl = weburl;
		this.webname = webname;
		this.category = category;
		this.subcategory = subcategory;
		this.servicetype = servicetype;
		this.softwarename = softwarename;
		this.softwareversion = softwareversion;
		this.manufacturer = manufacturer;
		this.unittype = unittype;
		this.os = os;
		this.hasweb = hasweb;
		this.comment = comment;
		this.registdate = registdate;
		this.isreport = isreport;
	}

	@Override
	public String toString() {
		return "AssetInfo [id=" + id + ", province=" + province + ", department=" + department + ", ip=" + ip
				+ ", port=" + port + ", weburl=" + weburl + ", webname=" + webname + ", category=" + category
				+ ", subcategory=" + subcategory + ", servicetype=" + servicetype + ", softwarename=" + softwarename
				+ ", softwareversion=" + softwareversion + ", manufacturer=" + manufacturer + ", unittype=" + unittype
				+ ", os=" + os + ", hasweb=" + hasweb + ", comment=" + comment + ", registdate=" + registdate + "]";
	}

}