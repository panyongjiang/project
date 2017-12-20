package com.uway.mobile.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("checkValidate")
public class CheckValidateController {
	@Autowired
	public RedisUtil redisUtil;
	
	/**
	 * 字符串验证码
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value="/check_validate", method = RequestMethod.POST)
	public Result checkValidate(HttpServletRequest request, @RequestBody Map<String, String> paraMap){
		Result result = new Result();
		if(!StringUtils.isEmpty(paraMap.get("uid"))){
			Object obj = redisUtil.get("ver:images:" + paraMap.get("uid").toString());
			if(obj == null || "".equals(obj.toString())){
				result.setMsg("请刷新验证码！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}else{
				if(!paraMap.get("ver_code").equals(obj.toString().toLowerCase())){
					result.setMsg("验证码不正确！");
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					return result;
				}
			}
		}else{
			result.setMsg("uid错误或验证码未存入session");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			return result;
		}
		result.setMsg("验证码正确");
		result.setCode(Constance.RESPONSE_SUCCESS);
		return result;
		
	}
	
	/**
	 * 图片点选验证码
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/check_click", method = RequestMethod.POST)
	public Result checkClick(HttpServletRequest request, @RequestBody Map<String, Object> paraMap ){
		Result result = new Result();
		if(!StringUtils.isEmpty(paraMap.get("uid").toString())){
			ArrayList<Object[]> obj = (ArrayList<Object[]>)redisUtil.get("ver:images:" + paraMap.get("uid").toString());
			if(obj == null || "".equals(obj.toString())){
				result.setMsg("请刷新验证码！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}else{
				String str = paraMap.get("ver_code").toString().replace("=", ":");
		    	JSONArray jsarr = JSON.parseObject(str).getJSONArray("value");
		    	int count=0;
		    	if(jsarr.size()==4){
		    		for(int i=0;i<jsarr.size();i++){
						JSONObject jj=jsarr.getJSONObject(i);
						Object[] obj2=(Object[])obj.get(i);
						Object[] obj1={jj.getString("x"),jj.getString("y")};
						if( (Integer.parseInt(obj2[0].toString())-20<Integer.parseInt(obj1[0].toString()))&&
							(Integer.parseInt(obj2[0].toString())+20>Integer.parseInt(obj1[0].toString()))&&
							(Integer.parseInt(obj2[1].toString())-20<Integer.parseInt(obj1[1].toString()))&&
							(Integer.parseInt(obj2[1].toString())+20>Integer.parseInt(obj1[1].toString()))
						)
						{
							count++;
						}
					}
		    		if(count==4){
		    			result.setMsg("验证码正确！");
						result.setCode(Constance.RESPONSE_SUCCESS);
						return result;
		    		}
		    	}
			}
		}else{
			result.setMsg("uid错误或验证码未存入session");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			return result;
		}
		result.setMsg("验证码错误");
		result.setCode(Constance.RESPONSE_AUTH_ERROR);
		return result;
	}

}
