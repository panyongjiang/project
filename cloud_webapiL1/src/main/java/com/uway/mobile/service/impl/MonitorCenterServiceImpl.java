package com.uway.mobile.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.AttackInfo;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.WordReport;
import com.uway.mobile.mapper.SiteMapper;
import com.uway.mobile.mapper.WordReportMapper;
import com.uway.mobile.service.MonitorCenterService;
import com.uway.mobile.util.ChartUtils;
import com.uway.mobile.util.DateUtil;
import com.uway.mobile.util.FreemarkerUtil;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.SignUtil;

@Service
public class MonitorCenterServiceImpl implements MonitorCenterService {
	@Autowired
	private Client esClient;
	@Autowired
	private SiteMapper siteMapper;
	@Autowired
	private WordReportMapper wordReportMapper;
	@Autowired
	private MongoUtil mu;
	@Value("${spring.data.mongodb.filedb}")	
	public String FILE_DB;
	
	@Override
	public Result getScore(Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		String create_user = paraMap.get("createUser").toString();
		long userId = Long.parseLong(create_user);
		List<Map<String,Object>> list = siteMapper.getAllSafeSite(userId);
		int avg = 0;
		int sum = 0;
		int emptyNum = 0;
		if( list.size() > 0){
			for (Map<String, Object> map : list) {
				int score = 0;
				String siteId = map.get("id").toString();
				map.put("siteId", siteId);
				Map<String,Object> last = siteMapper.getLast(map);
				if(last == null){
					emptyNum ++;
					continue;
				}else{
					String last_end_time = last.get("lastEndTime").toString();
					QueryBuilder qb = QueryBuilders
							.boolQuery()
							.must(QueryBuilders.termsQuery("create_user","" + create_user))
							.must(QueryBuilders.termsQuery("last_end_time","" + last_end_time));
					SearchResponse searchResponse = esClient.prepareSearch("cloud")
							.setTypes("site_risk").setQuery(qb)
							.execute().actionGet();
					SearchHits hits = searchResponse.getHits();
					if (hits.getTotalHits() == 0) {
						emptyNum ++;
						continue;
					}
					SearchHit[] searchHists = hits.getHits();
					Map<String, Object> data1 = searchHists[0].getSource();
					int risk =Integer.parseInt(data1.get("risk").toString());
					if(risk == 0 || risk == 1 || risk == 2){
						score = 100 - risk * 10 ;
					}else if(risk ==3 ){
						score = 78 ;
					}else if(risk ==4 ){
						score = 65 ;
					}else if(risk ==5 ){
						score = 60 ;
					}else if(risk ==6 ){
						score = 59 ;
					}else if(risk ==7 ){
						score = 47 ;
					}else if(risk ==8 ){
						score = 40 ;
					}else if(risk >8 && risk <=16 ){
						score = 35 ;
					}else if(risk >16 && risk <=24 ){
						score = 20 ;
					}else if(risk >24 && risk <=30 ){
						score = 7 ;
					}else{
						score = 2 ;
					}
					sum += score ;
				}
			}
			if(list.size() == emptyNum){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("暂无！");
				return result;
			}
			avg = sum/(list.size()-emptyNum);
			if(avg == 0){
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("用户安全站点还未进行扫描！");
				return result;
			}else{
				data.put("avgScore", avg);
			}
		}else{
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("该用户未添加安全监测站点！");
			return result;
		}
		double avg1 = (double) avg;
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//环比   同上一个月的当前时间点的得分进行比较
		long lastMonthNowTime = SignUtil.lastMonthNowTime();
		//获取create_user对应的站点最小create_time
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1 = siteMapper.getMinCreateTimeByUser(paraMap);		
		Date date = sdf.parse(map1.get("createTime").toString());
		long minCreateTime = Long.parseLong(dateFormater.format(date));
		if(lastMonthNowTime < minCreateTime){
			data.put("MOM", "暂无");
		}else{		
			Map<String,Object> map2 = this.score(lastMonthNowTime, create_user);
			if(Integer.parseInt(map2.get("avgScore").toString()) == -1){
				data.put("MOM", "暂无");
			}else{				
				double avg2 = Double.parseDouble(map2.get("avgScore").toString());
				if(avg1 > avg2){
					data.put("MOM", (int)((avg1-avg2)/avg2*100)+"%");
					data.put("mom_s", 1);
				}else if(avg1 < avg2){
					data.put("MOM", (int)((avg2-avg1)/avg2*100)+"%");
					data.put("mom_s", -1);
				}else{
					data.put("MOM", "不变");
					data.put("mom_s", 0);
				}
			}
		}
		
		//同比  与一年前的当前时间点得分进行比较
		long lastYearNowTime = SignUtil.getLastYearNowTime();
		if(lastYearNowTime < minCreateTime){
			data.put("YOY", "暂无");
		}else{		
			Map<String,Object> map3 = this.score(lastYearNowTime, create_user);
			if(Integer.parseInt(map3.get("avgScore").toString()) == -1){
				data.put("YOY", "暂无");
			}else{				
				double avg3 = Double.parseDouble(map3.get("avgScore").toString());
				if(avg1 > avg3){
					data.put("YOY", (int)((avg1-avg3)/avg3*100)+"%");
					data.put("yoy_s", 1);
				}else if(avg1 < avg3){
					data.put("YOY", (int)((avg3-avg1)/avg3*100)+"%");
					data.put("yoy_s", -1);
				}else{
					data.put("YOY", "不变");
					data.put("yoy_s", 0);
				}
			}
		}
		result.setData(data);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功！");
		return result;
	}
	
