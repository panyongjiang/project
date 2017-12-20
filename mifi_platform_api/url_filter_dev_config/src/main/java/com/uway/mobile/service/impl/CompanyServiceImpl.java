package com.uway.mobile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.Company;
import com.uway.mobile.mapper.CompanyMapper;
import com.uway.mobile.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	public CompanyMapper companyMapper;

	@Override
	public List<Company> getCompanyByName(String conpanyName) throws Exception {
		
		return companyMapper.getCompanyByName(conpanyName);
	}

}
