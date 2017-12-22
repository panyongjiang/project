package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface DomainQueryService {

	public Result selectDomain(Map<String, Object> paraMap)throws Exception;

}
