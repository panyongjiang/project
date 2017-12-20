package com.uway.mobile.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface AcceptanceProjectService {
	
	void insert() throws IOException;
	
	public Result importAcceptance(String filename,InputStream inputStream,int sheets) throws IOException;
	
	Map<String, Object> getAcceptanceAll(Map<String, Object> paraMap) throws Exception;
	
	Map<String, Object> getAcceptance(Map<String, Object> paraMap) throws Exception;
	
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception;
}
