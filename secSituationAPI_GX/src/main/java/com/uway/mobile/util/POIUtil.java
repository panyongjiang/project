package com.uway.mobile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.json.JSONArray;

public class POIUtil {

	@SuppressWarnings("resource")
	public static Sheet getExcelFile(String filePath) throws Exception {
		Sheet sheet = null;
		if (!StringUtils.isEmpty(filePath)) {
			boolean isExcel2003 = filePath.toLowerCase().endsWith("xls") ? true : false;
			Workbook workbook = null;
			if (isExcel2003) {
				workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
			} else {
				workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
			}
			sheet = workbook.getSheetAt(0);
		}
		return sheet;
	}

	@SuppressWarnings("resource")
	public static Sheet getExcelFile(String filename, InputStream fis) throws Exception {
		Sheet sheet = null;
		if (!StringUtils.isEmpty(filename)) {
			boolean isExcel2003 = filename.toLowerCase().endsWith("xls") ? true : false;
			Workbook workbook = null;
			if (isExcel2003) {
				workbook = new HSSFWorkbook(fis);
			} else {
				workbook = new XSSFWorkbook(fis);
			}
			sheet = workbook.getSheetAt(0);
		}
		return sheet;
	}

	public static Workbook getNumberOfSheets(String filename, InputStream fis) throws Exception {
		Workbook book = null;
		try {
			if (!StringUtils.isEmpty(filename)) {
				boolean isExcel2003 = filename.toLowerCase().endsWith("xls") ? true : false;
				if (fis.available() != 0) {
					if (isExcel2003) {
						book = new HSSFWorkbook(fis);
					} else {
						book = new XSSFWorkbook(fis);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return book;
	}

	@SuppressWarnings("resource")
	public static Sheet getExcelFile(String filename, InputStream fis, int sheet) throws Exception {
		Sheet sheet2 = null;
		if (!StringUtils.isEmpty(filename)) {
			boolean isExcel2003 = filename.toLowerCase().endsWith("xls") ? true : false;
			Workbook workbook = null;
			if (isExcel2003) {
				workbook = new HSSFWorkbook(fis);
			} else {
				workbook = new XSSFWorkbook(fis);
			}
			sheet2 = workbook.getSheetAt(sheet);
		}
		return sheet2;
	}

	// 读取解析json文件 返回json数组
	public static JSONArray getJsonFile(String filePath) throws Exception {
		String laststr = "";
		FileInputStream fileInputStream = new FileInputStream(filePath);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			laststr += tempString;
		}
		reader.close();
		JSONArray jsonArray = JSONArray.fromObject(laststr);
		return jsonArray;

	}

	public static JSONArray getJsonFile(InputStream fis) throws Exception {
		String laststr = "";
		InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			laststr += tempString;
		}
		reader.close();
		JSONArray jsonArray = JSONArray.fromObject(laststr);
		return jsonArray;

	}

	@SuppressWarnings("deprecation")
	public static Date buildDate(Cell cell, String dateFormat) {
		Date result = new Date();
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			double value = cell.getNumericCellValue();
			result = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
			break;
		case XSSFCell.CELL_TYPE_STRING:// String类型
			String cellStr = cell.getStringCellValue();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			try {
				result = sdf.parse(cellStr);
			} catch (ParseException e) {
				e.printStackTrace();
				result = null;
			}
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

}
