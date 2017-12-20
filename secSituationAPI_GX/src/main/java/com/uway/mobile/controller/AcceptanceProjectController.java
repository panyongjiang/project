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
import com.uway.mobile.service.AcceptanceProjectService;
import com.uway.mobile.util.FileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("acceptanceproject")
public class AcceptanceProjectController {
	@Autowired
	public AcceptanceProjectService acceptanceProjectService;
	

	@ApiOperation(value="条件查询", notes="按条件查询入网项目详情表格(包含签字率)")
    @ApiImplicitParam(name = "paraMap", value = "{\"page_size\":\"10\",\"page_num\":\"1\",\"time\":\"2\",\"page_num\":\"1\",\"unit\":\"责任单位\",\"sign\":\"是否签字\",\"filing\":\"是否备案\",\"status\":\"是否验收通过\"}", required = true, 
    		defaultValue ="{\"time\":\"2月\",\"page_num\":\"10\"}",
    dataType = "Map<String, Object>")
	@RequestMapping(value = "/List_Acceptance_All", method = RequestMethod.POST)
	public Result getAcceptanceAll(@RequestBody Map<String, Object> paraMap) {
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
			result.setData(acceptanceProjectService.getAcceptanceAll(paraMap));
			result.setMsg("查询成功！");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	
	@ApiOperation(value="条件查询", notes="按条件查询入网项目详情表格")
    @ApiImplicitParam(name = "paraMap", value = "{\"page_size\":\"10\",\"page_num\":\"1\",\"time\":\"2\",\"page_num\":\"1\",\"unit\":\"责任单位\",\"sign\":\"是否签字\",\"filing\":\"是否备案\",\"status\":\"是否验收通过\"}", required = true, 
    		defaultValue ="{\"time\":\"2\",\"page_num\":\"10\"}",
    dataType = "Map<String, Object>")
	@RequestMapping(value = "/List_Acceptance", method = RequestMethod.POST)
	public Result getAcceptance(@RequestBody Map<String, Object> paraMap) {
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
			result.setData(acceptanceProjectService.getAcceptance(paraMap));
			result.setMsg("查询成功！");
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
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
		//String fileName="C:/Users/huangy/Desktop/广西态势感知服务项目三期/数据/3．2017年2月全区入网验收项目完成情况汇总表.xlsx";
		//InputStream in=new FileInputStream(fileName);
		result  = acceptanceProjectService.importAcceptance(fileName, in, 1);
		return result;	
		}
	
	

 	@ApiOperation(value = "报表导出", notes = "入网验收表")
 	@ApiImplicitParam(name = "paraMap", value = "{\"time\":\"\",\"city\":\"责任单位\",\"status\":\"整改情况\",\"sign\":\"是否签字\",\"filing\":\"是否备案\"}", required = true, 
				defaultValue ="{\"page_size\":\"1\",\"page_num\":\"10\",\"city\":\"南宁\"}",dataType = "Map<String, Object>")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
	public Result exportExcel(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = acceptanceProjectService.createExcel(paraMap);
			String excelName = "入网验收数据" + ".xls";
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
		
		if (request.getParameter("time").equals("undefined")) {
			param.put("time", "");
		} else {
			param.put("time", request.getParameter("time"));
		}
		if (request.getParameter("unit").equals("undefined")) {
			param.put("unit", "");
		} else {
			param.put("unit", request.getParameter("unit"));
		}
		if (request.getParameter("sign").equals("undefined")) {
			param.put("sign", "");
		} else {
			param.put("sign", request.getParameter("sign"));
		}
		if (request.getParameter("filing").equals("undefined")) {
			param.put("filing", "");
		} else {
			param.put("filing", request.getParameter("filing"));
		}
		if (request.getParameter("status").equals("undefined")) {
			param.put("status", "");
		} else {
			param.put("status", request.getParameter("status"));
		}
		
		String agent = request.getHeader("user-agent");
		HSSFWorkbook workbook = null;
		try {
			workbook = acceptanceProjectService.createExcel(param);
			String excelName = "入网验收数据" + ".xls";
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
