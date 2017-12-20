package com.uway.mobile.util;

import java.util.Map;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;




public class PagingUtil {

	/**
	 * 验证分页参数
	 * 
	 * @param result
	 * @param paraMap
	 * @param name
	 */
	public static Result validatePagination(Result result,
			Map<String, Object> paraMap) {

		if (ObjectUtil.isEmpty(paraMap, "page_size")) {
			paraMap.put("page_size", Constance.PAGE_SIZE);
		} else {
			paraMap.put("page_size",
					Integer.parseInt(paraMap.get("page_size").toString()));
		}
		// 验证分页参数
		if (ObjectUtil.isEmpty(paraMap, "page_num")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请传入页码！");
			return result;
		}
		if (Integer.parseInt(paraMap.get("page_num").toString()) < 0
				|| Integer.parseInt(paraMap.get("page_size").toString()) <= 0) {
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("参数格式不正确！");
			return result;
		}
		return result;
	}

	/**
	 * 设置分页具体参数
	 * 
	 * @param paraMap
	 * @return
	 */
	public static Map<String, Object> installParameters(
			Map<String, Object> paraMap) {

		if (!ObjectUtil.isEmpty(paraMap, "page_num")) {
			paraMap.put(
					"page_num",
					(Integer.parseInt(paraMap.get("page_num").toString()) - 1)
							* Integer.parseInt(paraMap.get("page_size")
									.toString()));
		}
		return paraMap;
	}

	
	
}
