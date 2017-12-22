package udp;

import java.nio.ByteBuffer;

public class ack_url_check {
	int Number;
	int Result;
	byte Type;
	
	ByteBuffer b;
	
	public ack_url_check() {
		this.b = ByteBuffer.allocate(9);
	}
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public int getResult() {
		return Result;
	}
	public void setResult(int result) {
		Result = result;
	}
	public byte getType() {
		return Type;
	}
	public void setType(byte type) {
		Type = type;
	}
	public byte[] toData() {
		//ByteBuffer b = ByteBuffer.allocate(9);
		b.putInt(this.Number);
		b.putInt(this.Result);
		b.put(this.Type);
		return b.array();
	}
	
	public int assign(byte[] data) {
		int len = 0;
		b.clear();
		b = ByteBuffer.wrap(data);
		
		this.Number = b.getInt(0);
		this.Result = b.getInt(4);
		this.Type = b.get(8);
		
		len += 9;
		
		return len;
	}
	
}