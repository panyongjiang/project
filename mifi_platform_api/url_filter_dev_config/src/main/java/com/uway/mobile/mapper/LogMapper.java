package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Log;

@Mapper
public interface LogMapper {
	
	/**
	 * 新增操作日志
	 * @param log
	 * @return
	 * @throws Exception
	 */
	public int insertLog(Log log) throws Exception;
	
	/**
	 * 查找所有日志
	 * @return
	 * @throws Exception
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
