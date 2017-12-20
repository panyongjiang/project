package com.uway.mobile.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelCreate {

	/**
	 * 创建Excel
	 * 
	 * @param title
	 * 
	 * @param list
	 *            数据
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static HSSFWorkbook createExcel(String[] title, List<Object[]> list, String excelName) throws IOException {

		// 创建一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个工作表
		HSSFSheet sheet = workbook.createSheet("sheet1");
		// 添加表头行
		HSSFRow hssfRow = sheet.createRow(0);
		// 设置单元格格式居中
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 添加表头内容

		for (int i = 0; i < title.length; i++) {
			HSSFCell headCell = hssfRow.createCell(i);
			headCell.setCellValue(title[i]);
			headCell.setCellStyle(cellStyle);
		}

		// 添加数据内容
		for (int i = 0; i < list.size(); i++) {
			Object[] student = list.get(i);
			hssfRow = sheet.createRow((int) i + 1);
			for (int j = 0; j < student.length; j++) {

				// 创建单元格，并设置值
				HSSFCell cell = hssfRow.createCell(j);
				System.out.println(student[j].toString());
				cell.setCellValue(student[j].toString());
				cell.setCellStyle(cellStyle);
			}
		}

		return workbook;
	}
}