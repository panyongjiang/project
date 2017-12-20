package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class Ipaddress implements Serializable {
	private Integer id;

	private String ip;

	private String subnetmask;

	private Integer ipnumber;

	private String city;

	private String servicetype;

	private String computerroom;

	private String remark1;

	private String remark2;

	private String remark3;

	private Date addtime;

	private Date modifytime;

	private static final long serialVersionUID = 1L;

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
		this.ip = ip == null ? null : ip.trim();
	}

	public String getSubnetmask() {
		return subnetmask;
	}

	public void setSubnetmask(String subnetmask) {
		this.subnetmask = subnetmask == null ? null : subnetmask.trim();
	}

	public Integer getIpnumber() {
		return ipnumber;
	}

	public void setIpnumber(Integer ipnumber) {
		this.ipnumber = ipnumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype == null ? null : servicetype.trim();
	}

	public String getComputerroom() {
		return computerroom;
	}

	public void setComputerroom(String computerroom) {
		this.computerroom = computerroom == null ? null : computerroom.trim();
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1 == null ? null : remark1.trim();
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2 == null ? null : remark2.trim();
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3 == null ? null : remark3.trim();
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", ip=").append(ip);
		sb.append(", subnetmask=").append(subnetmask);
		sb.append(", ipnumber=").append(ipnumber);
		sb.append(", city=").append(city);
		sb.append(", servicetype=").append(servicetype);
		sb.append(", computerroom=").append(computerroom);
		sb.append(", remark1=").append(remark1);
		sb.append(", remark2=").append(remark2);
		sb.append(", remark3=").append(remark3);
		sb.append(", addtime=").append(addtime);
		sb.append(", modifytime=").append(modifytime);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}