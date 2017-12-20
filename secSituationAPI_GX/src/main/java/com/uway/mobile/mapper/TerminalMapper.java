package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Terminal;

public interface TerminalMapper {
	
	void insert(List<Terminal> list);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	public List<Map<String, Object>> getTerminal(Map<String, Object> paraMap);

	public long countTerminal(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> groupByTime(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> createExcel(Map<String, Object> paraMap);	
	
	
}
