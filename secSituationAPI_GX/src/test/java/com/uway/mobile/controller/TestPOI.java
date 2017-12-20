/*
 * package com.uway.mobile.controller;
 * 
 * import java.io.File; import java.io.FileInputStream; import
 * java.io.InputStream; import java.util.ArrayList; import java.util.Date;
 * import java.util.List;
 * 
 * import org.apache.poi.hssf.usermodel.HSSFWorkbook; import
 * org.apache.poi.ss.usermodel.Sheet;
 * 
 * import com.uway.mobile.domain.SecurityIncidents;
 * 
 * public class TestPOI {
 * 
 * public static void main(String[] args) throws Exception { String pathname =
 * "D:\\广西移动数据\\1．广西移动2月份互联网一般安全事件汇总.xls"; InputStream in = new
 * FileInputStream(new File(pathname)); // 得到整个excel对象 HSSFWorkbook workbook =
 * new HSSFWorkbook(in); // 获取整个excel有多少个sheet int sheets =
 * workbook.getNumberOfSheets(); System.out.println(sheets); // 遍历第一个sheet for
 * (int i = 5; i < sheets; i++) { List<SecurityIncidents> list = new
 * ArrayList<SecurityIncidents>(); // 获得页数 Sheet sheet = workbook.getSheetAt(i);
 * // 获得总行数 int rowNum = sheet.getLastRowNum(); for (int j = 0; j < rowNum; j++)
 * { // 获取行数对应列数值 String category = sheet.getRow(1).getCell(1).toString();
 * String event = sheet.getRow(1).getCell(4).toString(); String level =
 * sheet.getRow(2).getCell(1).toString(); Date occurencetime =
 * sheet.getRow(2).getCell(4).getDateCellValue(); String eventDescription =
 * sheet.getRow(3).getCell(1).toString(); String unitAddess =
 * sheet.getRow(4).getCell(1).toString(); String involveContent =
 * sheet.getRow(5).getCell(1).toString(); String briefpass =
 * sheet.getRow(6).getCell(1).toString(); String harmandeffect =
 * sheet.getRow(7).getCell(1).toString(); String measures =
 * sheet.getRow(8).getCell(1).toString(); String remarks =
 * sheet.getRow(9).getCell(1).toString();
 * 
 * SecurityIncidents securityIncidents = new SecurityIncidents(category, event,
 * level, occurencetime, eventDescription, unitAddess, involveContent,
 * briefpass, harmandeffect, measures, remarks); list.add(securityIncidents); }
 * int size = list.size(); System.out.println(size);
 * 
 * }
 * 
 * } }
 */