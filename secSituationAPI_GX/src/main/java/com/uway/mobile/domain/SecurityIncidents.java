package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class SecurityIncidents implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String category;

	private String event;

	private String level;

	private Date occurenceTime;

	private String eventDescription;

	private String unitAddess;

	private String involveContent;

	private String briefPass;

	private String harmAndEffect;

	private String measures;

	private String remarks;

	private String ip;

	private String department;
	private Date entrytime;

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	// 0 ：未处理 ~ 1 ：已处理
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public SecurityIncidents(String category, String event, String eventDescription, String unitAddess,
			String briefPass, String measures) {
		super();
		this.category = category;
		this.event = event;
		this.eventDescription = eventDescription;
		this.unitAddess = unitAddess;
		this.briefPass = briefPass;
		this.measures = measures;
	}

	public SecurityIncidents(String category, String event, String level, Date occurenceTime, String eventDescription,
			String unitAddess, String involveContent, String briefPass, String harmAndEffect, String measures,
			String remarks, String ip, String department, Date entrytime, Integer status) {
		super();
		this.category = category;
		this.event = event;
		this.level = level;
		this.occurenceTime = occurenceTime;
		this.eventDescription = eventDescription;
		this.unitAddess = unitAddess;
		this.involveContent = involveContent;
		this.briefPass = briefPass;
		this.harmAndEffect = harmAndEffect;
		this.measures = measures;
		this.remarks = remarks;
		this.ip = ip;
		this.department = department;
		this.entrytime = entrytime;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getOccurenceTime() {
		return occurenceTime;
	}

	public void setOccurenceTime(Date occurenceTime) {
		this.occurenceTime = occurenceTime;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getUnitAddess() {
		return unitAddess;
	}

	public void setUnitAddess(String unitAddess) {
		this.unitAddess = unitAddess;
	}

	public String getInvolveContent() {
		return involveContent;
	}

	public void setInvolveContent(String involveContent) {
		this.involveContent = involveContent;
	}

	public String getBriefPass() {
		return briefPass;
	}

	public void setBriefPass(String briefPass) {
		this.briefPass = briefPass;
	}

	public String getHarmAndEffect() {
		return harmAndEffect;
	}

	public void setHarmAndEffect(String harmAndEffect) {
		this.harmAndEffect = harmAndEffect;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((briefPass == null) ? 0 : briefPass.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((eventDescription == null) ? 0 : eventDescription.hashCode());
		result = prime * result + ((harmAndEffect == null) ? 0 : harmAndEffect.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((involveContent == null) ? 0 : involveContent.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((measures == null) ? 0 : measures.hashCode());
		result = prime * result + ((occurenceTime == null) ? 0 : occurenceTime.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((unitAddess == null) ? 0 : unitAddess.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityIncidents other = (SecurityIncidents) obj;
		if (briefPass == null) {
			if (other.briefPass != null)
				return false;
		} else if (!briefPass.equals(other.briefPass))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (eventDescription == null) {
			if (other.eventDescription != null)
				return false;
		} else if (!eventDescription.equals(other.eventDescription))
			return false;
		if (harmAndEffect == null) {
			if (other.harmAndEffect != null)
				return false;
		} else if (!harmAndEffect.equals(other.harmAndEffect))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (involveContent == null) {
			if (other.involveContent != null)
				return false;
		} else if (!involveContent.equals(other.involveContent))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (measures == null) {
			if (other.measures != null)
				return false;
		} else if (!measures.equals(other.measures))
			return false;
		if (occurenceTime == null) {
			if (other.occurenceTime != null)
				return false;
		} else if (!occurenceTime.equals(other.occurenceTime))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (unitAddess == null) {
			if (other.unitAddess != null)
				return false;
		} else if (!unitAddess.equals(other.unitAddess))
			return false;
		return true;
	}

}