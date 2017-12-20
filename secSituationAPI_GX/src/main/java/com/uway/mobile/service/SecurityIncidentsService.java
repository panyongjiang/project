package com.uway.mobile.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.uway.mobile.common.Result;

public interface SecurityIncidentsService {
	Map<String, Object> getSecurityIncidents(Map<String, Object> paraMap) throws Exception;

	Map<String, Object> groupByTime(Map<String, Object> paraMap);

	Map<String, Object> groupByParm(Map<String, Object> paraMap);

	Result importSecurityIncidents(String fileName, InputStream in) throws Exception;

	HSSFWorkbook findAll(Map<String, Object> param) throws Exception;

	public long countSecurityIncidents(Map<String, Object> paraMap);

	public Map<String, Object> handleSecurityIncidents(Map<String, Object> paraMap);

	Map<String, Object> groupByParmProportion(Map<String, Object> paraMap);

	HSSFWorkbook getAll(Map<String, String> param);

	// Map<String, Object> groupByEffect(Map<String, Object> paraMap);

	List<Object> groupByEffect(Map<String, Object> paraMap);

}
