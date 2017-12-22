package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Certificate;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.SubDomainList;

@Mapper
public interface CertificateMapper {


	public List<Site> getSite(Map<String, Object> paraMap);

	public Long countSite();

	public List<SubDomainList> getSiteSonById(Map<String, Object> paraMap);

	public void insertCer(Certificate cer);

	public SubDomainList getDomainById(Map<String, Object> paraMap);

}
