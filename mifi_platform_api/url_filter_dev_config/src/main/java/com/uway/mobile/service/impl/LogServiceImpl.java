package com.uway.mobile.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.Log;
import com.uway.mobile.mapper.LogMapper;
import com.uway.mobile.service.LogService;

@Service("logService")
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogMapper logMapper;

	@Override
	public int insertLog(Log log) throws Exception {
		return logMapper.insertLog(log);
	}

	@Override
	public List<Log> getLogsWithPage(Map<String, Object> param) throws Exception {
		return logMapper.getLogsWithPage(param);
	}

	@Override
	public Integer getLogsTotalCount(Map<String, Object> param) throws Exception {
		return logMapper.getLogsTotalCount(param);
	}

	@Override
	public void delDevice(Map<String, Object> paraMap) throws Exception {
		logMapper.delDevice(paraMap);
		
	}

}
