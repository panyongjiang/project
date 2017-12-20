package com.uway.mobile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.SecSetting;
import com.uway.mobile.mapper.SecSettingMapper;
import com.uway.mobile.service.SecSettingService;

@Service("secSettingService")
public class SecSettingServiceImpl implements SecSettingService {
	
	@Autowired
	private SecSettingMapper secSettingMapper;

	@Override
	public int insertSecSetting(SecSetting secSetting) throws Exception {
		// TODO Auto-generated method stub
		return secSettingMapper.insertSecSetting(secSetting);
	}

	@Override
	public int updateSecSetting(SecSetting secSetting) throws Exception {
		// TODO Auto-generated method stub
		return secSettingMapper.updateSecSetting(secSetting);
	}

	@Override
	public List<SecSetting> getSecSettingByDeviceId(String deviceId) throws Exception {
		// TODO Auto-generated method stub
		return secSettingMapper.getSecSettingByDeviceId(deviceId);
	}

}
