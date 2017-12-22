package com.uway.mobile.adminController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.MonitorCenterService;
@RestController
@RequestMapping("admin_monitor_center")
public class AdminMonitorCenterController {
	@Autowired
	private MonitorCenterService mcService;
	/**
	 * 运营-整体安全得分
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_overall_safety_score")
	public Result getOverallSafetyScore()
			throws Exception {
		Result result = new Result();
		try {			
			result = mcService.getAvgScore();
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 运营-漏洞分布情况
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
			result = mcService.vulsDistribution();
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 运营-高危漏洞top10
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/high_risk_top")
	public Result highRiskTop() throws Exception{
		Result result = new Result();
		try {
			result = mcService.getAllHighRiskTop();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}

	/**
	 * 运营-攻击类型分类汇总统计
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/get_attack_trend_condition")
	public Result getAttackTrendWithCondition(HttpServletRequest request) 
			throws Exception{
		Result result = new Result();
		try {
			result = mcService.getAllAttackTrendCondition();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 运营-实时攻击事件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get_real_time_Attacks")
	public Result  realTimeAttacks(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {
			result = mcService.getAllRealTimeAttacks();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 运营-攻击类型top10
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "attack_trend_top")
	public Result attackTrendTop(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {
			result = mcService.getAlAttackTrendTop();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	
	/**
	 * 运营-整体攻击趋势
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "overall_attack_trend")
	public Result overallAttackTrend(HttpServletRequest request)
			throws Exception{
		Result result = new Result();
		try {		
			result = mcService.getAllOverallAttackTrend();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
	/**
	 * 运营-监测中心-已接入的监测站点统计
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午2:36:12
	 */
	@RequestMapping(value = "get_site_totle")
	public Result safeSiteTotle(HttpServletRequest request) throws Exception{
		Result result = new Result();
		try {
			result = mcService.getSiteTotle();
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
		}
		return result;
	}
}
