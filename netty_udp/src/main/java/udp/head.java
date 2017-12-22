package udp;

import java.nio.ByteBuffer;

public class head {
	ByteBuffer b;
	
	short Length;
	short ZLength;
	short Command;
	long PlugID; // devid
	short EncryptSeq;
	
	final int REQ_URL_CHECK = 2002;
	final int ACK_URL_CHECK = 2003;
	final int NTF_URL_EVIL  = 2004;

	public head() {
		this.b = ByteBuffer.allocate(16);
	}
	public short getLength() {
		return Length;
	}

	public void setZLength(short Zlength) {
		ZLength = Zlength;
	}
	public short getZLength() {
		return ZLength;
	}

	public void setLength(short length) {
		Length = length;
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
		return EncryptSeq;
	}
	public void setEncryptSeq(short encryptSeq) {
		EncryptSeq = encryptSeq;
	}
	public byte[] toData() {
		this.b.clear();
		this.b.putShort(this.Length);		
		this.b.putShort(this.Command);
		this.b.putLong(this.PlugID);
		this.b.putShort(this.EncryptSeq);
		this.b.putShort(this.ZLength);
		return this.b.array();

	}
	//计算长度
	public int assign(byte[] data) {
		int len = 0;
		b.clear();
		b = ByteBuffer.wrap(data);
		
		this.Length = b.getShort(0);
		this.Command = b.getShort(2);
		this.PlugID = b.getLong(4);
		this.EncryptSeq = b.getShort(12);
		this.ZLength = b.getShort(14);
		
		len += 16;
		
		return len;
	}
	
	/*public static void main(String args[]) {
		head h = new head();

		h.setLength((short) 0x1234);
		h.setCommand((short) 0x2022);
		h.setPlugID(0xaabbccdd);
		byte[] a = h.toData();

		ByteBuffer s = ByteBuffer.wrap(h.toData());
		System.out.println(" " + s.getShort(0) + " " + s.getShort(2) + " " + s.getInt(4));

	}*/

}