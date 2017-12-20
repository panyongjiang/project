package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.domain.Industry;

@Transactional
public interface IndustryService {

	/**
	 * 得到行业列表
	 * @return
	 */
	public List<Map<String, Object>> getIndustry() throws Exception ;
	
	public Industry getIndustryById(String id) throws Exception ;
}
