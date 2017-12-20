package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.SecSetting;

@Transactional
public interface DeviceConfigService {

	public Result insertDevice(Map<String, Object> paraMap)throws Exception;

	public Result getDevice(Map<String, Object> paraMap)throws Exception;

	public Result selectDevice(Map<String, Object> paraMap)throws Exception;

	/**
	 * 根据设备ID查询安全设置
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public SecSetting findSecSettingByDeviceId(String deviceId,String userId) throws Exception;
	
	/**
	 * 安全设置的保存功能
	 * @param secSetting
	 * @throws Exception
	 */
	public void saveSecSetting(SecSetting secSetting) throws Exception;

	public Result getDeviceInfo(Map<String, Object> paraMap)throws Exception;
	
    /**
     * 导入设备
     * @param userId
     */
	public Result insertRoute(Map<String,Object> paraMap,MultipartFile file)throws Exception;

	public Result insertOneRoute(Map<String, Object> paraMap)throws Exception;

	public Result bindRoute(Map<String, Object> paraMap)throws Exception;

	public Result fBindRoute(Map<String, Object> paraMap)throws Exception;

	public Result getMacNum(Map<String, Object> paraMap)throws Exception;

	public Result delDevice(Map<String, Object> paraMap)throws Exception;

	public Result updateDevice(Map<String, Object> paraMap)throws Exception;

}
