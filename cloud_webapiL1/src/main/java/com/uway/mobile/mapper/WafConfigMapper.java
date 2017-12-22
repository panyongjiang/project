package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.InterceptUrl;
import com.uway.mobile.domain.Port;
import com.uway.mobile.domain.SafeManagement;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.SubDomainList;

@Mapper
public interface WafConfigMapper {
	
	public List<Port> getPort(Map<String, Object> paraMap)throws Exception;

	public List<SubDomainList> getDomainList(Map<String, Object> paraMap)throws Exception;

	public void insertPort(Map<String, Object> paraMap)throws Exception;

	public Long countSiteSon(Map<String, Object> paraMap)throws Exception;

	public Long countPort(Map<String, Object> paraMap)throws Exception;

	public Site getSiteById(Map<String, Object> paraMap)throws Exception;
	
	public List<SafeManagement> getSafeList(Map<String, Object> paraMap)throws Exception;

	public Long countSafeManagement()throws Exception;
	
	public Long countInList(Map<String, Object> paraMap);
	
	public List<Map<String, Object>> getAllList(Map<String, Object> paraMap);
	
	public List<InterceptUrl> getLen(Map<String, Object> paraMap);
	
	public void insertIpOrUrl(Map<String, Object> paraMap);

	public void updateIpOrUrl(Map<String, Object> paraMap);

	public void deleteIpOrUrl(Map<String, Object> paraMap);

	public Map<String, Object> SelectPortNameById(Map<String, Object> paraMap) throws Exception;

	public Map<String, Object> SelectPortInfoById(Map<String, Object> paraMap) throws Exception;

}
