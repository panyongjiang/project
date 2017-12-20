package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.DeviceCompany;
import com.uway.mobile.domain.MonthFlow;
import com.uway.mobile.mapper.DeviceConfigMapper;
import com.uway.mobile.mapper.DeviceMapper;
import com.uway.mobile.service.DeviceService;
import com.uway.mobile.util.CalendarFormatterUtil;
import com.uway.mobile.util.RedisUtil;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private CalendarFormatterUtil cfu;
	@Autowired
	public DeviceConfigMapper dcm;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	public RedisConnectionFactory rcf;
	
	
	@Override
	public List<Device> getDevicesByUserId(Map<String,String> paramMap) {
		return deviceMapper.getDevicesByUserId(paramMap);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Result queryFlow(Map<String,Object> paraMap) throws Exception {
		Result result = new Result();
		List<Object> flow=new ArrayList<Object>();
		//获取所有的设备ID
		List<Device> device = (List<Device>) paraMap.get("list");
		//设备ID
		List<String> deviceIds=new ArrayList<String>();
		for(Device value:device){
			deviceIds.add(value.getDeviceId());
		}
		
		if(paraMap.get("status").equals("month")){
			flow = cfu.countFlow(30, deviceIds);
		}else if(paraMap.get("status").equals("year")){
			List<MonthFlow> monthFlow=dcm.selectMonthFlow(deviceIds);
			result.setData(monthFlow);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("流量获取成功");
			return result;
		}else if(paraMap.get("status").equals("day")){
			flow = cfu.countFlow(7, deviceIds);
		}else {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setData(null);
			result.setMsg("统计失败，请稍后再试");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(flow);
		result.setMsg("流量获取成功");
		return result;
	}


	@Override
	public List<DeviceCompany> getCompanyId(int id) throws Exception {
		return deviceMapper.getCompanyId(id);
	}
	
}
