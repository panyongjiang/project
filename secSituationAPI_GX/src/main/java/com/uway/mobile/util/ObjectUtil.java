package com.uway.mobile.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ObjectUtil {
	/**
	 * 判断map中的key对应的value是否为空 注意此方法仅对Map<String, String>，或能够转为Map<String,
	 * String>的对象有效
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static boolean isEmpty(Map<String, Object> map, String key) {
		if (map == null) {
			return true;
		} else {
			if (map.get(key) == null) {
				return true;
			} else {
				String value = map.get(key).toString();
				if (StringUtils.isEmpty(value)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
