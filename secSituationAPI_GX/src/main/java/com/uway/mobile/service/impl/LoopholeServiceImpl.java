package com.uway.mobile.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.common.StaticFtp;
import com.uway.mobile.domain.Loophole;
import com.uway.mobile.mapper.LoopholeMapper;
import com.uway.mobile.service.LoopholeService;
import com.uway.mobile.util.ExcelCreate;
import com.uway.mobile.util.FTPClientHandler;
import com.uway.mobile.util.POIUtil;
import com.uway.mobile.util.StreamConvertUtil;

@Service("loopholedetailService")
public class LoopholeServiceImpl implements LoopholeService{
	
	private static final Logger log = Logger.getLogger(AssetInfoServiceImpl.class);
	
	@Autowired
	private LoopholeMapper loopholeMapper;

	@Override
	public void insert() throws IOException {
		FTPFile[] listFiles = null;
		log.debug("开始扫描ftp服务器目录待处理资产列表文件。");
		try {
			listFiles = FTPClientHandler.listFiles(StaticFtp.srvResource);
		} catch (Exception e) {
			log.error("扫描ftp服务器目录待处理文件异常！"+e.toString());
			e.printStackTrace();
		}
		if (listFiles != null) {
			log.debug("扫描ftp服务器目录待处理文件："+listFiles.length);
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.getName().startsWith(StaticFtp.srvResource_file)) {
					OutputStream outputStream = null;
					InputStream inputStream = null;
					try {
						log.debug("开始下载文件："+ftpFile.getName()+",文件大小："+ftpFile.getSize());
						outputStream = FTPClientHandler.downloadStream(StaticFtp.srvResource,
								ftpFile.getName());
						log.debug("完成下载文件："+ftpFile.getName()+",文件大小："+ftpFile.getSize());
						if (outputStream == null) {
							continue;
						}
						inputStream = StreamConvertUtil.parse(outputStream);
						
						importLoophole(ftpFile.getName(),inputStream,1);
						
						String sourceFilePath = StaticFtp.srvResource + ftpFile.getName();
						String destFilePath = StaticFtp.srvResource_his + ftpFile.getName();
						FTPClientHandler.renameFile(sourceFilePath, destFilePath);
						log.debug("系统安全漏洞数据汇总文件归档成功。");
					} catch (Exception e) {
						log.error("系统安全漏洞数据表同步出错:"+ftpFile.getName()+"."+e.toString());
						e.printStackTrace();
					}finally{
						if (outputStream != null) {
							outputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
					}
				}
			}
		}

	}
	
	public Result importLoophole(String filename,InputStream inputStream,int sheets) throws IOException{
		String msg ="";
		Result result = new Result();
		Map<String, Object> paraMap=new HashMap<String, Object>();
		try{
			Sheet sheet = POIUtil.getExcelFile(filename, inputStream,sheets);
			List<Loophole> list = new ArrayList<Loophole>();
			msg = "文件数据行数："+sheet.getLastRowNum();
			for (int i=2;i<sheet.getLastRowNum()-1;i++) {
				Row row =sheet.getRow(i);
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}				
				String aims=row.getCell(2).toString();
				paraMap.put("aims", aims);
				String units=row.getCell(3).toString();
				paraMap.put("units", units);
				String principal=row.getCell(4).toString();
				paraMap.put("principal", principal);
				DecimalFormat df = new DecimalFormat("0");  
				String phone=df.format(row.getCell(5).getNumericCellValue());
				paraMap.put("phone", phone);
				String level=row.getCell(6).toString();
				paraMap.put("level", level);
				Integer highscan=Integer.valueOf(row.getCell(7).toString().replaceAll("\\.0", ""));
				paraMap.put("highscan", highscan);
				Integer mediumscan=Integer.valueOf(row.getCell(8).toString().replaceAll("\\.0", ""));
				paraMap.put("mediumscan", mediumscan);
				Integer highseep=Integer.valueOf(row.getCell(9).toString().replaceAll("\\.0", ""));
				paraMap.put("highseep", highseep);
				Integer mediumseep=Integer.valueOf(row.getCell(10).toString().replaceAll("\\.0", ""));
				paraMap.put("mediumseep", mediumseep);
				Integer lowseep=Integer.valueOf(row.getCell(11).toString().replaceAll("\\.0", ""));
				paraMap.put("lowseep", lowseep);
				Integer weakpwd=Integer.valueOf(row.getCell(12).toString().replaceAll("\\.0", ""));
				paraMap.put("weakpwd", weakpwd);
				Integer rectifyhighscan=Integer.valueOf(row.getCell(13).toString().replaceAll("\\.0", ""));
				paraMap.put("rectifyhighscan", rectifyhighscan);
				Integer filinghighscan=Integer.valueOf(row.getCell(14).toString().replaceAll("\\.0", ""));
				paraMap.put("filinghighscan", filinghighscan);
				Integer rectifymediumscan=Integer.valueOf(row.getCell(15).toString().replaceAll("\\.0", ""));
				paraMap.put("rectifymediumscan", rectifymediumscan);
				Integer filingmediumscan=Integer.valueOf(row.getCell(16).toString().replaceAll("\\.0", ""));
				paraMap.put("filingmediumscan", filingmediumscan);
				Integer rectifyhighseep=Integer.valueOf(row.getCell(17).toString().replaceAll("\\.0", ""));
				paraMap.put("rectifyhighseep", rectifyhighseep);
				Integer rectifymediumseep=Integer.valueOf(row.getCell(18).toString().replaceAll("\\.0", ""));
				paraMap.put("rectifymediumseep", rectifymediumseep);
				Integer filingmediumseep=Integer.valueOf(row.getCell(19).toString().replaceAll("\\.0", ""));
				
				Integer rectifylowseep=Integer.valueOf(row.getCell(20).toString().replaceAll("\\.0", ""));	
				
				Integer rectifyweakpwd=Integer.valueOf(row.getCell(21).toString().replaceAll("\\.0", ""));	
				
				String remarks=row.getCell(25).toString();
				Date entrytime=new Date();
				
				Loophole loophole=new Loophole(aims, units, principal, phone, level, highscan, mediumscan, highseep
						, mediumseep, lowseep, weakpwd, rectifyhighscan, filinghighscan, rectifymediumscan, filingmediumscan
						, rectifyhighseep, rectifymediumseep, filingmediumseep, rectifylowseep, rectifyweakpwd, remarks,entrytime);
				
				//long loopNum=loopholeMapper.countLoophole(paraMap);
				
				/*if (loopNum<1){
					list.add(loophole);
				}*/
				list.add(loophole);
			
			}
			if (list != null && list.size() > 0) {
				loopholeMapper.insert(list);
				msg = msg + ("。系统安全漏洞数据入库记录数："+list.size());
				result.setCode(Constance.RESPONSE_SUCCESS);
			}else{
				msg = msg + ("。系统安全漏洞数据成功入库记录数：0。");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
			}
			
			
		} catch (Exception e) {
			log.error("系统安全漏洞数据表上传出错:"+e.toString());
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			msg = msg + ("。系统安全漏洞数据表上传出错:"+e.toString());
			e.printStackTrace();
		}finally{
			
			if (inputStream != null) {
				inputStream.close();
			}
		}
		result.setMsg(msg);
		return result;
	}

	@Override
	public Map<String, Object> getLoopholeSummary(Map<String, Object> paraMap) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = loopholeMapper.getLoopholeSummary(paraMap);
		if (details != null) {
			resultMap.put("details", details);
		}
		return resultMap;
	}

	
	@Override
	public Map<String, Object> Countloopholebyunits(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = loopholeMapper.Countloopholebyunits(paraMap);
		List<Map<String, Object>> newdetails=new ArrayList<Map<String,Object>>(); 
		if (details != null) {
			float sum = 0;
			for(int i=0;i<5;i++){
				sum+= Double.valueOf(details.get(i).get("risk").toString()) ;
			}			
			for(int i=0;i<5;i++){
				double risk=Double.valueOf(details.get(i).get("risk").toString());
				risk=risk/sum;
				details.get(i).put("propor", risk);	
			}
			newdetails.addAll(details.subList(0, 5));
			resultMap.put("details", newdetails);
		}
		return resultMap;
		
	}

	
	@Override
	public Map<String, Object> reskpropor(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = loopholeMapper.reskpropor(paraMap);
		if (details != null) {
			resultMap.put("details", details);
		}
		return resultMap;
	}

	@Override
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception {		
		String[] title={"责任单位","高风险","中风险","高风险整改率","中风险整改率"};
		String excelName="";
	  	List<Map<String, Object>> termList= loopholeMapper.getLoopholeSummary(paraMap);	
		
	    List<Object[]> list = new ArrayList<Object[]>();        
	    for(Map<String, Object> map:termList){
	        Object[] objects=new Object[5];
	        objects[0]=map.get("units")+"";
	        objects[1]=map.get("highrisk")+"";
	        objects[2]=map.get("mediumrisk")+"";
	        objects[3]=map.get("handlehighrisk")+"";
	        objects[4]=map.get("handlemediumrisk")+"";
	        list.add(objects);
	    }        
	    HSSFWorkbook result = ExcelCreate.createExcel(title,list,excelName);         		
	    return result;	
	
	}
	
}
