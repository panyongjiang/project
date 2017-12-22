package udp;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ntf_url_evil {
	
	byte  EvilType;
	byte[] Mac;
	byte URLLen;
	byte[] URL;
	short srcPort;
	short dstPort;
	int srcIP;
	int  dstIP;

	ByteBuffer b;
	
	public ntf_url_evil() {
		this.b = ByteBuffer.allocate(1024);
	}
	
	public byte getEvilType() {
		return EvilType;
	}


	public void setEvilType(byte evilType) {
		EvilType = evilType;
	}


	public byte[] getMac() {
		return Mac;
	}


	public void setMac(byte[] mac) {
		Mac = mac;
	}


	public byte getURLLen() {
		return URLLen;
	}


	public void setURLLen(byte uRLLen) {
		URLLen = uRLLen;
	}


	public byte[] getURL() {
		return URL;
	}


	public void setURL(byte[] uRL) {
		URL = uRL;
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


	public int getSrcIP() {
		return srcIP;
	}


	public void setSrcIP(int srcIP) {
		this.srcIP = srcIP;
	}


	public int getDstIP() {
		return dstIP;
	}


	public void setDstIP(int dstIP) {
		this.dstIP = dstIP;
	}


	public byte[] toData() {
		//ByteBuffer b = ByteBuffer.allocate(30);
		this.b.clear();
		b.put(this.EvilType);
		b.put(this.Mac);
		b.put(this.URLLen);
		b.put(this.URL);
		b.putShort(this.srcPort);
		b.putShort(this.dstPort);
		b.putInt(this.srcIP);
		b.putInt(this.dstIP);
		
		return Arrays.copyOfRange(b.array(), 0, 26+this.URLLen);
	}
	public int assign(byte[] data) {
		int len = 0;
		b.clear();
		b = ByteBuffer.wrap(data);
		
		this.EvilType = b.get(0);
		b.get(this.Mac, 1, 12);
		this.URLLen = b.get(13);
		b.get(this.URL, 14, this.URLLen);
		this.srcPort = b.getShort(14+this.URLLen);
		this.dstPort = b.getShort(16+this.URLLen);
		this.srcIP  =  b.getInt(18+this.URLLen);
		this.dstIP  =  b.getInt(22+this.URLLen);
		
		len += (22+this.URLLen);
		
		return len;
	}
}