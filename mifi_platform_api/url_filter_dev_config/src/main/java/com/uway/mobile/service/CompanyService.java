package com.uway.mobile.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.domain.Company;

@Transactional
public interface CompanyService {

	public List<Company> getCompanyByName(String companyName)throws Exception;

}
