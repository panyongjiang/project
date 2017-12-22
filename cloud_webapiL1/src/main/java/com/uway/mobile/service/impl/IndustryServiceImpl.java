package com.uway.mobile.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.Industry;
import com.uway.mobile.mapper.IndustryMapper;
import com.uway.mobile.service.IndustryService;

@Service("industryService")
public class IndustryServiceImpl implements IndustryService {

	@Autowired
	private IndustryMapper industryMapper;
	
	@Override
	public List<Map<String, Object>> getIndustry() throws Exception {
		// TODO Auto-generated method stub
		return industryMapper.getIndustry();
	}

	@Override
	public Industry getIndustryById(String id) throws Exception {
		// TODO Auto-generated method stub
		industryMapper.getIndustryById(id);
		return null;
	}

}
