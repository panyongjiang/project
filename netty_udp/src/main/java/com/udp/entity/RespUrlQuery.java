package com.udp.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class RespUrlQuery {

public static final Logger log = Logger.getLogger(RespUrlQuery.class);
	
	ByteBuffer b;
	
	private byte version;
	private byte result;
	private byte[] key;
	
	public RespUrlQuery(){
		b = ByteBuffer.allocate(8);
	}
	
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
	public byte[] getKey() {
		return key;
	}
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	public byte[] toData() {
		b.clear();
		b.put(this.version);
		b.put(this.result);
		b.put(this.key);
		return Arrays.copyOfRange(b.array(), 0, 8);
	}
	
	public int assign(byte[] data) {
		int len=0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			this.version = b.get(0);
			this.result = b.get(1);
			
			if(this.key==null) key=new byte[6];
			System.arraycopy(data, 2, this.key, 0, 6); 
			
			len = 8;
		}catch(Exception e){
			len=0;
			log.error("erro1:"+e.getStackTrace());
		}
		return len;
	}
}
