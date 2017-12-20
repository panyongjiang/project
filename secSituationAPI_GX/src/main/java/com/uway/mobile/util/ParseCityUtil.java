package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.List;

public class ParseCityUtil {
	public static final List<String> CITYLIST = new ArrayList<String>();

	static {
		CITYLIST.add("龙岩");
		CITYLIST.add("莆田");
		CITYLIST.add("福州");
		CITYLIST.add("漳州");
		CITYLIST.add("泉州");
		CITYLIST.add("宁德");
		CITYLIST.add("厦门");
		CITYLIST.add("三明");
		CITYLIST.add("南平");
		CITYLIST.add("省公司");
	}

	public static String getCityName(String value) {

		for (String city : CITYLIST) {
			if (value != null && value.contains(city)) {
				return city;
			}
		}
		return "省公司";
	}

	public static String getLongCityName(String value) {
		if (value != null && value.contains("福建")) {
			for (String city : CITYLIST) {
				if (value != null && value.contains(city)) {
					if (!city.equals("省公司")) {
						return "福建省" + city + "市移动";
					} else {
						return city;
					}
				}
			}
		}
		return null;
	}
}
