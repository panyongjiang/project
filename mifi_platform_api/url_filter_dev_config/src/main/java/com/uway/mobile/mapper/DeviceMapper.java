package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.DeviceCompany;

@Mapper
public interface DeviceMapper {
	
	public List<Device> getDevicesByUserId(Map<String,String> paramMap);

	public List<Device> selectIdByUserId(@Param("list") List<Integer> deviceList);

	public List<Device> queryAllDevice();

	public List<DeviceCompany> getCompanyId(int id);

}