	public Map<String,Object> score(long endTime,String create_user) throws Exception{
		Map<String,Object> dataMap = new HashMap<String,Object>();			
		long userId = Long.parseLong(create_user);
		List<Map<String,Object>> list = siteMapper.getAllSafeSite(userId);
		if( list.size() > 0){
			int avg = 0;
			int sum = 0;
			int emptyNum = 0;
			for (Map<String, Object> map : list) {
				map.put("endTime", endTime);
				int score = 0;
				String siteId = map.get("id").toString();
				map.put("siteId", siteId);
				Map<String,Object> last = siteMapper.getLastByEndTime(map);
				if(last == null){
					emptyNum ++;
					continue;
				}else{
					String last_end_time = last.get("lastEndTime").toString();
					QueryBuilder qb = QueryBuilders
							.boolQuery()
							.must(QueryBuilders.termsQuery("create_user","" + create_user))
							.must(QueryBuilders.termsQuery("last_end_time","" + last_end_time));
					SearchResponse searchResponse = esClient.prepareSearch("cloud")
							.setTypes("site_risk").setQuery(qb)
							.execute().actionGet();
					SearchHits hits = searchResponse.getHits();
					if (hits.getTotalHits() == 0) {
						emptyNum ++;
						continue;
					}
					SearchHit[] searchHists = hits.getHits();
					Map<String, Object> data1 = searchHists[0].getSource();
					int risk =Integer.parseInt(data1.get("risk").toString());
					if(risk == 0 || risk == 1 || risk == 2){
						score = 100 - risk * 10 ;
					}else if(risk ==3 ){
						score = 78 ;
					}else if(risk ==4 ){
						score = 65 ;
					}else if(risk ==5 ){
						score = 60 ;
					}else if(risk ==6 ){
						score = 59 ;
					}else if(risk ==7 ){
						score = 47 ;
					}else if(risk ==8 ){
						score = 40 ;
					}else if(risk >8 && risk <=16 ){
						score = 35 ;
					}else if(risk >16 && risk <=24 ){
						score = 20 ;
					}else if(risk >24 && risk <=30 ){
						score = 7 ;
					}else{
						score = 2 ;
					}
					sum += score ;
				}
			}
			if(list.size() == emptyNum){
				dataMap.put("avgScore", -1);
			}
			avg = sum/(list.size()-emptyNum);
			if(avg == 0){
				dataMap.put("avgScore", -1);
			}else{
				dataMap.put("avgScore", avg);
			}
		}else{
			dataMap.put("avgScore", -1);
		}
		return dataMap;
	}

	@Override
	public Result subtotal(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String create_user = paraMap.get("createUser").toString();
		long userId = Long.parseLong(create_user);
		list = siteMapper.getAllSafeSite(userId);
		if( list.size() == 0){
			result.setMsg("暂无数据！");
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(null);
			return result;
		}else{
			int high = 0;
			int mid = 0;
			int low = 0;
			for (Map<String, Object> map : list) {
				String siteId = map.get("id").toString();
				map.put("siteId", siteId);
				Map<String,Object> last = new HashMap<String,Object>();				
				last = siteMapper.getLast(map);
				if(last == null){
					continue;
				}else{
					String last_end_time = last.get("lastEndTime").toString();
					QueryBuilder query = QueryBuilders.boolQuery().must(
							QueryBuilders.termsQuery("site_id", siteId))
							.must(QueryBuilders.termsQuery("last_end_time", last_end_time));
					/*TermsBuilder aggregation = AggregationBuilders.terms("vuls").script(
							new Script("doc['severity_points'].value"));*/
					TermsBuilder aggregation = AggregationBuilders.terms("vuls").field("severity_points");
					SearchResponse searchResponse = esClient.prepareSearch("cloud")
							.setTypes("site_risk").setQuery(query)
							.addAggregation(aggregation)
							.execute().actionGet();
					Terms num = searchResponse.getAggregations().get("vuls");
					
					List<Bucket> vulN = num.getBuckets();
					for (Bucket vn : vulN) {
						String key = vn.getKeyAsString();
						if (Integer.parseInt(key) >= 8) {
							high += vn.getDocCount();
						} else if (5 <= Integer.parseInt(key)
								&& Integer.parseInt(key) <= 7) {
							mid += vn.getDocCount();
						} else {
							low += vn.getDocCount();
						}
					}
				}
			}			
			String[] arr = {"高危","中危","低危"};
			int[] sum = {high,mid,low};
			List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
			for(int i=0;i<3;i++){
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("name", arr[i]);
				mapData.put("value", sum[i]);
				list1.add(mapData);
			}
			result.setData(list1);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功！");
			return result;
		}
	}

