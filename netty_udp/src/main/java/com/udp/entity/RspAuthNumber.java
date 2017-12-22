package com.udp.entity;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 云端应答设备认证 2006
 * */
public class RspAuthNumber {

	public static final Logger log = Logger.getLogger(ReqUrlCheck.class);
	
	public int number;//报文序号
	public byte[] randomStr;//一个16字节的随机字节
	
	ByteBuffer b;
	
	public RspAuthNumber(){
		b=ByteBuffer.allocate(20);
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public byte[] getRandomStr() {
		return randomStr;
	}
	public void setRandomStr(byte[] randomStr) {
		this.randomStr = randomStr;
	}
	
	public byte[] toData() {
		b.clear();
		b.putInt(this.number);
		b.put(this.randomStr);
		return b.array();
	}
	
	public int assign(byte[] data) {
		int len = 0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			
			this.number = b.getInt(0);
			if(this.randomStr==null) randomStr=new byte[16];
			System.arraycopy(data, 4, this.randomStr, 0, 16); 
			
			len += 20;
		}catch(Exception e){
			len=0;
			log.error("erro:"+e.getStackTrace());
		}
		
		return len;
	}
}
