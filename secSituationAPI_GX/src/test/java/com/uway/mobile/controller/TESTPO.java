package com.uway.mobile.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TESTPO {
	private static final DataFormatter FORMATTER = new DataFormatter();

	public static void main(String[] args) {

		String filePath = "D:\\广西移动数据\\1．广西移动2月份互联网一般安全事件汇总.xls";
		int sheetIndex = 5;
		Map<String, String> map = new HashMap<>();

		String[] val = getExcelValue(filePath, sheetIndex).split(",");

		for (int i = 0; i < val.length; i++) {
			map.put("key" + i, val[i]);

			System.out.println(val[i]);
		}
		/*
		 * String string = map.get("key1"); String category = String event =
		 * String level = Date occurencetime = String eventDescription = String
		 * unitAddess = String involveContent = String briefpass = String
		 * harmandeffect = String measures = String remarks =
		 * 
		 * SecurityIncidents securityIncidents = new SecurityIncidents(null,
		 * category, event, level, occurencetime, eventDescription, unitAddess,
		 * involveContent, briefpass, harmandeffect, measures, remarks);
		 * list.add(securityIncidents);
		 */

	}

	private static String getCellContent(Cell cell) {
		return FORMATTER.formatCellValue(cell);
	}

	public static boolean isMergedRegion(Sheet sheet, Cell cell) {
		// 得到一个sheet中有多少个合并单元格
		int sheetmergerCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetmergerCount; i++) {
			// 得出具体的合并单元格
			CellRangeAddress ca = sheet.getMergedRegion(i);
			// 得到合并单元格的起始行, 结束行, 起始列, 结束列
			int firstC = ca.getFirstColumn();
			int lastC = ca.getLastColumn();
			int firstR = ca.getFirstRow();
			int lastR = ca.getLastRow();
			// 判断该单元格是否在合并单元格范围之内, 如果是, 则返回 true
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getMergedRegionValue(Sheet sheet, Cell cell) {
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 便利合并单元格
		for (int i = 1; i < sheetmergerCount; i++) {
			// 获得合并单元格
			CellRangeAddress ca = sheet.getMergedRegion(i);
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			int firstC = ca.getFirstColumn();
			int firstR = ca.getFirstRow();

			if (cell.getColumnIndex() == firstC && cell.getRowIndex() == firstR) {

				return "第" + (cell.getRowIndex() + 1) + "行 第" + (cell.getColumnIndex() + 1) + "列 的内容是： "
						+ getCellContent(cell) + ",";

				// return getCellContent(cell) + ",";
			}

		}
		return "";
	}

	@SuppressWarnings("resource")
	private static String getExcelValue(String filePath, int sheetIndex) {
		String value = "";
		try {
			// 创建对Excel工作簿文件
			Workbook book = null;
			try {
				book = new XSSFWorkbook(new FileInputStream(filePath));
			} catch (Exception ex) {
				book = new HSSFWorkbook(new FileInputStream(filePath));
			}

			Sheet sheet = book.getSheetAt(sheetIndex);
			// 获取到Excel文件中的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			// System.out.println("rows:" + rows);
			// 遍历行

			for (int i = 1; i < rows; i++) {
				Row row = sheet.getRow(i);
				// 行不为空
				if (row != null) {
					// 获取到Excel文件中的所有的列
					int cells = row.getPhysicalNumberOfCells();
					// System.out.println("cells:" + cells);

					// 遍历列
					for (int j = 0; j < cells; j++) {
						// 获取到列的值
						Cell cell = row.getCell(j);
						if (cell != null) {
							// 合并列和行数
							if (isMergedRegion(sheet, cell)) {
								value += getMergedRegionValue(sheet, cell);
							} else {
								value += "第" + (i + 1) + "行 第" + (j + 1) + "列的内容是： " + getCellContent(cell) + ",";

							}
						}
					}

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;

	}
}
