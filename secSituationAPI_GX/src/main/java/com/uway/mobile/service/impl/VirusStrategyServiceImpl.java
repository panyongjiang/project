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
import com.uway.mobile.domain.VirusStrategy;
import com.uway.mobile.mapper.VirusStrategyMapper;
import com.uway.mobile.service.VirusStrategyService;
import com.uway.mobile.util.ExcelCreate;
import com.uway.mobile.util.FTPClientHandler;
import com.uway.mobile.util.POIUtil;
import com.uway.mobile.util.StreamConvertUtil;

@Service("virusvstrategyService")
public class VirusStrategyServiceImpl implements VirusStrategyService{
	
	private static final Logger log = Logger.getLogger(AssetInfoServiceImpl.class);
	
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
						
						importVirusStrategy(ftpFile.getName(),inputStream);
						
						String sourceFilePath = StaticFtp.srvResource + ftpFile.getName();
						String destFilePath = StaticFtp.srvResource_his + ftpFile.getName();
						FTPClientHandler.renameFile(sourceFilePath, destFilePath);
						log.debug("防病毒策略不符合要求的终端文件归档成功。");
					} catch (Exception e) {
						log.error("防病毒策略不符合要求的终端同步出错:"+ftpFile.getName()+"."+e.toString());
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
	public Result importVirusStrategy(String filename, InputStream inputStream) throws IOException {
		// TODO Auto-generated method stub

		String msg ="";
		Result result = new Result();
		try{
			Sheet sheet = POIUtil.getExcelFile(filename, inputStream);
			List<VirusStrategy> list = new ArrayList<VirusStrategy>();
			msg = "文件数据行数："+sheet.getLastRowNum();	
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				
				String pc_name="";
				if(row.getCell(1).toString()!=""){
					pc_name=row.getCell(1).toString();
				}
				String ip="";
				if(row.getCell(2).toString()!=""){
					ip=row.getCell(2).toString();
				}
				String virusversion="";
				if(row.getCell(3).toString()!=""){
					virusversion=row.getCell(3).toString();
				}
				String clienttype="";
				if(row.getCell(4).toString()!=""){
					clienttype=row.getCell(4).toString();
				}				
				String system="";
				if(row.getCell(5).toString()!=""){
					system=row.getCell(5).toString();
				}				
				String group="";
				if(row.getCell(6).toString()!=""){
					group=row.getCell(6).toString();
				}				
				String city="";
				if(row.getCell(7).toString()!=""){
					city=row.getCell(7).toString();
				}				
				Integer untreated=0;
				if(row.getCell(8).toString().replaceAll("\\.0", "")!=""){
					untreated=Integer.valueOf(row.getCell(8).toString().replaceAll("\\.0", ""));
				}
				Date entrytime=new Date();
				
				VirusStrategy virusStrategy=new VirusStrategy(pc_name, ip, virusversion, clienttype, system, group, city, untreated, entrytime);
				
				list.add(virusStrategy);
			}
			if (list != null && list.size() > 0) {
				virusStrategyMapper.insert(list);
				msg = msg + ("防病毒策略不符合要求的终端入库记录数："+list.size());
				result.setCode(Constance.RESPONSE_SUCCESS);
			}else{
				msg = msg + ("防病毒策略不符合要求的终端成功入库记录数：0。");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
			}
			
			
		} catch (Exception e) {
			log.error("防病毒策略不符合要求的终端上传出错:"+e.toString());
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			msg = msg + ("防病毒策略不符合要求的终端上传出错:"+e.toString());
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
	public Map<String, Object> getVirusStrategy(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", virusStrategyMapper.getVirusStrategy(paraMap));
		long totalNum = virusStrategyMapper.countVirusStrategy(paraMap);
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
		// TODO Auto-generated method stub


		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = virusStrategyMapper.groupByParm(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	
	}

	@Override
	public Map<String, Object> groupByTime(Map<String, Object> paraMap) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = virusStrategyMapper.groupByTime(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	
	}

	@Override
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception {	
		
	String[] title={"序列号","计算机名","IP地址","病毒定义版本","客户端类型","操作系统","组名","责任单位","未处理次数"};
	String excelName="";
  	List<Map<String, Object>> termList= virusStrategyMapper.createExcel(paraMap);		
    List<Object[]> list = new ArrayList<Object[]>();        
    for(Map<String, Object> map:termList){
        Object[] objects=new Object[9];
        objects[0]=map.get("id")+"";
        objects[1]=map.get("pc_name")+"";
        objects[2]=map.get("ip")+"";
        objects[3]=map.get("virusversion")+"";
        objects[4]=map.get("clienttype")+"";
        objects[5]=map.get("system")+"";
        objects[6]=map.get("groups")+"";
        objects[7]=map.get("city")+"";
        objects[8]=map.get("untreated")+"";    
        list.add(objects);
    }        
    HSSFWorkbook result = ExcelCreate.createExcel(title,list,excelName);         		
    return result;	
	}

}
