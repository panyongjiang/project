package com.udp.entity;

import java.nio.ByteBuffer;

/**
 * 应答检测URL地址  ACK_URL_CHECK  2003
 * */
public class AckUrlCheck {

	ByteBuffer b;
	public int number;//报文序号
	public int result;//检测结果【0：非恶意URL地址 非0：恶意URL代号】
	public byte type;//恶意URL代号自定义分类
	
	public AckUrlCheck(){
		b=ByteBuffer.allocate(9);
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	
	public byte[] toData() {
		b.clear();
		b.putInt(this.number);
		b.putInt(this.result);
		b.put(this.type);
		return b.array();
	}
	
	public int assign(byte[] data) {
		int len = 0;
		b.clear();
		b = ByteBuffer.wrap(data);
		
		this.number = b.getInt(0);
		this.result = b.getInt(4);
		this.type = b.get(8);
		
		len += 9;
		
		return len;
	}
}
