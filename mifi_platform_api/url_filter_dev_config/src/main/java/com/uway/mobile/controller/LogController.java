package com.uway.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.DeviceService;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.LogService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.Pagination;
import com.uway.mobile.util.PagingUtil;

@RestController
@RequestMapping("log")
public class LogController extends BaseApplication{
	
	@Autowired
	private LogService logService;
	@Autowired
	private EsService esService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "findLogsWithPage", method = RequestMethod.POST)
	public Result findLogsWithPage(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			result = PagingUtil.validatePagination(result, paraMap);
			if (StringUtils.isNotBlank(result.getMsg())) {
				return result;
			}
			// 设置分页具体参数
			paraMap = PagingUtil.installParameters(paraMap);
			String userId = request.getAttribute("userId").toString();
			paraMap.put("userId", userId);
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("details", logService.getLogsWithPage(paraMap));
			map.put("total_num", logService.getLogsTotalCount(paraMap));
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			log.debug("findLogsWithPage", e);
		}
		return result;
	}
	
	@RequestMapping(value = "findInterceptInfoWithPage", method = RequestMethod.POST)
	public Result findInterceptInfoWithPage(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(paraMap.get("pageNo")==null){
				paraMap.put("pageNo", "1");
			}
			if(paraMap.get("pageSize")==null){
				paraMap.put("pageSize", "10");
			}
			String userId = request.getAttribute("userId").toString();
			Map<String,String> parMap=new HashMap<String,String>();
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()==1){
				parMap.put("role", "1");
			}
			parMap.put("companyId", String.valueOf(adminUser.getCompanyId()));
			List<Device> deviceList = deviceService.getDevicesByUserId(parMap);
			if(deviceList==null||deviceList.size()<=0){
				result.setData("");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("未接入设备或设备未激活");
				return result;
			}
			Pagination page = esService.getInterceptInfos(paraMap,deviceList);
			result.setData(page);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			log.debug("findInterceptInfoWithPage", e);
		}
		return result;
	}

}
