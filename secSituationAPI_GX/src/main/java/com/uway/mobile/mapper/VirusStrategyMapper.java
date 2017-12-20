package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.VirusStrategy;

public interface VirusStrategyMapper {
	
	void insert(List<VirusStrategy> list);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	public List<Map<String, Object>> getVirusStrategy(Map<String, Object> paraMap);

	public long countVirusStrategy(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> groupByTime(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> createExcel(Map<String, Object> paraMap);	
}
