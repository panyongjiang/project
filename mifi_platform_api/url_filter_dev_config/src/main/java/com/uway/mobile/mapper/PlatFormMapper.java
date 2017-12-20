package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.ConfigStatus;

@Mapper
public interface PlatFormMapper {

	public List<ConfigStatus> getConfig(Map<String, Object> paraMap);

	public List<Map<String, Object>> getVersion(Map<String, Object> paraMap);

	public void insertVersion(Map<String, Object> paraMap);

}
