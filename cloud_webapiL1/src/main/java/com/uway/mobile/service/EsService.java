package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface EsService {

	public void addToday(Map<String, Object> paraMap) throws Exception;
	public Result getDay(Map<String, Object> paraMap) throws Exception;
	public Result getTrend(Map<String, Object> paraMap) throws Exception;
	public void webTrend(Map<String, Object> paraMap) throws Exception;
	public Result getWeb(Map<String, Object> paraMap) throws Exception;
	public void webcc(Map<String, Object> paraMap) throws Exception;
	public Result getWebcc(Map<String, Object> paraMap) throws Exception;
	public void attackTop(Map<String, Object> paraMap) throws Exception;
	public Result getAttackTop(Map<String, Object> paraMap) throws Exception;
	public void attackTrend(Map<String, Object> paraMap) throws Exception;
	public Result getAttackTrend(Map<String, Object> paraMap) throws Exception;
	public void attackDetail(Map<String, Object> paraMap) throws Exception;
	public void ccDetail(Map<String, Object> paraMap) throws Exception;
	public Result getAttackDetail(Map<String, Object> paraMap)throws Exception;
	public Result getDayAll(Map<String, Object> paraMap) throws Exception;
	public void getReportXml(Map<String,Object> paraMap) throws Exception;
	public Result getSiteRisk(Map<String, Object> paraMap) throws Exception;
	public Result getVuls(Map<String, Object> paraMap) throws Exception;
	public Result getHoles(Map<String, Object> paraMap) throws Exception;
	public Result getAllSiteHole(String userId) throws Exception;
	public Result getRiskTrend(Map<String, Object> paraMap) throws Exception;
	public Result getVulKeypage(Map<String, Object> paraMap) throws Exception;
	public Result getChangeUrl(Map<String, Object> paraMap) throws Exception;
	public Result getKeyword(Map<String, Object> paraMap) throws Exception;
	public Result getSmooth(Map<String, Object> paraMap) throws Exception;
	public Result getHole(Map<String, Object> paraMap) throws Exception;
	public Result getVulLink(Map<String, Object> paraMap) throws Exception;
	public Result getSmoothData(Map<String, Object> paraMap) throws Exception;
	public Result getHolesType(Map<String, Object> paraMap) throws Exception;
	public Result getHighRiskTop(Map<String, Object> paraMap) throws Exception;
	public Result getAttackTrendWithCondition(Map<String, Object> paraMap) throws Exception;
	public Result getAttackCount(Map<String, Object> paraMap) throws Exception;
	public Result getAllReport(Map<String, Object> paraMap) throws Exception;
	public Result getReportBySite(Map<String, Object> paraMap) throws Exception;
	public List<Map<String, Object>> getData(Map<String, Object> map) throws Exception;
	public Workbook exporttReportXlsx(List<Map<String, Object>> data) throws Exception;
	public Result exportWordReport(Map<String, Object> paraMap) throws Exception;
	public Result getVulLevel(Map<String, Object> paraMap) throws Exception;
	public Result getTwoDayWafData(Map<String, Object> paraMap) throws Exception;
	Result getSafeMonitorTotal(Map<String, Object> paraMap) throws Exception;
}
