package com.uway.mobile.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.MasAssets;
import com.uway.mobile.mapper.MasAssetsMapper;
import com.uway.mobile.service.MacService;
import com.uway.mobile.util.POIUtil;

@Transactional
@Service
public class MacServiceImp implements MacService {

	private static final Logger log = Logger.getLogger(MacServiceImp.class);

	@Autowired
	private MasAssetsMapper masMappper;

	@Override
	public Map<String, Object> getMacesResources(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		List<Map<String, Object>> details = masMappper.getMasesResource(paraMap);
		if (details.size() > 0 && details != null) {
			resultMap.put("details", details);
		}
		long totalNum = masMappper.countMasesResource(paraMap);
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
		List<Map<String, Object>> details = masMappper.groupByTime(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> groupByEffect(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> details = masMappper.groupByParm(paraMap);
		for (Map<String, Object> map : details) {
			// 一般安全事件，较大、重大、特大安全事件
			if (!"较大安全事件".equals(map)) {
				resultMap.put("largeSecurityEvents", 0);
			} else if (!"重大安全事件".equals(map)) {
				resultMap.put("majorSecurityEvents", 0);
			} else if (!"特大安全事件".equals(map)) {
				resultMap.put("oversizeSecurityEvents", 0);
			} else if (!"一般安全事件".equals(map)) {
				resultMap.put("generalSecurityEvents", 0);
			} else {
				list.add(map);
			}
		}
		if (list != null && list.size() > 0) {
			resultMap.put("details", list);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> groupByParm(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = masMappper.groupByParm(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> groupByPort(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		ArrayList list = new ArrayList();

		List<MasAssets> details = masMappper.groupByPort(paraMap);
		for (MasAssets masAssets : details) {
			String port = masAssets.getPort();
			list.add(port);
		}
		Collections.sort(list);
		if (list != null && list.size() > 0) {
			resultMap.put("details", list);
			resultMap.put("total_num", list.size());
		}
		return resultMap;
	}

	@Override
	public Result importMasInfos(String fileName, InputStream in) throws Exception {
		String msg = "";
		Result result = new Result();
		try {
			Sheet sheet = POIUtil.getExcelFile(fileName, in);
			List<MasAssets> list = new ArrayList<MasAssets>();
			msg = "文件数据行数：" + sheet.getLastRowNum();
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				String name = row.getCell(1).toString();
				String ipaddress = row.getCell(2).toString();
				String department = row.getCell(3).toString();
				String productmodel = row.getCell(4).toString();
				String operatesystemversion = row.getCell(5).toString();
				String applicationversion = row.getCell(6).toString();
				String storebussinessdata = row.getCell(7).toString();
				String bussinesstype = row.getCell(8).toString();
				String deviceactualaddress = row.getCell(9).toString();
				String applicationexplain = row.getCell(10).toString();
				String administratorcontact = row.getCell(11).toString();
				String url = row.getCell(12).toString();
				String effect = row.getCell(13).toString();
				String comment = row.getCell(14).toString();
				Date time = new Date();
				String ip = null;
				String port = null;
				String province = "广西省";
				MasAssets masAssets = new MasAssets(name, ipaddress, department, productmodel, operatesystemversion,
						applicationversion, storebussinessdata, bussinesstype, deviceactualaddress, applicationexplain,
						administratorcontact, url, effect, comment, ip, port, time, province);
				list.add(masAssets);
			}
			if (list != null && list.size() > 0) {
				try {
					masMappper.insert(list);
					msg = msg + ("。资产成功入库记录数：" + list.size());
					result.setCode(Constance.RESPONSE_SUCCESS);
				} catch (Exception e) {
					log.debug("资产入库失败。");
					e.printStackTrace();
				}
			} else {
				msg = msg + ("。资产成功入库记录数：0。");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
			}

		} catch (Exception e) {
			log.error("资产表上传出错:" + e.toString());
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			msg = msg + ("。资产表上传出错:" + e.toString());
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		result.setMsg(msg);
		return result;
	}

	public HSSFWorkbook findAll(Map<String, Object> param) throws Exception {
		List<MasAssets> list = masMappper.findAll(param);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西移动MAS系统资产情况表");
		try {
			for (int i = 0; i < 8; i++) {
				sheet.setColumnWidth(i, 20 * 256);
			}
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("资产名称");
			headRow.createCell(1).setCellValue("ip地址");
			headRow.createCell(2).setCellValue("所属地市");
			headRow.createCell(3).setCellValue("产品型号");
			headRow.createCell(4).setCellValue("操作系统及版本");
			headRow.createCell(5).setCellValue("应用软件及版本");
			headRow.createCell(6).setCellValue("业务类型");
			headRow.createCell(7).setCellValue("应用访问路径");
			if (list.size() > 0 && list != null) {
				for (MasAssets masAssets : list) {
					HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
					dataRow.createCell(0).setCellValue(masAssets.getName());
					dataRow.createCell(1).setCellValue(masAssets.getIpAddress());
					dataRow.createCell(2).setCellValue(masAssets.getDepartment());
					dataRow.createCell(3).setCellValue(masAssets.getProductModel());
					dataRow.createCell(4).setCellValue(masAssets.getOperateSystemVersion());
					dataRow.createCell(5).setCellValue(masAssets.getApplicationVersion());
					dataRow.createCell(6).setCellValue(masAssets.getBussinessType());
					dataRow.createCell(7).setCellValue(masAssets.getUrl());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	@Override
	public long countMasesResource(Map<String, Object> paraMap) {
		return masMappper.countMasesResource(paraMap);
	}

	@Override
	public HSSFWorkbook getAll(Map<String, String> param) {
		List<MasAssets> list = masMappper.getAll(param);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西移动MAS系统资产情况表");
		try {
			for (int i = 0; i < 8; i++) {
				sheet.setColumnWidth(i, 20 * 256);
			}
			HSSFRow headRow = sheet.createRow(0);
			headRow.createCell(0).setCellValue("资产名称");
			headRow.createCell(1).setCellValue("ip地址");
			headRow.createCell(2).setCellValue("所属地市");
			headRow.createCell(3).setCellValue("产品型号");
			headRow.createCell(4).setCellValue("操作系统及版本");
			headRow.createCell(5).setCellValue("应用软件及版本");
			headRow.createCell(6).setCellValue("业务类型");
			headRow.createCell(7).setCellValue("应用访问路径");
			if (list.size() > 0 && list != null) {
				for (MasAssets masAssets : list) {
					HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
					dataRow.createCell(0).setCellValue(masAssets.getName());
					dataRow.createCell(1).setCellValue(masAssets.getIpAddress());
					dataRow.createCell(2).setCellValue(masAssets.getDepartment());
					dataRow.createCell(3).setCellValue(masAssets.getProductModel());
					dataRow.createCell(4).setCellValue(masAssets.getOperateSystemVersion());
					dataRow.createCell(5).setCellValue(masAssets.getApplicationVersion());
					dataRow.createCell(6).setCellValue(masAssets.getBussinessType());
					dataRow.createCell(7).setCellValue(masAssets.getUrl());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

}
