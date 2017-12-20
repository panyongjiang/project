package com.uway.mobile.domain;

public class CityReport {

	private String city;
	private String indecencyWebSit = "0"; // 不良网站
	private String malware = "0"; // 恶意软件
	private String vulnerable = "0"; // 有漏洞的
	private String unRegistVulner = "0";// 未报备有漏洞的

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIndecencyWebSit() {
		return indecencyWebSit;
	}

	public void setIndecencyWebSit(String indecencyWebSit) {
		this.indecencyWebSit = indecencyWebSit;
	}

	public String getMalware() {
		return malware;
	}

	public void setMalware(String malware) {
		this.malware = malware;
	}

	public String getVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(String vulnerable) {
		this.vulnerable = vulnerable;
	}

	public String getUnRegistVulner() {
		return unRegistVulner;
	}

	public void setUnRegistVulner(String unRegistVulner) {
		this.unRegistVulner = unRegistVulner;
	}

}
