package com.uway.mobile.adminController;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ResourceService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.PagingUtil;

@RestController
@RequestMapping("resource")
public class AdminResourceController {
	@Autowired
	private ResourceService resourceService;

	/**
	 * 根据分页参数获取资源
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_all_resource_limit", method = RequestMethod.POST)
	public Result getAllResourceLimit(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try {
			// 验证分页参数
			result = PagingUtil.validatePagination(result, paraMap);
			if (StringUtils.isNotBlank(result.getMsg())) {
				return result;
			}
			// 设置分页具体参数
			paraMap = PagingUtil.installParameters(paraMap);
			// 设置模糊查询条件
			if (!ObjectUtil.isEmpty(paraMap, "resource_name")) {
				paraMap.put("resource_name", "%"
						+ paraMap.get("resource_name").toString() + "%");
			}
			if (!ObjectUtil.isEmpty(paraMap, "resource_url")) {
				paraMap.put("resource_url", "%"
						+ paraMap.get("resource_url").toString() + "%");
			}
			if (!ObjectUtil.isEmpty(paraMap, "resource_id")) {
				paraMap.put("resource_id", paraMap.get("resource_id").toString());
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("details", resourceService.getAllResource(paraMap));
			map.put("total_num", resourceService.countResources(paraMap));
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据指定资源id 赋予权限给用户
	 * 
	 * @param paramap
	 * @return
	 */
	@RequestMapping(value = "/grant_resource_Authority",  method = RequestMethod.POST)
	public Result grantAuthorityByUserId(
			@RequestBody Map<String, Object> paramap) {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paramap, "resource_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择资源");
			return result;
		}
		if (ObjectUtil.isEmpty(paramap, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择需要赋予权限的管理员");
			return result;
		}
		try {
			return resourceService.saveUserAuthority(paramap);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}

	}
	
	
	/**
	 * 获取所有资源
	 * 
	 * @param paramap
	 * @return
	 */
	@RequestMapping(value = "/get_all_resource",  method = RequestMethod.POST)
	public Result getAllResource() {
		Result result = new Result();
		try {
			return resourceService.getAllResource(result);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}

	}
	
	
	/**
	 * 根据指定资源id 赋予权限给用户组
	 * 
	 * @param paramap
	 * @return
	 */
	@RequestMapping(value = "/grant_resource_Authority_togroup",  method = RequestMethod.POST)
	public Result grantAuthorityByGroups(
			@RequestBody Map<String, Object> paramap) {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paramap, "resource_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择资源");
			return result;
		}
		if (ObjectUtil.isEmpty(paramap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择需要赋予权限的用户组");
			return result;
		}
		try {
			return resourceService.saveGroupAuthority(paramap,result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}

	}


}
