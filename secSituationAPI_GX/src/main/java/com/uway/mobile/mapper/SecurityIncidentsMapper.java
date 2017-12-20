package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.SecurityIncidents;

public interface SecurityIncidentsMapper {

	public List<Map<String, Object>> groupByMonth(Map<String, Object> paraMap);

	public List<Map<String, Object>> groupByParm(Map<String, Object> paraMap);

	public List<Map<String, Object>> getSecurityIncidents(Map<String, Object> paraMap);

	public long countSecurityIncidents(Map<String, Object> paraMap);

	public long countMeasures(Map<String, Object> parm);

	public void insert(List<SecurityIncidents> list);

	public List<SecurityIncidents> findAll(Map<String, Object> param);

	int updateByPrimaryKeySelective(SecurityIncidents record);

	int updateByPrimaryKey(SecurityIncidents record);

	public List<SecurityIncidents> getAll(Map<String, String> param);
}