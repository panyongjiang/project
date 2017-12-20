package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Vul {

	private String site_id;
	private String hole_id;
	private String method;
	private String url;
	@XStreamImplicit(itemFieldName="param")
	private List<Param> param;
	private String exploit;
	private String issue;
	
	
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getHole_id() {
		return hole_id;
	}
	public void setHole_id(String hole_id) {
		this.hole_id = hole_id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<Param> getParam() {
		return param;
	}
	public void setParam(List<Param> param) {
		this.param = param;
	}
	public String getExploit() {
		return exploit;
	}
	public void setExploit(String exploit) {
		this.exploit = exploit;
	}
}
