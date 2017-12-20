package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.MistakePatch;

public interface MistakePatchMapper {

	void insert(List<MistakePatch> list);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	public List<Map<String, Object>> getMistakePatch(Map<String, Object> paraMap);

	public long countMistakePatch(Map<String, Object> paraMap);

	public List<Map<String, Object>> groupByTime(Map<String, Object> paraMap);

	
	public List<Map<String, Object>> createExcel(Map<String, Object> paraMap);	

}
