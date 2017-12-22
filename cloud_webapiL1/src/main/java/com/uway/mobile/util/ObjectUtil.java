package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ObjectUtil {
	/**
	 * 判断map中的key对应的value是否为空
	 * 注意此方法仅对Map<String, String>，或能够转为Map<String, String>的对象有效
	 * @param map
	 * @param key
	 * @return
	 */
	public static boolean isEmpty(Map<String, Object> map, String key){
		if(map == null){
			return true;
		}else{
			if(map.get(key) == null){
				return true;
			}else{
				String value = map.get(key).toString();
				if(StringUtils.isEmpty(value)){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Integer> sortMap(Map oldMap) {
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<java.lang.String, Integer> arg0,
					Entry<java.lang.String, Integer> arg1) {
				return arg1.getValue() - arg0.getValue();
			}
		});
		Map newMap = new LinkedHashMap();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}
}
