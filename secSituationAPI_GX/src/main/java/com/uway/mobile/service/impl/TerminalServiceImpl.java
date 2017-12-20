package com.uway.mobile.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.uway.mobile.domain.Terminal;
import com.uway.mobile.mapper.MistakePatchMapper;
import com.uway.mobile.mapper.TerminalMapper;
import com.uway.mobile.mapper.VirusStrategyMapper;
import com.uway.mobile.service.TerminalService;
import com.uway.mobile.util.ExcelCreate;
import com.uway.mobile.util.FTPClientHandler;
import com.uway.mobile.util.POIUtil;
import com.uway.mobile.util.StreamConvertUtil;

@Service("terminalService")
public class TerminalServiceImpl implements TerminalService{

	private static final Logger log = Logger.getLogger(AssetInfoServiceImpl.class);
	
	@Autowired
	private TerminalMapper terminalMapper;
	@Autowired
	private MistakePatchMapper mistakePatchMapper;
	@Autowired
	private VirusStrategyMapper virusStrategyMapper;
	
	@Override
	public void insert() throws IOException {
		// TODO Auto-generated method stub

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
						importTerminal(ftpFile.getName(),inputStream);						
						String sourceFilePath = StaticFtp.srvResource + ftpFile.getName();
						String destFilePath = StaticFtp.srvResource_his + ftpFile.getName();
						FTPClientHandler.renameFile(sourceFilePath, destFilePath);
						log.debug("终端命名情况文件归档成功。");
					} catch (Exception e) {
						log.error("终端命名情况同步出错:"+ftpFile.getName()+"."+e.toString());
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

	@Override
	public Result importTerminal(String filename, InputStream inputStream) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Object> parMap =new HashMap<String, Object>();
		String msg ="";
		Result result = new Result();
		try{
			Sheet sheet = POIUtil.getExcelFile(filename, inputStream);
			List<Terminal> list = new ArrayList<Terminal>();
			msg = "文件数据行数："+sheet.getLastRowNum();			
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}			
				String user=row.getCell(1).toString();
				parMap.put("user", user);
				String accounts=row.getCell(2).toString();
				parMap.put("accounts", accounts);
				String pc_name=row.getCell(3).toString();
				parMap.put("pc_name", pc_name);
				String ip=row.getCell(4).toString();
				parMap.put("ip", ip);
				String mac=row.getCell(5).toString();
				parMap.put("mac", mac);
				String city=row.getCell(6).toString();
				parMap.put("city", city);
				Integer untreated=Integer.valueOf(row.getCell(7).toString().replaceAll("\\.0", ""));
				parMap.put("untreated", untreated);
				String status=row.getCell(8).toString();
				parMap.put("status", status);
				String remarks=row.getCell(9).toString();
				Date entrytime=new Date();
				
				Terminal terminal=new Terminal(user, accounts, pc_name, ip, mac, city, untreated, status, remarks, entrytime);
				
				//long terNum=terminalMapper.countTerminal(parMap);
				
				/*if(terNum<1){
					list.add(terminal);
				}*/
				list.add(terminal);
			}
			if (list != null && list.size() > 0) {
				terminalMapper.insert(list);
				msg = msg + ("终端命名情况入库记录数："+list.size());
				result.setCode(Constance.RESPONSE_SUCCESS);
			}else{
				msg = msg + ("终端命名情况数据成功入库记录数：0。");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
			}
			
			
		} catch (Exception e) {
			log.error("终端命名情况数据表上传出错:"+e.toString());
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			msg = msg + ("终端命名情况数据表上传出错:"+e.toString());
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
	public Map<String, Object> getTerminal(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", terminalMapper.getTerminal(paraMap));
		long totalNum = terminalMapper.countTerminal(paraMap);
		resultMap.put("total_num", totalNum);
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}

		return resultMap;
	}

	
	@Override
	public Map<String, Object> groupByParm(Map<String, Object> paraMap) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = terminalMapper.groupByParm(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	
	public Map<String, Object> getTerminalAll(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> finishMap =paraMap;
		Map<String, Object> nofinishMap=paraMap;

		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", terminalMapper.getTerminal(paraMap));
		resultMap.put("mistakepath", mistakePatchMapper.countMistakePatch(paraMap));
		resultMap.put("virusstrategy", virusStrategyMapper.countVirusStrategy(paraMap));
		long totalNum = terminalMapper.countTerminal(paraMap);
		finishMap.put("status", "已完成");
		resultMap.put("finish", terminalMapper.countTerminal(finishMap));
		nofinishMap.put("status", "未完成");
		resultMap.put("nofinish", terminalMapper.countTerminal(nofinishMap));
		resultMap.put("total_num", totalNum);
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}

		return resultMap;
	}

	@Override
	public Map<String, Object> groupByTime(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = terminalMapper.groupByTime(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception {	
			
		String[] title={"序号","用户名","账号","主机名","登录IP","MAC","责任单位","未处理累计次数","整改情况"};
		String excelName="";
	  	List<Map<String, Object>> termList= terminalMapper.createExcel(paraMap);		
	    List<Object[]> list = new ArrayList<Object[]>();        
	    for(Map<String, Object> map:termList){
	        Object[] objects=new Object[9];
	        objects[0]=map.get("id")+"";
	        objects[1]=map.get("user")+"";
	        objects[2]=map.get("accounts")+"";
	        objects[3]=map.get("pc_name")+"";
	        objects[4]=map.get("ip")+"";
	        objects[5]=map.get("mac")+"";
	        objects[6]=map.get("city")+"";
	        objects[7]=map.get("untreated")+"";
	        objects[8]=map.get("status")+"";    
	        list.add(objects);
	    }        
	    HSSFWorkbook result = ExcelCreate.createExcel(title,list,excelName);         		
	    return result;	
		}



}
