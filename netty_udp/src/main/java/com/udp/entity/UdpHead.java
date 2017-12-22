package com.udp.entity;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 报头信息
 * */
public class UdpHead {
	
	public static final Logger log = Logger.getLogger(UdpHead.class);

	ByteBuffer b;
	
	private short Length;//加密整个数据包前总长	
	private short Command;//命令码
	private long PlugID;//设备ID
	private short encryptSeq;//加密序号
	private short ZLength;//加密后整个数据包前总长度
	
	public UdpHead() {
		this.b = ByteBuffer.allocate(16);
	}
	
	public short getLength() {
		return Length;
	}
	public void setLength(short length) {
		Length = length;
	}
	public short getZLength() {
		return ZLength;
	}
	public void setZLength(short Zlength) {
		ZLength = Zlength;
	}
	public short getCommand() {
		return Command;
	}
	public void setCommand(short command) {
		Command = command;
	}
	
	public long getPlugID() {
		return PlugID;
	}

	public void setPlugID(long plugID) {
		PlugID = plugID;
	}

	public short getEncryptSeq() {
		return encryptSeq;
	}

	public void setEncryptSeq(short encryptSeq) {
		this.encryptSeq = encryptSeq;
	}

	public byte[] toData() {
		b.clear();
		b.putShort(this.Length);		
		b.putShort(this.Command);
		b.putLong(this.PlugID);
		b.putShort(this.encryptSeq);
		b.putShort(this.ZLength);
		return b.array();
	}
	
	public int assign(byte[] data) {
		int len = 0;
		try{
			b.clear();
			b = ByteBuffer.wrap(data);
			
			this.Length = b.getShort(0);			
			this.Command = b.getShort(2);
			this.PlugID = b.getLong(4);
			this.encryptSeq = b.getShort(12);
			this.ZLength = b.getShort(14);
			
			len += 16;
		}catch(Exception e){
			len=0;
			log.error("erro:"+e.getStackTrace());
		}
		return len;
	}
}
