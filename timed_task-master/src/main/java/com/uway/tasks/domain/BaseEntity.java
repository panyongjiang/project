package com.uway.tasks.domain;

import java.sql.Timestamp;

public class BaseEntity {
	private Timestamp modifyTime;
	private Timestamp createTime;
	
	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
