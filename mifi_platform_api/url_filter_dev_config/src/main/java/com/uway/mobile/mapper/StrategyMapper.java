package com.uway.mobile.mapper;

import java.util.List;

import com.uway.mobile.domain.Strategy;

@Mapper
public interface StrategyMapper {
	
	/**
	 * 新增策略
	 * @param strategy
	 * @return
	 * @throws Exception
	 */
	public int insertStrategy(Strategy strategy) throws Exception;
	
	/**
	 * 根据ID修改策略
	 * @param strategy
	 * @return
	 * @throws Exception
	 */
	public int updateStrategy(Strategy strategy) throws Exception;
	
	/**
	 * 根据ID删除策略
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteById(Integer id) throws Exception;
	
	/**
	 * 根据ID查询策略
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Strategy getStrategyById(Integer id) throws Exception;
	
	/**
	 * 根据用户ID查询策略列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Strategy> getStrategysByUserId(Integer userId) throws Exception;

}
