package com.uway.mobile.service;

import com.uway.mobile.common.Result;

public interface SensitiveService {
	/**
	 * 新增敏感词
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result addSensitive(String word) throws Exception;
	
	/**
	 * 根据name删除敏感词
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delSensitiveByName(String name) throws Exception;
	
	/**
	 * 检查sName是否存在
	 * @param word
	 * @return
	 */
	public String checkSensitive(String word);
	
	
	
	/**
	 * 获取敏感词检查结果
	 * @return
	 * @throws Exception 
	 */
	public Result getResultSensitiveCheck(Result result,String text) throws Exception;

}
