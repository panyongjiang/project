package com.uway.mobile.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.MacService;
import com.uway.mobile.util.FileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * MAS系统资产汇总
 * 
 * @author panyj
 *
 */
@RestController
@RequestMapping("MasAssets")
public class MasController {

	@Autowired
	private MacService macService;

	/**
	 * 获取MAS系统资产列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "条件查询", notes = "按条件查询mas资产")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"page_size\":\"每页显示数量\",\"page_num\":\"起始页\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/List_MasAssets", method = RequestMethod.POST)
	public Result getMasResources(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try {
			if (paraMap.get("page_size") == null) {
				paraMap.put("page_size", Constance.PAGE_SIZE);
			} else {
				paraMap.put("page_size", Integer.parseInt(paraMap.get("page_size").toString()));
			}
			if (paraMap.get("page_num") == null) {
				paraMap.put("page_num", 0);
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请传入页码！");
				return result;
			} else {
				paraMap.put("page_num", (Integer.parseInt(paraMap.get("page_num").toString()) - 1)
						* Integer.parseInt(paraMap.get("page_size").toString()));
			}
			if (Integer.parseInt(paraMap.get("page_num").toString()) < 0
					|| Integer.parseInt(paraMap.get("page_size").toString()) <= 0) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(macService.getMacesResources(paraMap));
			result.setMsg("查询成功！");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 按字段组查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "按字段分组查询", notes = "按timeRange指定的时间分组查询mas资产")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"province\":\"省份\",\"groupfields\":\"department按所属城市分组\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/groupByParm", method = RequestMethod.POST)
	public Result groupByParm(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		Object countType = paraMap.get("groupfields");
		// 返回统计前几位的分组数据
		Object timeRange = paraMap.get("timeRange");
		if (timeRange == null || "".equals(timeRange)) {
			paraMap.put("timeRange", "");
		} else {
			if (!("4 MONTH".equals(timeRange) || "6 MONTH".equals(timeRange))) {
				paraMap.put("timeRange", "");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("不支持这种统计类型：" + timeRange);
				return result;
			}
		}
		if (countType == null || "".equals(countType)) {
			paraMap.put("groupfields", "");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请传入统计字段！");
			return result;
		} else {
			// TODO 统计字段,需要做防注入
			paraMap.put("groupfields", countType.toString());
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(macService.groupByParm(paraMap));
		result.setMsg("查询成功！");

		return result;
	}

	/**
	 * 按时间条件查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "按字段分组查询", notes = "按timeRange指定的时间分组查询mas资产")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"province\":\"省份\",\"timeRange\":\"时间段\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/groupByTime", method = RequestMethod.POST)
	public Result groupByTime(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 时间分组类型
		Object unit = paraMap.get("timeRange");
		if (unit == null || "".equals(unit)) {
			paraMap.put("timeRange", "");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请传入统计类型字段！");
			return result;
		} else {
			// unit 区间数，hours周期按小时为单位
			// 需要目标表里的记录大于等于区间数，否则返回数据只有表里的记录数量，数据不全不能完成计算
			if ("6 MONTH".equals(unit)) {
				paraMap.put("months", 6);
			} else if ("4 MONTH".equals(unit)) {
				paraMap.put("months", 6);
			} else {
				paraMap.put("timeRange", "");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("不支持这种统计类型：" + unit);
				return result;
			}

		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(macService.groupByTime(paraMap));
		result.setMsg("查询成功！");

		return result;
	}

	/**
	 * 手工上传excel文件，导入新数据
	 * 
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "导入excel文件", notes = "手工导入数据到数据库")
	@ApiImplicitParam(name = "file", value = "mas资产表", required = true, dataType = "MultipartFile")
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Result addAppCheck(HttpServletRequest request,
			@RequestParam(value = "exFile", required = true) MultipartFile file) throws Exception {
		Result result = new Result();
		String fileName = file.getOriginalFilename();
		InputStream in = file.getInputStream();
		try {
			result = macService.importMasInfos(fileName, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// @ApiOperation(value = "报表导出", notes = "广西移动互联网MAS资产汇总表")
	// @ApiImplicitParam(name = "param", required = true, value =
	// "{\"department\":\"所属城市\"}", dataType = "Map<String, Object>")
	// @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public Result exportExcel(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> param) throws Exception {
		Result result = new Result();
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = macService.findAll(param);
			String excelName = "广西移动互联网MAS资产汇总表" + ".xls";
			String filename = FileUtils.downloadFilename(excelName, agent);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			// 提示
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("导出excel失败！");
		}
		return result;
	}

	@RequestMapping(value = "/get_exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, String> param = new HashMap<>();
		if (request.getParameter("department").equals("undefined")) {
			param.put("department", "");
		} else {
			param.put("department", request.getParameter("department"));
		}
		if (request.getParameter("period_month").equals("undefined")) {
			param.put("period_month", "");
		} else {
			param.put("period_month", request.getParameter("period_month"));
		}
		if (request.getParameter("ipAddress").equals("undefined")) {
			param.put("ipAddress", "");
		} else {
			param.put("ipAddress", request.getParameter("ipAddress"));
		}
		if (request.getParameter("name").equals("undefined")) {
			param.put("name", "");
		} else {
			param.put("name", request.getParameter("name"));
		}
		if (request.getParameter("operateSystemVersion").equals("undefined")) {
			param.put("operateSystemVersion", "");
		} else {
			param.put("operateSystemVersion", request.getParameter("operateSystemVersion"));
		}
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = macService.getAll(param);
			String excelName = "广西移动互联网MAS资产汇总表" + ".xls";
			String filename = FileUtils.downloadFilename(excelName, agent);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "按字段分组查询", notes = "查询mas资产总资产量")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"department\":\"所属城市\",\"period_month\":\"201701年月时间\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/queryCount", method = RequestMethod.POST)
	public Result queryCount(@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		result.setData(macService.countMasesResource(paraMap));
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	/**
	 * 按字段组查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "按字段分组查询", notes = "按timeRange指定的时间分组查询mas资产")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"province\":\"省份\",\"groupfields\":\"department按所属城市分组\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/groupByEvent", method = RequestMethod.POST)
	public Result groupByEvent(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		Object countType = paraMap.get("groupfields");
		// 返回统计前几位的分组数据
		Object timeRange = paraMap.get("timeRange");
		if (timeRange == null || "".equals(timeRange)) {
			paraMap.put("timeRange", "");
		} else {
			if (!("4 MONTH".equals(timeRange) || "6 MONTH".equals(timeRange))) {
				paraMap.put("timeRange", "");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("不支持这种统计类型：" + timeRange);
				return result;
			}
		}
		if (countType == null || "".equals(countType)) {
			paraMap.put("groupfields", "");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请传入统计字段！");
			return result;
		} else {
			// TODO 统计字段,需要做防注入
			paraMap.put("groupfields", countType.toString());
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(macService.groupByEffect(paraMap));
		result.setMsg("查询成功！");

		return result;
	}

	@ApiOperation(value = "按字段分组查询", notes = "按timeRange指定的时间分组查询mas资产")
	@ApiImplicitParam(name = "paraMap", required = true, value = "{\"province\":\"省份\",\"groupfields\":\"department按所属城市分组\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/groupByPort", method = RequestMethod.POST)
	public Result groupByPort(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		Object countType = paraMap.get("groupfields");
		// 返回统计前几位的分组数据
		Object timeRange = paraMap.get("timeRange");
		if (timeRange == null || "".equals(timeRange)) {
			paraMap.put("timeRange", "");
		} else {
			if (!("4 MONTH".equals(timeRange) || "6 MONTH".equals(timeRange))) {
				paraMap.put("timeRange", "");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("不支持这种统计类型：" + timeRange);
				return result;
			}
		}
		if (countType == null || "".equals(countType)) {
			paraMap.put("groupfields", "");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请传入统计字段！");
			return result;
		} else {
			// TODO 统计字段,需要做防注入
			paraMap.put("groupfields", countType.toString());
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(macService.groupByPort(paraMap));
		result.setMsg("查询成功！");

		return result;
	}
}
