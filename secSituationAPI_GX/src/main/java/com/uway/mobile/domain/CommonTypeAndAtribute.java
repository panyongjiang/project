package com.uway.mobile.domain;

import java.util.HashMap;
import java.util.Map;

public class CommonTypeAndAtribute {

	public static Map<String, String> getMalwareAtribute() {
		Map<String, String> map = new HashMap<>();
		map.put("1", "恶意扣费");
		map.put("2", "隐私窃取");
		map.put("3", "远程控制");
		map.put("4", "恶意传播");
		map.put("5", "资费消耗");
		map.put("6", "系统破坏");
		map.put("7", "诱骗欺诈");
		map.put("8", "流氓行为");

		return map;
	}

	public static Map<String, String> getMalwareType() {
		Map<String, String> map = new HashMap<>();
		map.put("1", "病毒");
		map.put("2", "木马");
		map.put("3", "蠕虫");
		map.put("4", "僵尸");
		map.put("5", "后门");

		return map;
	}

}
