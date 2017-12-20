package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.util.Pagination;

@Transactional
public interface EsService {
	
	/**
	 * 获取警告总数
	 * @return
	 */
	public long getTotalAlert(List<Device> list);
	
	/**
	 * 获取被拦截的URL列表
	 * @param paramMap
	 * @return
	 */
	public Pagination getInterceptorUrls(Map<String,String> paramMap,List<Device> list);
	
	public void addTrust(Map<String,String> param) throws Exception;

	public Result getCountUrl(Map<String, Object> paraMap,List<Device> deviceList);
	
	/**
	 * 分页查询安全日志
	 * @param params
	 * @return
	 */
	public Pagination getInterceptInfos(Map<String,Object> params,List<Device> deviceList);
	

}
