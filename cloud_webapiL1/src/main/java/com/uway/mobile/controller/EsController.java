package com.uway.mobile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.EsService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("es")
public class EsController {

	@Autowired
    private EsService esService;
	
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 获取网站每日数据
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_day", method = RequestMethod.POST)
	public Result getDay(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getDay(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/get_day_all")
	public Result getDayAll(HttpServletRequest request){
		Result result = new Result();
		try{
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("createUser", request.getAttribute("userId").toString());
			result = esService.getDayAll(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	/**
	 * 获取每日web流量数
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_web", method = RequestMethod.POST)
	public Result getWeb(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try{
		result = esService.getWeb(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 获取每日cc攻击信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_cc", method = RequestMethod.POST)
	public Result getCC(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "siteId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getWebcc(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 根据网站id和时间获取攻击归属地信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_top", method = RequestMethod.POST)
	public Result getAttackTop(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getAttackTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 获取网站攻击防御信息
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend", method = RequestMethod.POST)
	public Result getAttackTrend(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getAttackTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
		
	/**
	 * 攻击类型分类汇总统计
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend_condition", method = RequestMethod.POST)
	public Result getAttackTrendWithCondition(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getAttackTrendWithCondition(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
		
	/**
	 * 攻击者来源统计
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_count", method = RequestMethod.POST)
	public Result getAttackCount(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getAttackCount(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 获取网站攻击防御趋势
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_trend", method = RequestMethod.POST)
	public Result getTrand(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try {
			result = esService.getTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	/**
	 * 获取王网站攻击详情
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_detail", method = RequestMethod.POST)
	public Result getAttackDetail(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "siteId")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		
		try{
			result = esService.getAttackDetail(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * cc攻击详情
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/cc_detail", method = RequestMethod.POST)
	public Result ccDetail(@RequestBody Map<String, Object>paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "siteId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			esService.ccDetail(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/get_xml", method = RequestMethod.POST)
	public Result getReportXml() throws Exception{
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("siteId", "25");
		esService.getReportXml(paraMap);
		return null;
	}
	/**
	 * 暂未使用
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/view_site_risk")
	public Result viewSiteRisk(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站不能为空");
			return result;
		}
		try{
			result = esService.getSiteRisk(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		
		return result;
	}
	/**
	 * 获取所有高危漏洞
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_vuls")
	public Result getVuls(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getVuls(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 获取所有漏洞分类
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_holes")
	public Result getHoles(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getHoles(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 获取漏洞风险值信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get_all_hole")
	public Result getAllHole(HttpServletRequest request){
		Result result = new Result();
        try {
        	
        	String userId = request.getAttribute("userId").toString();
        	
        	result = esService.getAllSiteHole(userId);
        	return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}
	/**
	 * 获取安全监测趋势
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_risk_trend")
	public Result getRiskTrend(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getRiskTrend(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 挂马集合
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_vul_keypage")
	public Result getVulKeypage(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getVulKeypage(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 发生篡改URL
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_change_url")
	public Result getChangeUrl(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getChangeUrl(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 敏感词检测结果
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_keyword")
	public Result getKeyword(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getKeyword(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 平稳度检测结果
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_smooth")
	public Result getSmooth(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getSmooth(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 漏洞检测结果
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_hole")
	public Result getHole(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (Integer.parseInt(paraMap.get("page_num").toString()) < 1){
				result.setMsg("页码错误！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (ObjectUtil.isEmpty(paraMap, "page_size")) {
				result.setMsg("每页显示条数不能为空不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			result = esService.getHole(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 最近两个周期扫描漏洞的数据环比
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_vul_link")
	public Result getVulLink(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getVulLink(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 平稳度变化趋势图
	 * @param paraMap
	 * @return
	 */
	
	@RequestMapping(value = "/get_smooth_data")
	public Result getSmoothData(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getSmoothData(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 漏洞分类与统计
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_holes_type")
	public Result getHolesType(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();		
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getHolesType(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 高危漏洞top10
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/high_risk_top10")
	public Result highRiskTop(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			if(ObjectUtil.isEmpty(paraMap, "id")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("网站不能为空");
				return result;
			}
			result = esService.getHighRiskTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 站点报告列表
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_report_list")
	public Result getReportList(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		if (ObjectUtil.isEmpty(paraMap, "page_num")) {
			result.setMsg("页码不能为空！");
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			return result;
		}
		String pageNum = paraMap.get("page_num").toString();
		String pageSize = "" + Constance.PAGE_SIZE;
		if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
			pageSize = paraMap.get("page_size").toString();
		}
		paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
		paraMap.put("pageSize", Integer.parseInt(pageSize));
		if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("参数格式不正确！");
			return result;
		}
		try {
			/*if(ObjectUtil.isEmpty(paraMap, "id")){
				result = esService.getAllReport(paraMap);
			}else{*/
				result = esService.getReportBySite(paraMap);
			//}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}		
		return result;
	}
	
	/**
	 * 根据站点id导出excel报告
	 * 
	 * @param map
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export_user_xlsx", method = RequestMethod.GET)
	public void exportReportExcel(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "last_end_time", required = true) String last_end_time,
			HttpServletResponse response,
			@RequestParam(value = "sid", required = true) String sid)
					throws Exception{	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", id);
		map.put("lastEndTime", last_end_time);
		Workbook book = null;
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("sid", sid);
			boolean flag = false;
			if(StringUtils.isEmpty(sid)){
				flag = true;
			}
			String sessionId = Constance.REDIS_USER_PRE + sid;
			if (redisUtil.get(sessionId) == null
					|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
				flag = true;
			}			
			if(flag){
				//清空response
				response.reset();
				//设置response的header
				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.getWriter().print("<script type='text/javascript'>alert('请登录！');</script>");
				response.getWriter().close();
			}else{
				book = esService
						.exporttReportXlsx(esService.getData(map));
	
				if (book == null) {
					return;
				}
				String excelName = UUID.randomUUID().toString() + ".xls";
				// 清空response
				response.reset();
				response.setContentType("application/octet-stream");// 定义输出类型
				String downLoadName = new String(excelName.getBytes("gbk"),
						"iso8859-1");
				response.setHeader("Content-Disposition","attachment; filename="
						+ downLoadName);// 设定输出文件头
				OutputStream os = response.getOutputStream();// 取得输出流
				book.write(os);
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			response.reset();
			// 设置response的header
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter()
					.print("<script type='text/javascript'>alert('数据异常！');</script>");
			response.getWriter().close();
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/exportWordReport")
	public Result exportWordReport(HttpServletRequest request,HttpServletResponse resp,@RequestParam(value = "id", required = true) String id){
		Result result = new Result();
		try {
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("siteId", id);
			result = esService.exportWordReport(paraMap);
			
			File downloadFile = (File)result.getData();
			  
	        ServletContext context = request.getServletContext();  
	  
	        String mimeType = context.getMimeType(downloadFile.getPath());  
	        if (mimeType == null) {  
	            mimeType = "application/octet-stream";  
	        }  
	  
	        resp.setContentType(mimeType);  
	        resp.setContentLength((int) downloadFile.length());  
	  
	        String headerKey = "Content-Disposition";  
	        String headerValue = String.format("attachment; filename=\"%s\"",  
	                downloadFile.getName());  
	        resp.setHeader(headerKey, headerValue); 
	        InputStream myStream = new FileInputStream(downloadFile.getPath());  
            IOUtils.copy(myStream, resp.getOutputStream());  
            myStream.close();
            resp.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		result.setData("");
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("导出成功！");
		return result;
	}
	
}
