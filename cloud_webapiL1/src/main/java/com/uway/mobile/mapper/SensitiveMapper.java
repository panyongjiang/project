package com.uway.mobile.mapper;

import com.uway.mobile.domain.SensitiveWord;

@Mapper
public interface SensitiveMapper {
	/**
	 * 新增敏感词
	 * @param sqlMap
	 * @throws Exception
	 */
	public void insertSensitive(SensitiveWord sensitive) throws Exception;
	
	/**
	 * 根据name删除敏感词
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delSensitive(String name) throws Exception;

	public String checkWord(String word);
}
