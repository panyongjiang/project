package com.udp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


/**
 * Implementation of the Tiny Encryption Algorithm (TEA).
 * The Tiny Encryption Algorithm is one of the fastest and most efficient
 * cryptographic algorithms in existence. It was developed by David Wheeler and
 * Roger Needham at the Computer Laboratory of Cambridge University.
 *
 * See http://www.cl.cam.ac.uk/ftp/users/djw3/tea.ps
 *
 * This software was written to provide simple encryption for J2ME.
 * The homepage for this software is http://winterwell.com/software/TEA.php
 *
 * (c) 2008 Joe Halliwell <joe.halliwell@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

public class TEA {
	private final static int SUGAR = 0x9E3779B9;
	private final static int CUPS  = 32;
	private final static int UNSUGAR = 0xC6EF3720;

	private int[] S = new int[4];

	/**专用设备分析
	 * Initialize the cipher for encryption or decryption.
	 * @param key a 16 byte (128-bit) key
	 */
	public TEA(byte[] key) {
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

	/**加密
	 * Encrypt an array of bytes.
	 * @param clear the cleartext to encrypt
	 * @return the encrypted text
	 */
	public byte[] encrypt(byte[] clear) {
		int blocks = (((clear.length) + 7) / 8) + 1;
		int[] buffer = new int[2 * blocks];
		buffer[2 * blocks-1] = clear.length;
		pack(clear, buffer, 0);
		brew(buffer);
		return unpack(buffer, 0, buffer.length * 4);
	}

	/**解密-
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
		return unpack(buffer, 0, buffer[crypt.length/4-1]);
	}

	/**策划
	 * @param buf
	 */
	void brew(int[] buf) {
		assert buf.length % 2 == 1;
		int i, n;
		int v0, v1,sum;
		//i = 1;
		i = 0;
		while (i<buf.length) {
			n = CUPS;
			v0 = buf[i];
			v1 = buf[i+1];
			sum = 0;
			while (n-->0) {
				sum += SUGAR;
		        v0 += ((v1<<4) + S[0]) ^ (v1 + sum) ^ (((v1>>5)&0x07ffffff) + S[1]);
		        v1 += ((v0<<4) + S[2]) ^ (v0 + sum) ^ (((v0>>5)&0x07ffffff) + S[3]);
			}
			buf[i] = v0;
			buf[i+1] = v1;
			i+=2;
		}
	}
	
	/**未策划
	 * @param buf
	 */
	void unbrew(int[] buf) {
		assert buf.length % 2 == 1;
		int i, v0, v1, sum, n;
		i = 0;
		while (i<buf.length) {
			n = CUPS;
			v0 = buf[i]; 
			v1 = buf[i+1];
			sum = UNSUGAR;
			while (n--> 0) {	
				v1 -= ((v0<<4) + S[2]) ^ (v0 + sum) ^ (((v0>>5)&0x07ffffff) + S[3]);
		        v0 -= ((v1<<4) + S[0]) ^ (v1 + sum) ^ (((v1>>5)&0x07ffffff) + S[1]);
		        sum -= SUGAR;                      
			}
			buf[i] = v0;
			buf[i+1] = v1;
			i+=2;
		}
	}
	
	/**包体
	 * @param src
	 * @param dest
	 * @param destOffset
	 */
	void pack(byte[] src, int[] dest, int destOffset) {
		assert destOffset + (src.length / 4) <= dest.length;
		int i = 0, shift = 0;
		int j = destOffset;
		dest[j] = 0;
		//while (i<src.length) {
		while (i<src.length) {
			dest[j] |= ((src[i] & 0xff) << shift);
			if (shift==24) {
				shift = 0;
				j++;
				if (j<dest.length) dest[j] = 0;
			}else {
				shift += 8;
			}
			i++;
		}
	}
	
	/**解包
	 * @param src
	 * @param srcOffset
	 * @param destLength
	 * @return
	 */
	byte[] unpack(int[] src, int srcOffset, int destLength) {
		assert destLength <= (src.length - srcOffset) * 4;
		byte[] dest = new byte[destLength];
		int i = srcOffset;
		int count = 0;
		for (int j = 0; j < destLength; j++) {
			dest[j] = (byte) ((src[i] >> (0 + (8*count))) & 0xff);
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
		
		
		//byte[] randoms1={29, 123, 49, -109, 64, -82, -7, -40, -35, -44, 73, -123, 115, -66, -12, -55};
		
		byte[] randomss=TEA.reds("E:/pwd.txt");
		//TEA tea = new TEA(randomss);
		
		
		byte[] str=TEA.reds("E:/zz_wjm");
		byte[] str1s=StringUtil.enCodeBodys(randomss, str);
		
		//TEA.writs(str1s, "E:/java_jm");
		byte[] str1=TEA.reds("E:/tea_jm");
		
		byte[] str2=StringUtil.deCodeBodys(randomss, str1);
		
		//byte[] strs=TEA.reds("E:/zz");
		int a=0;
		System.out.println(""+new String(str2));
	}*/
	
}

