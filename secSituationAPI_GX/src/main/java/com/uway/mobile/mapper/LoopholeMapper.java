package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Loophole;

@Mapper
public interface LoopholeMapper {
	
	void insert(List<Loophole> list);
	//获取汇总信息
	public List<Map<String, Object>> getLoopholeSummary(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> Countloopholebyunits(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> reskpropor(Map<String, Object> paraMap);
	
	public long countLoophole(Map<String, Object> paraMap);
	
}
