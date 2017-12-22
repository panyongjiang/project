package com.udp.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ReqUrlQuery {

	public static final Logger log = Logger.getLogger(ReqUrlQuery.class);
	
	ByteBuffer b;
	
	private byte version;
	private byte result;
	private byte[] key;
	private byte[] server_reserve;
	private byte[] url;
	
	public ReqUrlQuery(){
		b = ByteBuffer.allocate(1024);
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
	public byte[] getServer_reserve() {
		return server_reserve;
	}
	public void setServer_reserve(byte[] server_reserve) {
		this.server_reserve = server_reserve;
	}
	public byte[] getUrl() {
		return url;
	}
	public void setUrl(byte[] url) {
		this.url = url;
	}
	
	public byte[] toData() {
		b.clear();
		b.put(this.version);
		b.put(this.result);
		b.put(this.key);
		b.put(this.server_reserve);
		b.put(this.url);
		return Arrays.copyOfRange(b.array(), 0, 16+this.url.length);
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
			
			if(this.server_reserve==null) server_reserve=new byte[8];
			System.arraycopy(data, 8, this.key, 0, 8);
			
			byte[] urls=new byte[data.length-16];			
			System.arraycopy(data, 16, urls, 0, data.length-16);
			String surl=new String(urls);
			if(surl.contains("\0")){
				int a=surl.indexOf("\0");
				if(this.url==null) url=new byte[surl.length()-a];
				this.url = surl.substring(0, surl.indexOf("\0")).getBytes();
			}
			len = 16+(this.url==null?0:this.url.length);
		}catch(Exception e){
			len=0;
			log.error("erro1:"+e.getStackTrace());
		}
		return len;
	}
}
