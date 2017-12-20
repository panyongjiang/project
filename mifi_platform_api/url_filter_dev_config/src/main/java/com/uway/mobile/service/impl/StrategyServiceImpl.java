package com.uway.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.HengtongConstance;
import com.uway.mobile.domain.BlackWhiteList;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.Strategy;
import com.uway.mobile.mapper.BlackWhiteListMapper;
import com.uway.mobile.mapper.DeviceMapper;
import com.uway.mobile.mapper.StrategyMapper;
import com.uway.mobile.service.ConfigurationService;
import com.uway.mobile.service.PlatFormService;
import com.uway.mobile.service.StrategyService;

@Service("strategyService")
public class StrategyServiceImpl implements StrategyService {
	
	@Autowired
	private StrategyMapper strategyMapper;
	@Autowired
	private BlackWhiteListMapper bwListMapper;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	public JedisConnectionFactory JedisConnectionFactory;
	@Autowired
	public PlatFormService ps;

	@Override
	public int insertStrategy(Strategy strategy) throws Exception {
		return strategyMapper.insertStrategy(strategy);
	}

	@Override
	public void updateStrategy(Strategy strategy) throws Exception {
		Strategy strategy_db = strategyMapper.getStrategyById(strategy.getId());
		if("sec".equals(strategy.getType())){//保存安全设置
			strategy_db.setSecCheatWebsite(strategy.getSecCheatWebsite());
			strategy_db.setSecHarmProgram(strategy.getSecHarmProgram());
			strategy_db.setSecIllegalGamble(strategy.getSecIllegalGamble());
			strategy_db.setSecSexyInfo(strategy.getSecSexyInfo());
			strategy_db.setSecShamAdv(strategy.getSecShamAdv());
			strategy_db.setSecTortContent(strategy.getSecTortContent());
			strategy_db.setSecIllegalSite(strategy.getSecIllegalSite());
			strategy_db.setSecPhishingSite(strategy.getSecPhishingSite());
			strategy_db.setSecSexySite(strategy.getSecSexySite());
			strategyMapper.updateStrategy(strategy_db);
		}else if("apn".equals(strategy.getType())){//保存APN设置
			strategy_db.setApnName(strategy.getApnName());
			strategy_db.setApnType(strategy.getApnType());
			strategy_db.setApnPort(strategy.getApnPort());
			strategy_db.setApnProxy(strategy.getApnProxy());
			strategyMapper.updateStrategy(strategy_db);
		}else if("vpn".equals(strategy.getType())){//保存VPN设置
			strategy_db.setVpnAuth(strategy.getVpnAuth());
			strategy_db.setVpnIp(strategy.getVpnIp());
			strategy_db.setVpnPort(strategy.getVpnPort());
			strategy_db.setEnable(strategy.getEnable());
			strategyMapper.updateStrategy(strategy_db);
//		}else if("blist".equals(strategy.getType())){//保存黑名单
//			List<BlackWhiteList> bList = strategy.getbList();
//			Map<Integer,BlackWhiteList> map1 = transList2Map(bList);
//			Map<String,String> m1 = new HashMap<String,String>();
//			m1.put("strategyId", strategy.getId()+"");
//			m1.put("type", "0");
//			List<BlackWhiteList> bList_db = bwListMapper.getListByTypeAndStrategyId(m1);
//			for(BlackWhiteList blackList:bList_db){
//				if(map1.containsKey(blackList.getId())){
//					map1.remove(blackList.getId());
//				}else{
//					bwListMapper.deleteById(blackList.getId());
//				}
//			}
//			Iterator<Integer> it1 = map1.keySet().iterator();
//			while(it1.hasNext()){
//				BlackWhiteList blackWhiteList = map1.get(it1.next());
//				bwListMapper.insertBlackWhiteList(blackWhiteList);
//			}
//		}else if("wlist".equals(strategy.getType())){//保存白名单
//			List<BlackWhiteList> wList = strategy.getwList();
//			Map<Integer,BlackWhiteList> map2 = transList2Map(wList);
//			Map<String,String> m2 = new HashMap<String,String>();
//			m2.put("strategyId", strategy.getId()+"");
//			m2.put("type", "1");
//			List<BlackWhiteList> wList_db = bwListMapper.getListByTypeAndStrategyId(m2);
//			for(BlackWhiteList whiteList:wList_db){
//				if(map2.containsKey(whiteList.getId())){
//					map2.remove(whiteList.getId());
//				}else{
//					bwListMapper.deleteById(whiteList.getId());
//				}
//			}
//			Iterator<Integer> it2 = map2.keySet().iterator();
//			while(it2.hasNext()){
//				BlackWhiteList blackWhiteList = map2.get(it2.next());
//				bwListMapper.insertBlackWhiteList(blackWhiteList);
//			}
		}
		
	}
	
//	private Map<Integer,BlackWhiteList> transList2Map(List<BlackWhiteList> bwList){
//		Map<Integer,BlackWhiteList> map = new HashMap<Integer,BlackWhiteList>();
//		for(BlackWhiteList bw:bwList){
//			map.put(bw.getId(), bw);
//		}
//		return map;
//	}

