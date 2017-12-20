package com.uway.mobile.domain;

public class User {
	
	private Integer id;
	
	private String user;
	
	private Integer role;
	
	private String passwd;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public User(String user, Integer role, String passwd) {
		super();
		this.user = user;
		this.role = role;
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user=" + user + ", role=" + role + ", passwd=" + passwd + "]";
	}
	
	
}
