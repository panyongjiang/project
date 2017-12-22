package com.uway.mobile.adminController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.DomainQueryService;

@RestController
@RequestMapping("admin_domainQue")
public class AdminDomainQueryController {
	
	@Autowired
	DomainQueryService dqservice;
	
	/**
	 * 域名查询服务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/domain_query")
	public Result domainQuery(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=dqservice.selectDomain(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}

}