	@Override
	public String deleteById(Integer id) throws Exception {
		Strategy strategy = strategyMapper.getStrategyById(id);
		if(strategy!=null&&"1".equals(strategy.getStatus())){
			return "已下发的策略不能删除";
		}
		bwListMapper.deleteByStrategyId(id);
		strategyMapper.deleteById(id);
		return "删除成功";
	}

	@Override
	public Strategy getStrategyById(Integer id) throws Exception {
		Strategy strategy = strategyMapper.getStrategyById(id);
		strategy.setOptSeq("");
		strategy.setStatus("0");
		strategyMapper.updateStrategy(strategy);
		Map<String,String> m = new HashMap<String,String>();
		m.put("strategyId", id+"");
		m.put("type", "0");
		List<BlackWhiteList> bList = bwListMapper.getListByTypeAndStrategyId(m);//查询黑名单
		m.put("type", "1");
		List<BlackWhiteList> wList = bwListMapper.getListByTypeAndStrategyId(m);//查询白名单
		strategy.setbList(bList);
		strategy.setwList(wList);
		return strategy;
	}

	@Override
	public List<Strategy> getStrategysByUserId(Integer userId) throws Exception {
		return strategyMapper.getStrategysByUserId(userId);
	}
	
	@SuppressWarnings("unchecked")
	public void order(Map<String,Object> paraMap) throws Exception{
		List<String> deviceIds=new ArrayList<String>();
		Map<String,String> paramMap = new HashMap<String,String>();
		Calendar calendar = Calendar.getInstance();
		Integer strategyId = Integer.parseInt(paraMap.get("strategyId").toString());
		if(paraMap.containsKey("userIds")){
			List<String> userIds = (List<String>)paraMap.get("userIds");
			for(String userId:userIds){
				paramMap.put("userId", userId);
				List<Device> devices_db = deviceMapper.getDevicesByUserId(paramMap);
				for(Device deviceid:devices_db){
					deviceIds.add(deviceid.getDeviceId());
				}
			}
		}
		if(paraMap.containsKey("deviceIds")){
			List<String> deviceId=(List<String>)paraMap.get("deviceIds");
			deviceIds.addAll(deviceId);
		}
		
		String optSeq=paraMap.get("optSeq").toString();
		
		Strategy strategy_db = strategyMapper.getStrategyById(strategyId);
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat formatterTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = formatterTime.format(calendar.getTime());
		for(String d:deviceIds){
			//安全设置
			String key = Constance.STRATEGY_PREFIX+"-"+d;
			System.out.println("连接redis:"+JedisConnectionFactory);
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_SHAM_ADV.getBytes(), strategy_db.getSecShamAdv().getBytes());
			System.out.println("写入redis******");
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_HARM_PROGRAM.getBytes(), strategy_db.getSecHarmProgram().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_ILLEGAL_GAMBLE.getBytes(), strategy_db.getSecIllegalGamble().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_SEXY_INFO.getBytes(), strategy_db.getSecSexyInfo().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_CHEAT_WEBSITE.getBytes(), strategy_db.getSecCheatWebsite().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_TORT_CONTENT.getBytes(), strategy_db.getSecTortContent().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_ILLEGAL_SITE.getBytes(), strategy_db.getSecIllegalSite().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_PHISHING_SITE.getBytes(), strategy_db.getSecPhishingSite().getBytes());
			JedisConnectionFactory.getConnection().hSet(key.getBytes(), Constance.SEC_SEXY_SITE.getBytes(), strategy_db.getSecSexySite().getBytes());
			
