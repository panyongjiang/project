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
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.VerifyCodeUtils;

@RestController
@RequestMapping("common")
public class CommonController {
	@Autowired
	public RedisUtil redisUtil;
	
	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/rand_ver_image", method = RequestMethod.GET)
	public void randVerImage(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "uid", required = true) String uid)
			throws Exception {
		response.setHeader("Pragma", "No-cache"); 
        response.setHeader("Cache-Control", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        response.setContentType("image/jpeg"); 
           
        //生成随机字串 
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        redisUtil.set("ver:images:" + uid, verifyCode, Long.valueOf(10 * 60));
        
        //生成图片 
        int w = 100, h = 30; 
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode); 
	}

	// @RequestMapping(value = "get_image")
	public Result getImage(HttpServletRequest request) throws Exception{
		Result result = new Result();
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("测试成功！");
		result.setData(request.getSession().getAttribute("verCode"));
		return result;
	}
}
