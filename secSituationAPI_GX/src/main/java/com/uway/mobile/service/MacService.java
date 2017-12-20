package com.uway.mobile.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.uway.mobile.common.Result;

public interface MacService {
	Map<String, Object> getMacesResources(Map<String, Object> paraMap) throws Exception;

	Map<String, Object> groupByTime(Map<String, Object> paraMap);

	Map<String, Object> groupByParm(Map<String, Object> paraMap);

	Result importMasInfos(String fileName, InputStream in) throws Exception;

	HSSFWorkbook findAll(Map<String, Object> param) throws Exception;

	public long countMasesResource(Map<String, Object> paraMap);

	HSSFWorkbook getAll(Map<String, String> param);

	Map<String, Object> groupByEffect(Map<String, Object> paraMap);

	Map<String, Object> groupByPort(Map<String, Object> paraMap);

}
