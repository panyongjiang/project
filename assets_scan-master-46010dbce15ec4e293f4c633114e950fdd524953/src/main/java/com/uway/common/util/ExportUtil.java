package com.uway.common.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExportUtil {
	
	@SuppressWarnings("deprecation")
	public static HSSFWorkbook getExcel(String title, List<String> titleList, List<String> paramList, List<Map<String, Object>> contentList) {
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(title);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
  
        HSSFCell cell = null;
        for(int i = 0; i < titleList.size(); i++){
        	cell = row.createCell((short) i);
        	cell.setCellValue(titleList.get(i));
            cell.setCellStyle(style);
        }
  
        for(int i = 0; i < contentList.size(); i++){
        	Map<String, Object> map = contentList.get(i);
        	row = sheet.createRow((int) i + 1);
        	for(int j = 0; j < paramList.size(); j++){
        		row.createCell((short) j).setCellValue(map.get(paramList.get(j)) == null ? "" : map.get(paramList.get(j)).toString());
        	}
        }
        return wb;
	}
}
