package com.uway.mobile.mapper;

import java.util.List;

import com.uway.mobile.domain.Params;

@Mapper
public interface ParamsMapper {

	/*public Map<String,Object> getById(String device_company_id)throws Exception;*/
	public List<Params> getById(String device_company_id)throws Exception;
	

}
