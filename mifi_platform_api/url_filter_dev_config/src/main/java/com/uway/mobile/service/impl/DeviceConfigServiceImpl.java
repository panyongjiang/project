package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.HengtongConstance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.SecSetting;
import com.uway.mobile.mapper.DeviceConfigMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.ConfigurationService;
import com.uway.mobile.service.DeviceConfigService;
import com.uway.mobile.service.LogService;
import com.uway.mobile.service.PlatFormService;
import com.uway.mobile.service.SecSettingService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.AuthorityUser;
import com.uway.mobile.util.ExcelUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.TraversalMapUtil;

@Service
public class DeviceConfigServiceImpl implements DeviceConfigService {
	
	@Autowired
	public DeviceConfigMapper dcm;
	@Autowired
	public AuthorityUser au;
	@Autowired
	private SecSettingService secSettingService;
	@Autowired
	public ConfigurationService config;
	@Autowired
	private ApiService apiService;
	@Value("${site.safe.prefix.url_hengtong}")
	public String URL_TCP;
	@Autowired
	public JedisConnectionFactory jcf;
	@Autowired
	public UserService userService;
	@Autowired
	public LogService logService;
	@Autowired
	public TraversalMapUtil tmu;
	@Autowired
	public DeviceConfigService dcs;
	@Autowired
	public PlatFormService pfs;

