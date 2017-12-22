package com.udp.entity;

import java.nio.ByteBuffer;
import org.apache.log4j.Logger;

/**
 * 触云设备请求服务端认证  2005
 * */
public class ReqAuthNumber {

	public static final Logger log = Logger.getLogger(ReqUrlCheck.class);
	
	public int number;//报文序号
	public byte EncryptMethod;//值为1的时候，对应的加密算法为 TinyEncrypt，目前仅支持一种加密算法
	
	ByteBuffer b;
	
	public ReqAuthNumber(){
		b=ByteBuffer.allocate(5);
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public byte getEncryptMethod() {
		return EncryptMethod;
	}
	public void setEncryptMethod(byte encryptMethod) {
		EncryptMethod = encryptMethod;
	}
	
	public byte[] toData() {
		b.clear();
		b.putInt(this.number);
		b.put(this.EncryptMethod);
		return b.array();
	}
	
	public int assign(byte[] data) {
		int len = 0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			
			this.number = b.getInt(0);
			this.EncryptMethod = b.get(4);
			
			len += 5;
		}catch(Exception e){
			len=0;
			log.error("erro:"+e.getStackTrace());
		}
		return len;
	}
}
