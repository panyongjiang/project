package com.uway.mobile.controller;

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
@RequestMapping("note")
public class NoteController {
	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	public NoteService noteService;
	
	/**
	 * 用户进行新增留言
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_note", method = RequestMethod.POST)
	public Result addNote(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "phone")){
				result.setMsg("手机号不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "content")){
				result.setMsg("留言内容不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if(ObjectUtil.isEmpty(paraMap, "ver_code")){
				result.setMsg("验证码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if(!ObjectUtil.isEmpty(paraMap, "uid")){
				Object obj = redisUtil.get("ver:images:" + paraMap.get("uid").toString());
				if(obj == null || "".equals(obj.toString())){
					result.setMsg("请刷新验证码！");
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					return result;
				}else{
					if(!paraMap.get("ver_code").toString().toLowerCase().equals(obj.toString().toLowerCase())){
						result.setMsg("验证码不正确！");
						result.setCode(Constance.RESPONSE_PARAM_ERROR);
						return result;
					}
				}
			}else{
				result.setMsg("参数错误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			noteService.insertNote(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			e.printStackTrace();
		}
		return result;
	}
}
