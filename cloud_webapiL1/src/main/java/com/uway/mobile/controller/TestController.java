package com.uway.mobile.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.MonitorCenterService;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.util.ObjectUtil;
@RestController
@RequestMapping("testController")
public class TestController {
	@Autowired
    private EsService esService;
	@Autowired
    private SiteService siteService;
	@Autowired
	private Client esClient;
	@Autowired
	private MonitorCenterService mcService;
	@RequestMapping(value = "/get_today")
	public Result getToday(){
		Result result = new Result();
		try{
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("siteId", "103");
			esService.addToday(paraMap);
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/attack_trend")
	public Result attackTrend(){
		Result result = new Result();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			paraMap.put("siteId", "103");
			esService.attackTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
		
	}
	
	@RequestMapping(value = "/attack_top")
	public Result attackTop(){
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("siteId", "103");
			esService.attackTop(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/get_attack_top")
	public Result getAttackTop(){
		
		Result result = new Result();
		
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("siteId", "103");
			result = esService.getAttackTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 查看相应的创宇盾上的某一个域名的存活情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/view_site", method = RequestMethod.GET)
	public Result viewSite(HttpServletRequest request){
		
		Result result = new Result();
		try {
			
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("domain", request.getParameter("domain"));
			result = siteService.viewSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		return result;
	}

	@RequestMapping(value = "/view_son_site", method = RequestMethod.GET)
	public Result viewSonSite(HttpServletRequest request){
		
		Result result = new Result();
		
		try{
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("id", request.getParameter("id"));
			result = siteService.viewSonSite(paraMap);
		}catch(Exception e){
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
		}
		
		return result;
	}
	
	
	@RequestMapping(value = "/get_xml")
	public void getXml() throws Exception{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("siteId", 223);
		esService.getReportXml(paraMap);
	}
	
	@RequestMapping(value = "/active_site", method = RequestMethod.GET)
	public Result activeSite(HttpServletRequest request){
		Result result = new Result();
		try {
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("id", request.getParameter("id"));
			result = siteService.activeSite(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/active_cname", method = RequestMethod.GET)
	public Result activeCname(HttpServletRequest request){
		Result result = new Result();
		try {
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("id", request.getParameter("id"));
			result = siteService.activeCname(paraMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/get_day", method = RequestMethod.GET)
	public Result getDay(HttpServletRequest request) {
		
		Result result = new Result();
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("siteId", request.getParameter("id"));
		try {
			result = esService.getDay(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/get_web", method = RequestMethod.GET)
	public Result getWeb(HttpServletRequest request){
		Result result = new Result();
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("siteId", request.getParameter("id"));
		try{
		result = esService.getWeb(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	@RequestMapping(value = "/get_holes_type", method = RequestMethod.GET)
	public Result getHolesType(HttpServletRequest request){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Result result = new Result();
		paraMap.put("id",223);
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
	 * 站点报告列表
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_report_list")
	public Result getReportList(){
		Result result = new Result();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page_num", 1);
		paraMap.put("page_size", 10);
		paraMap.put("siteId", 223);
		paraMap.put("from_time", "");
		paraMap.put("end_time", "");
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
			result = esService.getReportBySite(paraMap);			
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
	@RequestMapping(value = "/export_user_xlsx")
	public void exportReportExcel(HttpServletResponse response) throws Exception{	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", 223);
		map.put("lastEndTime", "20170615170602");
		Workbook book = null;
		try {
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
		} catch (Exception e) {
			response.reset();
			// 设置response的header
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter()
					.print("<script type='text/javascript'>alert('该excel无权导出！');</script>");
			response.getWriter().close();
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/list_safe_site")
	public Result listAllSafeSite(HttpServletRequest request) throws Exception {
		Result result = new Result();
		try {
			return siteService.getAllSaftSite("1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
			return result;
		}
	}
	@RequestMapping(value = "/get_overall_safety_score")
	public Result getOverallSafetyScore() throws Exception {
		Result result = new Result();
		try {
			Map<String,Object> paraMap = new HashMap<String,Object>();
			//String createUser = request.getAttribute("userId").toString();
			paraMap.put("createUser", 1);
			result = mcService.getScore(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
