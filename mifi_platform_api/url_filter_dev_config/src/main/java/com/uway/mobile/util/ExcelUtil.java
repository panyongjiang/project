package com.uway.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelUtil {

	/**
	 * Excel 2003
	 */
	private final static String XLS = "xls";
	/**
	 * Excel 2007
	 */
	private final static String XLSX = "xlsx";
	/**
	 * 分隔符
	 */
	@SuppressWarnings("unused")
	private final static String SEPARATOR = "|";

	/**
	 * 由Excel文件的Sheet导出至StringBuffer
	 * 
	 * @param file
	 * @param sheetNum
	 * @return
	 */
	public static String exportListFromExcel(File file, int sheetNum,String[] strCol) throws IOException {
		return exportListFromExcel(new FileInputStream(file), FilenameUtils.getExtension(file.getName()), sheetNum,strCol);
	}

	/**
	 * 由Excel流的Sheet导出至StringBuffer
	 * 
	 * @param is
	 * @param extensionName
	 * @param sheetNum
	 * @return
	 * @throws IOException
	 */
	public static String exportListFromExcel(InputStream is, String extensionName, int sheetNum,String[] strCol)
			throws IOException {

		Workbook workbook = null;

		if (extensionName.toLowerCase().equals(XLS)) {
			workbook = new HSSFWorkbook(is);
		} else if (extensionName.toLowerCase().equals(XLSX)) {  
            workbook = new XSSFWorkbook(is);  
        }  

		return exportListFromExcel(workbook, sheetNum,strCol);
	}

	/**
	 * 由指定的Sheet导出至StringBuffer
	 * 
	 * @param workbook
	 * @param sheetNum
	 * @return
	 * @throws IOException
	 */
	private static String exportListFromExcel(Workbook workbook, int sheetNum,String[] strCol) {
		Sheet sheet = workbook.getSheetAt(sheetNum);
		
		int minRowIx = sheet.getFirstRowNum();
		int maxRowIx = sheet.getLastRowNum();
		// 解析公式结果  
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 
		StringBuilder sb = new StringBuilder("[");
		for (int rowIx = minRowIx + 1; rowIx <= maxRowIx; rowIx++) {
			Row row = sheet.getRow(rowIx);
			sb.append("{");
			for (int i = 0; i < strCol.length; i++) {
				Cell cell = row.getCell(new Integer(i));
				CellValue cellValue = evaluator.evaluate(cell);  
                if (cellValue == null) {  
                    continue;  
                }  
                if(cellValue.getCellTypeEnum()==CellType.NUMERIC){
                	sb.append("'").append(strCol[i]).append("':").append(cell.getNumericCellValue());
                }else{
                	sb.append("'").append(strCol[i]).append("':'").append(cell.getStringCellValue()).append("'");
                }
                if(i!=strCol.length-1){
                	sb.append(",");
                }
			}
			if(sb.toString().endsWith(",")){
				sb.delete(sb.length()-1, sb.length());
			}
			sb.append("}");
			if (rowIx != maxRowIx) {
				sb.append(",");
			}
		}
		sb.append("]");
		System.out.println(sb);
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException {
		String[] strCol = {"userName","password","name","role","accountHolder","department","manufacturer","area"};
		File f = new File("F:/a.xls");
		exportListFromExcel(f,0,strCol);
	}

}