			//VPN下发
			if(strategy_db.getVpnIp()!=null
					&&!"".equals(strategy_db.getVpnIp())
					&&strategy_db.getVpnAuth()!=null
					&&!"".equals(strategy_db.getVpnAuth())
					&&String.valueOf(strategy_db.getVpnPort())!=null
					&&!"".equals(String.valueOf(strategy_db.getVpnPort()))){
				map.put("CommandType", HengtongConstance.SET_DEVICE_VPN);
				map.put("enable", strategy_db.getEnable());
				map.put("serverip", strategy_db.getVpnIp());
				map.put("uwayStatus", HengtongConstance.UWAY_STATUS_SET);
				map.put("type", strategy_db.getVpnAuth());
				map.put("deviceid", d);
				map.put("deviceCompanyId", paraMap.get("deviceCompanyId"));
				map.put("optSeq", optSeq);
				map.put("port", strategy_db.getVpnPort());
				map.put("userName", "test");
				map.put("Password", "test");
				map.put("optTime", dateTime);
				map.put("optType", 1);
				map.put("userId", paraMap.get("userId"));
				System.out.println(map.toString());
				ps.setConfig(map);
			}
			//APN下发
			if(strategy_db.getApnType()!=null&&!"".equals(strategy_db.getApnType())){
				Map<String,Object> apnMap=new HashMap<String,Object>();
				if(strategy_db.getApnName()==null||"".equals(strategy_db.getApnName())){
					apnMap.put("Access_Name", "cmnet");
				}else{
					apnMap.put("Access_Name", strategy_db.getApnName());
				}
				
				if(strategy_db.getApnName()==null||"".equals(strategy_db.getApnName())){
					apnMap.put("Configfile_Name", "admin");
				}else{
					apnMap.put("Configfile_Name", strategy_db.getApnName());
				}
				apnMap.put("userName", "test");
				apnMap.put("Password", "test");
				apnMap.put("Auth_Mode", strategy_db.getApnType());
				apnMap.put("CommandType", HengtongConstance.SET_DEVICE_WAN_APN);
				apnMap.put("deviceCompanyId", paraMap.get("deviceCompanyId"));
				apnMap.put("deviceid", d);
				apnMap.put("uwaStatus", HengtongConstance.UWAY_STATUS_SET);
				apnMap.put("optSeq", optSeq);
				apnMap.put("optTime", dateTime);
				apnMap.put("optType", 1);
				apnMap.put("userId", paraMap.get("userId"));
				System.out.println(apnMap.toString());
				ps.setConfig(apnMap);
			}
			
			//黑名单添加
			Map<String,String> parMap=new HashMap<String,String>();
			parMap.put("strategyId", String.valueOf(strategy_db.getId()));
			List<BlackWhiteList> bwl=bwListMapper.getListByTypeAndStrategyId(parMap);
			if(bwl.size()>0){
				BlackWhiteList url=bwl.get(0);
				Map<String,Object> urlMap=new HashMap<String,Object>();
				urlMap.put("Action", 0);
				urlMap.put("Policy", 1);
				urlMap.put("Url_Lists", url.getUrl());
				urlMap.put("optSeq", optSeq);
				urlMap.put("optTime", dateTime);
				urlMap.put("optType", 1);
				urlMap.put("userId", paraMap.get("userId"));
				urlMap.put("deviceid", d);
				urlMap.put("deviceCompanyId", paraMap.get("deviceCompanyId"));
				urlMap.put("uwayStatus", HengtongConstance.UWAY_STATUS_SET);
				urlMap.put("CommandType", HengtongConstance.SET_DEVICE_URL_LISTS);
				ps.setConfig(urlMap);
				
			}
	
		}
		strategy_db.setStatus("1");
		strategy_db.setOptSeq(optSeq);
		System.out.println(strategy_db.toString());
		strategyMapper.updateStrategy(strategy_db);
	}
	
	/**
	 * 新增黑白名单
	 * @param blackWhiteList
	 * @throws Exception
	 */
	public void insertBlackWhiteList(BlackWhiteList blackWhiteList) throws Exception{
		bwListMapper.insertBlackWhiteList(blackWhiteList);
	}
	
	public void deleteBlackWhiteList(List<Integer> listIds) throws Exception{
		for(Integer id:listIds){
			bwListMapper.deleteById(id);
		}
	}

}
