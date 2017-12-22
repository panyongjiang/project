package com.uway.tasks.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.uway.tasks.common.Constant;
import com.uway.tasks.common.Result;

public class ObjectUtil {
	/**
	 * 判断map中的key对应的value是否为空
	 * 注意此方法仅对Map<String, String>，或能够转为Map<String, String>的对象有效
	 * @param map
	 * @param key
	 * @return
	 */
	public static boolean isEmpty(Map<String, Object> map, String key){
		if(map == null){
			return true;
		}else{
			if(map.get(key) == null){
				return true;
			}else{
				String value = map.get(key).toString();
				if(StringUtils.isEmpty(value)){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	
	
	/**
	 * 检查文件类型,并返回状态
	 * @param parameter
	 * @param result
	 * @return
	 */
	public static Result checkNumberParameter(String parameter,Result result){
		if(parameter!=null&&parameter!=""){
			Pattern pattern = Pattern.compile("[0-9]*"); 
			   Matcher isNum = pattern.matcher(parameter);
			   if( !isNum.matches() ){
				   result.setCode(Constant.RESPONSE_PARAM_ERROR);
				   result.setMsg("参数格式错误");
			       return result; 
			   }
		}
		return result;
	}
}
