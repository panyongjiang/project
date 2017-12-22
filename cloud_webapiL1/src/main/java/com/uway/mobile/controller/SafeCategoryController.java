package com.uway.mobile.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.SafeCategoryService;

@RestController
@RequestMapping("safe_service")
public class SafeCategoryController extends BaseApplication {
	@Autowired
	private SafeCategoryService safeCategoryService;

	@RequestMapping(value = "/list_user_safe_service", method = RequestMethod.POST)
	public Result listArtilceCategory(HttpServletRequest request) throws Exception {
		Result result = new Result();
		try {
			String userId = request.getAttribute("userId").toString();
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			result.setData(safeCategoryService.getSafeServiceByUser(userId));
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}
}
