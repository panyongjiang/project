package com.udp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class TEAS {

	private final static int SUGAR = 0x9E3779B9;
	private final static int CUPS  = 32;
	private final static int UNSUGAR = 0xC6EF3720;

	private int[] S = new int[4];

	/**
	 * Initialize the cipher for encryption or decryption.
	 * @param key a 16 byte (128-bit) key
	 */
	public TEAS(byte[] key) {
		if (key == null)
			throw new RuntimeException("Invalid key: Key was null");
		if (key.length < 16)
			throw new RuntimeException("Invalid key: Length was less than 16 bytes");
		for (int off=0, i=0; i<4; i++) {
			S[i] = ((key[off++] & 0xff)) |
			((key[off++] & 0xff) <<  8) |
			((key[off++] & 0xff) << 16) |
			((key[off++] & 0xff) << 24);
		}
	}

	/**
	 * Encrypt an array of bytes.
	 * @param clear the cleartext to encrypt
	 * @return the encrypted text
	 */
	public byte[] encrypt(byte[] clear) {
		int blocks = (((clear.length) + 7) / 8) + 1;
		int[] buffer = new int[2 * blocks];
		for(int i=0;i<buffer.length;i++){
			buffer[i] = 0;
		}
		buffer[2 * blocks-1] = clear.length;
		pack(clear, buffer, 0);
		brew(buffer);
		return unpack(buffer, 0, buffer.length * 4);
	}

	/**
	 * Decrypt an array of bytes.
	 * @param ciper the cipher text to decrypt
	 * @return the decrypted text
	 */
	public byte[] decrypt(byte[] crypt) {
		assert crypt.length % 4 == 0;
		assert (crypt.length / 4) % 2 == 1;
		int[] buffer = new int[crypt.length / 4];
		pack(crypt, buffer, 0);
		unbrew(buffer);
		return unpack(buffer, 1, buffer[0]);
	}

	void brew(int[] buf) {
		assert buf.length % 2 == 1;
		int i, v0, v1, sum, n;
		//i = 1;
		i = 0;
		while (i<buf.length) {
			n = CUPS;
			v0 = buf[i];
			v1 = buf[i+1];
			sum = 0;
			while (n-->0) {
				sum += SUGAR;
		        v0 += ((v1<<4) + S[0]) ^ (v1 + sum) ^ ((v1>>5) + S[1]);
		        v1 += ((v0<<4) + S[2]) ^ (v0 + sum) ^ ((v0>>5) + S[3]); 
			}
			buf[i] = v0;
			buf[i+1] = v1;
			i+=2;
		}
	}
	
	void unbrew(int[] buf) {
		assert buf.length % 2 == 1;
		int i, v0, v1, sum, n;
		i = 1;
		while (i<buf.length) {
			n = CUPS;
			v0 = buf[i]; 
			v1 = buf[i+1];
			sum = UNSUGAR;
			while (n--> 0) {
				v1  -= ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
				v0  -= ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
				sum -= SUGAR;				
			}
			buf[i] = v0;
			buf[i+1] = v1;
			i+=2;
		}
	}
	
	void pack(byte[] src, int[] dest, int destOffset) {
		assert destOffset + (src.length / 4) <= dest.length;
		int i = 0, shift = 24;
		int j = destOffset;
		dest[j] = 0;
		//while (i<src.length) {
		while (i<src.length-1) {
			dest[j] |= ((src[i] & 0xff) << shift);
			if (shift==0) {
				shift = 24;
				j++;
				if (j<dest.length) dest[j] = 0;
			}
			else {
				shift -= 8;
			}
			i++;
		}
	}
	
	byte[] unpack(int[] src, int srcOffset, int destLength) {
		assert destLength <= (src.length - srcOffset) * 4;
		byte[] dest = new byte[destLength];
		int i = srcOffset;
		int count = 0;
		for (int j = 0; j < destLength; j++) {
			dest[j] = (byte) ((src[i] >> (24 - (8*count))) & 0xff);
			count++;
			if (count == 4) {
				count = 0;
				i++;
			}
		}
		return dest;
	}
	
	public static byte[] readFileByBytes(String fileName) {

		InputStream in = null;
		byte[] tempbytes = new byte[248];
		try {
			// 一次读多个字节
			
			int byteread = 0;
			in = new FileInputStream(fileName);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
			return tempbytes;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
		return tempbytes;
	}
	
	public static byte[] reds(String filename) throws IOException{
		RandomAccessFile f = new RandomAccessFile(filename, "r");
	    byte[] b = new byte[(int)f.length()];
	    //将文件按照字节方式读入到字节缓存中
	    f.read(b);
	    f.close();
	    return b;
	}
	
	public static void writs(byte[] b,String filename) throws IOException{
		RandomAccessFile f = new RandomAccessFile(filename, "rw");
	    //将文件按照字节方式读入到字节缓存中
	    f.write(b);
	    f.close();
	}
	
	/*public static void main(String[] args) throws Exception{
		byte[] wjm=TEA.reds("E:/wjm");
		System.out.println(new String(wjm));
		byte[] randomss=TEA.reds("E:/pwd.txt");
		
		byte[] sgs=TEA.reds("E:/cc");
		byte[] bs=StringUtil.deCodeBodys(randomss, sgs);
		System.out.println(new String(bs));
	}*/
	
	/*public static void main(String[] args) throws Exception{
		
		
		byte[] randoms1={29, 123, 49, -109, 64, -82, -7, -40, -35, -44, 73, -123, 115, -66, -12, -55};
		
		byte[] randomss=TEA.reds("E:/pwd.txt");

		byte[] str=TEA.reds("E:/wjm");
		
		byte[] strs=StringUtil.enCodeBodys(randoms1, str);
		byte[] sgs=TEA.reds("E:/cc");
		
		byte[] bs=StringUtil.deCodeBodys(randomss, strs);
		
		System.out.println(new String(bs));
	}*/
	
	/*public static void main(String[] args) throws Exception{
		
		
		byte[] randoms1={29, 123, 49, -109, 64, -82, -7, -40, -35, -44, 73, -123, 115, -66, -12, -55};
		
		byte[] randomss=TEA.reds("E:/pwd.txt");
		//TEA tea = new TEA(randomss);
		
		
		byte[] str=TEAS.reds("E:/zz_wjm");
		byte[] wjm=new byte[8];
		System.arraycopy(str, 0, wjm, 0,8);
		byte[] str1=StringUtil.enCodeBodys(null,randomss, str);
		//TEA.writs(str1, "E:/java_zz_jm");
		System.out.println("str1siz=="+str1.length);
		int[] dest=new int[4];
		
		tea.pack(wjm, dest, 0);

		byte[] sg1=Utilities.intToByteArray(dest[0]);
		byte[] sg2=Utilities.intToByteArray(dest[1]);
		byte[] sg3=Utilities.intToByteArray(dest[2]);
		byte[] sg4=Utilities.intToByteArray(dest[3]);
		
		byte[] sgs=StringUtil.copyByte(sg1, sg2);
		byte[] sgs1=StringUtil.copyByte(sgs, sg3);
		byte[] sgs2=StringUtil.copyByte(sgs1, sg4);
		
		byte[] strs=TEAS.reds("E:/zz");
		int a=0;
		System.out.println(""+a);
		//byte[] strs=StringUtil.enCodeBodys(randoms1, str);
		byte[] sgs=TEA.reds("E:/cc");
		
		byte[] bs=StringUtil.deCodeBodys(randomss, strs);
		
		System.out.println(new String(bs));
	}*/

}