	@Override
	public Result insertDevice(Map<String, Object> paraMap) {
		Result result=new Result();
		dcm.insertDevice(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("注册设备成功");;
		return result;
	}

	@Override
	public Result getDevice(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> checkOnline=new HashMap<String,Object>();
		checkOnline.put("CommandType", "ONLINE_DEVICES");
		checkOnline=apiService.doPost(URL_TCP,checkOnline);
		if(checkOnline==null||!checkOnline.containsKey("result")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设备连接超时,请联系设备商!");
			result.setData("");
			return result;
		}
		Long totalNum=dcm.countDevice(paraMap); 
		List<Device> device = dcm.queryDeviceById(paraMap);
		List<Object> value=new ArrayList<Object>();
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		for(Device di:device){
			Map<String,Object> configMap=new HashMap<String,Object>();
			String deviceId=di.getDeviceId();
			configMap.put("deviceid", deviceId);
			if("".equals(di.getMac())||di.getMac()==null){
				di.setStatus("3");
			}else{
				if(checkOnline.get("result").toString().length()>0){
					boolean flag=checkOnline.toString().contains(deviceId.toString()+"_");
					if(!flag){
						di.setStatus("0");
					}else{
						di.setStatus("1");
					}
				}else{
					di.setStatus("0");
				}
			}
			value.add(di);
		}
		map.put("value", value);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("获取设备信息成功");
		return result;
	}

	@Override
	public Result selectDevice(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		if(paraMap.isEmpty()){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请至少填写一个搜索条件");
			return result;
		}
		if (ObjectUtil.isEmpty(paraMap, "page_num")) {
			result.setMsg("页码不能为空！");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			return result;
		}
		String pageNum = paraMap.get("page_num").toString();
		String pageSizes = "" + Constance.PAGE_SIZE;
		if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
			pageSizes = paraMap.get("page_size").toString();
		}
		List<Device> device = dcm.selectByCondition(paraMap);
		Long totalNum=(long)device.size();
		paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSizes));
		paraMap.put("pageSize", Integer.parseInt(pageSizes));
		paraMap.remove("page_num");
		paraMap.remove("page_size");
		device= dcm.selectByCondition(paraMap);
		map.put("totalNum", totalNum);
		List<Device> deviceLists=new ArrayList<Device>();
		Map<String,Object> checkOnline=new HashMap<String,Object>();
		checkOnline.put("CommandType", "ONLINE_DEVICES");
		checkOnline=apiService.doPost(URL_TCP,checkOnline);
		if(checkOnline==null||!checkOnline.containsKey("result")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("设备请求超时,请联系设备商!");
			return result;
		}
		if(device.size()>0){
			for(Device di:device){
				if("".equals(di.getMac())||di.getMac()==null){
					di.setStatus("3");
				}else{
					if(checkOnline.containsKey("result")){
						if(checkOnline.get("result").toString().length()>0){
							boolean flag=checkOnline.toString().contains(di.getDeviceId().toString()+"_");
							if(!flag){
								di.setStatus("0");
							}else{
								di.setStatus("1");
							}
						}else{
							di.setStatus("0");
						}
					}else{
						di.setStatus("0");
					}
				}
				if(paraMap.containsKey("status")){
					if(!"".equals(paraMap.get("status").toString())){
						if(paraMap.get("status").toString().equals("1")){
							if(di.getStatus().equals("1")){
								deviceLists.add(di);
							}
						}else if(paraMap.get("status").toString().equals("0")){
							if(di.getStatus().equals("0")){
								deviceLists.add(di);
							}
						}else if(paraMap.get("status").toString().equals("3")){
							if(di.getStatus().equals("3")){
								deviceLists.add(di);
							}
						}
						device=deviceLists;
						map.put("totalNum", device.size());
					}
				}
			}
		}
		map.put("device", device);
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("搜索成功");
		return result;
	}
	
	public SecSetting findSecSettingByDeviceId(String deviceId,String userId) throws Exception{
		List<SecSetting> list = secSettingService.getSecSettingByDeviceId(deviceId);
		if(list.size()>0){
			SecSetting sec=list.get(0);
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("deviceid", deviceId);
			paraMap.put("CommandType", HengtongConstance.SET_DEVICE_URL_SWITCH);
			paraMap.put("uwayStatus", HengtongConstance.UWAY_STATUS_SET);
			paraMap.put("deviceCompanyId", HengtongConstance.DEVICE_COMPANY_ID);
			paraMap.put("userId", userId);
			if("1".equals(sec.getSecCheatWebsite())||"1".equals(sec.getSecHarmProgram())
					||"1".equals(sec.getSecIllegalGamble())||"1".equals(sec.getSecIllegalSite())
					||"1".equals(sec.getSecPhishingSite())||"1".equals(sec.getSecSexyInfo())
					||"1".equals(sec.getSecSexySite())||"1".equals(sec.getSecShamAdv())
					||"1".equals(sec.getSecTortContent())){
				paraMap.put("Switch", 1);
			}else{
				paraMap.put("Switch", 0);
			}
			pfs.setConfig(paraMap);
			return list.get(0);
		}
		SecSetting secSetting = new SecSetting();
		return secSetting;
	}
	
	public void saveSecSetting(SecSetting secSetting) throws Exception{
		if(secSetting==null||secSetting.getId()==0){
			secSettingService.insertSecSetting(secSetting);
		}else{
			secSettingService.updateSecSetting(secSetting);
		}
		if(secSetting!=null){
			String key = Constance.STRATEGY_PREFIX+"-"+secSetting.getDeviceId();
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_SHAM_ADV.getBytes(), secSetting.getSecShamAdv().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_SHAM_ADV.getBytes(), secSetting.getSecShamAdv().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_HARM_PROGRAM.getBytes(), secSetting.getSecHarmProgram().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_ILLEGAL_GAMBLE.getBytes(), secSetting.getSecIllegalGamble().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_SEXY_INFO.getBytes(), secSetting.getSecSexyInfo().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_CHEAT_WEBSITE.getBytes(), secSetting.getSecCheatWebsite().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_TORT_CONTENT.getBytes(), secSetting.getSecTortContent().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_ILLEGAL_SITE.getBytes(), secSetting.getSecIllegalSite().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_PHISHING_SITE.getBytes(), secSetting.getSecPhishingSite().getBytes());
			jcf.getConnection().hSet(key.getBytes(), Constance.SEC_SEXY_SITE.getBytes(), secSetting.getSecSexySite().getBytes());
		}
	}

	@Override
	public Result getDeviceInfo(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		List<Device> route = dcm.getDeviceInfo(paraMap);
		if(route.size()<1){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("该设备无信息，请联系设备商!");
			return result;
		}
		tmu.getICCID(route);
		Map<String,Object> para= new HashMap<String,Object>();
		para.put("device", route);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(para);
		result.setMsg("获取设备信息成功");
		return result;
	}

	@Override
	public Result insertRoute(Map<String,Object> paraMap,MultipartFile file) throws Exception {
		Result result =new Result();
		String[] strCol = {"deviceId"};
		String resStr = ExcelUtil.exportListFromExcel(file.getInputStream(),FilenameUtils.getExtension(file.getOriginalFilename()),0,strCol);
		List<Device> devices = JSONObject.parseArray(resStr, Device.class);
		StringBuilder sb= new StringBuilder("[");
		
		String msg="";
		for(Device di:devices){
			if(StringUtils.isBlank(di.getDeviceId())){
				continue;
			}
			String id=di.getDeviceId().substring(0,di.getDeviceId().lastIndexOf("."));
			paraMap.put("deviceId", id);
			List<Device> deviceLists=dcm.queryDeviceById(paraMap);
			if(deviceLists.size()>0){
				sb.append(di.getDeviceId()).append(",");
			}else{
				di.setDeviceId(id);
				di.setCompanyId(Integer.parseInt(paraMap.get("companyId").toString()));
				di.setDeviceCompanyId(paraMap.get("deviceCompanyId").toString());
				dcm.inserRoute(di);
				SecSetting secSetting=new SecSetting();
				secSetting.setDeviceId(id);
				secSetting.setId(0);
				secSetting.setDeviceId(paraMap.get("deviceId").toString());
				secSetting.setSecCheatWebsite("1");
				secSetting.setSecHarmProgram("1");
				secSetting.setSecIllegalGamble("1");
				secSetting.setSecIllegalSite("1");
				secSetting.setSecPhishingSite("1");
				secSetting.setSecSexyInfo("1");
				secSetting.setSecSexySite("1");
				secSetting.setSecShamAdv("1");
				secSetting.setSecTortContent("1");
				dcs.saveSecSetting(secSetting);
			}
		}
		sb.append("]");
		if(!sb.toString().equals("[]")){
			msg+="列表中"+sb.toString()+"的设备ID重复，未导入到系统.";
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
		}else{
			msg="导入成功";
			result.setCode(Constance.RESPONSE_SUCCESS);
		}
		
		result.setMsg(msg);
		return result;
		
	}

	@Override
	public Result insertOneRoute(Map<String, Object> paraMap) throws Exception {
		Result result =new Result();
		List<Device> deviceLists=dcm.queryDeviceById(paraMap);
		if(deviceLists.size()>0){
			result.setMsg("该设备已导入数据库");
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
		}else{
			Device di=new Device();
			if(paraMap.containsKey("userId")){
				di.setUserId(Integer.parseInt(paraMap.get("userId").toString()));
			}else{
				di.setUserId(0);
			}
			di.setDeviceId(paraMap.get("deviceId").toString());
			di.setCompanyId(Integer.parseInt(paraMap.get("companyId").toString()));
			di.setDeviceCompanyId(paraMap.get("deviceCompanyId").toString());
			dcm.inserRoute(di);
			SecSetting secSetting=new SecSetting();
			secSetting.setId(0);
			secSetting.setDeviceId(paraMap.get("deviceId").toString());
			secSetting.setSecCheatWebsite("1");
			secSetting.setSecHarmProgram("1");
			secSetting.setSecIllegalGamble("1");
			secSetting.setSecIllegalSite("1");
			secSetting.setSecPhishingSite("1");
			secSetting.setSecSexyInfo("1");
			secSetting.setSecSexySite("1");
			secSetting.setSecShamAdv("1");
			secSetting.setSecTortContent("1");
			dcs.saveSecSetting(secSetting);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("导入成功");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result bindRoute(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		if(paraMap.containsKey("deviceIds")){
			if(((List<String>)paraMap.get("deviceIds")).size()>0){
				for(String str:(List<String>)paraMap.get("deviceIds")){
					paraMap.put("deviceId", str);
					dcm.bindRoute(paraMap);
				}
			}else{
				result.setMsg("请选择绑定的设备ID");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
		}else{
			dcm.bindRoute(paraMap);
		}
		if(!paraMap.containsKey("userId")){
			//解绑
			result.setMsg("解绑成功");
		}else{
			//绑定
			result.setMsg("绑定成功");
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		return result;
	}

	@Override
	public Result fBindRoute(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Device> lists=new ArrayList<Device>();
		Long totalNum =dcm.fbroute(paraMap);
		if(paraMap.containsKey("deviceId")){
			if(totalNum<=0){
				result.setMsg("未搜索到设备");
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setData(lists);
				return result;
			}
		}
		if(totalNum>0){
			String pageSize = paraMap.get("pageSize").toString();
			if(totalNum % Long.parseLong(pageSize) > 0){
				map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
			}else{
				map.put("totalPage", totalNum / Long.parseLong(pageSize));
			}
			lists=dcm.fBindRoute(paraMap);
			map.put("lists", lists);
			map.put("totalNum", totalNum);
			result.setData(map);
			result.setMsg("获取未绑定设备列表成功");
		}else{
			result.setMsg("没有未绑定的设备");
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		return result;
	}

	@Override
	public Result getMacNum(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Integer i=dcm.getMacNum(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(i);
		result.setMsg("获取激活设备成功");
		return result;
	}

	@Override
	public Result delDevice(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		dcm.delDevice(paraMap);
		dcm.bindRoute(paraMap);
		dcm.delFlowByDeviceId(paraMap);
		dcm.delMonthFlowByDeviceId(paraMap);
		dcm.delYearFlowByDeviceId(paraMap);
		dcm.delSecSettinByDeviceId(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除设备成功");
		return result;
	}

	@Override
	public Result updateDevice(Map<String, Object> paraMap) throws Exception {
		Result result=new Result();
		dcm.updateDevice(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("更新成功");
		return result;
	}

}
