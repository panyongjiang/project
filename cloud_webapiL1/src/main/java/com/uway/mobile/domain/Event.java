package com.uway.mobile.domain;

public class Event {
	private String check_time;     //检测时间
	private String time_out;       //用户设定的超时时间
	private String respone_time;   //访问响应时间
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
	public String getTime_out() {
		return time_out;
	}
	public void setTime_out(String time_out) {
		this.time_out = time_out;
	}
	public String getRespone_time() {
		return respone_time;
	}
	public void setRespone_time(String respone_time) {
		this.respone_time = respone_time;
	}
	
}
