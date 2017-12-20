package com.uway.mobile.domain;

import java.sql.Timestamp;

/**
 * 资源表
 * @author lin
 *
 */
public class Resource {
	
	private int id;

	private String resourceName;

	private String resourceUrl;

	private String resourceId;

	private String resourceParentid;
	
	private Timestamp createTime;

	private Timestamp modifyTime;
	
	private String menuSign;
	
	private String primaryName;
	//资源子资源
	private String resourceChildid;
  

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceParentid() {
		return resourceParentid;
	}

	public void setResourceParentid(String resourceParentid) {
		this.resourceParentid = resourceParentid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getMenuSign() {
		return menuSign;
	}

	public void setMenuSign(String menuSign) {
		this.menuSign = menuSign;
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	public String getResourceChildid() {
		return resourceChildid;
	}

	public void setResourceChildid(String resourceChildid) {
		this.resourceChildid = resourceChildid;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", resourceName=" + resourceName
				+ ", resourceUrl=" + resourceUrl + ", resourceId=" + resourceId
				+ ", resourceParentid=" + resourceParentid + ", createTime="
				+ createTime + ", modifyTime=" + modifyTime + ", menuSign="
				+ menuSign + ", primaryName=" + primaryName
				+ ", resourceChildid=" + resourceChildid + "]";
	}

	
    
	

	

	
	
	

}
