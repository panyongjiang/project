package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoteMsgMapper {
	/**
	 * 获取该用户所有的消息
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllNoteMsg(Map<String, Object> sqlMap)
			throws Exception;

	/**
	 * 该用户所收消息的条数
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public long countAllNoteMsg(Map<String, Object> sqlMap) throws Exception;

	/**
	 * 获取消息详情
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoteMsgById(Map<String, Object> sqlMap)
			throws Exception;

	/**
	 * 更改消息的阅读状态
	 * @param sqlMap
	 * @throws Exception
	 */
	public void updNoteMsgStatus(Map<String, Object> sqlMap) throws Exception;

	/**
	 * 删除相应的消息
	 * @param delTime
	 * @throws Exception
	 */
	public void delNoteMsg(String delTime) throws Exception;
}
