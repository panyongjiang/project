package com.uway.mobile.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 我 用于匹配的正则表达式为 ：([1-9]\d*\.?\d*)|(0\.\d*[1-9]) ( [1-9] ：匹配1~9的数字；
 *         \d ：匹配数字，包括0~9；  ：紧跟在 \d 之后，表明可以匹配零个及多个数字； \. ：匹配小数点； ? ：紧跟在 \.
 *         之后，表明可以匹配零个或一个小数点； 0 ：匹配一个数字0； )
 *         其中的 [1-9]\d*\.?\d* 用以匹配诸如：1、23、34.0、56.78 之类的非负的整数和浮点数；
 *         其中的 0\.\d*[1-9] 用以匹配诸如：0.1、0.23、0.405 之类的非负浮点数；
 */
public class TestLong {
	public static void main(String[] args) {
		String str = "117.141.104.20为防城港公司管辖地址";
		String regEx = "\\d+\\.\\d+\\.\\d+\\.\\d+";
		Pattern pattern = Pattern.compile(regEx);

		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}
}

class TestJava {
	public static void main(String[] args) {
		Integer i = new Integer(100);//
		Integer e = 100;
		int f = 100;
		System.out.println(e.equals(i));// true：equals比较引用对应的值
		System.out.println(e == f);// true
		System.out.println(e == i);// false==号比较的是引用的地址值

		int[] in = new int[] { 1, 1, 1, 1, 1 };
		int length = in.length;
		System.out.println(length);
		String s = new String();
		int length2 = s.length();
		System.out.println(length2);
		tv tv = new tv();
		Thread t = new Thread(tv);
		t.start();
	}
}

class tv extends Thread {
	private int num = 100;

	public void run() {
		while (true) {
			synchronized (tv.class) {
				for (int i = num; i > 0; i--) {
					System.out.println(i);
				}

			}
		}
	}
}
