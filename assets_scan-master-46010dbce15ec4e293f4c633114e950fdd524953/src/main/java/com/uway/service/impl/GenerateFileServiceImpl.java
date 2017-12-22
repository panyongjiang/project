package com.uway.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.uway.service.GenerateFileService;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {

	@Override
	public void generateScanOutput(HSSFWorkbook wb,String filePath) throws Exception {
		File genFile = new File(filePath);
		if(genFile.exists()){
			genFile.delete();
			genFile = new File(filePath);
		}
		
		OutputStream fos = null;
		try {
			fos= new FileOutputStream(genFile);
			wb.write(fos);
		} finally {
			if(fos != null) fos.close();
			if(wb != null) wb.close();
		}
	}

}
