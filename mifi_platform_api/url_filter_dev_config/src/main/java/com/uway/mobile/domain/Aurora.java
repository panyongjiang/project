package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Aurora {

	private String fromtime;
	private String endtime;
	private String siteurl;
	private String risk;
	private String last_end_time;
	@XStreamImplicit(itemFieldName="vul_keypage")
	private List<Keypage> vul_keypages;
	//@XStreamImplicit(itemFieldName="pagecrack,darkchain")
	//private List<ChangeUrl> change_url;
	private ChangeUrl pagecrack;
	private ChangeUrl darkchain;
	private Keyword keyword;
	private Smooth smooth;
	private Vuls vuls;
	
	
	public String getLast_end_time() {
		return last_end_time;
	}
	public void setLast_end_time(String last_end_time) {
		this.last_end_time = last_end_time;
	}
	public String getFromtime() {
		return fromtime;
	}
	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getSiteurl() {
		return siteurl;
	}
	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public Vuls getVuls() {
		return vuls;
	}
	public void setVuls(Vuls vuls) {
		this.vuls = vuls;
	}
	
	public List<Keypage> getVul_keypages() {
		return vul_keypages;
	}
	public void setVul_keypages(List<Keypage> vul_keypages) {
		this.vul_keypages = vul_keypages;
	}

	public ChangeUrl getPagecrack() {
		return pagecrack;
	}
	public void setPagecrack(ChangeUrl pagecrack) {
		this.pagecrack = pagecrack;
	}
	public ChangeUrl getDarkchain() {
		return darkchain;
	}
	public void setDarkchain(ChangeUrl darkchain) {
		this.darkchain = darkchain;
	}
	public Keyword getKeyword() {
		return keyword;
	}
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	public Smooth getSmooth() {
		return smooth;
	}
	public void setSmooth(Smooth smooth) {
		this.smooth = smooth;
	}
	
	
}
