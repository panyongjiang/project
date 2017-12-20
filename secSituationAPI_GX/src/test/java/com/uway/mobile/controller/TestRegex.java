package com.uway.mobile.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
	public static void main(String[] args) {
		String str = "117.141.104.20为 防城港 公司管 辖地址";
		String regEx = "\\d+\\.\\d+\\.\\d+\\.\\d+";
		Pattern pattern = Pattern.compile(regEx);

		Matcher matcher = pattern.matcher(str);

		String ip = "";
		if (matcher.find()) {
			ip = matcher.group();
		}

		String address = str.replaceAll(ip, "");
		address = address.replaceAll(" ", "");
		System.out.println(ip + " " + address.substring(1, address.length()));

	}
}