	@Override
	public Result getHighRiskTop(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String create_user = paraMap.get("createUser").toString();
		long userId = Long.parseLong(create_user);
		list = siteMapper.getAllSafeSite(userId);
		if( list.size() == 0){
			result.setMsg("暂无数据！");
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(null);
			return result;
		}else{
			int emptyNum = 0;
			Map<String,Object> data = new HashMap<String,Object>();
			for (Map<String, Object> map : list) {
				String siteId = map.get("id").toString();
				map.put("siteId", siteId);
				Map<String,Object> last = new HashMap<String,Object>();				
				last = siteMapper.getLast(map);				
				if(last == null){
					emptyNum++;
					continue;
				}else{
					BoolQueryBuilder builder = QueryBuilders.boolQuery();
					builder.must(QueryBuilders.matchAllQuery());
					builder.must(QueryBuilders.termsQuery("site_id", siteId));
					builder.must(QueryBuilders.termsQuery("last_end_time", ""+last.get("lastEndTime")));
					QueryBuilder severity_points = QueryBuilders
							.rangeQuery("severity_points")
							.gte(8)
							.lte(10);
					builder.must(severity_points);
					TermsBuilder aggregationT =
							AggregationBuilders.terms("data")
							 .script(new Script("doc['site_id'].value+ '|' + doc['title'].value "))
							 .size(1000);		
					SearchResponse searchResponse = esClient.prepareSearch("cloud").setTypes("site_risk")
						        .setQuery(builder).setSize(1000)
						        .addAggregation(aggregationT)
						        .execute()
						        .actionGet();
					Terms t = searchResponse.getAggregations().get("data");
				    List<Bucket> counts = t.getBuckets();
				    if(counts.size() == 0){
				    	continue;
				    }else{
					    for(Bucket b:counts){
						    String[] keys = b.getKey().toString().split("\\|");
						    if(data.containsKey(keys[1])){
						    	data.put(keys[1], Long.parseLong(data.get(keys[1]).toString())+b.getDocCount());
						    }else{
						    	data.put(keys[1],b.getDocCount());	
						    }
					    }
				    }
				}
			}
			if(list.size() == emptyNum || data.size() == 0){				
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setData(null);
				result.setMsg("暂无数据！");
				return result;
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(data);
			result.setMsg("获取成功！");
		}
		return result;
	}

	@Override
	public Result getAttackTrendCondition(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		builder.must(QueryBuilders.termsQuery("create_user", ""+paraMap.get("createUser").toString()));
		builder.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		//builder.must(QueryBuilders.termsQuery("date", "20170820"));
		String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
				{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
				{"文件包含","LRFI"},{"特殊攻击","SPECIAL"},{"其他","OTHERS"}};
		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
		srb.setTypes("waf_attack_trend");
		srb.setSearchType(SearchType.QUERY_THEN_FETCH);
		for(int i=0;i<arrs.length;i++){
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
			srb.addAggregation(sumBuilder);
		}
		SearchResponse sr = srb.execute().get();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<arrs.length;i++){
			Sum sum = sr.getAggregations().get(arrs[i][1]);
			Map<String,Object> mapData = new HashMap<String,Object>();
			mapData.put("name", arrs[i][0]);
			mapData.put("value", sum.getValue());
			list.add(mapData);
		}
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(list);
		return result;
	}
	
	
	@SuppressWarnings({ "deprecation" })
	@Override
	public Result exportWafWordReport(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String,Object> wordMap = new HashMap<String,Object>();
		String reportType = paraMap.get("reportType").toString();
		String siteId = paraMap.get("siteId").toString();
		String type1 = paraMap.get("type1").toString();
		String type2 = paraMap.get("type2").toString();
//		String siteId = "253";
		paraMap.put("type", 1);
		paraMap.put("siteId",siteId);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("站点不存在");
			return result;
		}
		wordMap.put("title", site.getSiteTitle()==null?"":site.getSiteTitle());
		wordMap.put("domain", site.getSiteDomain()==null?"":site.getSiteDomain());
		wordMap.put("ip", site.getSiteIp()==null?"":site.getSiteIp());
		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		List<String> dateList = DateUtil.getBetweenDate(startTime, endTime);
		String rtName;
		if("M".equals(reportType)){			//月报
			rtName="月报";
//			startTime = SignUtil.lastMonthStartTime(-1);
		}else{													//季报
			rtName="季报";
//			startTime = SignUtil.lastMonthStartTime(-3);
		}
		wordMap.put("date", endTime.substring(0, 4)+"年"+endTime.substring(4,6)+"月");
		wordMap.put("date2", SignUtil.dateFormat(SignUtil.dateStrParse(startTime, "yyyyMMdd"), "yyyy年MM月dd日"));
		wordMap.put("date3", SignUtil.dateFormat(SignUtil.dateStrParse(endTime, "yyyyMMdd"), "yyyy年MM月dd日"));
		
