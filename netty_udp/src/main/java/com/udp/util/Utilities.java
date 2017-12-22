package com.udp.util;

import java.nio.ByteBuffer;


/**
 * int 和 byte[] 之间互转
 * long 和 byte[] 之间互转
 * 
 * */
public class Utilities {

	//byte 数组与 int 的相互转换  
	public static int byteArrayToInt(byte[] b) {  
	    return   b[3] & 0xFF |  
	            (b[2] & 0xFF) << 8 |  
	            (b[1] & 0xFF) << 16 |  
	            (b[0] & 0xFF) << 24;  
	}  
	  
	public static byte[] intToByteArray(int a) {  
	    return new byte[] {  
	        (byte) ((a >> 24) & 0xFF),  
	        (byte) ((a >> 16) & 0xFF),     
	        (byte) ((a >> 8) & 0xFF),     
	        (byte) (a & 0xFF)  
	    };  
	}  
	
	private static ByteBuffer buffer = ByteBuffer.allocate(8);   
	//byte 数组与 long 的相互转换  
	public static byte[] longToBytes(long x) {  
		buffer.putLong(0, x);  
		return buffer.array();  
	}
	public static long bytesToLong(byte[] bytes) {  
		buffer.put(bytes, 0, bytes.length);  
		buffer.flip();//need flip   
		return buffer.getLong();  
	} 
	
	 /**
	  * 将int型数据转换成标准的IP地址格式
	 * @param a
	 * @return
	 */
	public static String fmtInt2Ip(int a) {  
		 byte[] bs=intToByteArray(a);
		 return (bs[0]&0xff)+"."+(bs[1]&0xff)+"."+(bs[2]&0xff)+"."+(bs[3]&0xff);  
	}
	
}
