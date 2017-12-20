package com.uway.mobile.domain;

import java.util.HashMap;
import java.util.Map;

public class ECity {

	public static Map<String, String> getCity() {
		Map<String, String> map = new HashMap<>();
		map.put("13001", "三明市");
		map.put("13002", "南平市");
		map.put("13003", "厦门市");
		map.put("13004", "宁德市");
		map.put("13005", "泉州市");
		map.put("13006", "漳州市");
		map.put("13007", "福州市");
		map.put("13008", "莆田市");
		map.put("13009", "龙岩市");

		return map;
	}

}