		//web流量
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.termQuery("site_id", siteId));
		builder.must(QueryBuilders.rangeQuery("date").gte(startTime).lte(endTime));
		SearchRequestBuilder srb1 = esClient.prepareSearch("cloud").setTypes("waf_web").setQuery(builder);
		srb1.setSearchType(SearchType.COUNT);
		TermsBuilder termBuilder1= AggregationBuilders.terms("webDate").field("date").size(0);
		SumBuilder sumBuilder1= AggregationBuilders.sum("sumFlow").field("flow");
		srb1.addAggregation(termBuilder1.subAggregation(sumBuilder1));
		SearchResponse sr1 = srb1.execute().actionGet();
		Terms term1 = sr1.getAggregations().get("webDate");
		List<Bucket> buckets1 = term1.getBuckets();
		
		//cc流量
		SearchRequestBuilder srb2 = esClient.prepareSearch("cloud").setTypes("waf_cc_detail").setQuery(builder);
		srb2.setSearchType(SearchType.COUNT);
		TermsBuilder termBuilder2= AggregationBuilders.terms("ccDate").field("date");
		SumBuilder sumBuilder2= AggregationBuilders.sum("sumFlow").field("flow");
		srb2.addAggregation(termBuilder2.subAggregation(sumBuilder2));
		SearchResponse sr2 = srb2.execute().actionGet();
		Terms term2 = sr2.getAggregations().get("ccDate");
		List<Bucket> buckets2 = term2.getBuckets();
		String chart1 = ChartUtils.genCompareLineChart("流量数对比", "web流量数", "cc流量数", buckets1, buckets2,dateList);
		wordMap.put("vulh", chart1);
		
		//攻击者地区分布
		SearchRequestBuilder srb3 = esClient.prepareSearch("cloud").setTypes("waf_attack_detail").setQuery(builder);
		srb3.setSearchType(SearchType.COUNT);
		TermsBuilder termBuilder3= AggregationBuilders.terms("attackLocation").field("location");
		srb3.addAggregation(termBuilder3);
		srb3.addSort("location",SortOrder.DESC);
		SearchResponse sr3 = srb3.execute().actionGet();
		Terms term3 = sr3.getAggregations().get("attackLocation");
		List<Bucket> buckets3 = term3.getBuckets();
		String chart2 = ChartUtils.createPieChart("攻击者地区分布", "攻击者地区", buckets3);
		wordMap.put("vulf", chart2);
		
		//历史攻击拦截趋势
		SearchRequestBuilder srb4 = esClient.prepareSearch("cloud").setTypes("waf_attack_detail").setQuery(builder);
		srb4.setSearchType(SearchType.COUNT);
		TermsBuilder termBuilder4= AggregationBuilders.terms("attackDate").field("date");
		srb4.addAggregation(termBuilder4);
		SearchResponse sr4 = srb4.execute().actionGet();
		Terms term4 = sr4.getAggregations().get("attackDate");
		List<Bucket> buckets4 = term4.getBuckets();
		String chart3 = ChartUtils.genLineChart("历史攻击拦截趋势", "攻击拦截", buckets4);
		wordMap.put("vuld", chart3);
		long totalAttacts = 0;
		for (Bucket bucket : buckets4) {
			totalAttacts = totalAttacts + bucket.getDocCount();
        }
		wordMap.put("totle1", totalAttacts);
		wordMap.put("totle2", totalAttacts);
		
		//月度攻击拦截详情
		String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
				{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
				{"文件包含","LRFI"},{"特殊攻击","SPECIAL"},{"其他","OTHERS"}};
		SearchRequestBuilder srb5 = esClient.prepareSearch("cloud").setTypes("waf_attack_trend").setQuery(builder);
		srb5.setSearchType(SearchType.COUNT);
		for(int i=0;i<arrs.length;i++){
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
			srb5.addAggregation(sumBuilder);
		}
		SearchResponse sr5 = srb5.execute().actionGet();
		Map<String,Aggregation> rsMap=sr5.getAggregations().getAsMap();
		String chart4 = ChartUtils.genBarChart("月度攻击拦截详情","攻击拦截",arrs,rsMap);
		wordMap.put("vulg", chart4);
		
		//攻击IP次数TOP10
		SearchRequestBuilder srb6 = esClient.prepareSearch("cloud").setTypes("waf_attack_detail").setQuery(builder);
		srb6.setSearchType(SearchType.COUNT);
		TermsBuilder termBuilder6= AggregationBuilders.terms("attackIp").field("ip");
		srb6.addAggregation(termBuilder6);
		srb6.addSort("ip",SortOrder.DESC);
		SearchResponse sr6 = srb6.execute().actionGet();
		Terms term6 = sr6.getAggregations().get("attackIp");
		List<Bucket> buckets6 = term6.getBuckets();
		String chart5 = ChartUtils.createBarChart("攻击IP次数TOP10", "攻击", buckets6);
		wordMap.put("vuls", chart5);
		
		SearchRequestBuilder srb7 = esClient.prepareSearch("cloud").setTypes("waf_attack_detail").setQuery(builder);
		srb7.addSort("create_time",SortOrder.DESC).setFrom(0).setSize(5);
		SearchResponse sr7 = srb7.execute().actionGet();
		SearchHits searchHits = sr7.getHits();
		List<AttackInfo> attackList = new ArrayList<AttackInfo>();
		for(SearchHit sh:searchHits){
			Map<String,Object> source = sh.getSource();
			AttackInfo attackInfo = new AttackInfo();
			String attackTime = "";
			if(source.get("time")!=null){
				attackTime = new BigDecimal(source.get("time").toString()).toPlainString();
			}
			attackInfo.setDate4(attackTime);
			attackInfo.setPosition2(source.get("location")==null?"":source.get("location").toString());
			attackInfo.setIp2(source.get("ip")==null?"":source.get("ip").toString());
			attackInfo.setAttack(source.get("type")==null?"":source.get("type").toString());
			attackInfo.setResult(source.get("status")==null?"":source.get("status").toString());
			attackList.add(attackInfo);
		}
		wordMap.put("latestAttackTimes", attackList.size());
		wordMap.put("alist", attackList);
		FreemarkerUtil util = new FreemarkerUtil();
		File outFile = util.createDoc(wordMap,"waf_templet.ftl","安全防护"+rtName+"("+startTime.substring(0,6)+").doc");
		
		saveMongoFile(outFile,siteId,type1,type2,startTime.substring(0,6));
		
		result.setData(outFile);
		result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("报表产生成功");
		return result;
	}
	
	/**
	 * 将报表文档存到mongoDB中，并记录在mysql中
	 * @param wordFile
	 * @param siteId
	 * @throws Exception
	 */
	private void saveMongoFile(File wordFile,String siteId,String type1,String type2,String rptTime) throws Exception{
		DB db = mu.getDB(FILE_DB);
		GridFS gridFS = new GridFS(db);
		GridFSInputFile gridFSInputFile = gridFS.createFile(wordFile);  
		String uuid = UUID.randomUUID().toString();
        gridFSInputFile.setId(uuid);  
        gridFSInputFile.setFilename(wordFile.getName()); 
        gridFSInputFile.setContentType("application/msword");
        gridFSInputFile.save();
        
        WordReport wordReport = new WordReport();
        wordReport.setSiteId(Integer.parseInt(siteId));
        wordReport.setReportTime(rptTime);
        wordReport.setType1(type1);
        wordReport.setType2(type2);
        wordReport.setFileId(uuid);
        wordReport.setFileName(wordFile.getName());
        wordReport.setCreateTime(new Date());
        wordReport.setFileSize(wordFile.length());
        wordReportMapper.insertWordReport(wordReport);
        wordFile.delete();
	}

	@Override
	public List<WordReport> findReportsByCondition(Map<String, Object> paraMap) throws Exception {
		List<WordReport> reportList = wordReportMapper.findReportByCondition(paraMap);
		return reportList;
	}
	
	public long countReportByCondition(Map<String, Object> paraMap) throws Exception {
		long totalPage = wordReportMapper.countReportByCondition(paraMap);
		return totalPage;
	}

	@Override
	public Result getRealTimeAttacks(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		QueryBuilder builder = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("create_user","" + paraMap.get("createUser")));
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_detail").setQuery(builder)
				.addSort("time", SortOrder.DESC).setSize(10).execute()
				.actionGet();
		SearchHits hits = searchResponse.getHits();
		if (hits == null || hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SearchHit[] searchHists = hits.getHits();
		for (int i = 0; i < searchHists.length; i++) {
			Map<String, Object> data = searchHists[i].getSource();
			Map<String, Object> m = new HashMap<String, Object>();
			BigDecimal bigDecimal = new BigDecimal(String.valueOf(data
					.get("time")));
			Date thisTime = dateFormater2.parse(bigDecimal.toPlainString());
			m.put("time", dateFormater1.format(thisTime));
			String url = data.get("url").toString();
			String domain = url.split("/")[2];
			m.put("url", domain);
			m.put("ip", data.get("ip"));
			m.put("location", data.get("location"));
			m.put("type", data.get("type"));
			m.put("times", data.get("times"));
			list.add(m);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(list);
		return result;
	}

	@Override
	public Result getAttackTrendTop(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		builder.must(QueryBuilders.termsQuery("create_user", ""+paraMap.get("createUser").toString()));
		//builder.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		String endDate = SignUtil.getDate();
		String startDate = SignUtil.getStart();
		builder.must(QueryBuilders.rangeQuery("date").gte(startDate).lte(endDate));		
		String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
				{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
				{"文件包含","LRFI"},{"特殊攻击","SPECIAL"}};
		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
		srb.setTypes("waf_attack_trend");
		srb.setSearchType(SearchType.QUERY_THEN_FETCH);
		for(int i=0;i<arrs.length;i++){
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
			srb.addAggregation(sumBuilder);
		}
		SearchResponse sr = srb.execute().get();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<arrs.length;i++){
			Sum sum = sr.getAggregations().get(arrs[i][1]);
			Map<String,Object> mapData = new HashMap<String,Object>();
			if(sum.getValue()>0){
				mapData.put("name", arrs[i][0]);
				mapData.put("value", sum.getValue());
				list.add(mapData);
			}
		}
		if(list.size() == 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
	        result.setMsg("今日未受到攻击！");
	        return result;
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1,
					Map<String, Object> o2) {
				int ret = 0;
				// 比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
				ret = new Double(o2.get("value").toString()).intValue()
						- new Double(o1.get("value").toString()).intValue();// 逆序的话就用o2.compareTo(o1)即可
				return ret;
			}
		});
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(list);
		return result;
	}

	@Override
	public Result getOverallAttackTrend(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		List<String> dateList = SignUtil.getAllMoDays(SignUtil.getDate());
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for (String date : dateList) {
			Map<String,Object> dataMap = new HashMap<String,Object>();
			int sumTimes = 0;
			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			builder.must(QueryBuilders.matchAllQuery());
			builder.must(QueryBuilders.termsQuery("create_user", ""+paraMap.get("createUser").toString()));
			builder.must(QueryBuilders.termsQuery("date", date));
			String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
					{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
					{"文件包含","LRFI"},{"特殊攻击","SPECIAL"},{"其他","OTHERS"}};
			SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
			srb.setTypes("waf_attack_trend");
			srb.setSearchType(SearchType.QUERY_THEN_FETCH);
			for(int i=0;i<arrs.length;i++){
				SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
				srb.addAggregation(sumBuilder);
			}
			SearchResponse sr = srb.execute().get();
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i=0;i<arrs.length;i++){
				Sum sum = sr.getAggregations().get(arrs[i][1]);
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("name", arrs[i][0]);
				mapData.put("value", sum.getValue());
				sumTimes += sum.getValue();
				list.add(mapData);
			}
			dataMap.put("date", date.substring(4, 6)+"-"+date.substring(6, 8));
			dataMap.put("times", sumTimes);
			dataMap.put("data", list);
			dataList.add(dataMap);
		}
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(dataList);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result getAvgScore() throws Exception {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String,Object>();
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(
				"cloud").setTypes("site_risk");   
		AggregationBuilder aggregation = AggregationBuilders.terms("agg")
				  .field("siteUrl").size(0).subAggregation(AggregationBuilders
			                .topHits("top")
							.addSort("last_end_time",SortOrder.DESC).setSize(1));							
		SearchResponse sr1 = searchRequestBuilder.addAggregation(aggregation).addField("risk")
                            .get();
		Terms agg = sr1.getAggregations().get("agg");
		List<Bucket> vulN1 = agg.getBuckets();
		if(vulN1.size()>0){
			int avgScore1 = getavg(vulN1);
			double a = avgScore1;
			data.put("avgScore", avgScore1);
			//环比时间节点
			long lastMonthNowTime = SignUtil.lastMonthNowTime();
			BoolQueryBuilder builder1 = QueryBuilders.boolQuery();
			builder1.must(QueryBuilders.matchAllQuery());
			builder1.must(QueryBuilders.rangeQuery("last_end_time")
					.to(lastMonthNowTime));
			AggregationBuilder aggregation2 = AggregationBuilders.terms("agg2")
					  .field("siteUrl").size(0).subAggregation(AggregationBuilders
				                .topHits("top")
								.addSort("last_end_time",SortOrder.DESC).setSize(1));	
			SearchResponse sr2 = searchRequestBuilder.setQuery(builder1).addSort("last_end_time", SortOrder.DESC)
					.addAggregation(aggregation2).get();
			Terms agg2 = sr2.getAggregations().get("agg2");		
			List<Bucket> vulN2 = agg2.getBuckets();
			if(vulN2.size()>0){
				int avgScore2 = getavg(vulN2);
				double b = avgScore2;
				if(a > b){
					data.put("MOM", (int)((a-b)/b*100)+"%");
					data.put("mom_s", 1);
				}else if(a < b){
					data.put("MOM", (int)((b-a)/b*100)+"%");
					data.put("mom_s", -1);
				}else{
					data.put("MOM", "不变");
					data.put("mom_s", 0);
				}
			}else{
				data.put("MOM", "暂无");
			}		
			//同比时间节点
			long lastYearNowTime = SignUtil.getLastYearNowTime();
			BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
			builder2.must(QueryBuilders.matchAllQuery());
			builder2.must(QueryBuilders.rangeQuery("last_end_time")
					.to(lastYearNowTime));
			AggregationBuilder aggregation3 = AggregationBuilders.terms("agg3")
					  .field("siteUrl").size(0).subAggregation(AggregationBuilders
				                .topHits("top")
								.addSort("last_end_time",SortOrder.DESC).setSize(1));	
			SearchResponse sr3 = searchRequestBuilder.setQuery(builder2).addSort("last_end_time", SortOrder.DESC)
					.addAggregation(aggregation3).get();
			Terms agg3 = sr3.getAggregations().get("agg3");		
			List<Bucket> vulN3 = agg3.getBuckets();
			if(vulN3.size()>0){
				int avgScore3 = getavg(vulN3);
				double c = avgScore3;
				if(a > c){
					data.put("YOY", (int)((a-c)/c*100)+"%");
					data.put("yoy_s", 1);
				}else if(a < c){
					data.put("YOY", (int)((c-a)/c*100)+"%");
					data.put("yoy_s", -1);
				}else{
					data.put("YOY", "不变");
					data.put("yoy_s", 0);
				}
			}else{
				data.put("YOY", "暂无");
			}		
		}else{
			data.put("avgScore", "暂无");
		}		
		result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
		result.setData(data);
		return result;
	}
	
	public int getavg(List<Bucket> bucketList){
		int sumScore = 0;
		for(Bucket test: bucketList){
			TopHits topHits  = 	test.getAggregations().get("top"); 			
			for(SearchHit hit :topHits.getHits()){
				int score = 0;
				Map<String, Object> element = hit.sourceAsMap();
				int risk = Integer.parseInt(element.get("risk").toString());				
				if(risk == 0 || risk == 1 || risk == 2){
					score = 100 - risk * 10 ;
				}else if(risk ==3 ){
					score = 78 ;
				}else if(risk ==4 ){
					score = 65 ;
				}else if(risk ==5 ){
					score = 60 ;
				}else if(risk ==6 ){
					score = 59 ;
				}else if(risk ==7 ){
					score = 47 ;
				}else if(risk ==8 ){
					score = 40 ;
				}else if(risk >8 && risk <=16 ){
					score = 35 ;
				}else if(risk >16 && risk <=24 ){
					score = 20 ;
				}else if(risk >24 && risk <=30 ){
					score = 7 ;
				}else{
					score = 2 ;
				}
				sumScore += score;
			}
		}
		int avgScore = sumScore/bucketList.size();
		return avgScore;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Result vulsDistribution() throws Exception {
		Result result = new Result();
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(
				"cloud").setTypes("site_risk"); 
		AggregationBuilder aggregation = AggregationBuilders
				.terms("agg")
				.field("site_id")
				.size(0)
				.subAggregation(
						AggregationBuilders
								.terms("last_end_time")
								.field("last_end_time")
								.size(1)
								.order(Terms.Order.aggregation("_term", false))
								.subAggregation(
										AggregationBuilders
												.terms("data")
												.script(new Script(
														"doc['site_id'].value+ '|' + doc['severity_points'].value "))
												.size(0)));
		SearchResponse sr = searchRequestBuilder.addAggregation(aggregation)
				            .get();
		Terms agg = sr.getAggregations().get("agg");
		List<Bucket> parents = agg.getBuckets();
		int high = 0;
		int mid = 0;
		int low = 0;
		for(Bucket parent: parents){
			Terms node  = parent.getAggregations().get("last_end_time"); 
			List<Bucket> childs = node.getBuckets();
			for(Bucket child:childs){
				Terms topHits  = child.getAggregations().get("data"); 
				List<Bucket> vulNs = topHits.getBuckets();
				for(Bucket vulN:vulNs){					
					String[] keys = vulN.getKey().toString().split("\\|");					
					if (Integer.parseInt(keys[1]) > 7) {
						high += vulN.getDocCount();		
					} else if (Integer.parseInt(keys[1]) < 5) {
						low += vulN.getDocCount();
					} else {
						mid += vulN.getDocCount();
					}
				}
			}
		}
		String[] arr = {"高危","中危","低危"};
		int[] sum = {high,mid,low};
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<3;i++){
			Map<String,Object> mapData = new HashMap<String,Object>();
			mapData.put("name", arr[i]);
			mapData.put("value", sum[i]);
			list1.add(mapData);
		}
		result.setData(list1);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功！");
		return result;		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result getAllHighRiskTop() throws Exception {
		Result result = new Result();
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		QueryBuilder severity_points = QueryBuilders
				.rangeQuery("severity_points")
				.gte(8)
				.lte(10);
		builder.must(severity_points);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(
				"cloud").setTypes("site_risk");   
		AggregationBuilder aggregation = AggregationBuilders
				.terms("agg")
				.field("site_id")
				.size(0)
				.subAggregation(
						AggregationBuilders
								.terms("last_end_time")
								.field("last_end_time")
								.size(1)
								.order(Terms.Order.aggregation("_term", false))
								.subAggregation(
										AggregationBuilders
												.terms("data")
												.script(new Script(
														"doc['title'].value "))
												.size(0)));
		SearchResponse sr = searchRequestBuilder.setQuery(builder).addAggregation(aggregation)
				            .get();
		// 所有的网站集合
				Map<String, Integer> map = new LinkedHashMap<String, Integer>();
				Terms agg = sr.getAggregations().get("agg");
				List<Bucket> parents = agg.getBuckets();
				for (Bucket parent : parents) {
					// 每一个网站最近一次扫描数据
					Terms node = parent.getAggregations().get("last_end_time");
					List<Bucket> childs = node.getBuckets();
					for (Bucket child : childs) {
						// 每一个网站最近一次扫描漏洞的统计集
						Terms topHits = child.getAggregations().get("data");
						List<Bucket> vulNs = topHits.getBuckets();
						for (Bucket vulN : vulNs) {
							String key = vulN.getKey().toString();							
							if (map.get(key) != null) {
								int num = map.get(key);
								num += vulN.getDocCount();
								map.put(key, num);
							} else {
								int num = (int) vulN.getDocCount();
								map.put(key, num);
							}
						}
					}
				}
				map = ObjectUtil.sortMap(map);
				Iterator it = map.entrySet().iterator();
				int i = 0;
				List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
				while (it.hasNext()) {
					if (map.size() >= 10) {
						if (i < 10) {
							i++;
						} else {
							break;
						}
					}
					Map<String, Integer> resultMap = new HashMap<String, Integer>();
					Map.Entry entry = (Map.Entry) it.next();
					resultMap.put(entry.getKey().toString(),
							Integer.valueOf(entry.getValue().toString()));
					list.add(resultMap);
				}
				result.setCode(Constance.RESPONSE_SUCCESS);
		        result.setMsg("获取成功");
		        result.setData(list);
				return result;
	}

	@Override
	public Result getAllAttackTrendCondition() throws Exception {
		Result result = new Result();
		String endDate = SignUtil.getDate();
		String startDate = SignUtil.getStart();
		String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
				{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
				{"文件包含","LRFI"},{"特殊攻击","SPECIAL"},{"其他","OTHERS"}};
		QueryBuilder builder = QueryBuilders.boolQuery()				
				.must(QueryBuilders.rangeQuery("date").gte(startDate).lte(endDate));
		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
		srb.setTypes("waf_attack_trend");
		srb.setSearchType(SearchType.QUERY_THEN_FETCH);
		for(int i=0;i<arrs.length;i++){
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
			srb.addAggregation(sumBuilder);
		}
		SearchResponse searchResponse = srb.execute().get();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<arrs.length;i++){
			Sum sum = searchResponse.getAggregations().get(arrs[i][1]);
			Map<String,Object> mapData = new HashMap<String,Object>();
			mapData.put("name", arrs[i][0]);
			mapData.put("value", sum.getValue());
			list.add(mapData);
		}
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(list);
		return result;
	}

	@Override
	public Result getAlAttackTrendTop() throws Exception {
		Result result = new Result();
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		String endDate = SignUtil.getDate();
		String startDate = SignUtil.getStart();
		builder.must(QueryBuilders.rangeQuery("date").gte(startDate).lte(endDate));		
		String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
				{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
				{"文件包含","LRFI"},{"特殊攻击","SPECIAL"}};
		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
		srb.setTypes("waf_attack_trend");
		srb.setSearchType(SearchType.QUERY_THEN_FETCH);
		for(int i=0;i<arrs.length;i++){
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
			srb.addAggregation(sumBuilder);
		}
		SearchResponse sr = srb.execute().get();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<arrs.length;i++){
			Sum sum = sr.getAggregations().get(arrs[i][1]);
			Map<String,Object> mapData = new HashMap<String,Object>();
			if(sum.getValue()>0){
				mapData.put("name", arrs[i][0]);
				mapData.put("value", sum.getValue());
				list.add(mapData);
			}
		}
		if(list.size() == 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
	        result.setMsg("今日未受到攻击！");
	        return result;
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1,
					Map<String, Object> o2) {
				int ret = 0;
				// 比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
				//String s=o1.get("value").toString();
				ret = new Double(o2.get("value").toString()).intValue()
						- new Double(o1.get("value").toString()).intValue();// 逆序的话就用o2.compareTo(o1)即可
				return ret;
			}
		});
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(list);
		return result;
	}

	@Override
	public Result getAllOverallAttackTrend() throws Exception {
		Result result = new Result();
		List<String> dateList = SignUtil.getAllMoDays(SignUtil.getDate());
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for (String date : dateList) {
			Map<String,Object> dataMap = new HashMap<String,Object>();
			int sumTimes = 0;
			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			builder.must(QueryBuilders.matchAllQuery());
			builder.must(QueryBuilders.termsQuery("date", date));
			String[][] arrs = {{"恶意扫描","SCANNER"},{"远程命令","OS_COMMAND"},{"XSS跨站","XSS"},{"SQL注入","SQLI"},
					{"文件注入","FILEI"},{"webshell","WEBSHELL"},{"代码执行","CODE"},{"恶意采集","COLLECTOR"},
					{"文件包含","LRFI"},{"特殊攻击","SPECIAL"},{"其他","OTHERS"}};
			SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(builder);
			srb.setTypes("waf_attack_trend");
			srb.setSearchType(SearchType.QUERY_THEN_FETCH);
			for(int i=0;i<arrs.length;i++){
				SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(arrs[i][1]);
				srb.addAggregation(sumBuilder);
			}
			SearchResponse sr = srb.execute().get();
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i=0;i<arrs.length;i++){
				Sum sum = sr.getAggregations().get(arrs[i][1]);
				Map<String,Object> mapData = new HashMap<String,Object>();
				mapData.put("name", arrs[i][0]);
				mapData.put("value", sum.getValue());
				sumTimes += sum.getValue();
				list.add(mapData);
			}
			dataMap.put("date", date.substring(4, 6)+"-"+date.substring(6, 8));
			dataMap.put("times", sumTimes);
			dataMap.put("data", list);
			dataList.add(dataMap);
		}
        result.setCode(Constance.RESPONSE_SUCCESS);
        result.setMsg("获取成功");
        result.setData(dataList);
		return result;
	}

	@Override
	public Result getAllRealTimeAttacks() throws Exception {
		Result result = new Result();
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_detail").setQuery(builder)
				.addSort("time", SortOrder.DESC).setSize(10).execute()
				.actionGet();
		SearchHits hits = searchResponse.getHits();
		if (hits == null || hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SearchHit[] searchHists = hits.getHits();
		for (int i = 0; i < searchHists.length; i++) {
			Map<String, Object> data = searchHists[i].getSource();
			Map<String, Object> m = new HashMap<String, Object>();
			BigDecimal bigDecimal = new BigDecimal(String.valueOf(data
					.get("time")));
			Date thisTime = dateFormater2.parse(bigDecimal.toPlainString());
			m.put("time", dateFormater1.format(thisTime));
			String url = data.get("url").toString();
			String domain = url.split("/")[2];
			m.put("url", domain);
			m.put("ip", data.get("ip"));
			m.put("location", data.get("location"));
			m.put("type", data.get("type"));
			m.put("times", data.get("times"));
			list.add(m);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(list);
		return result;
	}

	@Override
	public Result getSafeSiteTotle(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String,Object> data = new HashMap<String,Object>();
		List<Map<String,Object>> safeSiteList = siteMapper.getSafeSiteByUser(paraMap);
		int safeSiteCount = siteMapper.getSafeSiteCountByUser(paraMap);
		data.put("safeSiteList", safeSiteList);
		data.put("safeSiteCount", safeSiteCount);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(data);
		return result;
	}

	@Override
	public Result getWafSiteTotle(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String,Object> data = new HashMap<String,Object>();
		List<Map<String,Object>> wafSiteList = siteMapper.getWafSiteByUser(paraMap);
		int wafSiteCount = siteMapper.getWafSiteCountByUser(paraMap);
		data.put("wafSiteList", wafSiteList);
		data.put("wafSiteCount", wafSiteCount);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(data);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Result getSiteTotle() throws Exception {
		Result result = new Result();
		Map<String,Object> data = siteMapper.getSiteCount();
		QueryBuilder builder = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.rangeQuery("risk")
									.gte(8));
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(
				"cloud").setTypes("site_risk");   
		AggregationBuilder aggregation = AggregationBuilders.terms("agg")
				  .field("siteUrl").size(0).subAggregation(AggregationBuilders
			                .topHits("top")
							.addSort("last_end_time",SortOrder.DESC).setSize(1));							
		SearchResponse sr1 = searchRequestBuilder.setQuery(builder).setSize(1000).addAggregation(aggregation)
                            .get();
		Terms agg = sr1.getAggregations().get("agg");
		List<Bucket> vulN1 = agg.getBuckets();
		data.put("highRiskTotle", vulN1.size());
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(data);
		return result;
	}

	@Override
	public Result getHighRiskSiteTotle(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String create_user = paraMap.get("createUser").toString();
		long userId = Long.parseLong(create_user);
		list = siteMapper.getAllSafeSite(userId);
		if( list.size() == 0){
			result.setMsg("暂无数据！");
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(null);
			return result;
		}else{
			Map<String,Object> data = new HashMap<String,Object>();
			int count = 0;
			for (Map<String, Object> map : list) {
				String siteId = map.get("id").toString();
				map.put("siteId", siteId);
				Map<String,Object> last = new HashMap<String,Object>();				
				last = siteMapper.getLast(map);				
				if(last == null){
					continue;
				}else{
					String last_end_time = last.get("lastEndTime").toString();
					QueryBuilder qb = QueryBuilders
							.boolQuery()
							.must(QueryBuilders.termsQuery("create_user","" + create_user))
							.must(QueryBuilders.termsQuery("last_end_time","" + last_end_time));
					SearchResponse searchResponse = esClient.prepareSearch("cloud")
							.setTypes("site_risk").setQuery(qb)
							.execute().actionGet();
					SearchHits hits = searchResponse.getHits();
					if (hits.getTotalHits() == 0) {
						continue;
					}
					SearchHit[] searchHists = hits.getHits();
					Map<String, Object> data1 = searchHists[0].getSource();
					int risk =Integer.parseInt(data1.get("risk").toString());
					if(risk >= 8){
						count++;
					}
				}
			}
			data.put("highRiskTotle", count);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			result.setData(data);
			return result;
		}
	}
}
