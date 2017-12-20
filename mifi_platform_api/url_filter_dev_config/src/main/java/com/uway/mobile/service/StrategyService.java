package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.domain.BlackWhiteList;
import com.uway.mobile.domain.Strategy;

@Transactional
public interface StrategyService {
	
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
	public void updateStrategy(Strategy strategy) throws Exception;
	
	/**
	 * 根据ID删除策略
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String deleteById(Integer id) throws Exception;
	
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
	
	/**
	 * 策略下发
	 * @param strategyId
	 * @param userIds
	 * @throws Exception
	 */
	public void order(Map<String,Object> paraMap) throws Exception;
	
	/**
	 * 新增黑白名单
	 * @param blackWhiteList
	 * @throws Exception
	 */
	public void insertBlackWhiteList(BlackWhiteList blackWhiteList) throws Exception;
	
	/**
	 * 删除黑白名单
	 * @param listIds
	 * @throws Exception
	 */
	public void deleteBlackWhiteList(List<Integer> listIds) throws Exception;

}
