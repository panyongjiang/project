package udp;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class req_url_check {
	ByteBuffer b;
	int Number;
	short URLLen;
	byte[] URL;
	byte New;
	short URLSRCLen;
	byte[] URLSRC;
	
	public req_url_check() {
		b = ByteBuffer.allocate(1024);
	}
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public short getURLLen() {
		return URLLen;
	}
	public void setURLLen(short uRLLen) {
		URLLen = uRLLen;
	}
	public byte[] getURL() {
		return URL;
	}
	public void setURL(byte[] uRL) {
		URL = uRL;
	}
	public byte getNew() {
		return New;
	}
	public void setNew(byte new1) {
		New = new1;
	}
	public short getURLSRCLen() {
		return URLSRCLen;
	}
	public void setURLSRCLen(short uRLSRCLen) {
		URLSRCLen = uRLSRCLen;
	}
	public byte[] getURLSRC() {
		return URLSRC;
	}
	public void setURLSRC(byte[] uRLSRC) {
		URLSRC = uRLSRC;
	}
	public byte[] toData() {
		//ByteBuffer b = ByteBuffer.allocate(7+this.URLLen);
		b.clear();
		b.putInt(this.Number);
		b.putShort(this.URLLen);
		b.put(URL);
		b.put(this.New);
		b.putShort(this.URLSRCLen);
		b.put(URLSRC);
		
		return Arrays.copyOfRange(b.array(), 0, 9+this.URLLen+this.URLSRCLen);
	}
	public int assign(byte[] data) {
		int len = 0;
		b.clear();
		b = ByteBuffer.wrap(data);
		
		this.Number = b.getInt(0);
		this.URLLen = b.getShort(4);
		System.arraycopy(data, 6, this.URL, 0, this.URLLen);
		this.New = b.get(6+this.URLLen);
		
		this.URLSRCLen = b.getShort(7+this.URLLen);
		
		System.arraycopy(data, 7+this.URLLen+2, this.URLSRC, 0, this.URLSRCLen); 
		//b.get(this.URLSRC, 7+this.URLLen+2, this.URLSRCLen);
		
		len += (7+this.URLLen+this.URLLen+2);
		return len;
	}
}