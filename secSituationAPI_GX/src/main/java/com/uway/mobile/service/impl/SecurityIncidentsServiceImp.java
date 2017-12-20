package com.uway.mobile.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Proportion;
import com.uway.mobile.domain.SecurityIncidents;
import com.uway.mobile.mapper.SecurityIncidentsMapper;
import com.uway.mobile.service.SecurityIncidentsService;
import com.uway.mobile.util.POIUtil;

@Transactional
@Service
public class SecurityIncidentsServiceImp implements SecurityIncidentsService {
	private static final Logger log = Logger.getLogger(SecurityIncidentsServiceImp.class);

	@Autowired
	private SecurityIncidentsMapper securityIncidentsMapper;

	@Override
	public Map<String, Object> getSecurityIncidents(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		List<Map<String, Object>> details = securityIncidentsMapper.getSecurityIncidents(paraMap);
		if (details.size() > 0 && details != null) {
			resultMap.put("details", details);
		}
		long totalNum = securityIncidentsMapper.countSecurityIncidents(paraMap);
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
		List<Map<String, Object>> details = securityIncidentsMapper.groupByMonth(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> groupByParm(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = securityIncidentsMapper.groupByParm(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> groupByParmProportion(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = securityIncidentsMapper.groupByParm(paraMap);
		List<Object> list = new ArrayList<>();
		String event = null;
		for (Map<String, Object> map : details) {
			String s = map.get("sum").toString();
			event = map.get("event").toString();
			double sum = Double.parseDouble(String.valueOf(s));
			long lon = securityIncidentsMapper.countSecurityIncidents(paraMap);
			double total = Double.parseDouble(String.valueOf(lon).substring(0, 2));
			Double proportion = sum / total * 100;
			Proportion pro = new Proportion();
			pro.setProportion(String.valueOf(proportion) + "%");
			pro.setEvent(event);

			list.add(pro);
			list.add(event);
		}
		resultMap.put("details", list);
		resultMap.put("total_num", details.size());

		return resultMap;
	}

	@Override
	public HSSFWorkbook findAll(Map<String, Object> param) throws Exception {
		List<SecurityIncidents> list = securityIncidentsMapper.findAll(param);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西移动一般安全事件资产数据情况表");
		for (int i = 0; i < 6; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("地市公司");
		headRow.createCell(1).setCellValue("事件类型");
		headRow.createCell(2).setCellValue("事件分类");
		headRow.createCell(3).setCellValue("对象");
		headRow.createCell(4).setCellValue("事件简要经过");
		headRow.createCell(5).setCellValue("是否采取措施");
		if (list.size() > 0 && list != null) {
			for (SecurityIncidents securityIncidents : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(securityIncidents.getDepartment());
				dataRow.createCell(1).setCellValue(securityIncidents.getEventDescription());
				dataRow.createCell(2).setCellValue(securityIncidents.getCategory());
				dataRow.createCell(3).setCellValue(securityIncidents.getEvent());
				dataRow.createCell(4).setCellValue(securityIncidents.getBriefPass());
				dataRow.createCell(5).setCellValue(securityIncidents.getMeasures());
			}
		}
		return workbook;
	}

	public long countSecurityIncidents(Map<String, Object> paraMap) {
		return securityIncidentsMapper.countSecurityIncidents(paraMap);
	}

	public Map<String, Object> handleSecurityIncidents(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			long count = securityIncidentsMapper.countSecurityIncidents(paraMap);
			resultMap.put("event", count);
			resultMap.put("eventDescription", count);
			Map<String, Object> parm = new HashMap<>();
			parm.put("isRegisted", paraMap.get("isRegisted"));
			parm.put("department", paraMap.get("department"));
			parm.put("period_month", paraMap.get("period_month"));
			long measures = securityIncidentsMapper.countMeasures(parm);
			resultMap.put("measures", measures);
			resultMap.put("period_month", paraMap.get("period_month"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Result importSecurityIncidents(String fileName, InputStream in) throws Exception {
		Result result = new Result();
		List<SecurityIncidents> list = new ArrayList<SecurityIncidents>();
		Workbook book = POIUtil.getNumberOfSheets(fileName, in);
		try {
			int sheets = book.getNumberOfSheets();
			for (int i = 5; i < sheets; i++) {
				log.debug("文件解析开始。");
				Sheet sheet = book.getSheetAt(i);
				String category = sheet.getRow(1).getCell(1).toString();
				String event = sheet.getRow(1).getCell(4).toString();
				String level = sheet.getRow(2).getCell(1).toString();
				Date occurencetime = sheet.getRow(2).getCell(4).getDateCellValue();
				String eventDescription = sheet.getRow(3).getCell(1).toString();
				String unitAddess = sheet.getRow(4).getCell(1).toString();
				String regEx = "\\d+\\.\\d+\\.\\d+\\.\\d+";
				Pattern pattern = Pattern.compile(regEx);
				Matcher matcher = pattern.matcher(unitAddess);
				String ip = "";
				if (matcher.find()) {
					ip = matcher.group();
				}
				String department = unitAddess.replaceAll(ip, "").replace(" ", "");
				department = department.substring(1, 3);
				if (department.equals("防城")) {
					department = department + "港";
				}
				String involveContent = sheet.getRow(5).getCell(1).toString();
				String briefpass = sheet.getRow(6).getCell(1).toString();
				String harmandeffect = sheet.getRow(7).getCell(1).toString();
				String measures = sheet.getRow(8).getCell(1).toString();
				String remarks = sheet.getRow(9).getCell(1).toString();
				Date entrytime = new Date();
				Integer status = 0;
				if (StringUtils.isEmpty(category) || StringUtils.isEmpty(event) || StringUtils.isEmpty(level)
						|| occurencetime == null || StringUtils.isEmpty(eventDescription)
						|| StringUtils.isEmpty(unitAddess) || StringUtils.isEmpty(involveContent)
						|| StringUtils.isEmpty(briefpass) || StringUtils.isEmpty(harmandeffect)
						|| StringUtils.isEmpty(measures) || StringUtils.isEmpty(remarks) || StringUtils.isEmpty(ip)
						|| StringUtils.isEmpty(department)) {
					log.debug("参数为空字段。");
				}
				SecurityIncidents securityIncidents = new SecurityIncidents(category, event, level, occurencetime,
						eventDescription, unitAddess, involveContent, briefpass, harmandeffect, measures, remarks, ip,
						department, entrytime, status);
				list.add(securityIncidents);
			}
			log.debug("文件解析完成。");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() > 0 && list != null) {
			try {
				log.debug("数据开始入库。。。");
				securityIncidentsMapper.insert(list);
				log.debug("数据开始入库记录：" + list.size());
			} catch (Exception e) {
				log.debug("数据入库失败。。。");
				e.printStackTrace();
			}
		}
		result.setMsg("入库成功！");
		return result;
	}

	@Override
	public HSSFWorkbook getAll(Map<String, String> param) {
		List<SecurityIncidents> list = securityIncidentsMapper.getAll(param);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西移动一般安全事件资产数据情况表");
		for (int i = 0; i < 6; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("地市公司");
		headRow.createCell(1).setCellValue("事件类型");
		headRow.createCell(2).setCellValue("事件分类");
		headRow.createCell(3).setCellValue("对象");
		headRow.createCell(4).setCellValue("事件简要经过");
		headRow.createCell(5).setCellValue("是否采取措施");
		if (list.size() > 0 && list != null) {
			for (SecurityIncidents securityIncidents : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(securityIncidents.getDepartment());
				dataRow.createCell(1).setCellValue(securityIncidents.getEventDescription());
				dataRow.createCell(2).setCellValue(securityIncidents.getCategory());
				dataRow.createCell(3).setCellValue(securityIncidents.getEvent());
				dataRow.createCell(4).setCellValue(securityIncidents.getBriefPass());
				dataRow.createCell(5).setCellValue(securityIncidents.getMeasures());
			}
		}
		return workbook;
	}

	public List<Object> groupByEffect(Map<String, Object> paraMap) {
		List<Object> arrayList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> details = securityIncidentsMapper.groupByParm(paraMap);
		for (Map<String, Object> map : details) {
			// 一般安全事件，较大、重大、特大安全事件
			if (!"较大安全事件".equals(map.get("level"))) {
				// resultMap.put("largeSecurityEvents", 0);
				arrayList.add(0);
			} else {
				arrayList.add(map.get("sum"));
				// resultMap.put("largeSecurityEvents", map.get("sum"));
			}
			if (!"重大安全事件".equals(map.get("level"))) {
				arrayList.add(0);
				// resultMap.put("majorSecurityEvents", 0);
			} else {
				arrayList.add(map.get("sum"));
				// resultMap.put("majorSecurityEvents", map.get("sum"));
			}
			if (!"特大安全事件".equals(map.get("level"))) {
				arrayList.add(0);
				// resultMap.put("oversizeSecurityEvents", 0);
			} else {
				arrayList.add(map.get("sum"));
				// resultMap.put("oversizeSecurityEvents", map.get("sum"));
			}
			if (!"一般事件".equals(map.get("level"))) {
				arrayList.add(0);
				// resultMap.put("generalSecurityEvents", 0);
			} else {
				arrayList.add(map.get("sum"));
				// resultMap.put("generalSecurityEvents", map.get("sum"));
			}
			// resultMap.put("details", map);
			// list.add(map);
		}
		/*
		 * if (list != null && list.size() > 0) { resultMap.put("details",
		 * list); }
		 */
		return arrayList;
	}
}
