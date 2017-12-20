package com.uway.mobile.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface TerminalService {
	
	void insert() throws IOException;
	
	public Result importTerminal(String filename,InputStream inputStream) throws IOException;
	
	Map<String, Object> getTerminal(Map<String, Object> paraMap) throws Exception;
	
	Map<String, Object> getTerminalAll(Map<String, Object> paraMap) throws Exception;

	
	Map<String, Object> groupByParm(Map<String, Object> paraMap);
	
	Map<String, Object> groupByTime(Map<String, Object> paraMap);

	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception;
	

}
