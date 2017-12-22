package com.udp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class StringUtil {

	/**利用MD5进行加密
	 　　* @param str  待加密的字符串
	　　* @return  加密后的字符串
	　　* @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
	*/
	public static byte[] getMD5(byte[] str) throws NoSuchAlgorithmException{		
		//确定计算方法
		MessageDigest md5=MessageDigest.getInstance("MD5");
		return md5.digest(str);
	}
	
	public static byte[] getSBytes(){
		Random random = new Random();
		byte[] bytes = new byte[16];
		for (int i = 0; i < bytes.length; i++)
		bytes[i] = (byte)random.nextInt(256);
		return bytes;
	}
	
	//字符串计算公式 "24*60*60" or "43*(2 + 1.4)+2*32/(3-2.1)"
	public static int caucalStringToInt(String str){
		int gc=0;
		ScriptEngineManager manager = new ScriptEngineManager();  
        ScriptEngine engine = manager.getEngineByName("js");  
        try {
			Object result = engine.eval(str);
			gc=Integer.parseInt(result.toString());
			return gc;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return gc;
	}
	
	//解密报体
	public static byte[] deCodeBodys(TEA tea,byte[] strRandom,byte[] bt) throws Exception{
		if(tea==null) tea = new TEA(getMD5(copyByte(Constants.OTHERS.getBytes(),strRandom)));
		//TEA tea = new TEA(strRandom);
		return tea.decrypt(bt);
	}
	
	//加密报体
	public static byte[] enCodeBodys(TEA tea,byte[] strRandom,byte[] bt) throws Exception{
		if(tea==null) tea = new TEA(getMD5(copyByte(Constants.OTHERS.getBytes(),strRandom)));
		//TEA tea = new TEA(strRandom);
		return tea.encrypt(bt);
	}
	
	public static byte[] copyByte(byte[] data1,byte[] data2){
		byte[] data3 = new byte[data1.length + data2.length];  
		System.arraycopy(data1, 0, data3, 0, data1.length);  
		System.arraycopy(data2, 0, data3, data1.length, data2.length);  
		return data3;  
	}
}
