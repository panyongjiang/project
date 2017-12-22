package com.uway.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface GenerateFileService {
	
	public void generateScanOutput(HSSFWorkbook wb,String filePath) throws Exception;
}
