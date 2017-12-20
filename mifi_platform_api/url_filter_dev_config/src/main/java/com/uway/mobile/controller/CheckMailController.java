package com.uway.mobile.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.UserService;

@RestController
@RequestMapping("checkMail")
public class CheckMailController {
	
	@Autowired
	public UserService userService;
	
	/**
	 * 邮箱验证
	 */
	@RequestMapping(value="/checkMail",method=RequestMethod.GET)
	public Result checkMail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "action", required = true) String action,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "validateCode", required = true) String validateCode){
		Result result = new Result();
		try {
			if(!action.equals("activate")){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("激活失败,请重新注册!");
				return result;
			}
			result=userService.checkMail(email,validateCode);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}

}
