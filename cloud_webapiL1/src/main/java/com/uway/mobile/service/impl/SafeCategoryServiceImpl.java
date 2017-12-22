package com.uway.mobile.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.SafeCategory;
import com.uway.mobile.mapper.SafeCategoryMapper;
import com.uway.mobile.mapper.SiteMapper;
import com.uway.mobile.service.SafeCategoryService;
import com.uway.mobile.service.SiteService;

@Service
public class SafeCategoryServiceImpl implements SafeCategoryService {
	@Autowired
	private SafeCategoryMapper safeCategoryMapper;
	@Autowired
	private SiteMapper siteMapper;
	@Autowired
	private SiteService siteService;

	@Override
	public Map<String, Object> getSafeServiceByUser(String userId)
			throws Exception {
		// TODO Auto-generated method stub
		return safeCategoryMapper.getSafeServiceByUser(userId);
	}

	@Override
	public void updSafeService(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		SafeCategory sc = safeCategoryMapper.getSafeService(paraMap.get("user_id").toString());
		String type = paraMap.get("type").toString();
		if(type.equals("waf")){
			sc.setWafStartTime((Timestamp) paraMap.get("startTime"));
			sc.setWafEndTime((Timestamp) paraMap.get("endTime"));
		}else if(type.equals("app")){
			sc.setAppStartTime((Timestamp) paraMap.get("startTime"));
			sc.setAppEndTime((Timestamp) paraMap.get("endTime"));
		}else if(type.equals("expert")){
			sc.setExpertStartTime((Timestamp) paraMap.get("startTime"));
			sc.setExpertEndTime((Timestamp) paraMap.get("endTime"));
		}else if(type.equals("site")){
			sc.setSiteStartTime((Timestamp) paraMap.get("startTime"));
			sc.setSiteEndTime((Timestamp) paraMap.get("endTime"));
		}
		safeCategoryMapper.updSafeServiceTime(sc);
	}

	@Override
	public void startSafeService(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		safeCategoryMapper.startService(paraMap);
	}

	@Override
	public void trialSafeService(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		safeCategoryMapper.trialService(paraMap);
	}

	@Override
	public void offSafeService(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		if(paraMap.get("type").toString().equals("site")){
			List<Map<String, Object>> listSite = siteMapper.getAllSafeSite(Long.parseLong(paraMap.get("user_id").toString()));
			for(Map<String, Object> site : listSite){
				String id = site.get("id").toString();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				siteService.deleteSite(map);
			}
		}
		if(paraMap.get("type").toString().equals("waf")){
			List<Map<String, Object>> listSite = siteMapper.getAllWAFSite(Long.parseLong(paraMap.get("user_id").toString()));
			for(Map<String, Object> site : listSite){
				String id = site.get("id").toString();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				siteService.deleteWafSite(map);
			}
		}
		safeCategoryMapper.offService(paraMap);
	}

}
