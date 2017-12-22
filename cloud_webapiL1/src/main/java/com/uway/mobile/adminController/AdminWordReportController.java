package com.uway.mobile.adminController;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.WordReport;
import com.uway.mobile.mapper.WordReportMapper;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.MonitorCenterService;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.util.DateUtil;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.PagingUtil;
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.SignUtil;

@RestController
@RequestMapping("word_report")
public class AdminWordReportController {
	@Autowired
	public SiteService siteService;
	@Autowired
	private WordReportMapper wordReportMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private MonitorCenterService mcService;
	@Autowired
	private EsService esService;
	@Autowired
	private MongoUtil mu;
	@Value("${spring.data.mongodb.filedb}")	
	public String FILE_DB;
	/**
	 * 月报和季报查询和生成报告
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/find_reports")
	public Result findReportsByCondition(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result = PagingUtil.validatePagination(result, paraMap);
			if (StringUtils.isNotBlank(result.getMsg())) {
				return result;
			}
			// 设置分页具体参数
			paraMap = PagingUtil.installParameters(paraMap);
			// 设置模糊查询条件
			String sd = paraMap.get("startDate").toString();
			String ed = paraMap.get("endDate").toString();
			boolean flag = true;
			if(StringUtils.isNotBlank(sd)&&!"".equals(sd)){
				paraMap.put("startDate", sd.substring(0,4)+sd.substring(5,7));
			}else{
				flag = false;
			}
			if(StringUtils.isNotBlank(ed)&&!"".equals(ed)){
				paraMap.put("endDate", ed.substring(0,4)+ed.substring(5,7));
			}else{
				flag = false;
			}
			
			if(flag){
				String type1 = paraMap.get("type1")==null?"":paraMap.get("type1").toString();
				type1 = "0".equals(type1)?"1":type1;
				String type2 = paraMap.get("type2")==null?"":paraMap.get("type2").toString();
//				String curMonth = SignUtil.getDate().substring(0, 6);
				List<String> sList = SignUtil.getMonthBetween(sd, ed);
				String seasons = "01-04-07-10";
				if("1".equals(type2)){
					for(String d:sList){
//						if(curMonth.equals(d.substring(0,4)+d.substring(5,7))){
//							continue;
//						}
						String reportTime = d.substring(0,4)+d.substring(5,7);
						Map<String,Object> m = new HashMap<String,Object>();
						m.put("reportTime", reportTime);
						m.put("siteId", paraMap.get("siteId"));
						m.put("type1", type1);
						m.put("type2", type2);
						if("2".equals(type1)){
							m.put("reportType", "S");
							if(seasons.indexOf(d.substring(5,7))!=-1){
								Date rptDate = DateUtil.getDateTime(d.substring(0,4)+d.substring(5,7)+"01", "yyyyMMdd");
								String startDate = DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -3)), "yyyyMMdd");
								String endDate = DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, 0)), "yyyyMMdd");
								m.put("reportTime", startDate.substring(0,6));
								List<WordReport> wrList = wordReportMapper.getReportWithCondition(m);
								if(wrList==null||wrList.size()==0){
									m.put("startTime", startDate);
									m.put("endTime", endDate);
									mcService.exportWafWordReport(m);
								}
							}
						}else{
							m.put("reportType", "M");
							Date rptDate = DateUtil.getDateTime(d.substring(0,4)+d.substring(5,7)+"01", "yyyyMMdd");
							String startDate = DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -1)), "yyyyMMdd");
							String endDate = DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, 0)), "yyyyMMdd");
							m.put("reportTime", startDate.substring(0,6));
							List<WordReport> wrList = wordReportMapper.getReportWithCondition(m);
							if(wrList==null||wrList.size()==0){
								m.put("startTime", startDate);
								m.put("endTime", endDate);
								mcService.exportWafWordReport(m);
							}
						}
					}
				}else{
					for(String d:sList){
//						if(curMonth.equals(d.substring(0,4)+d.substring(5,7))){
//							continue;
//						}
						Map<String,Object> m = new HashMap<String,Object>();
						m.put("siteId", paraMap.get("siteId"));
						m.put("type1", type1);
						m.put("type2", type2);
						if("2".equals(type1)){
							m.put("reportType", "S");
							if(seasons.indexOf(d.substring(5,7))!=-1){
								Date rptDate = DateUtil.getDateTime(d.substring(0,4)+d.substring(5,7)+"01", "yyyyMMdd");
								String startDate = DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -3)), "yyyyMMdd");
								String endDate = DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, 0)), "yyyyMMdd");
								m.put("reportTime", startDate.substring(0,6));
								List<WordReport> wrList = wordReportMapper.getReportWithCondition(m);
								if(wrList==null||wrList.size()==0){
									m.put("startTime", startDate);
									m.put("endTime", endDate);
									m.put("preMonthST", DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -6)), "yyyyMMdd"));
									m.put("preMonthET", DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, -3)), "yyyyMMdd"));
									m.put("fromTime", startDate+"000000");
									m.put("toTime", endDate+"235959");
									esService.exportWordReport(m);
								}
							}
						}else{
							m.put("reportType", "M");
							Date rptDate = DateUtil.getDateTime(d.substring(0,4)+d.substring(5,7)+"01", "yyyyMMdd");
							String startDate = DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -1)), "yyyyMMdd");
							String endDate = DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, 0)), "yyyyMMdd");
							m.put("reportTime", startDate.substring(0,6));
							List<WordReport> wrList = wordReportMapper.getReportWithCondition(m);
							if(wrList==null||wrList.size()==0){
								m.put("startTime", startDate);
								m.put("endTime", endDate);
								m.put("preMonthST", DateUtil.getStringTime(DateUtil.getFirstDateOfMonth(DateUtil.getDiffMonth(rptDate, -2)), "yyyyMMdd"));
								m.put("preMonthET", DateUtil.getStringTime(DateUtil.getLastDateOfMonth(DateUtil.getDiffMonth(rptDate, -1)), "yyyyMMdd"));
								m.put("fromTime", startDate+"000000");
								m.put("toTime", endDate+"235959");
								esService.exportWordReport(m);
							}
						}
					}
				}
				
			}
			
			String type1 = paraMap.get("type1").toString();
			String type2 = paraMap.get("type2").toString();
			if("0".equals(type1)){
				paraMap.put("type1", null);
			}
			if("0".equals(type2)){
				paraMap.put("type2", null);
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("details", mcService.findReportsByCondition(paraMap));
			map.put("total_num", mcService.countReportByCondition(paraMap));
//			List<WordReport> reportList = mcService.findReportsByCondition(paraMap);
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}

		return result;
	}
	/**
	 * word下载
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/download_word_report")
	public Result downloadWordReport(HttpServletResponse response,@RequestParam(value = "fileId", required = true) String fileId){
		Result result = new Result();
		try {
			DB db = mu.getDB(FILE_DB);
			GridFS gridFS =new GridFS(db);
			DBObject query  = new BasicDBObject("_id", fileId);
			GridFSDBFile gridFSDBFile = gridFS.findOne(query);
			String fileName = gridFSDBFile.get("filename").toString();
			//清空response
			response.reset();
			//设置response的header
			response.setContentType("application/octet-stream");
			response.setHeader("Access-Control-Allow-Origin", "*");
			String downLoadName = new String(fileName.getBytes("gbk"),
					"iso8859-1");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ downLoadName);
			OutputStream out = response.getOutputStream();
			gridFSDBFile.writeTo(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 监测与防护切换站点下拉框
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_sites_user_type")
	public Result getSitesByUserAndType(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
//			String sid = request.getHeader("sid");
//			String sessionId = Constance.REDIS_USER_PRE + sid;
//			String userId = redisUtil.get(sessionId).toString();
//			paraMap.put("userId", userId);
			String type2 = paraMap.get("type2").toString();
			String type = "1".equals(type2)?"1":"0";
			paraMap.put("type",type);
			List<Map<String,Object>> sites = siteService.getSitesByUserAndType(paraMap);
			result.setData(sites);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误!");
		}
		return result;
	}
	}
