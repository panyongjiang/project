package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class MistakePatch implements Serializable {

	private Integer id;

	private String pc_name;

	private String ip;

	private String demand;

	private String city;

	private Integer untreated;

	private String status;

	private Date entrytime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPc_name() {
		return pc_name;
	}

	public void setPa_name(String pc_name) {
		this.pc_name = pc_name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getUntreated() {
		return untreated;
	}

	public void setUntreated(Integer untreated) {
		this.untreated = untreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public MistakePatch(String pc_name, String ip, String demand, String city, Integer untreated, String status,
			Date entrytime) {
		super();
		this.pc_name = pc_name;
		this.ip = ip;
		this.demand = demand;
		this.city = city;
		this.untreated = untreated;
		this.status = status;
		this.entrytime = entrytime;
	}

	@Override
	public String toString() {
		return "MistakePatch [id=" + id + ", pa_name=" + pc_name + ", ip=" + ip + ", demand=" + demand + ", city="
				+ city + ", untreated=" + untreated + ", status=" + status + ", entrytime=" + entrytime + "]";
	}

}
