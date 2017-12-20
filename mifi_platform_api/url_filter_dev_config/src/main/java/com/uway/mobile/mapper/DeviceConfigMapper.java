package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uway.mobile.domain.DayFlow;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.MonthFlow;

@Mapper
public interface DeviceConfigMapper {

	public void insertDevice(Map<String, Object> paraMap);

	public List<Device> queryDeviceById(Map<String, Object> paraMap);

	public Long countDevice(Map<String, Object> paraMap);

	public List<Device> selectByCondition(Map<String,Object> paraMap);

	public List<Device> getDeviceInfo(Map<String, Object> paraMap);

	public List<DayFlow> selectFlow(Map<String,Object> paraMap);

	public List<MonthFlow> selectMonthFlow(@Param("list") List<String> list);

	public List<DayFlow> selectTotalFlow(Map<String,Object> paraMap);

	public void inserRoute(Device di);

	public void bindRoute(Map<String, Object> paraMap);

	public List<Device> fBindRoute(Map<String, Object> paraMap);

	public Long fbroute(Map<String, Object> paraMap);

	public int getMacNum(Map<String, Object> paraMap);

	/**
	 * 删除设备
	 * @param paraMap 根据设备ID
	 */
	public void delDevice(Map<String, Object> paraMap);

	/**
	 * 删除流量统计表 天
	 * @param paraMap 根据设备ID
	 */
	public void delFlowByDeviceId(Map<String, Object> paraMap);

	/**
	 * 删除流量统计表 月
	 * @param paraMap 根据设备ID
	 */
	public void delMonthFlowByDeviceId(Map<String, Object> paraMap);

	/**
	 * 删除安全开关配置
	 * @param paraMap 根据设备ID
	 */
	public void delSecSettinByDeviceId(Map<String, Object> paraMap);

	/**
	 * 删除流量统计表 年
	 * @param paraMap 根据设备ID
	 */
	public void delYearFlowByDeviceId(Map<String, Object> paraMap);

	public void updateDevice(Map<String, Object> paraMap);


}
