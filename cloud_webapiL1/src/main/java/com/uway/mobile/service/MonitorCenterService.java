package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.WordReport;

@Transactional
public interface MonitorCenterService {
	/**
	 * 获取整体安全得分信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getScore(Map<String, Object> paraMap) throws Exception ;
	
	/**
	 * 分类汇总
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result subtotal(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 用户所有安全监测站点的高危漏洞TOP10
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getHighRiskTop(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 攻击类型分类汇总
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getAttackTrendCondition(Map<String, Object> paraMap) throws Exception;
	
	
	/**
	 * 产生月报或季报
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result exportWafWordReport(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据条件查询报表数据
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<WordReport> findReportsByCondition(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据条件查询报表数据的总数
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public long countReportByCondition(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取用户防护站点实时攻击事件
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getRealTimeAttacks(Map<String, Object> paraMap) throws Exception;
	/**
	 * 攻击类型top10
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getAttackTrendTop(Map<String, Object> paraMap) throws Exception;
	/**
	 * 整体攻击趋势
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result getOverallAttackTrend(Map<String, Object> paraMap) throws Exception;
	/**
	 * 运营-整体安全得分
	 * @return
	 * @throws Exception
	 */
	public Result getAvgScore() throws Exception;
	/**
	 * 运营-漏洞分布情况
	 * @return
	 * @throws Exception
	 */
	public Result vulsDistribution() throws Exception;
	/**
	 * 运营-高危漏洞top10
	 * @return
	 */
	public Result getAllHighRiskTop() throws Exception;
	/**
	 * 运营-攻击类型分类汇总统计
	 * @return
	 */
	public Result getAllAttackTrendCondition() throws Exception;
	/**
	 * 运营-攻击类型top10
	 * @return
	 */
	public Result getAlAttackTrendTop() throws Exception;
	/**
	 * 运营-整体攻击趋势
	 * @return
	 */
	public Result getAllOverallAttackTrend() throws Exception;
	/**
	 * 运营-实时攻击事件
	 * @return
	 */
	public Result getAllRealTimeAttacks() throws Exception;
	
	/**
	 * 已接入的监测站点总览情况
	 * @param paraMap
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午2:38:02
	 */
	public Result getSafeSiteTotle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 已接入的防护站点总览情况
	 * @param paraMap
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午4:04:40
	 */
	public Result getWafSiteTotle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 运营-已接入的网站总览情况
	 * @param paraMap
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月29日下午5:16:00
	 */
	public Result getSiteTotle() throws Exception;
	
	/**
	 * 门户-监测中心-高危站点统计
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wanglei
	 * @create_time 2017年8月30日上午9:01:17
	 */
	public Result getHighRiskSiteTotle(Map<String, Object> paraMap) throws Exception;

}
