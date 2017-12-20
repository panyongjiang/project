package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class VirusStrategy implements Serializable {

	private Integer id;
	private String pc_name;
	private String ip;
	private String virusversion;
	private String clienttype;
	private String system;
	private String group;
	private String city;
	private Integer untreated;
	private Date entrytime;

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getVirusversion() {
		return virusversion;
	}

	public void setVirusversion(String virusversion) {
		this.virusversion = virusversion;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
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

	public Date getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Date entrytime) {
		this.entrytime = entrytime;
	}

	public VirusStrategy(String pc_name, String ip, String virusversion, String clienttype, String system, String group,
			String city, Integer untreated, Date entrytime) {
		super();
		this.pc_name = pc_name;
		this.ip = ip;
		this.virusversion = virusversion;
		this.clienttype = clienttype;
		this.system = system;
		this.group = group;
		this.city = city;
		this.untreated = untreated;
		this.entrytime = entrytime;
	}

	@Override
	public String toString() {
		return "VirusStrategy [id=" + id + ", pc_name=" + pc_name + ", ip=" + ip + ", virusversion=" + virusversion
				+ ", clienttype=" + clienttype + ", system=" + system + ", group=" + group + ", city=" + city
				+ ", untreated=" + untreated + ", entrytime=" + entrytime + "]";
	}

}
