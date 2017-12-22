package com.uway.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.IndustryService;

@RestController
@RequestMapping("industry")
public class IndustryController {

	@Autowired
	public IndustryService industryService;
	
	/**
	 * 获取行业列表
	 * @return
	 */
	@RequestMapping(value = "/get_industry", method = RequestMethod.POST)
	public Result getIndustry(){
		Result result = new Result();
		try {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(industryService.getIndustry());
			result.setMsg("查询成功！");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
}
