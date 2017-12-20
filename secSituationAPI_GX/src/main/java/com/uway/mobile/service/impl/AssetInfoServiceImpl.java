package com.uway.mobile.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.common.StaticFtp;
import com.uway.mobile.domain.AssetInfo;
import com.uway.mobile.mapper.AssetInfoMapper;
import com.uway.mobile.service.AssetInfoService;
import com.uway.mobile.util.FTPClientHandler;
import com.uway.mobile.util.POIUtil;
import com.uway.mobile.util.StreamConvertUtil;

@Service("assetInfoService")
public class AssetInfoServiceImpl implements AssetInfoService {

	private static final Logger log = Logger.getLogger(AssetInfoServiceImpl.class);

	@Autowired
	private AssetInfoMapper assetInforMapper;

	@Override
	public Map<String, Object> getAssetInfos(Map<String, Object> paraMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", assetInforMapper.getAssets(paraMap));
		long totalNum = assetInforMapper.countAllAssets(paraMap);
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
		List<Map<String, Object>> details = assetInforMapper.groupByTime(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> groupByParm(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = assetInforMapper.groupByParm(paraMap);
		if (details != null) {
			resultMap.put("details", details);
			resultMap.put("total_num", details.size());
		}
		return resultMap;
	}

	public void synchronizedSrvResourceFile() throws Exception {
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

						importAssetInfos(ftpFile.getName(), inputStream);

						String sourceFilePath = StaticFtp.srvResource + ftpFile.getName();
						String destFilePath = StaticFtp.srvResource_his + ftpFile.getName();
						FTPClientHandler.renameFile(sourceFilePath, destFilePath);
						log.debug("资产表文件归档成功。");
					} catch (Exception e) {
						log.error("资产表同步出错:" + ftpFile.getName() + "." + e.toString());
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

	public Result importAssetInfos(String filename, InputStream inputStream) throws IOException {
		String msg = "";
		Result result = new Result();
		try {
			Sheet sheet = POIUtil.getExcelFile(filename, inputStream);
			List<AssetInfo> list = new ArrayList<AssetInfo>();
			msg = "文件数据行数：" + sheet.getLastRowNum();
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				String province = row.getCell(1).toString();
				String department = row.getCell(2).toString();
				String ip = row.getCell(3).toString();
				String port = row.getCell(4).toString();
				String url = row.getCell(5).toString();
				String webname = row.getCell(6).toString();
				String category = row.getCell(7).toString();
				String subCategory = row.getCell(8).toString();
				String serviceType = row.getCell(9).toString();
				String softName = row.getCell(10).toString();
				String softVersion = row.getCell(11).toString();
				String manufacturer = row.getCell(12).toString();
				String unittype = row.getCell(13).toString();
				String os = row.getCell(14).toString();
				String hasweb = row.getCell(15).toString();
				String comment = row.getCell(16).toString();
				@SuppressWarnings("unused")
				Date update = new Date();
				String isreport = "已报备";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date registdate = sdf.parse(row.getCell(17).toString());
				if (registdate == null) {
					continue;
				}
				if (!StringUtils.isEmpty(port)) {
					AssetInfo assetInfo = new AssetInfo(province, department, ip, port, url, webname, category,
							subCategory, serviceType, softName, softVersion, manufacturer, unittype, os, hasweb,
							comment, registdate, isreport);
					list.add(assetInfo);
				}
			}
			if (list != null && list.size() > 0) {
				assetInforMapper.insertBatch(list);
				msg = msg + ("。资产成功入库记录数：" + list.size());
				result.setCode(Constance.RESPONSE_SUCCESS);
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

			if (inputStream != null) {
				inputStream.close();
			}
		}

		result.setMsg(msg);
		return result;
	}

	@Override
	public HSSFWorkbook findAll(Map<String, String> paraMap) throws Exception {
		List<AssetInfo> list = assetInforMapper.findAll(paraMap);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西互联网暴露面资产报表");
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < 9; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}
		row.createCell(0).setCellValue("资产所属部门");
		row.createCell(1).setCellValue("公网ip地址");
		row.createCell(2).setCellValue("端口");
		row.createCell(3).setCellValue("域名");
		row.createCell(4).setCellValue("网站名称");
		row.createCell(5).setCellValue("操作系统");
		row.createCell(6).setCellValue("服务类型");
		row.createCell(7).setCellValue("有无web界面");
		row.createCell(8).setCellValue("是否报备");
		if (list.size() > 0 && list != null) {
			for (AssetInfo assetInfo : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(assetInfo.getDepartment());
				dataRow.createCell(1).setCellValue(assetInfo.getIp());
				dataRow.createCell(2).setCellValue(assetInfo.getPort());
				dataRow.createCell(3).setCellValue(assetInfo.getWeburl());
				dataRow.createCell(4).setCellValue(assetInfo.getWebname());
				dataRow.createCell(5).setCellValue(assetInfo.getOs());
				dataRow.createCell(6).setCellValue(assetInfo.getServicetype());
				dataRow.createCell(7).setCellValue(assetInfo.getHasweb());
				dataRow.createCell(8).setCellValue(assetInfo.getIsreport());
			}
		}
		return workbook;
	}

	@Override
	public HSSFWorkbook getAll(Map<String, Object> paraMap) {
		List<AssetInfo> list = assetInforMapper.getExcelAll(paraMap);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("广西互联网暴露面资产报表");
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < 9; i++) {
			sheet.setColumnWidth(i, 20 * 256);
		}
		row.createCell(0).setCellValue("资产所属部门");
		row.createCell(1).setCellValue("公网ip地址");
		row.createCell(2).setCellValue("端口");
		row.createCell(3).setCellValue("域名");
		row.createCell(4).setCellValue("网站名称");
		row.createCell(5).setCellValue("操作系统");
		row.createCell(6).setCellValue("服务类型");
		row.createCell(7).setCellValue("有无web界面");
		row.createCell(8).setCellValue("是否报备");
		if (list.size() > 0 && list != null) {
			for (AssetInfo assetInfo : list) {
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(assetInfo.getDepartment());
				dataRow.createCell(1).setCellValue(assetInfo.getIp());
				dataRow.createCell(2).setCellValue(assetInfo.getPort());
				dataRow.createCell(3).setCellValue(assetInfo.getWeburl());
				dataRow.createCell(4).setCellValue(assetInfo.getWebname());
				dataRow.createCell(5).setCellValue(assetInfo.getOs());
				dataRow.createCell(6).setCellValue(assetInfo.getServicetype());
				dataRow.createCell(7).setCellValue(assetInfo.getHasweb());
				dataRow.createCell(8).setCellValue(assetInfo.getIsreport());
			}
		}
		return workbook;
	}

	@Override
	public long countAllAssets(Map<String, Object> sqlMap) {
		return assetInforMapper.countAllAssets(sqlMap);
	}

	public Map<String, Object> groupByPort(Map<String, Object> paraMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = assetInforMapper.groupByParm(paraMap);
		List<Map<String, Object>> list = SelectSorts(details);
		resultMap.put("details", list);
		return resultMap;
	}

	List<Map<String, Object>> SelectSorts(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Integer i = Integer.valueOf(o1.get("port").toString());
				Integer j = Integer.valueOf(o2.get("port").toString());
				return j.compareTo(i);

			}
		});
		Collections.reverse(list);
		return list;
	}

}
