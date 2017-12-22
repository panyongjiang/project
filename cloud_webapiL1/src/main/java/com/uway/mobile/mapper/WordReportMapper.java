package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.WordReport;

@Mapper
public interface WordReportMapper {
	
	/**
	 * 新增报表记录
	 * @param wordReport
	 * @return
	 * @throws Exception
	 */
	public int insertWordReport(WordReport wordReport) throws Exception;
	
	public WordReport getReportById(Map<String,Object> paraMap);
	
	public List<WordReport> findReportByCondition(Map<String,Object> paraMap) throws Exception;
	
	public long countReportByCondition(Map<String,Object> paraMap) throws Exception;
	
	public List<WordReport> getReportWithCondition(Map<String,Object> paraMap) throws Exception;

}
