package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.BlackWhiteList;

@Mapper
public interface BlackWhiteListMapper {

	/**
	 * 新增黑白名单
	 * @param blackWhiteList
	 * @return
	 * @throws Exception
	 */
	public int insertBlackWhiteList(BlackWhiteList blackWhiteList) throws Exception;
	
	/**
	 * 根据ID删除黑白名单
	 * @return
	 * @throws Exception
	 */
	public int deleteById(int id) throws Exception;
	
	/**
	 * 根据策略ID删除黑白名单
	 * @param strategyId
	 * @return
	 * @throws Exception
	 */
	public int deleteByStrategyId(int strategyId) throws Exception;
	
	/**
	 * 根据类型和策略ID查询黑白名单列表
	 * @param paramMap
	 * @return
	 */
	public List<BlackWhiteList> getListByTypeAndStrategyId(Map<String, String> paramMap);
	
}
