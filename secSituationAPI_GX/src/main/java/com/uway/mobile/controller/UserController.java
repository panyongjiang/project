package com.uway.mobile.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	private Result getUser(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result=new Result();
		if(paraMap.get("userName") == null || "".equals(paraMap.get("userName"))){
			result.setMsg("用户名不能为空！");
			result.setCode(Constance.RESPONSE_USER_ERROR);
			return result;
		}
		if(paraMap.get("passwd") == null || "".equals(paraMap.get("passwd"))){
			result.setMsg("密码不能为空！");
			result.setCode(Constance.RESPONSE_PWD_ERROR);
			return result;
		}
		result=userService.getUserByName(paraMap);
		return result;
		
	}
	
	@RequestMapping(value = "/upd_passwd", method = RequestMethod.POST)
	private Result updPasswd(@RequestBody Map<String, Object> paraMap) throws Exception{
		Result result=new Result();
		if(paraMap.get("passwd_old")== null || "".equals(paraMap.get("passwd_old"))){
			result.setMsg("原密码不能为空！");
			result.setCode(Constance.RESPONSE_PWD_ERROR);
			return result;
		}else {
			paraMap.put("passwd", paraMap.get("passwd_old").toString());
		}
		System.out.println(paraMap);
		if(paraMap.get("passwd_new") == null || "".equals(paraMap.get("passwd_new"))){
			result.setMsg("新密码不能为空！");
			result.setCode(Constance.RESPONSE_PWD_ERROR);
			return result;
		}
		result=userService.getUserByName(paraMap);
		if(result.getCode()==200){
			userService.updPwd(paraMap);
			result.setMsg("修改成功");
		}
		return result;
		
	}
}
