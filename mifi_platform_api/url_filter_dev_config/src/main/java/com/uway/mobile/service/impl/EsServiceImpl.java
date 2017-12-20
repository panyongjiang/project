package com.uway.mobile.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jiguang.common.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.InterceptInfo;
import com.uway.mobile.domain.UrlCheck;
import com.uway.mobile.mapper.DeviceMapper;
import com.uway.mobile.service.EsService;
import com.uway.mobile.util.ElasticSearchUtils;
import com.uway.mobile.util.Pagination;

@Service
public class EsServiceImpl implements EsService {
	
	@Autowired
	private Client esClient;
	@Autowired
	DeviceMapper dm;

	@Override
	public long getTotalAlert(List<Device> list) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if(list.size()>0){
			for(Device device:list){
				queryBuilder.should(QueryBuilders.termQuery("deviceid", device.getDeviceId()));
			}
		}
		queryBuilder.mustNot(QueryBuilders.termQuery("enilType", 0));
		SearchResponse searchResponse = esClient.prepareSearch("udptest")
				.setTypes("udptest").setQuery(queryBuilder)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		return hits.getTotalHits();
	}
	
	@Override
	public Pagination getInterceptorUrls(Map<String,String> paramMap,List<Device> list){
		Pagination pagination = new Pagination();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.mustNot(QueryBuilders.termQuery("enilType", 0));
		if(list.size()>0){
			for(Device device:list){
				queryBuilder.should(QueryBuilders.termQuery("deviceid",device.getDeviceId()));
			}
		}
		if(paramMap.containsKey("url")){
			String url = paramMap.get("url");
			if(StringUtils.isNotEmpty(url)){
				queryBuilder.must(QueryBuilders.wildcardQuery("url", "*"+url+"*"));
			}
		}else{
			queryBuilder.must(QueryBuilders.wildcardQuery("url", "*"));
		}
        TermsBuilder termBuilder = AggregationBuilders.terms("url_count").field("url").order(Order.aggregation("_count", false));
        MaxBuilder maxBuilder = AggregationBuilders.max("createTime").field("createTime");
		SearchRequestBuilder srb = esClient.prepareSearch("udptest").setTypes("udptest").setQuery(queryBuilder);
		SearchResponse response = srb.addAggregation(termBuilder.subAggregation(maxBuilder)).addSort("createTime", SortOrder.DESC)
				.setSize(10000).execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.hits();
        Terms agg = response.getAggregations().get("url_count");

		if(hits.length>0){
			List<InterceptInfo> interceptorList = new ArrayList<InterceptInfo>();
			List<InterceptInfo> interceptorList1 = new ArrayList<InterceptInfo>();
					if(agg.getBuckets().size()>0){
						for(Terms.Bucket entry : agg.getBuckets()){
								InterceptInfo interceptInfo=new InterceptInfo();
								InternalMax maxHit = entry.getAggregations().get("createTime"); 
								String s = new BigDecimal(maxHit.getValue()).toPlainString();
								setInterceptInfo(interceptInfo,entry.getKeyAsString(),Long.parseLong(s),list);
								long docCount = entry.getDocCount(); // Doc count
								interceptInfo.setCreateTime(new Date(Long.parseLong(s)));
								interceptInfo.setTimes(docCount);
								if(String.valueOf(interceptInfo.getEnilType()).equals("1")){
									interceptInfo.setEnilTypeName("钓鱼网站");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("2")){
									interceptInfo.setEnilTypeName("欺诈网站");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("3")){
									interceptInfo.setEnilTypeName("侵权内容");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("4")){
									interceptInfo.setEnilTypeName("虚假广告");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("5")){
									interceptInfo.setEnilTypeName("非法赌博信息");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("6")){
									interceptInfo.setEnilTypeName("有害程序");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("7")){
									interceptInfo.setEnilTypeName("非法网站");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("8")){
									interceptInfo.setEnilTypeName("色情信息");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("9")){
									interceptInfo.setEnilTypeName("色情网站");
								}else if(String.valueOf(interceptInfo.getEnilType()).equals("10")){
									interceptInfo.setEnilTypeName("黑名单");
								}else {
									interceptInfo.setEnilTypeName("其他"+interceptInfo.getEnilType());
								}
								if(interceptInfo.getEnilType()!=0){
									interceptorList.add(interceptInfo);
								}
								if(interceptorList.size()>10){
									interceptorList1 = interceptorList.subList(0,10);
									pagination.setDetails(interceptorList1);
								}else{
									pagination.setDetails(interceptorList);
								}
						}
					}	
			pagination.setTotal_num(10);
		}
		return pagination;
	}
	/*@Override
	public Pagination getInterceptorUrls(Map<String,String> paramMap,List<Device> list){
		Pagination pagination = new Pagination();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.mustNot(QueryBuilders.termQuery("enilType", 0));
		if(list.size()>0){
			for(Device device:list){
				queryBuilder.should(QueryBuilders.termQuery("deviceid",device.getDeviceId()));
			}
		}
		if(paramMap.containsKey("url")){
			String url = paramMap.get("url");
			if(StringUtils.isNotEmpty(url)){
				queryBuilder.must(QueryBuilders.wildcardQuery("url", "*"+url+"*"));
			}
		}else{
			queryBuilder.must(QueryBuilders.wildcardQuery("url", "*"));
		}
		TermsBuilder tb= AggregationBuilders.terms("url_count").field("url").order(Terms.Order.compound(
                Terms.Order.aggregation("_count",false)//先按count，降序排
        ));
        SearchRequestBuilder search=esClient.prepareSearch("udptest").setTypes("udptest");
        search.addAggregation(tb).setSize(1000);
        //发送查询，获取聚合结果
        Terms tms=  search.get().getAggregations().get("url");
        
		return pagination;
	}*/
	
	private void setInterceptInfo(InterceptInfo interceptInfo,String url,long createTime,List<Device> list){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.termQuery("createTime", createTime));
		queryBuilder.must(QueryBuilders.termQuery("url", url));
		if(list.size()>0){
			for(Device device:list){
				queryBuilder.should(QueryBuilders.termQuery("deviceid",device.getDeviceId()));
			}
		}
		SearchRequestBuilder srb = esClient.prepareSearch("udptest").setTypes("udptest").setQuery(queryBuilder);
		SearchResponse response = srb.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.hits();
		if(hits.length>0){
			SearchHit hit = hits[0];
			interceptInfo.setId(hit.getId());
			InterceptInfo intercept = JSON.parseObject(hit.sourceAsString(), InterceptInfo.class);
			interceptInfo.setStatus(intercept.getStatus());
			interceptInfo.setDeviceid(intercept.getDeviceid());
			interceptInfo.setEnilType(intercept.getEnilType());
			interceptInfo.setDstIp(intercept.getDstIp());
			interceptInfo.setSrcIp(intercept.getSrcIp());
			interceptInfo.setDstPort(intercept.getDstPort());
			interceptInfo.setSrcPort(intercept.getSrcPort());
			interceptInfo.setDstFmtIp(intercept.getDstFmtIp());
			interceptInfo.setSrcFmtIp(intercept.getSrcFmtIp());
			interceptInfo.setUrl(intercept.getUrl());
		}
	}
	
	public void addTrust(Map<String,String> param) throws Exception{
		String id = param.get("id");
		ElasticSearchUtils.updateDoc(esClient, "udptest", "udptest", id, "status", "1");
	}


	@SuppressWarnings("rawtypes")
	@Override
	public Result getCountUrl(Map<String, Object> paraMap,List<Device> deviceList) {
		Result result = new Result();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.mustNot(QueryBuilders.termQuery("enilType", 0));
		List<Device> deviceId = deviceList;
		if(deviceId.size()>0){
			for(Device device:deviceId){
				queryBuilder.should(QueryBuilders.termQuery("deviceid", device.getDeviceId()));
			}
		}
		AggregationBuilder aggreation=AggregationBuilders
				.terms("agg")
				.field("enilType")
				.size(0)
				.subAggregation(
						AggregationBuilders.topHits("top").addSort(
								"createTime",SortOrder.DESC)
								.setSize(1));
		SearchRequestBuilder srb = esClient.prepareSearch("udptest").setTypes("udptest").setQuery(queryBuilder);
		SearchResponse sResponse = srb
				.addSort("createTime", SortOrder.DESC)
				.addAggregation(aggreation)
				.execute().actionGet();
		Terms term = sResponse.getAggregations().get("agg");
		List<Bucket> buckets = term.getBuckets();
		List<UrlCheck> list = new ArrayList<UrlCheck>();
		for(Bucket value:buckets){
			UrlCheck uc=new UrlCheck();
			uc.setDocCount((int) value.getDocCount());
			if(value.getKeyAsString().equals("1")){
				uc.setEnilType("钓鱼网站");
			}else if(value.getKeyAsString().equals("2")){
				uc.setEnilType("欺诈网站");
			}else if(value.getKeyAsString().equals("3")){
				uc.setEnilType("侵权内容");
			}else if(value.getKeyAsString().equals("4")){
				uc.setEnilType("虚假广告");
			}else if(value.getKeyAsString().equals("5")){
				uc.setEnilType("非法赌博信息");
			}else if(value.getKeyAsString().equals("6")){
				uc.setEnilType("有害程序");
			}else if(value.getKeyAsString().equals("7")){
				uc.setEnilType("非法网站");
			}else if(value.getKeyAsString().equals("8")){
				uc.setEnilType("色情信息");
			}else if(value.getKeyAsString().equals("9")){
				uc.setEnilType("色情网站");
			}else if(value.getKeyAsString().equals("10")){
				uc.setEnilType("黑名单");
			}else {
				uc.setEnilType("其他"+value.getKeyAsString());
			}
			list.add(uc);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("饼状图数据查询成功");
		result.setData(list);
		return result;
	}
	
	public Pagination getInterceptInfos(Map<String,Object> params,List<Device> deviceList){
		Integer pageNo = Integer.parseInt(params.get("pageNo").toString());
		Integer pageSize = Integer.parseInt(params.get("pageSize").toString());
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.mustNot(QueryBuilders.termQuery("enilType", 0));
		if(params.containsKey("url")){
			String url=params.get("url").toString();
            queryBuilder.must(QueryBuilders.wildcardQuery("url", "*"+url+"*"));
		}
		if(params.containsKey("enilType")){
			queryBuilder.must(QueryBuilders.termQuery("enilType", params.get("enilType").toString()));
		}
		if(deviceList.size()>0){
			for(Device device:deviceList){
				queryBuilder.should(QueryBuilders.termQuery("deviceid", device.getDeviceId()));
			}
		}
		SearchRequestBuilder srb = esClient.prepareSearch("udptest").setTypes("udptest");
		QueryBuilder qb = QueryBuilders.boolQuery().should(queryBuilder);
		srb.addSort("createTime",SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom((pageNo-1)*pageSize).setSize(pageSize);
		SearchResponse response = srb.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.hits();
		
		List<InterceptInfo> list = new ArrayList<InterceptInfo>();
		for(SearchHit hit:hits){
			InterceptInfo intercept = JSON.parseObject(hit.sourceAsString(), InterceptInfo.class);
			if(String.valueOf(intercept.getEnilType()).equals("1")){
				intercept.setEnilTypeName("钓鱼网站");
			}else if(String.valueOf(intercept.getEnilType()).equals("2")){
				intercept.setEnilTypeName("欺诈网站");
			}else if(String.valueOf(intercept.getEnilType()).equals("3")){
				intercept.setEnilTypeName("侵权内容");
			}else if(String.valueOf(intercept.getEnilType()).equals("4")){
				intercept.setEnilTypeName("虚假广告");
			}else if(String.valueOf(intercept.getEnilType()).equals("5")){
				intercept.setEnilTypeName("非法赌博信息");
			}else if(String.valueOf(intercept.getEnilType()).equals("6")){
				intercept.setEnilTypeName("有害程序");
			}else if(String.valueOf(intercept.getEnilType()).equals("7")){
				intercept.setEnilTypeName("非法网站");
			}else if(String.valueOf(intercept.getEnilType()).equals("8")){
				intercept.setEnilTypeName("色情信息");
			}else if(String.valueOf(intercept.getEnilType()).equals("9")){
				intercept.setEnilTypeName("色情网站");
			}else if(String.valueOf(intercept.getEnilType()).equals("10")){
				intercept.setEnilTypeName("黑名单");
			}else {
				intercept.setEnilTypeName("其他"+intercept.getEnilType());
			}
			list.add(intercept);
		}
		Pagination pagination = new Pagination();
		pagination.setDetails(list);
		pagination.setTotal_num(searchHits.getTotalHits());
		pagination.setPageNo(pageNo);
		pagination.setPageSize(pageSize);
		return pagination;
	}

}
