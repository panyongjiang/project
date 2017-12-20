package com.uway.mobile.common;

/**
 * 设备商亨通
 * 常量参数实体类
 * @author java_ztx
 *
 */
public class HengtongConstance {
	
	//亨通设备商ID
	public static final String DEVICE_COMPANY_ID="2";
	//设备离线
	public static final String STATUS_OFFLINE="0";
	//设备在线
	public static final String STATUS_ONLINE="1";
	//设备未激活
	public static final String STATUS_INACTIVATED="3";
	//设备  查询操作标识符
	public static final String UWAY_STATUS_GET="0";
	//设备  设置操作标识符
	public static final String UWAY_STATUS_SET="1";
    
	//设备设置操作指令参数
	
	
	//无线设置
	public static final String SET_DEVICE_WIFI="WIFI_SETTING";
	//无线黑白名单设置
	public static final String SET_DEVICE_WIFI_FILTER="MAC_FILTER_SETTING";
	//wifi定时任务设置
	public static final String SET_DEVICE_WIFI_SCHTASK="WIFI_SCH_TASK";
	//4G联网设置
	public static final String SET_DEVICE_WAN_4G="WAN_SETTING_4G";
	//APN设置
	public static final String SET_DEVICE_WAN_APN="WAN_SETTING_APN";
	//MAC克隆
	public static final String SET_DEVICE_WAN_MAC="WAN_MAC_CLONE";
	//MAC克隆恢复
	public static final String SET_DEVICE_WAN_REMAC="WAN_MAC_RESTORE";
	//网络测速
	public static final String SET_DEVICE_WAN_SPEED="WAN_SPEEDTEST";
	//终端修改及限速
	public static final String SET_DEVICE_CLIENT="CLIENT_SETTING";
	//网址黑白名单
	public static final String SET_DEVICE_URL_LISTS="URL_LISTS_SETTING";
	//url上报开关
	public static final String SET_DEVICE_URL_SWITCH="URL_SWITCH_SETTING";
	//VPN设置
	public static final String SET_DEVICE_VPN="VPN_SETTING";
	//LAN口设置
	public static final String SET_DEVICE_LAN="LAN_SETTING";
	//DHCP设置
	public static final String SET_DEVICE_LAN_DHCP="LAN_DHCP_SETTING";
	//设备重启
	public static final String SET_DEVICE_REBOOT="DEVICE_REBOOT";
	//设备恢复默认值
	public static final String SET_DEVICE_FACTORY="DEVICE_FACTORY";
	//设备联网设置
	public static final String SET_DEVICE_WAN_SETTING_4G="WAN_SETTING_4G";
	//设备升级
	public static final String SET_DEVICE_UPGRADE_ACTION="DEVICE_UPGRADE_ACTION";
	
	
	//设备查询操作指令参数
	
	
	//设备运行时信息
	public static final String GET_DEVICE_RUNTIME="RUNTIME_INFO";
	//设备版本更新查询
	public static final String GET_DEVICE_UPGRADE="DEVICE_UPGRADE_INFO";
	//设备下载进度查询
	public static final String GET_DEVICE_FIRM_DOWN="FIRM_DOWN_PROGRESS";
	//无线配置信息获取
	public static final String GET_DEVICE_WIFI="WIFI_INFO_GET";
	//wifi定时任务信息获取
	public static final String GET_DEVICE_WIFI_SCHGET="WIFI_SCH_GET";
	//外网信息获取
	public static final String GET_DEVICE_WAN="WAN_INFO_GET";
	//APN信息获取
	public static final String GET_DEVICE_WAN_APN="APN_INFO_GET";
	//MAC克隆信息获取
	public static final String GET_DEVICE_WAN_MAC="MAC_CLONE_INFO";
	//网络测速信息获取
	public static final String GET_DEVICE_WAN_SPEED="WAN_SPEEDTEST_INFO";
	//终端信息获取
	public static final String GET_DEVICE_CLIENT="CLIENT_INFO_GETTING";
	//网址黑白名单获取
	public static final String GET_DEVICE_URL_LISTS="URL_LISTS_GET";
	//VPN信息获取
	public static final String GET_DEVICE_VPN="VPN_INFO_GET";
	//局域网信息获取
	public static final String GET_DEVICE_LAN="LAN_INFO_GET";
	//外网信息获取
	public static final String GET_DEVICE_WAN_INFO_GET="WAN_INFO_GET";
	
	//TCP服务端  约定的参数
	
	//TCP端接收指令成功
	public static final String TCP_RESULT_CODE="0";
	//获取设备在线列表
	public static final String TCP_GET_DEVICE_ONLINE="ONLINE_DEVICES";
	
			

}
