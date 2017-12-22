package com.uway.mobile.domain;

public class SensitiveWord {
	private String id; //UUID生成
	private String name;
	public String getId(){
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
