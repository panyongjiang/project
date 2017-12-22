package com.uway.mobile.domain;

import java.sql.Timestamp;

/**
 * 用户组pojo
 * @author lin
 *
 */
public class UserGroup {
	
	
	private int id;
	
	private String groupName;
	
	private String description;
	
	private Timestamp createTime;

	private Timestamp modifyTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public String toString() {
		return "UserGroup [id=" + id + ", groupName=" + groupName
				+ ", description=" + description + ", createTime=" + createTime
				+ ", modifyTime=" + modifyTime + "]";
	}
	
	
	
}
