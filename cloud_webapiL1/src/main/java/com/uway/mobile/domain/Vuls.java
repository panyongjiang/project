package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Vuls {
	private Object title;
	private Object url;
	private Object solution;
	private Object vulurl;
	private Object parameter;
	private Object parameterurl;
	private Object vulVerification;
	private Object vulVerificationurl;
	
	public Object getVulurl() {
		return vulurl;
	}

	public void setVulurl(Object vulurl) {
		this.vulurl = vulurl;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	public Object getParameterurl() {
		return parameterurl;
	}

	public void setParameterurl(Object parameterurl) {
		this.parameterurl = parameterurl;
	}

	public Object getVulVerification() {
		return vulVerification;
	}

	public void setVulVerification(Object vulVerification) {
		this.vulVerification = vulVerification;
	}

	public Object getVulVerificationurl() {
		return vulVerificationurl;
	}

	public void setVulVerificationurl(Object vulVerificationurl) {
		this.vulVerificationurl = vulVerificationurl;
	}

	public Object getTitle() {
		return title;
	}

	public void setTitle(Object title) {
		this.title = title;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	public Object getSolution() {
		return solution;
	}

	public void setSolution(Object solution) {
		this.solution = solution;
	}

	@XStreamImplicit(itemFieldName="hole")
	private List<Hole> hole;

	public List<Hole> getHole() {
		return hole;
	}

	public void setHole(List<Hole> hole) {
		this.hole = hole;
	}
}
