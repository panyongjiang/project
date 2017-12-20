package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.domain.Log;

@Transactional
public interface LogService {
	
	/**
	 * 新增日志
	 * @param log
	 * @return
	 * @throws Exception
	 */
	public int insertLog(Log log) throws Exception;
	
	/**
	 * 日志分页查询
	 * @param param
	 * @return
	 */
	public List<Log> getLogsWithPage(Map<String,Object> param) throws Exception;
	
	/**
	 * 获取分页查询的总数
	 * @return
	 * @throws Exception
	 */
	public Integer getLogsTotalCount(Map<String, Object> param) throws Exception;

	public void delDevice(Map<String, Object> paraMap)throws Exception;

}
