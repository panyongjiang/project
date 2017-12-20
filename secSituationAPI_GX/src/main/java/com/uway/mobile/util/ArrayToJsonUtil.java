package com.uway.mobile.util;

import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;

public class ArrayToJsonUtil {

	public static StringEntity toJson(String[] paras) throws Exception {
		JSONObject jsonParam = new JSONObject();
		for (int i = 0; i < paras.length; i++) {
			String[] para = paras[i].split("=");
			if (para.length >= 2) {
				jsonParam.put(para[0], para[1]);
			}
		}

		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		return entity;
	}
}
