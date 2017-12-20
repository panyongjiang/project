package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CodeUtil {

	public static List<Map<String, Object>> getCode(int num) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Random random = new Random();
		for (int n = 0; n < num; n++) {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 16; i++) {
				int number = random.nextInt(base.length());
				sb.append(base.charAt(number));
			}
			map.put("code", sb.toString());
			list.add(map);
		}

		return list;
	}

}
