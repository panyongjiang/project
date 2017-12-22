package com.uway.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.uway.domain.NmapScanningResult;

public interface CommonService {
	
	public List<NmapScanningResult> getAssetScanDetails(String filePath) throws Exception;
	
	public HSSFWorkbook getExcelContent(String filePath) throws Exception;
}
