package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class AcceptanceProject implements Serializable{
	
	  private Integer id;	
	 
	  private String unit;
	  
	  private String time;
	  
	  private String numbering;
	  
	  private String project;
	  
	  private String billingmsg;
	  
	  private String contact;
	 
	  private String sign;
	  
	  private String filing;
	  
	  private String status;
	 
	  private String nosign;
	  
	  private Integer acceptance;
	  
	  private Integer report;
	 
	  private String remarks;
	  
	  private Date entrytime;
	
	public Date getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNumbering() {
		return numbering;
	}
	public void setNumbering(String numbering) {
		this.numbering = numbering;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getBillingmsg() {
		return billingmsg;
	}
	public void setBillingmsg(String billingmsg) {
		this.billingmsg = billingmsg;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getFiling() {
		return filing;
	}
	public void setFiling(String filing) {
		this.filing = filing;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNosign() {
		return nosign;
	}
	public void setNosign(String nosign) {
		this.nosign = nosign;
	}
	public Integer getAcceptance() {
		return acceptance;
	}
	public void setAcceptance(Integer acceptance) {
		this.acceptance = acceptance;
	}
	public Integer getReport() {
		return report;
	}
	public void setReport(Integer report) {
		this.report = report;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "AcceptanceProject [id=" + id + ", unit=" + unit + ", time=" + time + ", numbering=" + numbering
				+ ", project=" + project + ", billingmsg=" + billingmsg + ", contact=" + contact + ", sign=" + sign
				+ ", filing=" + filing + ", status=" + status + ", nosign=" + nosign + ", acceptance=" + acceptance
				+ ", report=" + report + ", remarks=" + remarks + ", entrytime=" + entrytime + "]";
	}
	public AcceptanceProject(String unit, String time, String numbering, String project, String billingmsg,
			String contact, String sign, String filing, String status, String nosign, Integer acceptance,
			Integer report, String remarks, Date entrytime) {
		super();
		this.unit = unit;
		this.time = time;
		this.numbering = numbering;
		this.project = project;
		this.billingmsg = billingmsg;
		this.contact = contact;
		this.sign = sign;
		this.filing = filing;
		this.status = status;
		this.nosign = nosign;
		this.acceptance = acceptance;
		this.report = report;
		this.remarks = remarks;
		this.entrytime = entrytime;
	}

}
