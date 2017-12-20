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
import com.uway.mobile.service.LoopholeService;
import com.uway.mobile.util.FileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("loophole")
public class LoopholeController {
	@Autowired
	public LoopholeService loopholeService;

	/**
	 * 按字段组查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "条件查询", notes = "漏洞数据汇总")
	@ApiImplicitParam(name = "paraMap", value = "{\"beginTime\":\"\",\"endTime\":\"\"}", required = true, defaultValue = "{\"beginTime\":\"\",\"endTime\":\"\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/getLoopholeSummary", method = RequestMethod.POST)
	public Result groupByParm(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		/*
		 * Object countType = paraMap.get("time"); if (countType == null ||
		 * "".equals(countType)) { paraMap.put("time", "");
		 * result.setCode(Constance.RESPONSE_PARAM_EMPTY);
		 * result.setMsg("请传入统计字段！"); return result; } else { // TODO
		 * 统计字段,需要做防注入 paraMap.put("groupfields", countType.toString()); }
		 */
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(loopholeService.getLoopholeSummary(paraMap));
		result.setMsg("查询成功！");
		return result;
	}

	@ApiOperation(value = "条件查询", notes = "占比Top5")
	@ApiImplicitParam(name = "paraMap", value = "{\"beginTime\":\"\",\"endTime\":\"\"}", required = true, defaultValue = "{\"beginTime\":\"\",\"endTime\":\"\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/Countloopholebyunits", method = RequestMethod.POST)
	public Result Countloopholebyunits(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		/*
		 * Object countType = paraMap.get("time"); if (countType == null ||
		 * "".equals(countType)) { paraMap.put("time", "");
		 * result.setCode(Constance.RESPONSE_PARAM_EMPTY);
		 * result.setMsg("请传入统计字段！"); return result; } else { // TODO
		 * 统计字段,需要做防注入 paraMap.put("groupfields", countType.toString()); }
		 */
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(loopholeService.Countloopholebyunits(paraMap));
		result.setMsg("查询成功！");
		return result;
	}

	@ApiOperation(value = "条件查询", notes = "风险等级占比")
	@ApiImplicitParam(name = "paraMap", value = "{\"beginTime\":\"\",\n\"endTime\":\"\"}", required = true, defaultValue = "{\"beginTime\":\"\",\"endTime\":\"\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/reskpropor", method = RequestMethod.POST)
	public Result reskpropor(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		/*
		 * Object countType = paraMap.get("time"); if (countType == null ||
		 * "".equals(countType)) { paraMap.put("time", "");
		 * result.setCode(Constance.RESPONSE_PARAM_EMPTY);
		 * result.setMsg("请传入统计字段！"); return result; } else { // TODO
		 * 统计字段,需要做防注入 paraMap.put("groupfields", countType.toString()); }
		 */
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(loopholeService.reskpropor(paraMap));
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
	@ApiImplicitParam(name = "file", value = "导入文件", required = true, dataType = "MultipartFile")
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Result addAppCheck(HttpServletRequest request,
			@RequestParam(value = "exFile", required = true) MultipartFile file) throws Exception {
		Result result = new Result();
		String fileName = file.getOriginalFilename();
		InputStream in = file.getInputStream();
		result = loopholeService.importLoophole(fileName, in, 1);
		return result;
	}

	@ApiOperation(value = "报表导出", notes = "漏洞汇总表")
	@ApiImplicitParam(name = "paraMap", value = "{\"beginTime\":\"\",\"endTime\":\"\"}", required = true, defaultValue = "{\"page_size\":\"1\",\"page_num\":\"10\",\"beginTime\":\"\",\"endTime\":\"\"}", dataType = "Map<String, Object>")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public Result exportExcel(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = loopholeService.createExcel(paraMap);
			String excelName = "二三级漏洞汇总信息" + ".xls";
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
		Map<String, Object> param = new HashMap<>();

		if (request.getParameter("beginTime").equals("undefined")) {
			param.put("beginTime", "");
		} else {
			param.put("beginTime", request.getParameter("beginTime"));
		}
		if (request.getParameter("endTime").equals("undefined")) {
			param.put("endTime", "");
		} else {
			param.put("endTime", request.getParameter("endTime"));
		}

		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = loopholeService.createExcel(param);
			String excelName = "二三级漏洞汇总信息" + ".xls";
			String filename = FileUtils.downloadFilename(excelName, agent);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
