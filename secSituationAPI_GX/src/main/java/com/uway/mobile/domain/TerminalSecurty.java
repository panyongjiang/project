package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class TerminalSecurty implements Serializable {
	private Integer id;

	private String number;

	private String position;

	private String logcategory;

	private String description;

	private String duration;

	private String imei;

	private String plaformsystem;

	private String ipaddress;

	private Date reportingtime;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number == null ? null : number.trim();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position == null ? null : position.trim();
	}

	public String getLogcategory() {
		return logcategory;
	}

	public void setLogcategory(String logcategory) {
		this.logcategory = logcategory == null ? null : logcategory.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration == null ? null : duration.trim();
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei == null ? null : imei.trim();
	}

	public String getPlaformsystem() {
		return plaformsystem;
	}

	public void setPlaformsystem(String plaformsystem) {
		this.plaformsystem = plaformsystem == null ? null : plaformsystem.trim();
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress == null ? null : ipaddress.trim();
	}

	public Date getReportingtime() {
		return reportingtime;
	}

	public void setReportingtime(Date reportingtime) {
		this.reportingtime = reportingtime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", number=").append(number);
		sb.append(", position=").append(position);
		sb.append(", logcategory=").append(logcategory);
		sb.append(", description=").append(description);
		sb.append(", duration=").append(duration);
		sb.append(", imei=").append(imei);
		sb.append(", plaformsystem=").append(plaformsystem);
		sb.append(", ipaddress=").append(ipaddress);
		sb.append(", reportingtime=").append(reportingtime);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		TerminalSecurty other = (TerminalSecurty) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
				&& (this.getPosition() == null ? other.getPosition() == null
						: this.getPosition().equals(other.getPosition()))
				&& (this.getLogcategory() == null ? other.getLogcategory() == null
						: this.getLogcategory().equals(other.getLogcategory()))
				&& (this.getDescription() == null ? other.getDescription() == null
						: this.getDescription().equals(other.getDescription()))
				&& (this.getDuration() == null ? other.getDuration() == null
						: this.getDuration().equals(other.getDuration()))
				&& (this.getImei() == null ? other.getImei() == null : this.getImei().equals(other.getImei()))
				&& (this.getPlaformsystem() == null ? other.getPlaformsystem() == null
						: this.getPlaformsystem().equals(other.getPlaformsystem()))
				&& (this.getIpaddress() == null ? other.getIpaddress() == null
						: this.getIpaddress().equals(other.getIpaddress()))
				&& (this.getReportingtime() == null ? other.getReportingtime() == null
						: this.getReportingtime().equals(other.getReportingtime()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
		result = prime * result + ((getPosition() == null) ? 0 : getPosition().hashCode());
		result = prime * result + ((getLogcategory() == null) ? 0 : getLogcategory().hashCode());
		result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
		result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
		result = prime * result + ((getImei() == null) ? 0 : getImei().hashCode());
		result = prime * result + ((getPlaformsystem() == null) ? 0 : getPlaformsystem().hashCode());
		result = prime * result + ((getIpaddress() == null) ? 0 : getIpaddress().hashCode());
		result = prime * result + ((getReportingtime() == null) ? 0 : getReportingtime().hashCode());
		return result;
	}
}