package com.uway.mobile.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Loophole implements Serializable {

	private Integer id;

	private String aims;

	private String units;

	private String principal;

	private String phone;

	private String level;

	private Integer highscan;

	private Integer mediumscan;

	private Integer highseep;

	private Integer mediumseep;

	private Integer lowseep;

	private Integer weakpwd;

	private Integer rectifyhighscan;

	private Integer filinghighscan;

	private Integer rectifymediumscan;

	private Integer filingmediumscan;

	private Integer rectifyhighseep;

	private Integer rectifymediumseep;

	private Integer filingmediumseep;

	private Integer rectifylowseep;

	private Integer rectifyweakpwd;

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

	public String getAims() {
		return aims;
	}

	public void setAims(String aims) {
		this.aims = aims;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getHighscan() {
		return highscan;
	}

	public void setHighscan(Integer highscan) {
		this.highscan = highscan;
	}

	public Integer getMediumscan() {
		return mediumscan;
	}

	public void setMediumscan(Integer mediumscan) {
		this.mediumscan = mediumscan;
	}

	public Integer getHighseep() {
		return highseep;
	}

	public void setHighseep(Integer highseep) {
		this.highseep = highseep;
	}

	public Integer getMediumseep() {
		return mediumseep;
	}

	public void setMediumseep(Integer mediumseep) {
		this.mediumseep = mediumseep;
	}

	public Integer getLowseep() {
		return lowseep;
	}

	public void setLowseep(Integer lowseep) {
		this.lowseep = lowseep;
	}

	public Integer getWeakpwd() {
		return weakpwd;
	}

	public void setWeakpwd(Integer weakpwd) {
		this.weakpwd = weakpwd;
	}

	public Integer getRectifyhighscan() {
		return rectifyhighscan;
	}

	public void setRectifyhighscan(Integer rectifyhighscan) {
		this.rectifyhighscan = rectifyhighscan;
	}

	public Integer getFilinghighscan() {
		return filinghighscan;
	}

	public void setFilinghighscan(Integer filinghighscan) {
		this.filinghighscan = filinghighscan;
	}

	public Integer getRectifymediumscan() {
		return rectifymediumscan;
	}

	public void setRectifymediumscan(Integer rectifymediumscan) {
		this.rectifymediumscan = rectifymediumscan;
	}

	public Integer getFilingmediumscan() {
		return filingmediumscan;
	}

	public void setFilingmediumscan(Integer filingmediumscan) {
		this.filingmediumscan = filingmediumscan;
	}

	public Integer getRectifyhighseep() {
		return rectifyhighseep;
	}

	public void setRectifyhighseep(Integer rectifyhighseep) {
		this.rectifyhighseep = rectifyhighseep;
	}

	public Integer getRectifymediumseep() {
		return rectifymediumseep;
	}

	public void setRectifymediumseep(Integer rectifymediumseep) {
		this.rectifymediumseep = rectifymediumseep;
	}

	public Integer getFilingmediumseep() {
		return filingmediumseep;
	}

	public void setFilingmediumseep(Integer filingmediumseep) {
		this.filingmediumseep = filingmediumseep;
	}

	public Integer getRectifylowseep() {
		return rectifylowseep;
	}

	public void setRectifylowseep(Integer rectifylowseep) {
		this.rectifylowseep = rectifylowseep;
	}

	public Integer getRectifyweakpwd() {
		return rectifyweakpwd;
	}

	public void setRectifyweakpwd(Integer rectifyweakpwd) {
		this.rectifyweakpwd = rectifyweakpwd;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Loophole(String aims, String units, String principal, String phone, String level, Integer highscan,
			Integer mediumscan, Integer highseep, Integer mediumseep, Integer lowseep, Integer weakpwd,
			Integer rectifyhighscan, Integer filinghighscan, Integer rectifymediumscan, Integer filingmediumscan,
			Integer rectifyhighseep, Integer rectifymediumseep, Integer filingmediumseep, Integer rectifylowseep,
			Integer rectifyweakpwd, String remarks, Date entrytime) {
		super();
		this.aims = aims;
		this.units = units;
		this.principal = principal;
		this.phone = phone;
		this.level = level;
		this.highscan = highscan;
		this.mediumscan = mediumscan;
		this.highseep = highseep;
		this.mediumseep = mediumseep;
		this.lowseep = lowseep;
		this.weakpwd = weakpwd;
		this.rectifyhighscan = rectifyhighscan;
		this.filinghighscan = filinghighscan;
		this.rectifymediumscan = rectifymediumscan;
		this.filingmediumscan = filingmediumscan;
		this.rectifyhighseep = rectifyhighseep;
		this.rectifymediumseep = rectifymediumseep;
		this.filingmediumseep = filingmediumseep;
		this.rectifylowseep = rectifylowseep;
		this.rectifyweakpwd = rectifyweakpwd;
		this.remarks = remarks;
		this.entrytime = entrytime;
	}

	@Override
	public String toString() {
		return "Loophole [id=" + id + ", aims=" + aims + ", units=" + units + ", principal=" + principal + ", phone="
				+ phone + ", level=" + level + ", highscan=" + highscan + ", mediumscan=" + mediumscan + ", highseep="
				+ highseep + ", mediumseep=" + mediumseep + ", lowseep=" + lowseep + ", weakpwd=" + weakpwd
				+ ", rectifyhighscan=" + rectifyhighscan + ", filinghighscan=" + filinghighscan + ", rectifymediumscan="
				+ rectifymediumscan + ", filingmediumscan=" + filingmediumscan + ", rectifyhighseep=" + rectifyhighseep
				+ ", rectifymediumseep=" + rectifymediumseep + ", filingmediumseep=" + filingmediumseep
				+ ", rectifylowseep=" + rectifylowseep + ", rectifyweakpwd=" + rectifyweakpwd + ", remarks=" + remarks
				+ ", entrytime=" + entrytime + "]";
	}

}
