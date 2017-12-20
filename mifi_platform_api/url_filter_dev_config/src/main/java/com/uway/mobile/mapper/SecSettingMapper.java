package com.uway.mobile.mapper;

import java.util.List;

import com.uway.mobile.domain.SecSetting;

@Mapper
public interface SecSettingMapper {
	
	/**
	 * 新增安全设置
	 * @param secSetting
	 * @return
	 */
	public int insertSecSetting(SecSetting secSetting) throws Exception;
	
	/**
	 * 更新安全设置
	 * @param secSetting
	 * @return
	 */
	public int updateSecSetting(SecSetting secSetting) throws Exception;
	
	/**
	 * 根据设备ID查询设备的安全设置
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<SecSetting> getSecSettingByDeviceId(String deviceId) throws Exception;

}
