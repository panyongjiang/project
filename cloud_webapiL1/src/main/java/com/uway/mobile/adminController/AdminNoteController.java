package com.uway.mobile.adminController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.NoteService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("admin_note")
public class AdminNoteController {
	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	public NoteService noteService;
	
	/**
	 * 列出所有的留言信息
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_note", method = RequestMethod.POST)
	public Result listNote(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			return noteService.getAllNote(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 获取留言信息
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_note", method = RequestMethod.POST)
	public Result getNote(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setMsg("ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取留言信息成功!");
			result.setData(noteService.getNoteById(paraMap.get("id").toString()));
			return result;
		} catch (Exception e) {		
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 处理留言信息
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upd_note_status", method = RequestMethod.POST)
	public Result updNoteStatus(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setMsg("ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "status")) {
				result.setMsg("状态不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			noteService.updNoteStatus(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("处理留言信息成功!");
			return result;
		} catch (Exception e) {
			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 删除留言信息
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/del_note", method = RequestMethod.POST)
	public Result delNote(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setMsg("ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			noteService.delNote(paraMap.get("id").toString());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("删除留言信息成功!");
			return result;
		} catch (Exception e) {
			
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
			return result;
		}
	}
}
