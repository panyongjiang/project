package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.mapper.CodeMapper;
import com.uway.mobile.service.CodeService;
import com.uway.mobile.util.CodeUtil;

@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	private CodeMapper codeMapper;
	
	@Override
	public Result addCodes(String userId) throws Exception {
		Result result = new Result();
		List<Map<String, Object>> list = CodeUtil.getCode(100);
		for(int i=0;i<list.size();i++){
			list.get(i).put("createUser", userId);
		}
		int num = codeMapper.insertCode(list);
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("新增邀请码成功");
		result.setData(num);
		return result;
	}

	@Override
	public Result listCodes(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		Long totalNum = codeMapper.countCodes();	
		List<Map<String, Object>> list = codeMapper.listCodes(paraMap);
		map.put("details", list);
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(map);
		result.setMsg("获取成功");
		return result;
	}

}
