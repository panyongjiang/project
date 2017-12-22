package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface NoteService {
	/**
	 * 获取所有的留言信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getAllNote(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 新增留言信息
	 * 
	 * @param paraMap
	 * @throws Exception
	 */
	public void insertNote(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取留言信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoteById(String  id) throws Exception;
	
	/**
	 * 修改留言信息
	 * @param id
	 * @throws Exception
	 */
	public void updNoteStatus(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 删除留言信息
	 * @param id
	 * @throws Exception
	 */
	public void delNote(String id) throws Exception;
}
