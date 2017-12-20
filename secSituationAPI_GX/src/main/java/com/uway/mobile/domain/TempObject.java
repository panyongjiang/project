package com.uway.mobile.domain;

public class TempObject {
	private long port;
	private long sum;

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public long getSum() {
		return sum;
	}

	public void setSum(long sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "TempObject [port=" + port + ", sum=" + sum + "]";
	}

}
