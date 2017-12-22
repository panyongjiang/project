package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CodeMapper {

	/**
	 * 插入邀请码
	 * @param list
	 * @return
	 */
	public int insertCode(List<Map<String, Object>> list) throws Exception;
	
	/**
	 * 获取所有邀请码
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listCodes(Map<String, Object> paraMap) throws Exception;
	/**
	 * 获取所有邀请码的数量
	 * @return
	 * @throws Exception
	 */
	public long countCodes() throws Exception;
	
	/**
	 * 根据code获取邀请码
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCode(Map<String, Object> paraMap) throws Exception;
	/**
	 * 验证邀请码是否正确
	 * @param paraMap
	 * @throws Exception
	 */
	public void updCode(Map<String, Object> paraMap) throws Exception;
}
