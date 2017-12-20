package com.uway.mobile.domain;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.uway.mobile.util.ConverterUtil;

@XStreamConverter(strings={"content"}, value = ConverterUtil.class)
public class Param {

	private String attr;
	private String content;
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
