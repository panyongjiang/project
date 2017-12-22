package com.uway.mobile.adminController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.CodeService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("admin_code")
public class AdminCodeController {

	@Autowired
	public CodeService codeService;
	
	@RequestMapping(value = "/add_code")
	public Result addCode(HttpServletRequest request){
		Result result = new Result();
		String userId = request.getAttribute("userId").toString();
		try {
			result = codeService.addCodes(userId);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/list_code")
	public Result listCode(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
		if (ObjectUtil.isEmpty(paraMap, "page_num")) {
			result.setMsg("页码不能为空！");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			return result;
		}
		String pageNum = paraMap.get("page_num").toString();
		String pageSize = "" + Constance.PAGE_SIZE;
		if(!ObjectUtil.isEmpty(paraMap, "page_size")){
			pageSize = paraMap.get("page_size").toString();
		}
		paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
		paraMap.put("pageSize", Integer.parseInt(pageSize));
		
		if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("参数格式不正确！");
			return result;
		}
		
			result = codeService.listCodes(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
}
