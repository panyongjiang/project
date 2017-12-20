package com.uway.mobile.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.uway.mobile.domain.AcceptanceProject;
import com.uway.mobile.mapper.AcceptanceProjectMapper;
import com.uway.mobile.service.AcceptanceProjectService;
import com.uway.mobile.util.ExcelCreate;
import com.uway.mobile.util.FTPClientHandler;
import com.uway.mobile.util.POIUtil;
import com.uway.mobile.util.StreamConvertUtil;

@Service("acceptanceservice")
public class AcceptanceServiceImpl implements AcceptanceProjectService {

	private static final Logger log = Logger.getLogger(AssetInfoServiceImpl.class);

	@Autowired
	private AcceptanceProjectMapper acceptanceProjectMapper;

	@Override
	public void insert() throws IOException {
		// TODO Auto-generated method stub

		FTPFile[] listFiles = null;
		log.debug("开始扫描ftp服务器目录待处理资产列表文件。");
		try {
			listFiles = FTPClientHandler.listFiles(StaticFtp.srvResource);
		} catch (Exception e) {
			log.error("扫描ftp服务器目录待处理文件异常！" + e.toString());
			e.printStackTrace();
		}
		if (listFiles != null) {
			log.debug("扫描ftp服务器目录待处理文件：" + listFiles.length);
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.getName().startsWith(StaticFtp.srvResource_file)) {
					OutputStream outputStream = null;
					InputStream inputStream = null;
					try {
						log.debug("开始下载文件：" + ftpFile.getName() + ",文件大小：" + ftpFile.getSize());
						outputStream = FTPClientHandler.downloadStream(StaticFtp.srvResource, ftpFile.getName());
						log.debug("完成下载文件：" + ftpFile.getName() + ",文件大小：" + ftpFile.getSize());
						if (outputStream == null) {
							continue;
						}
						inputStream = StreamConvertUtil.parse(outputStream);

						importAcceptance(ftpFile.getName(), inputStream, 1);

						String sourceFilePath = StaticFtp.srvResource + ftpFile.getName();
						String destFilePath = StaticFtp.srvResource_his + ftpFile.getName();
						FTPClientHandler.renameFile(sourceFilePath, destFilePath);
						log.debug("系统安全漏洞数据汇总文件归档成功。");
					} catch (Exception e) {
						log.error("系统安全漏洞数据表同步出错:" + ftpFile.getName() + "." + e.toString());
						e.printStackTrace();
					} finally {
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

	public Result importAcceptance(String filename, InputStream inputStream, int sheets) throws IOException {
		String msg = "";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Result result = new Result();
		try {
			Sheet sheet = POIUtil.getExcelFile(filename, inputStream, sheets);
			List<AcceptanceProject> list = new ArrayList<AcceptanceProject>();
			msg = "文件数据行数：" + sheet.getLastRowNum();
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				String unit = row.getCell(1).toString();
				paraMap.put("unit", unit);
				String time = get_time(row.getCell(2).toString());
				// String time=row.getCell(2).toString();
				paraMap.put("time", time);
				String numbering = row.getCell(3).toString();
				paraMap.put("numbering", numbering);
				String project = row.getCell(4).toString();
				paraMap.put("project", project);
				String billingmsg = row.getCell(5).toString();
				paraMap.put("billingmsg", billingmsg);
				String contact = row.getCell(6).toString();
				paraMap.put("contact", contact);
				String sign = row.getCell(7).toString();
				paraMap.put("sign", sign);
				String filing = row.getCell(8).toString();
				paraMap.put("filing", filing);
				String status = row.getCell(9).toString();
				paraMap.put("status", status);
				String nosign = row.getCell(10).toString();
				paraMap.put("nosign", nosign);
				Integer acceptance = Integer.valueOf(row.getCell(11).toString().replaceAll("\\.0", ""));
				Integer report = Integer.valueOf(row.getCell(12).toString().replaceAll("\\.0", ""));
				String remarks = row.getCell(13).toString();
				Date entrytime = new Date();

				// long totalNum =
				// acceptanceProjectMapper.countAcceptance(paraMap);

				AcceptanceProject atp = new AcceptanceProject(unit, time, numbering, project, billingmsg, contact, sign,
						filing, status, nosign, acceptance, report, remarks, entrytime);

				/*
				 * if(totalNum<1){ list.add(atp); }
				 */
				list.add(atp);
			}
			if (list != null && list.size() > 0) {
				acceptanceProjectMapper.insert(list);
				msg = msg + ("。系统安全漏洞数据入库记录数：" + list.size());
				result.setCode(Constance.RESPONSE_SUCCESS);
			} else {
				msg = msg + ("。系统安全漏洞数据成功入库记录数：0。");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
			}

		} catch (Exception e) {
			log.error("系统安全漏洞数据表上传出错。");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			msg = msg + ("系统安全漏洞数据表上传出错。");
			e.printStackTrace();
		} finally {

			if (inputStream != null) {
				inputStream.close();
			}
		}
		result.setMsg(msg);
		return result;
	}

	public String get_time(String str) {
		str = str.trim();
		String str2 = "";
		String time = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		Integer sum = Integer.valueOf(str2);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		if (month < sum) {
			year = year - 1;
		}
		time = String.valueOf(year) + "年" + String.valueOf(sum) + "月";
		return time;
	}

	@Override
	public Map<String, Object> getAcceptance(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", acceptanceProjectMapper.getAcceptance(paraMap));
		long totalNum = acceptanceProjectMapper.countAcceptance(paraMap);
		resultMap.put("total_num", totalNum);
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}
		return resultMap;

	}

	@Override
	public Map<String, Object> getAcceptanceAll(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", acceptanceProjectMapper.getAcceptance(paraMap));
		List<Map<String, Object>> sign = acceptanceProjectMapper.countSign(paraMap);

		for (int i = 0; i < sign.size(); i++) {
			if (sign.get(i).get("sign") == null) {
				sign.get(i).put("sign", 0);
			}
		}

		resultMap.put("sign", sign);
		long totalNum = acceptanceProjectMapper.countAcceptance(paraMap);
		resultMap.put("total_num", totalNum);
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}
		return resultMap;

	}

	@Override
	public HSSFWorkbook createExcel(Map<String, Object> paraMap) throws Exception {

		String[] title = { "序号", "责任单位", "月份", "入网验收项目名称", "发单信息", "接入联系人", "是否签字", "是否备案", "是否通过验收", "验收项目次数",
				"是否出具合格报告" };
		String excelName = "";
		List<Map<String, Object>> termList = acceptanceProjectMapper.createExcel(paraMap);
		List<Object[]> list = new ArrayList<Object[]>();
		for (Map<String, Object> map : termList) {
			Object[] objects = new Object[11];
			objects[0] = map.get("id") + "";
			objects[1] = map.get("unit") + "";
			objects[2] = map.get("time") + "";
			objects[3] = map.get("project") + "";
			objects[4] = map.get("billingmsg") + "";
			objects[5] = map.get("contact") + "";
			objects[6] = map.get("sign") + "";
			objects[7] = map.get("filing") + "";
			objects[8] = map.get("status") + "";
			objects[9] = map.get("acceptance") + "";
			objects[10] = map.get("report") + "";
			list.add(objects);
		}
		HSSFWorkbook result = ExcelCreate.createExcel(title, list, excelName);
		return result;
	}
}
