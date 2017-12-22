package com.uway.mobile.adminController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.SafeCategoryService;
import com.uway.mobile.util.DateUtil;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("admin_product")
public class AdminProductController {
	@Autowired
	private SafeCategoryService safeCategoryService;

	/**
	 * 产品服务信息展示
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_service_by_user", method = RequestMethod.POST)
	public Result getServiceByUser(@RequestBody Map<String, String> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			String userId = paraMap.get("user_id");
			if (StringUtils.isEmpty(userId)) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			Map<String, Object> list = safeCategoryService
					.getSafeServiceByUser(userId);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			result.setData(list);
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 修改产品生效的时间
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upd_validate_time", method = RequestMethod.POST)
	public Result updValidateTime(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "type")) {
				result.setMsg("服务类型不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String type = paraMap.get("type").toString();
			if (ObjectUtil.isEmpty(paraMap, "user_id")) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "start_time")
					|| ObjectUtil.isEmpty(paraMap, "end_time")) {
				result.setMsg("起止时间不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			} else {
				// 时间校验
				String startTime = paraMap.get("start_time").toString();
				String endTime = paraMap.get("end_time").toString();

				Date startDate = DateUtil.getDateTime(startTime,
						"yyyy-MM-dd HH:mm:ss");
				Date endDate = DateUtil.getDateTime(endTime,
						"yyyy-MM-dd HH:mm:ss");
				if (startDate.getTime() >= endDate.getTime()) {
					result.setMsg("开始时间不得大于结束时间!");
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					return result;
				}
				paraMap.put("startTime",
						Timestamp.valueOf(paraMap.get("start_time").toString()));
				paraMap.put("endTime",
						Timestamp.valueOf(paraMap.get("end_time").toString()));
			}
			if (!type.equals("app") && !type.equals("site")
					&& !type.equals("expert") && !type.equals("waf")) {
				result.setMsg("type类型有误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			safeCategoryService.updSafeService(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 开通服务
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/start_service", method = RequestMethod.POST)
	public Result startService(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "type")) {
				result.setMsg("服务类型不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String type = paraMap.get("type").toString();
			if (ObjectUtil.isEmpty(paraMap, "user_id")) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (!type.equals("app") && !type.equals("site")
					&& !type.equals("expert") && !type.equals("waf")) {
				result.setMsg("type类型有误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			safeCategoryService.startSafeService(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 开通试用服务
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/trial_service", method = RequestMethod.POST)
	public Result trialService(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "type")) {
				result.setMsg("服务类型不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String type = paraMap.get("type").toString();
			if (ObjectUtil.isEmpty(paraMap, "user_id")) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (!type.equals("app") && !type.equals("site")
					&& !type.equals("expert") && !type.equals("waf")) {
				result.setMsg("type类型有误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			safeCategoryService.trialSafeService(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 关闭服务
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/off_service", method = RequestMethod.POST)
	public Result offService(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "type")) {
				result.setMsg("服务类型不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String type = paraMap.get("type").toString();
			if (ObjectUtil.isEmpty(paraMap, "user_id")) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (!type.equals("app") && !type.equals("site")
					&& !type.equals("expert") && !type.equals("waf")) {
				result.setMsg("type类型有误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			safeCategoryService.offSafeService(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}
}
