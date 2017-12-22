package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Industry;

@Mapper
public interface IndustryMapper {
	/**
	 * 得到所有行业
	 * @return
	 */
	public List<Map<String, Object>> getIndustry();
	
	public Industry getIndustryById(String id);
}
