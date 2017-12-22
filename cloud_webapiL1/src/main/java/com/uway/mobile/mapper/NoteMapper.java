package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Note;

@Mapper
public interface NoteMapper {
	/**
	 * 据相应的查询条件，查询所有的留言信息
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllNote(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 获取所有的留言数据条数
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public long countAllNote(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 新增留言信息
	 * @param note
	 * @throws Exception
	 */
	public void insertNote(Note note) throws Exception;
	
	/**
	 * 获取留言信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoteById(String id) throws Exception;
	
	/**
	 * 修改留言状态
	 * @param note
	 * @throws Exception
	 */
	public void updNoteStatus(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 删除留言
	 * @param note
	 * @throws Exception
	 */
	public void delNote(String id) throws Exception;
}
