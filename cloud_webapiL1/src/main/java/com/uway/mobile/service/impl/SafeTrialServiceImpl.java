package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.mapper.SafeTrialMapper;
import com.uway.mobile.service.SafeTrialService;
import com.uway.mobile.util.ObjectUtil;

@Service
public class SafeTrialServiceImpl implements SafeTrialService {
	@Autowired
	private SafeTrialMapper safeTrialMapper;
	
	@Override
	public Map<String, Object> getAllSafeTrial(Map<String, Object> paraMap)
			throws Exception {
		// TODO Auto-generated method stub
		if(!ObjectUtil.isEmpty(paraMap, "user_name")){
			paraMap.put("userName", "%" + paraMap.get("user_name").toString() + "%");
		}
		if(!ObjectUtil.isEmpty(paraMap, "trial_status")){
			paraMap.put("trialStatus", paraMap.get("trial_status").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "start_time")){
			paraMap.put("startTime", paraMap.get("start_time").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "end_time")){
			paraMap.put("endTime", paraMap.get("end_time").toString());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long pageSize = Long.parseLong(paraMap.get("page_size").toString());
		resultMap.put("details", safeTrialMapper.getAllSafeTrial(paraMap));
		long totalNum = safeTrialMapper.countAllSafeTrial(paraMap);
		resultMap.put("total_num", totalNum);
		if (totalNum % pageSize > 0) {
			resultMap.put("total_page", totalNum / pageSize + 1);
		} else {
			resultMap.put("total_page", totalNum / pageSize);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getSafeTrialById(Map<String, Object> paraMap) throws Exception{
		// TODO Auto-generated method stub
		if(!ObjectUtil.isEmpty(paraMap, "id")){
			paraMap.put("id", paraMap.get("id").toString());
		}
		return safeTrialMapper.getSafeTrialById(paraMap);
	}

	@Override
	public int UpdateTrialStatusById(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		if(!ObjectUtil.isEmpty(paraMap, "trial_status")){
			paraMap.put("trial_status", paraMap.get("trial_status").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "id")){
			paraMap.put("id", paraMap.get("id").toString());
		}
		return safeTrialMapper.updateTrialStatus(paraMap);
	}

	@Override
	public int delById(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		if(!ObjectUtil.isEmpty(paraMap, "id")){
			paraMap.put("id", paraMap.get("id").toString());
		}
		return safeTrialMapper.delSafeTrialById(paraMap);
	}

	@Override
	public long countAllSafeTrial(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return safeTrialMapper.countAllSafeTrial(paraMap);
	}
	
}
