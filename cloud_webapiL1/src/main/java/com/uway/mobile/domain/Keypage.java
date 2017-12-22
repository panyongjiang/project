package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Keypage {
	
	@XStreamImplicit(itemFieldName="page")
	private List<Param> params;
	
	private Malware malware;

	public Malware getMalware() {
		return malware;
	}

	public void setMalware(Malware malware) {
		this.malware = malware;
	}

	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}
	
		
}
