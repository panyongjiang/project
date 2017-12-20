package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.AssetInfo;

@Mapper
public interface AssetInfoMapper {

	public List<Map<String, Object>> getAssets(Map<String, Object> sqlMap);

	public long countAllAssets(Map<String, Object> sqlMap);

	AssetInfo selectByPrimaryKey(Long id);

	public List<Map<String, Object>> groupByTime(Map<String, Object> paraMap);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	// public List<AssetInfo> getAll();

	public List<AssetInfo> getAll(Map<String, Object> paraMap);

	public List<AssetInfo> findAll(Map<String, String> paraMap);

	public List<AssetInfo> getExcelAll(Map<String, Object> paraMap);

	public void insertBatch(List<AssetInfo> assetInfos);

	public List<AssetInfo> groupByPort(Map<String, Object> paraMap);
}