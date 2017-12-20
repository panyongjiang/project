package com.uway.mobile.domain;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.uway.mobile.util.ConverterUtil;

@XStreamConverter(strings={"content"}, value = ConverterUtil.class)
public class Page {
	private String url;               //被挂马的关键页面链接
	private String find_time;         //扫描时期
	private String ref_urls;          //对应的url
	private String ref_vuls;          //利用漏洞
	private String find_time_latest;  //最近发现时间
	private String info;              //相应的信息
	private String content;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFind_time() {
		return find_time;
	}
	public void setFind_time(String find_time) {
		this.find_time = find_time;
	}
	public String getRef_urls() {
		return ref_urls;
	}
	public void setRef_urls(String ref_urls) {
		this.ref_urls = ref_urls;
	}
	public String getRef_vuls() {
		return ref_vuls;
	}
	public void setRef_vuls(String ref_vuls) {
		this.ref_vuls = ref_vuls;
	}
	public String getFind_time_latest() {
		return find_time_latest;
	}
	public void setFind_time_latest(String find_time_latest) {
		this.find_time_latest = find_time_latest;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
