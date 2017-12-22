package com.udp.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * 2002 触云云端发送URL地址到服务端检测
 * */
public class ReqUrlCheck {

	
	public static final Logger log = Logger.getLogger(ReqUrlCheck.class);
	
	ByteBuffer b;
	//ByteBuffer urlKey;
	
	public int number;//报文序号
	public short urllen;//URL地址MD5长度
	public byte[] url;// URL地址MD5 
	public byte news;//有该字段则不统计数量
	public short urlsrclen;//URL原始数据长度
	public byte[] urlsrc;//URL原始数据	
	
	public ReqUrlCheck(){
		b = ByteBuffer.allocate(1024);
		//urlKey = ByteBuffer.allocate(1024);
		//urlKey.put(Constants.URLBEFS.getBytes());
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public short getUrllen() {
		return urllen;
	}
	public void setUrllen(short urllen) {
		this.urllen = urllen;
	}
	public byte[] getUrl() {
		return url;
	}
	public void setUrl(byte[] url) {
		this.url = url;
	}
	public byte getNews() {
		return news;
	}
	public void setNews(byte news) {
		this.news = news;
	}
	public short getUrlsrclen() {
		return urlsrclen;
	}
	public void setUrlsrclen(short urlsrclen) {
		this.urlsrclen = urlsrclen;
	}
	public byte[] getUrlsrc() {
		return urlsrc;
	}
	public void setUrlsrc(byte[] urlsrc) {
		this.urlsrc = urlsrc;
	}
	
	public byte[] toData() {
		b.clear();
		b.putInt(this.number);
		b.putShort(this.urllen);
		b.put(url);
		b.put(this.news);
		b.putShort(this.urlsrclen);
		b.put(urlsrc);
		
		return Arrays.copyOfRange(b.array(), 0, 9+this.urllen+this.urlsrclen);
	}
	public int assign(byte[] data) {
		int len = 0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			
			this.number = b.getInt(0);
			this.urllen = b.getShort(4);
			if(this.url==null) url=new byte[this.urllen];
			System.arraycopy(data, 6, this.url, 0, this.urllen); 
			//b.get(this.url, 0, this.urllen);
			this.news = b.get(6+this.urllen);
			
			this.urlsrclen = b.getShort(7+this.urllen);
			if(this.urlsrc==null) this.urlsrc=new byte[this.urlsrclen];
			//b.get(this.urlsrc, 0, this.urlsrclen);
			System.arraycopy(data, 7+this.urllen+2, this.urlsrc, 0, this.urlsrclen); 
			
			len += (7+this.urllen+this.urlsrclen+2);
			
			//String hexUrl = new BigInteger(1, this.url).toString(16);
			//int hexUrlLen = hexUrl.length();
			
			//byte[] tmp = new byte[hexUrlLen];
			//System.arraycopy(Constants.URLBEFS.getBytes(),0,tmp,0,Constants.URLBEFS.length());
			
			//System.arraycopy(hexUrl.getBytes(),0,tmp,Constants.URLBEFS.length(),hexUrlLen);
			
			//this.urlKey = ByteBuffer.wrap(tmp);
		}catch(Exception e){
			len=0;
			log.error("erro1:"+e.getStackTrace());
		}
		
		return len;
	}
	
	/*public byte[] getUrlKey()
	{
		return this.urlKey.array();
	}*/
}
