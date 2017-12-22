package com.uway.mobile.controller;

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
import com.uway.mobile.util.DateUtil;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.PagingUtil;
import com.uway.mobile.util.SignUtil;

@RestController
@RequestMapping("monitorCenter")
public class MonitorCenterController {
	@Autowired
	private MonitorCenterService mcService;
	@Autowired
	private EsService esService;
	@Autowired
	private WordReportMapper wordReportMapper;
	@Autowired
	private MongoUtil mu;
	@Value("${spring.data.mongodb.filedb}")
	public String FILE_DB;

	/**
	 * 门户-监测中心-整体安全得分
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_overall_safety_score")
	public Result getOverallSafetyScore(HttpServletRequest request)
			throws Exception {
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			paraMap.put("createUser", createUser);
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			result = mcService.getScore(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 门户-监测中心-漏洞分布情况
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "vul_category_gather")
	public Result vulCategoryGather(HttpServletRequest request)
			throws Exception {
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.subtotal(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 门户-监测中心-高危漏洞top10
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/high_risk_top")
	public Result highRiskTop(HttpServletRequest request) throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getHighRiskTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 门户-监测中心-攻击类型分类汇总统计
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend_condition")
	public Result getAttackTrendWithCondition(HttpServletRequest request) 
			throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getAttackTrendCondition(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-实时攻击事件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get_real_time_Attacks")
	public Result  realTimeAttacks(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getRealTimeAttacks(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-攻击类型top10
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "attack_trend_top")
	public Result attackTrendTop(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getAttackTrendTop(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-整体攻击趋势
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "overall_attack_trend")
	public Result overallAttackTrend(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getOverallAttackTrend(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-已接入的监测站点统计
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午2:36:12
	 */
	@RequestMapping(value = "get_safe_site_totle")
	public Result safeSiteTotle(HttpServletRequest request) throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getSafeSiteTotle(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-已接入的防护站点统计
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午2:36:12
	 */
	@RequestMapping(value = "get_waf_site_totle")
	public Result safeWafTotle(HttpServletRequest request) throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getWafSiteTotle(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 门户-监测中心-高危站点统计
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月30日上午9:01:17
	 */
	@RequestMapping(value = "get_high_risk_site_totle")
	public Result getHighRiskSiteTotle(HttpServletRequest request) 
			throws Exception{
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			paraMap.put("createUser", createUser);
			result = mcService.getHighRiskSiteTotle(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 产生月报和季报
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/export_waf_word_report")
	public Result exportWafWordReport(HttpServletRequest request) {
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			result = mcService.exportWafWordReport(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 月报和季报查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/find_reports")
	public Result findReportsByCondition(
			@RequestBody Map<String, Object> paraMap) {
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
			if (StringUtils.isNotBlank(sd) && !"".equals(sd)) {
				paraMap.put("startDate",
						sd.substring(0, 4) + sd.substring(5, 7));
			}
			if (StringUtils.isNotBlank(ed) && !"".equals(ed)) {
				paraMap.put("endDate", ed.substring(0, 4) + ed.substring(5, 7));
			}

			String type1 = paraMap.get("type1").toString();
			String type2 = paraMap.get("type2").toString();
			if ("0".equals(type1)) {
				paraMap.put("type1", null);
			}
			if ("0".equals(type2)) {
				paraMap.put("type2", null);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("details", mcService.findReportsByCondition(paraMap));
			map.put("total_num", mcService.countReportByCondition(paraMap));
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
	 * 月报和季报查询
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/find_reports1")
	public Result findReportsByCondition1(
			@RequestBody Map<String, Object> paraMap) {
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
			if (StringUtils.isNotBlank(sd) && !"".equals(sd)) {
				paraMap.put("startDate",
						sd.substring(0, 4) + sd.substring(5, 7));
			} else {
				flag = false;
			}
			if (StringUtils.isNotBlank(ed) && !"".equals(ed)) {
				paraMap.put("endDate", ed.substring(0, 4) + ed.substring(5, 7));
			} else {
				flag = false;
			}

			if (flag) {
				String type1 = paraMap.get("type1") == null ? "" : paraMap.get(
						"type1").toString();
				type1 = "0".equals(type1) ? "1" : type1;
				String type2 = paraMap.get("type2") == null ? "" : paraMap.get(
						"type2").toString();
				// String curMonth = SignUtil.getDate().substring(0, 6);
				List<String> sList = SignUtil.getMonthBetween(sd, ed);
				String seasons = "01-04-07-10";
				if ("1".equals(type2)) {
					for (String d : sList) {
						// if(curMonth.equals(d.substring(0,4)+d.substring(5,7))){
						// continue;
						// }
						String reportTime = d.substring(0, 4)
								+ d.substring(5, 7);
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("reportTime", reportTime);
						m.put("siteId", paraMap.get("siteId"));
						m.put("type1", type1);
						m.put("type2", type2);
						if ("2".equals(type1)) {
							m.put("reportType", "S");
							if (seasons.indexOf(d.substring(5, 7)) != -1) {
								Date rptDate = DateUtil.getDateTime(
										d.substring(0, 4) + d.substring(5, 7)
												+ "01", "yyyyMMdd");
								String startDate = DateUtil.getStringTime(
										DateUtil.getFirstDateOfMonth(DateUtil
												.getDiffMonth(rptDate, -3)),
										"yyyyMMdd");
								String endDate = DateUtil.getStringTime(
										DateUtil.getLastDateOfMonth(DateUtil
												.getDiffMonth(rptDate, 0)),
										"yyyyMMdd");
								m.put("reportTime", startDate.substring(0, 6));
								List<WordReport> wrList = wordReportMapper
										.getReportWithCondition(m);
								if (wrList == null || wrList.size() == 0) {
									m.put("startTime", startDate);
									m.put("endTime", endDate);
									mcService.exportWafWordReport(m);
								}
							}
						} else {
							m.put("reportType", "M");
							Date rptDate = DateUtil.getDateTime(
									d.substring(0, 4) + d.substring(5, 7)
											+ "01", "yyyyMMdd");
							String startDate = DateUtil.getStringTime(DateUtil
									.getFirstDateOfMonth(DateUtil.getDiffMonth(
											rptDate, -1)), "yyyyMMdd");
							String endDate = DateUtil.getStringTime(DateUtil
									.getLastDateOfMonth(DateUtil.getDiffMonth(
											rptDate, 0)), "yyyyMMdd");
							m.put("reportTime", startDate.substring(0, 6));
							List<WordReport> wrList = wordReportMapper
									.getReportWithCondition(m);
							if (wrList == null || wrList.size() == 0) {
								m.put("startTime", startDate);
								m.put("endTime", endDate);
								mcService.exportWafWordReport(m);
							}
						}
					}
				} else {
					for (String d : sList) {
						// if(curMonth.equals(d.substring(0,4)+d.substring(5,7))){
						// continue;
						// }
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("siteId", paraMap.get("siteId"));
						m.put("type1", type1);
						m.put("type2", type2);
						if ("2".equals(type1)) {
							m.put("reportType", "S");
							if (seasons.indexOf(d.substring(5, 7)) != -1) {
								Date rptDate = DateUtil.getDateTime(
										d.substring(0, 4) + d.substring(5, 7)
												+ "01", "yyyyMMdd");
								String startDate = DateUtil.getStringTime(
										DateUtil.getFirstDateOfMonth(DateUtil
												.getDiffMonth(rptDate, -3)),
										"yyyyMMdd");
								String endDate = DateUtil.getStringTime(
										DateUtil.getLastDateOfMonth(DateUtil
												.getDiffMonth(rptDate, 0)),
										"yyyyMMdd");
								m.put("reportTime", startDate.substring(0, 6));
								List<WordReport> wrList = wordReportMapper
										.getReportWithCondition(m);
								if (wrList == null || wrList.size() == 0) {
									m.put("startTime", startDate);
									m.put("endTime", endDate);
									m.put("preMonthST",
											DateUtil.getStringTime(
													DateUtil.getFirstDateOfMonth(DateUtil
															.getDiffMonth(
																	rptDate, -6)),
													"yyyyMMdd"));
									m.put("preMonthET",
											DateUtil.getStringTime(
													DateUtil.getLastDateOfMonth(DateUtil
															.getDiffMonth(
																	rptDate, -3)),
													"yyyyMMdd"));
									m.put("fromTime", startDate + "000000");
									m.put("toTime", endDate + "235959");
									esService.exportWordReport(m);
								}
							}
						} else {
							m.put("reportType", "M");
							Date rptDate = DateUtil.getDateTime(
									d.substring(0, 4) + d.substring(5, 7)
											+ "01", "yyyyMMdd");
							String startDate = DateUtil.getStringTime(DateUtil
									.getFirstDateOfMonth(DateUtil.getDiffMonth(
											rptDate, -1)), "yyyyMMdd");
							String endDate = DateUtil.getStringTime(DateUtil
									.getLastDateOfMonth(DateUtil.getDiffMonth(
											rptDate, 0)), "yyyyMMdd");
							m.put("reportTime", startDate.substring(0, 6));
							List<WordReport> wrList = wordReportMapper
									.getReportWithCondition(m);
							if (wrList == null || wrList.size() == 0) {
								m.put("startTime", startDate);
								m.put("endTime", endDate);
								m.put("preMonthST", DateUtil.getStringTime(
										DateUtil.getFirstDateOfMonth(DateUtil
												.getDiffMonth(rptDate, -2)),
										"yyyyMMdd"));
								m.put("preMonthET", DateUtil.getStringTime(
										DateUtil.getLastDateOfMonth(DateUtil
												.getDiffMonth(rptDate, -1)),
										"yyyyMMdd"));
								m.put("fromTime", startDate + "000000");
								m.put("toTime", endDate + "235959");
								esService.exportWordReport(m);
							}
						}
					}
				}

			}

			String type1 = paraMap.get("type1").toString();
			String type2 = paraMap.get("type2").toString();
			if ("0".equals(type1)) {
				paraMap.put("type1", null);
			}
			if ("0".equals(type2)) {
				paraMap.put("type2", null);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("details", mcService.findReportsByCondition(paraMap));
			map.put("total_num", mcService.countReportByCondition(paraMap));
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
	 * 报表导出
	 * 
	 * @param response
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = "/download_word_report")
	public Result downloadWordReport(HttpServletResponse response,
			@RequestParam(value = "fileId", required = true) String fileId) {
		Result result = new Result();
		try {
			DB db = mu.getDB(FILE_DB);
			GridFS gridFS = new GridFS(db);
			DBObject query = new BasicDBObject("_id", fileId);
			GridFSDBFile gridFSDBFile = gridFS.findOne(query);
			String fileName = gridFSDBFile.get("filename").toString();
			// 清空response
			response.reset();
			// 设置response的header
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
}
