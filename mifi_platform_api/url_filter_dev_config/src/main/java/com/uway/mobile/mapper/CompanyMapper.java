package com.uway.mobile.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uway.mobile.domain.Company;

@Mapper
public interface CompanyMapper {

	public List<Company> getCompanyByName(@Param("companyName") String companyName);

	public void insertCompany(Company company);
	

}
