package com.uway.mobile.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.uway.mobile.common.Constance;

public class SignUtil {

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	/**
	 * 得到签名
	 * 
	 * @param strs
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String[] strs) throws Exception {
		String str = sort(strs);
		String sign = bytesToHexString(HmacSHA1Encrypt(str, Constance.WAF_API_KEY));
		return sign;
	}

	public static long getTime() {
		Date date = new Date();
		return date.getTime() / 1000;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		byte[] data = encryptKey.getBytes(ENCODING);
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes(ENCODING);
		// 完成 Mac 操作
		return mac.doFinal(text);
	}

	public static String sort(String[] strs) {
		String newStr = "";
		Arrays.sort(strs);
		for (String str : strs) {
			newStr += str + "&";
		}
		return newStr.substring(0, newStr.length() - 1);
	}

	public static String getDate() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return (dateFormater.format(date));
	}

	public static double getDoubleDate() {
		return (Double.valueOf(getDate()));
	}

	public static double getDoubleStart() {
		return (Double.valueOf(getStart()));
	}

	public static double getNow() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		return (Double.valueOf(dateFormater.format(date)));
	}

	public static String getStart() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -30);
		Date m = c.getTime();
		String startTime = format.format(m);
		return startTime;
	}

	public static String[] getDates() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();

		String dates[] = new String[30];
		for (int i = 0; i < 30; i++) {
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -i);
			Date m = c.getTime();
			String time = format.format(m);
			dates[i] = time;
		}
		return dates;
	}

	public static String[] getEightWeek() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		String dates[] = new String[8];
		for (int i = 0; i < 8; i++) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.add(Calendar.WEEK_OF_YEAR, -i);
			Date m = c.getTime();
			String time = format.format(m);
			dates[i] = time;
		}
		return dates;
	}

	public static String[] getFromEnd() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
		String dates[] = new String[2];
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		a.add(Calendar.WEEK_OF_YEAR, -1);
		Date f = a.getTime();
		String from = format.format(f);
		Calendar b = Calendar.getInstance();
		b.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		b.add(Calendar.WEEK_OF_YEAR, 0);
		Date e = b.getTime();
		String end = format.format(e);

		dates[0] = from;
		dates[1] = end;
		return dates;
	}

	public static String toUnicode(String str) {
		String res = "";
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			res += "\\u" + Integer.toString(chars[i], 16);
		}
		return res;
	}
}
