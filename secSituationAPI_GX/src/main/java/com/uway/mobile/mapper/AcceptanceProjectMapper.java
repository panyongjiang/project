package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.AcceptanceProject;

@Mapper
public interface AcceptanceProjectMapper {

	void insert(List<AcceptanceProject> list);

	public List<Map<String, Object>> getAcceptance(Map<String, Object> paraMap);

	public long countAcceptance(Map<String, Object> paraMap);

	public List<Map<String, Object>> countSign(Map<String, Object> paraMap);

	public List<Map<String, Object>> createExcel(Map<String, Object> paraMap);

	
}
