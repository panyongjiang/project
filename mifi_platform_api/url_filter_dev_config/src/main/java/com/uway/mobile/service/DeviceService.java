package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.DeviceCompany;

@Transactional
public interface DeviceService {
	
	public List<Device> getDevicesByUserId(Map<String,String> paramMap)throws Exception;

	public Result queryFlow(Map<String,Object> paraMap)throws Exception;

	public List<DeviceCompany> getCompanyId(int id)throws Exception;

}
