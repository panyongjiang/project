package com.uway.mobile.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface AppCheckService {

	/**
	 * 新增app
	 * @param appCheck
	 */
	public Result addAppCheck(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取appCheck列表
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result listAppCheck(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 上传app检测报告
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result addAppReport(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 下载app检测报告
	 * @return
	 * @throws Exception
	 */
	public String downloadReport(HttpServletResponse response,Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 下载APP
	 * @param response
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public String downloadApp(HttpServletResponse response,Map<String, Object> paraMap) throws Exception;
}
