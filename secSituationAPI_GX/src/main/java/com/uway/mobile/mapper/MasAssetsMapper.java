package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.MasAssets;

public interface MasAssetsMapper {

	public List<Map<String, Object>> groupByTime(Map<String, Object> paraMap);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	public List<MasAssets> groupByPort(Map<String, Object> paraMap);

	public List<Map<String, Object>> getMasesResource(Map<String, Object> paraMap);

	public long countMasesResource(Map<String, Object> paraMap);

	public void insert(List<MasAssets> list);

	public List<MasAssets> findAll(Map<String, Object> param);

	public List<MasAssets> getAll(Map<String, String> param);

}