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
import com.uway.mobile.service.VirusStrategyService;
import com.uway.mobile.util.FileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("virusstrategy")
public class VirusStrategyController {
	
	@Autowired
	private VirusStrategyService virusStrategyService;
	
	@ApiOperation(value="条件查询", notes="防护病毒不符合要求")
    @ApiImplicitParam(name = "paraMap", value = "{\"page_size\":\"1\",\"page_num\":\"10\",\"beginTime\":\"\",\"endTime\":\"\","
    		+ "\"period_month\":\"\",\"ip\":\"\",\"pc_name\":\"\",\"virusversion\":\"\",\"clienttype\":\"\""
    		+ ",\"system\":\"\",\"groups\":\"\",\"city\":\"\",\"timeRange\":\"\"}", required = true, 
    		defaultValue ="{\"page_size\":\"1\",\"page_num\":\"10\",\"beginTime\":\"\",\"endTime\":\"\"}",
    dataType = "Map<String, Object>")
	@RequestMapping(value = "/List_VirusStrategy", method = RequestMethod.POST)
	public Result getTerminal(@RequestBody Map<String, Object> paraMap) {
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
			result.setData(virusStrategyService.getVirusStrategy(paraMap));
			result.setMsg("查询成功！");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	

 	@ApiOperation(value = "报表导出", notes = "终端命名表")
 	@ApiImplicitParam(name = "paraMap", value = "{\"beginTime\":\"\",\"endTime\":\"\",\"city\":\"责任单位\",}", required = true, 
				defaultValue ="{\"city\":\"南宁\",\"beginTime\":\"\",\"endTime\":\"\"}",dataType = "Map<String, Object>")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public Result exportExcel(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = virusStrategyService.createExcel(paraMap);
			String excelName = "病毒防护不符合要求情况" + ".xls";
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
			workbook = virusStrategyService.createExcel(param);
			String excelName = "病毒防护不符合要求情况" + ".xls";
			String filename = FileUtils.downloadFilename(excelName, agent);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	/**
	 * 按字段组查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value = "按字段分组查询", notes = "按groupfields指定的字段分组查询防护病毒不符合要求列表")
	@ApiImplicitParam(name = "paraMap", value = "{\"groupfields\":\"\",\"beginTime\":\"\",\"endTime\":\"\","
    		+ "\"period_month\":\"\",\"ip\":\"\",\"pc_name\":\"\",\"virusversion\":\"\",\"clienttype\":\"\""
    		+ ",\"system\":\"\",\"groups\":\"\",\"city\":\"\",\"timeRange\":\"\"}", defaultValue = "{\"groupfields\":\"department\",\"timeRange\":\"6 MONTH\"}", required = true, dataType = "Map<String, Object>")
	@RequestMapping(value = "/groupByParm", method = RequestMethod.POST)
	public Result groupByParm(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		// 分组字段，可用逗号隔开多个分组字段
		Object countType = paraMap.get("groupfields");
		// 返回统计前几位的分组数据
		/*
		 * //Object topn = paraMap.get("topn");
		 * 
		 * Object timeRange = paraMap.get("timeRange"); if (timeRange == null ||
		 * "".equals(timeRange)) { paraMap.put("timeRange", ""); } else { if (!(
		 * "1 HOUR".equals(timeRange) || "6 HOUR".equals(timeRange) || "1 DAY"
		 * .equals(timeRange) || "1 MONTH".equals(timeRange))) {
		 * paraMap.put("timeRange", "");
		 * result.setCode(Constance.RESPONSE_PARAM_ERROR);
		 * result.setMsg("不支持这种统计类型：" + timeRange); return result; } }
		 * 
		 * if (topn == null) { paraMap.put("topn", Constance.TOP_N); } else {
		 * paraMap.put("topn", Integer.parseInt(topn.toString())); }
		 */
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
		result.setData(virusStrategyService.groupByParm(paraMap));
		result.setMsg("查询成功！");

		return result;
	}
	
	
	/**
	 * 按时间组查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@ApiOperation(value="按时间分组查询", notes="按timeRange指定的时间分组查询互联网暴露资产")
    @ApiImplicitParam(name = "paraMap", value = "查询请求参数", required = true, 
    		defaultValue ="{\"province\":\"广西\",\"timeRange\":\"6 MONTH\"}",
    dataType = "Map<String, Object>")
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
			}  else {
				paraMap.put("timeRange", "");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("不支持这种统计类型：" + unit);
				return result;
			}

		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(virusStrategyService.groupByTime(paraMap));
		result.setMsg("查询成功！");

		return result;
	}


	/**
	 * 手工上传excel文件，导入新数据
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="导入excel文件", notes="手工导入数据到数据库")
    @ApiImplicitParam(name = "file", value = "导入文件", required = true, dataType = "MultipartFile")
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Result addAppCheck(HttpServletRequest request,
			@RequestParam(value = "exFile", required = true) MultipartFile file) throws Exception{
		Result result = new Result();
		String fileName = file.getOriginalFilename();
		InputStream in = file.getInputStream();
	
		result  = virusStrategyService.importVirusStrategy(fileName, in);
		return result;
	}
}
