package com.uway.mobile.util;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtil {

	public static Map<String, Object> paging(long totalNum, long pageSize) {
		Map<String, Object> resultMap = new HashMap<>();
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}
		return resultMap;
	}
}