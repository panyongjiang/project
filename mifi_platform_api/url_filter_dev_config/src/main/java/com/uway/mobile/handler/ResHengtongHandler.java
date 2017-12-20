package com.uway.mobile.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.HengtongConstance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Log;
import com.uway.mobile.service.LogService;
import com.uway.mobile.util.TraversalMapUtil;

@Component
public class ResHengtongHandler extends ResParamHandler{
	
	@Autowired
	TraversalMapUtil tm;
	@Autowired
	private LogService logService;
	
	public static final Logger log = Logger.getLogger(ResHengtongHandler.class);
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public Result doHandler(Map<String, Object> paraMap) {
		Result result = new Result();
		//判断map的key  固定的key进行封装  定义常量 便于维护
		if(paraMap.get("deviceCompanyId").toString().equals(HengtongConstance.DEVICE_COMPANY_ID.toString())&&paraMap.get("uwayStatus").toString().equals(HengtongConstance.UWAY_STATUS_GET.toString())){
			if(!paraMap.containsKey("CmdStatus")||!paraMap.containsKey("Response")){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("设备商未按照约定格式,返回数据");
				return result;
			}
			Map<String,Object> mapstatus=(Map<String,Object>)JSON.parse(paraMap.get("CmdStatus").toString());
			if(!mapstatus.containsKey("ResultCode")){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("设备商返回缺少参数，无法判断操作是否成功");
				return result;
			}
			if(paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_CLIENT)){
				if(mapstatus.get("ResultCode").toString().equals(HengtongConstance.TCP_RESULT_CODE)){
					if(paraMap.get("Response").toString().contains("Status")){
						result.setMsg("当前无终端连接");
						result.setCode(Constance.RESPONSE_PARAM_EMPTY);
						return result;
					}
					result.setCode(Constance.RESPONSE_SUCCESS);
					result.setMsg("查询成功");
					List<JSONObject> resparam=JSONObject.parseArray(paraMap.get("Response").toString(),JSONObject.class);
					List<Map<String,Object>> lists=new ArrayList<Map<String,Object>>();
					for(JSONObject jsonString:resparam){
						Map<String,Object> cmap=(Map<String, Object>) JSONObject.parse(jsonString.toString());
						cmap.put("deviceCompanyId", paraMap.get("deviceCompanyId").toString());
						cmap=tm.changeResParas(cmap);
						lists.add(cmap);
					}
					Map<String,Object> param=new HashMap<String,Object>();
					param.put("deviceCompanyId", paraMap.get("deviceCompanyId").toString());
					param.put("data", lists);
					param.put("CommandType", paraMap.get("CommandType"));
					result.setData(param);
				}else{
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					result.setMsg(mapstatus.get("Message").toString());
				}
				return result;
				
			}
			Map<String,Object> resparam=(Map<String,Object>)JSON.parse(paraMap.get("Response").toString());
			if(paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_FIRM_DOWN) ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_RUNTIME)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WIFI) ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WIFI_SCHGET)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WAN)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WAN_APN)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WAN_MAC)||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_URL_LISTS) ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_VPN)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_LAN)  ||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WAN_SPEED)||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_WAN_INFO_GET)||
						paraMap.get("CommandType").toString().equals(HengtongConstance.GET_DEVICE_UPGRADE))
				{
					if(mapstatus.get("ResultCode").toString().equals(HengtongConstance.TCP_RESULT_CODE)){
						result.setCode(Constance.RESPONSE_SUCCESS);
						result.setMsg("查询成功");
						resparam.put("deviceCompanyId", paraMap.get("deviceCompanyId").toString());
						resparam=tm.changeResParas(resparam);
						resparam.put("CommandType", paraMap.get("CommandType"));
						result.setData(resparam);
					}else{
						result.setCode(Constance.RESPONSE_PARAM_ERROR);
						result.setMsg(mapstatus.get("Message").toString());
					}
				}else{
					result.setCode(Constance.RESPONSE_INNER_ERROR);
					result.setMsg("无效的指令");
				}
		}else if(paraMap.get("deviceCompanyId").toString().equals(HengtongConstance.DEVICE_COMPANY_ID.toString())&&paraMap.get("uwayStatus").toString().equals(HengtongConstance.UWAY_STATUS_SET.toString())){
            try {
            	if("1".equals(paraMap.get("optType").toString())){
            		if(paraMap.get("ResultCode").toString().equals(HengtongConstance.TCP_RESULT_CODE)){
        				result.setCode(Constance.RESPONSE_SUCCESS);
        				result.setMsg("操作指令已下发，请等待生效!");
        			}else{
        				result.setCode(Constance.RESPONSE_PARAM_ERROR);
        				result.setMsg("操作下发失败");
        			}
            	}else{
            		if(!paraMap.containsKey("CmdStatus")||!paraMap.containsKey("Response")){
        				result.setCode(Constance.RESPONSE_INNER_ERROR);
        				result.setMsg("设备商未按照约定格式,返回数据");
        				return result;
        			}
        			Map<String,Object> mapstatus=(Map<String,Object>)JSON.parse(paraMap.get("CmdStatus").toString());
        			if(!mapstatus.containsKey("ResultCode")){
        				result.setCode(Constance.RESPONSE_INNER_ERROR);
        				result.setMsg("设备商返回缺少参数，无法判断操作是否成功");
        				return result;
        			}
        			Map<String,Object> resparam=(Map<String,Object>)JSON.parse(paraMap.get("Response").toString());
        			if("0".equals(mapstatus.get("ResultCode").toString())&&"0".equals(resparam.get("Status").toString())){
        				result.setCode(Constance.RESPONSE_SUCCESS);
        				result.setMsg("设置成功");
        			}else{
        				result.setCode(Constance.RESPONSE_PARAM_ERROR);
        				result.setMsg("设置失败");
        			}
            	}
    			Log optLog=new Log();
    			optLog.setOptUser(paraMap.get("userId").toString());
    			optLog.setSeqNum(paraMap.get("seqNum").toString());
                if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WIFI)){
                    //无线设置
                    optLog.setOperation("无线设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WIFI_FILTER)){
                    //无线黑白名单设置
                	optLog.setOperation("无线黑白名单设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WIFI_SCHTASK)){
                    //wifi定时任务设置
                	optLog.setOperation("wifi定时任务设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_4G)){
                    //4G联网设置
                	optLog.setOperation("4G联网设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_APN)){
                    //APN参数设置
                	optLog.setOperation("APN参数设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_MAC)){
                    //Mac克隆
                	optLog.setOperation("Mac克隆设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_REMAC)){
                    //Mac克隆恢复
                	optLog.setOperation("Mac克隆恢复");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_SPEED)){
                    //网络测速
                	optLog.setOperation("网络测速");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_CLIENT)){
                    //终端名称修改及限速
                	optLog.setOperation("终端名称修改及限速");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_URL_LISTS)){
                    //网址黑白名单设置
                	optLog.setOperation("网址黑白名单设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_URL_SWITCH)){
                    //URL上报开关设置
                	optLog.setOperation("URL上报开关设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_VPN)){
                    //VPN设置
                	optLog.setOperation("VPN设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_LAN)){
                    //LAN口设置
                	optLog.setOperation("LAN口设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_LAN_DHCP)){
                    //DHCP设置
                	optLog.setOperation("DHCP设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_REBOOT)){
                    //设备重启
                	optLog.setOperation("设备重启");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_FACTORY)){
                    //设备恢复默认值
                	optLog.setOperation("设备恢复默认值");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_WAN_SETTING_4G)){
                	//联网设置
                	optLog.setOperation("联网设置");
                }else if(paraMap.get("CommandType").toString().equals(HengtongConstance.SET_DEVICE_UPGRADE_ACTION)){
                	//联网设置
                	optLog.setOperation("设备升级");
                }
                if(Integer.parseInt(paraMap.get("logStatus").toString())==0){
                	logService.insertLog(optLog);
                }
			} catch (Exception e) {
				
				e.printStackTrace();
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("内部错误");
				return result;
			}

		}else{
			if(getNextHandler()!=null){
				getNextHandler().doHandler(paraMap);
			}
		}
		return result;
	}
}
