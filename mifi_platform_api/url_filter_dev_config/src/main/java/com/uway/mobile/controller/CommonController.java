package com.uway.mobile.controller;

import java.util.ArrayList;

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
import com.uway.mobile.util.ValidateCodeUtil;
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
	
	/** 
	 * 响应验证码 
	 * @return 
	 */  
	@RequestMapping(value="/validateCode", method = RequestMethod.POST)  
	public String validateCode(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "uid", required = true) String uid) throws Exception{  
	    // 设置响应的类型格式为图片格式  
	    response.setContentType("image/jpeg");  
	    //禁止图像缓存。  
	    response.setHeader("Pragma", "no-cache");  
	    response.setHeader("Cache-Control", "no-cache");  
	    response.setDateHeader("Expires", 0);  
	  
	    ValidateCodeUtil vCode = new ValidateCodeUtil(100,30,5,100);
	    String code=vCode.createCode();
	    redisUtil.set("ver:images:" + uid, code, Long.valueOf(10 * 60));
	    vCode.write(response.getOutputStream());  
	    return null;  
	} 
	
	/** 
	 * 响应中文验证码页面 
	 * @return 
	 */  
	@RequestMapping(value="/GraphicsCode", method = RequestMethod.POST)  
	public String GraphicsCode(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "uid", required = true) String uid) throws Exception{
        ValidateCodeUtil vCode = new ValidateCodeUtil(110,33,5,100);
	    String code = vCode.drawRandomNum();
	    vCode.write(response.getOutputStream()); 
	    redisUtil.set("ver:images:" + uid, code, Long.valueOf(10 * 60));
		return null;
	}
	
	/**
	 * 图片点选验证
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/click_code", method = RequestMethod.POST)  
	public String ClickNode(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "uid", required = true) String uid) throws Exception{
        ValidateCodeUtil vCode = new ValidateCodeUtil(300,300,4,100);
	    ArrayList<Object[]> code = (ArrayList<Object[]>) vCode.clickCodes();
	    redisUtil.set("ver:images:" + uid, code, Long.valueOf(10 * 60));
	    vCode.write(response.getOutputStream()); 
		return null;
	}
	
}
