package com.uway.mobile.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface AssetInfoService {

	public Map<String, Object> getAssetInfos(Map<String, Object> paraMap) throws Exception;

	// public List<AssetInfo> findAll(Map<String, Object> paraMap) throws
	// Exception;
	public HSSFWorkbook findAll(Map<String, String> paraMap) throws Exception;

	public Map<String, Object> groupByTime(Map<String, Object> paraMap);

	public Map<String, Object> groupByParm(Map<String, Object> paraMap);

	public Map<String, Object> groupByPort(Map<String, Object> paraMap);

	public void synchronizedSrvResourceFile() throws Exception;

	public Result importAssetInfos(String filename, InputStream inputStream) throws IOException;

	public long countAllAssets(Map<String, Object> sqlMap);

	public HSSFWorkbook getAll(Map<String, Object> param);
}
