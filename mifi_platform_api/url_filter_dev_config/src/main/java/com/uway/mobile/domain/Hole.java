package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Hole {

	private String id;
	private String site_id;
	private String site_risk_id;
	private String title;
	private String vulid;
	private String vultype;
	private String description;
	private String solution;
	private String cve_id;
	private String bugtraq;
	private String severity_points;
	private String date_found;
	
	@XStreamImplicit(itemFieldName="vul")
	private List<Vul> vul;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getSite_risk_id() {
		return site_risk_id;
	}
	public void setSite_risk_id(String site_risk_id) {
		this.site_risk_id = site_risk_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVulid() {
		return vulid;
	}
	public void setVulid(String vulid) {
		this.vulid = vulid;
	}
	public String getVultype() {
		return vultype;
	}
	public void setVultype(String vultype) {
		this.vultype = vultype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getCve_id() {
		return cve_id;
	}
	public void setCve_id(String cve_id) {
		this.cve_id = cve_id;
	}
	public String getBugtraq() {
		return bugtraq;
	}
	public void setBugtraq(String bugtraq) {
		this.bugtraq = bugtraq;
	}
	public String getSeverity_points() {
		return severity_points;
	}
	public void setSeverity_points(String severity_points) {
		this.severity_points = severity_points;
	}
	public String getDate_found() {
		return date_found;
	}
	public void setDate_found(String date_found) {
		this.date_found = date_found;
	}
	public List<Vul> getVul() {
		return vul;
	}
	public void setVul(List<Vul> vul) {
		this.vul = vul;
	}
	
}
