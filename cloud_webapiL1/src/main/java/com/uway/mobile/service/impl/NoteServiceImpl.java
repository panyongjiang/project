package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Note;
import com.uway.mobile.mapper.NoteMapper;
import com.uway.mobile.service.NoteService;
import com.uway.mobile.util.ObjectUtil;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteMapper noteMapper;

	@Override
	public Result getAllNote(Map<String, Object> paraMap)
			throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "start_time")){
			paraMap.put("startTime", paraMap.get("start_time").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "end_time")){
			paraMap.put("endTime", paraMap.get("end_time").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "mobile")){
			paraMap.put("mobile", "%" + paraMap.get("mobile").toString() + "%");
		}
		if(!ObjectUtil.isEmpty(paraMap, "status")){
			paraMap.put("status", paraMap.get("status").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "content")){
			paraMap.put("content", "%" + paraMap.get("content").toString() + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("details", noteMapper.getAllNote(paraMap));
		long totalNum = noteMapper.countAllNote(paraMap);
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setData(map);
		result.setMsg("查询成功！");
		result.setCode(Constance.RESPONSE_SUCCESS);
		return result;
	}
	
	@Override
	public void insertNote(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Note note = new Note();
		note.setPhone(paraMap.get("phone").toString());
		note.setContent(paraMap.get("content").toString());
		noteMapper.insertNote(note);
	}
	
	@Override
	public Map<String, Object> getNoteById(String  id) throws Exception {
		// TODO Auto-generated method stub
		return noteMapper.getNoteById(id);
	}

	@Override
	public void updNoteStatus(Map<String, Object> sqlMap) throws Exception {
		// TODO Auto-generated method stub
		noteMapper.updNoteStatus(sqlMap);
	}

	@Override
	public void delNote(String id) throws Exception {
		// TODO Auto-generated method stub
		noteMapper.delNote(id);
	}

}
