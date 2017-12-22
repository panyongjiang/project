package com.udp.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * 上报恶意URL代号  NTF_URL_EVIL  2004
 * */
public class NtfUrlEnil {

	public static final Logger log = Logger.getLogger(NtfUrlEnil.class);
	
	ByteBuffer b;
	
	public byte enilType;//恶意URL代号自定义分类
	public byte[] Mac;//固定12字节mac地址
	public byte urlLen;//URL地址长度
	public byte[] url;//URL地址
	public short srcPort;//来源 port
	public short dstPort;//目的 port
	public int srcIp;//来源 IP
	public int dstIp;//目的 IP
	
	public NtfUrlEnil(){
		b=ByteBuffer.allocate(1024);
	}
	
	public byte getEnilType() {
		return enilType;
	}
	public void setEnilType(byte enilType) {
		this.enilType = enilType;
	}
	public byte[] getMac() {
		return Mac;
	}
	public void setMac(byte[] mac) {
		Mac = mac;
	}
	public byte getUrlLen() {
		return urlLen;
	}
	public void setUrlLen(byte urlLen) {
		this.urlLen = urlLen;
	}
	public byte[] getUrl() {
		return url;
	}
	public void setUrl(byte[] url) {
		this.url = url;
	}
	public short getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(short srcPort) {
		this.srcPort = srcPort;
	}
	public short getDstPort() {
		return dstPort;
	}
	public void setDstPort(short dstPort) {
		this.dstPort = dstPort;
	}
	public int getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(int srcIp) {
		this.srcIp = srcIp;
	}
	public int getDstIp() {
		return dstIp;
	}
	public void setDstIp(int dstIp) {
		this.dstIp = dstIp;
	}

	public byte[] toData() {
		//ByteBuffer b = ByteBuffer.allocate(30);
		this.b.clear();
		b.put(this.enilType);
		b.put(this.Mac);
		b.put(this.urlLen);
		b.put(this.url);
		b.putShort(this.srcPort);
		b.putShort(this.dstPort);
		b.putInt(this.srcIp);
		b.putInt(this.dstIp);
		
		return Arrays.copyOfRange(b.array(), 0, 26+this.urlLen);
	}
	public int assign(byte[] data) {
		int len = 0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			
			this.enilType = b.get(0);
			if(this.Mac==null) Mac=new byte[12];
			System.arraycopy(data, 1, this.Mac, 0, 12); 
			//b.get(this.Mac, 0, 12);
			this.urlLen = b.get(13);
			if(url==null) url=new byte[this.urlLen];
			System.arraycopy(data, 14, this.url, 0, this.urlLen); 
//			b.get(this.url, 14, this.urlLen);
			this.srcPort = b.getShort(14+this.urlLen);
			this.dstPort = b.getShort(16+this.urlLen);
			this.srcIp  =  b.getInt(18+this.urlLen);
			this.dstIp  =  b.getInt(22+this.urlLen);
			
			len += (22+this.urlLen);
		}catch(Exception e){
			len=0;
			log.error("erro:"+e.getStackTrace());
		}
		
		
		return len;
	}
}
