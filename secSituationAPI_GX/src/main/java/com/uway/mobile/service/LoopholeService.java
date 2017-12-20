package com.uway.mobile.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface LoopholeService {
	
	void insert() throws IOException;
	public Result importLoophole(String filename,InputStream inputStream,int sheets) throws IOException;
	//获取汇总信息
	public Map<String, Object> getLoopholeSummary(Map<String, Object> paraMap);
	
	public Map<String, Object> Countloopholebyunits(Map<String, Object> paraMap);
	
	public Map<String, Object> reskpropor(Map<String, Object> paraMap);
	
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception;
}
