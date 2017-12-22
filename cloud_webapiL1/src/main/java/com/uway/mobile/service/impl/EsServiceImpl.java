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
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;

import java.util.TreeMap;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Aurora;
import com.uway.mobile.domain.ChangeUrl;
import com.uway.mobile.domain.Event;
import com.uway.mobile.domain.Hole;
import com.uway.mobile.domain.Keypage;
import com.uway.mobile.domain.Keyword;
import com.uway.mobile.domain.Malware;
import com.uway.mobile.domain.Page;
import com.uway.mobile.domain.Param;
import com.uway.mobile.domain.Site;
import com.uway.mobile.domain.Smooth;
import com.uway.mobile.domain.Vul;
import com.uway.mobile.domain.Vuls;
import com.uway.mobile.domain.WordReport;
import com.uway.mobile.mapper.SiteMapper;
import com.uway.mobile.mapper.WordReportMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.EsService;
import com.uway.mobile.util.ChartUtils;
import com.uway.mobile.util.DateUtil;
import com.uway.mobile.util.ElasticSearchUtils;
import com.uway.mobile.util.FreemarkerUtil;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.PagingUtil;
import com.uway.mobile.util.ReadXmlUtil;
import com.uway.mobile.util.SignUtil;

@Service
public class EsServiceImpl implements EsService {

	// 网站安全监测接口URL前缀
	@Value("${site.safe.prefix.url}")
	public String SAFE_URL;
	// 网站安全监测接口授权码
	@Value("${site.safe.code}")
	public String SAFE_CODE;
	@Autowired
	private MongoUtil mu;
	@Value("${spring.data.mongodb.filedb}")
	public String FILE_DB;

	@Autowired
	private WordReportMapper wordReportMapper;
	@Autowired
	private ApiService apiService;
	@Autowired
	private ElasticSearchUtils elasticUtils;
	@Autowired
	private SiteMapper siteMapper;
	@Autowired
	private Client esClient;

	@Override
	public Result getDay(Map<String, Object> paraMap) throws Exception {

		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启此服务");
			return result;
		}

		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_day_data").setQuery(query).execute().actionGet();
		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() < 1) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		Map<String, Object> data = searchHists[0].getSource();
		Map<String, Object> newData = new HashMap<String, Object>();
		if (data != null) {
			newData.put("attack_total", data.get("attack_total"));
			newData.put("attack_ips", data.get("attack_ips"));
			newData.put("req", data.get("req"));
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(newData);
		return result;
	}

	@Override
	public Result getTrend(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启此服务");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> newMap = new HashMap<String, Object>();
		QueryBuilder qb = QueryBuilders.boolQuery().must(
				QueryBuilders.termsQuery("site_id", "" + site.getId()));

		QueryBuilder date = QueryBuilders.rangeQuery("date")
				.gte(SignUtil.getDoubleStart()).lte(SignUtil.getDoubleDate());

		QueryBuilder filter = QueryBuilders.boolQuery().must(qb).must(date);
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_trend").setQuery(filter)
				.addSort("date", SortOrder.DESC).setSize(31).execute()
				.actionGet();

		SearchHits hits = searchResponse.getHits();

		if (hits != null && hits.getTotalHits() > 0) {
			SearchHit[] searchHists = hits.getHits();
			for (int i = 0; i < searchHists.length; i++) {
				Map<String, Object> data = searchHists[i].getSource();
				BigDecimal bigDecimal = new BigDecimal(data.get("date")
						.toString());
				map.put(bigDecimal.toPlainString(), data.get("counts"));
			}
		}

		String time[] = SignUtil.getDates();
		for (int i = 0; i < time.length; i++) {
			if (map.get(time[i]) == null || map.get(time[i]) == "") {
				newMap.put(time[i], 0);
			} else {
				newMap.put(time[i], map.get(time[i]));
			}
		}

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(newMap);
		return result;
	}

	@Override
	public void addToday(Map<String, Object> paraMap) throws Exception {
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId() };
		paraMap.put("paras", paras);
		JSONObject dayJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/sum?"
						+ SignUtil.sort(paras), paraMap);
		if (dayJson.get("code") == null
				|| !dayJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = dayJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		Map<String, Object> map = (Map<String, Object>) data;

		String id = site.getSiteWafId() + SignUtil.getDate();
		map.put("site_id", site.getId());
		map.put("site_waf_id", site.getSiteWafId());
		map.put("create_time", SignUtil.getNow());
		map.put("date", SignUtil.getDoubleDate());
		map.put("create_user", site.getCreateUser());

		elasticUtils.insert(Constance.ESINDEX, "waf_day_data", id, map);
	}

	@Override
	public void webTrend(Map<String, Object> paraMap) throws Exception {

		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String date = SignUtil.getDate();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId(), "type=general", "unit=day",
				"start_date=" + date, "end_date=" + date };
		paraMap.put("paras", paras);
		JSONObject webJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/clean_data?"
						+ SignUtil.sort(paras), paraMap);
		if (webJson.get("code") == null
				|| !webJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = webJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		JSONArray total = data.getJSONArray("total");
		if (total == null || total.size() == 0) {
			return;
		}
		JSONObject flow = (JSONObject) total.get(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("site_id", site.getId());
		map.put("site_waf_id", site.getSiteWafId());
		map.put("type", "general");
		map.put("date", date);
		map.put("flow", flow.get(date));
		map.put("create_time", SignUtil.getNow());
		map.put("create_user", site.getCreateUser());

		String id = "web" + site.getSiteWafId() + SignUtil.getDate();
		elasticUtils.insert(Constance.ESINDEX, "waf_web", id, map);
	}

	@Override
	public Result getWeb(Map<String, Object> paraMap) throws Exception {

		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启此服务");
			return result;
		}

		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()))
				.must(QueryBuilders.termsQuery("type", "general"));

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_web").setQuery(query).execute().actionGet();
		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() < 1) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		Map<String, Object> data = searchHists[0].getSource();
		Map<String, Object> newData = new HashMap<String, Object>();
		if (data != null) {
			newData.put("flow", data.get("flow"));
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(newData);
		return result;
	}

	@Override
	public void webcc(Map<String, Object> paraMap) throws Exception {
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String date = SignUtil.getDate();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId(), "type=cc", "unit=day",
				"start_date=" + date, "end_date=" + date };
		paraMap.put("paras", paras);
		JSONObject webJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/clean_data?"
						+ SignUtil.sort(paras), paraMap);
		if (webJson.get("code") == null
				|| !webJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = webJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		JSONArray total = data.getJSONArray("total");
		if (total == null || total.size() == 0) {
			return;
		}
		JSONObject flow = (JSONObject) total.get(0);
		JSONObject cc = flow.getJSONObject(date);
		if (cc == null) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("site_id", site.getId());
		map.put("site_waf_id", site.getSiteWafId());
		map.put("type", "cc");
		map.put("date", SignUtil.getDoubleDate());
		map.put("cc_ip", cc.get("cc_ip"));
		map.put("cc_num", cc.get("cc_num"));
		map.put("create_time", SignUtil.getNow());
		map.put("create_user", site.getCreateUser());

		String id = "cc" + site.getSiteWafId() + SignUtil.getDate();
		elasticUtils.insert(Constance.ESINDEX, "waf_web", id, map);
	}

	@Override
	public Result getWebcc(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()))
				.must(QueryBuilders.termsQuery("type", "cc"));

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_cc").setQuery(query).execute().actionGet();
		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() < 1) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		Map<String, Object> data = searchHists[0].getSource();

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(data);
		return result;
	}

	@Override
	public void attackTop(Map<String, Object> paraMap) throws Exception {
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String date = SignUtil.getDate();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId(), "date=" + date };
		paraMap.put("paras", paras);
		JSONObject topJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/attack_top?"
						+ SignUtil.sort(paras), paraMap);
		if (topJson.get("code") == null
				|| !topJson.getString("code").equals("0")) {

			return;
		}
		JSONObject data = topJson.getJSONObject("data");
		if (data == null) {

			return;
		}
		JSONArray top = data.getJSONArray("top");
		if (top == null || top.size() == 0) {
			return;
		}
		for (int j = 0; j < top.size(); j++) {
			JSONObject nv = top.getJSONObject(j);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("site_id", site.getId());
			map.put("site_waf_id", site.getSiteWafId());
			map.put("date", SignUtil.getDoubleDate());
			map.put("name", nv.get("name"));
			map.put("value", nv.get("value"));
			map.put("create_time", SignUtil.getNow());
			map.put("create_user", site.getCreateUser());

			String id = site.getSiteWafId() + SignUtil.getDate()
					+ SignUtil.toUnicode(nv.getString("name"));
			elasticUtils.insert(Constance.ESINDEX, "waf_attack_top", id, map);
		}
	}

	@Override
	public Result getAttackTop(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		QueryBuilder qb = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_top").setQuery(qb)
				.addSort("value", SortOrder.DESC).execute().actionGet();

		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		for (int i = 0; i < searchHists.length; i++) {
			Map<String, Object> data = searchHists[i].getSource();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", data.get("name"));
			m.put("value", data.get("value"));
			list.add(m);
		}

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(list);
		return result;
	}

	@Override
	public void attackTrend(Map<String, Object> paraMap) throws Exception {

		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}

		long time = SignUtil.getTime();
		String date = SignUtil.getDate();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId(), "type=general", "unit=day",
				"start_date=" + date, "end_date=" + date };
		paraMap.put("paras", paras);
		JSONObject attackJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/attack_trend?"
						+ SignUtil.sort(paras), paraMap);
		if (attackJson.get("code") == null
				|| !attackJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = attackJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		JSONArray total = data.getJSONArray("total");
		if (total == null || total.size() == 0) {
			return;
		}
		JSONObject all = total.getJSONObject(0).getJSONObject(date);
		JSONObject details = all.getJSONObject("details");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("site_id", paraMap.get("siteId"));
		map.put("site_waf_id", site.getSiteWafId());
		map.put("date", SignUtil.getDoubleDate());
		map.put("counts", all.get("counts"));
		map.put("WEBSHELL", details.get("WEBSHELL"));
		map.put("OS_COMMAND", details.get("OS_COMMAND"));
		map.put("FILEI", details.get("FILEI"));
		map.put("OTHERS", details.get("OTHERS"));
		map.put("LRFI", details.get("LRFI"));
		map.put("COLLECTOR", details.get("COLLECTOR"));
		map.put("SCANNER", details.get("SCANNER"));
		map.put("CODE", details.get("CODE"));
		map.put("XSS", details.get("XSS"));
		map.put("SPECIAL", details.get("SPECIAL"));
		map.put("SQLI", details.get("SQLI"));
		map.put("create_time", SignUtil.getNow());
		map.put("create_user", site.getCreateUser());
		if (details.get("LIMIT_RATE") != null) {
			map.put("LIMIT_RATE", details.get("LIMIT_RATE"));
		}

		String id = site.getSiteWafId() + SignUtil.getDate();
		elasticUtils.insert(Constance.ESINDEX, "waf_attack_trend", id, map);
	}

	@Override
	public Result getAttackTrend(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_trend").setQuery(query).execute()
				.actionGet();
		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		Map<String, Object> data = searchHists[0].getSource();

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(data);
		return result;
	}

	@Override
	public void attackDetail(Map<String, Object> paraMap) throws Exception {

		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String date = SignUtil.getDate();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId(), "search_date=" + date };
		paraMap.put("paras", paras);
		JSONObject attackJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/attack_detail?"
						+ SignUtil.sort(paras), paraMap);
		if (attackJson.get("code") == null
				|| !attackJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = attackJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		JSONArray detail = data.getJSONArray("detail");
		if (detail == null) {
			return;
		}
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		for (int j = 0; j < detail.size(); j++) {

			JSONObject d = detail.getJSONObject(j);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("site_id", site.getId());
			map.put("site_waf_id", site.getSiteWafId());
			map.put("date", SignUtil.getDoubleDate());
			map.put("status", d.get("status"));
			map.put("url", d.get("url"));
			map.put("ip", d.get("ip"));
			map.put("times", d.get("times"));
			map.put("location", d.get("location"));
			Date t = dateFormater1.parse(d.get("date").toString());
			map.put("time", Double.valueOf(dateFormater2.format(t)));
			map.put("type", d.get("type"));
			map.put("create_time", SignUtil.getNow());
			map.put("create_user", site.getCreateUser());

			String id = site.getSiteWafId() + d.get("ip") + d.get("date")
					+ d.get("type");
			elasticUtils
					.insert(Constance.ESINDEX, "waf_attack_detail", id, map);
		}

	}

	@Override
	public Result getAttackDetail(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}

		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		builder.must(QueryBuilders.termsQuery("site_id", "" + site.getId()));

		String startTime = paraMap.get("start_time") == null ? "" : paraMap
				.get("start_time").toString().trim();
		String endTime = paraMap.get("end_time") == null ? "" : paraMap
				.get("end_time").toString().trim();
		if (!"".equals(startTime) && !"".equals(endTime)) {
			QueryBuilder date = QueryBuilders
					.rangeQuery("date")
					.gte(Double.valueOf(startTime.substring(0, 4)
							+ startTime.substring(5, 7)
							+ startTime.substring(8)))
					.lte(Double.valueOf(endTime.substring(0, 4)
							+ endTime.substring(5, 7) + endTime.substring(8)));
			builder.must(date);
		} else {
			builder.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		}

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_detail").setQuery(builder)
				.addSort("time", SortOrder.DESC).setSize(1000).execute()
				.actionGet();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		SearchHits hits = searchResponse.getHits();

		if (hits == null || hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		SearchHit[] searchHists = hits.getHits();

		for (int i = 0; i < searchHists.length; i++) {
			Map<String, Object> data = searchHists[i].getSource();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ip", data.get("ip"));
			m.put("type", data.get("type"));
			BigDecimal bigDecimal = new BigDecimal(String.valueOf(data
					.get("time")));
			Date thisTime = dateFormater2.parse(bigDecimal.toPlainString());
			m.put("time", dateFormater1.format(thisTime));
			m.put("url", data.get("url"));
			m.put("location", data.get("location"));
			m.put("times", data.get("times"));
			m.put("status", data.get("status"));
			list.add(m);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(list);
		return result;
	}

	@Override
	public Result getAttackTrendWithCondition(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}

		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		builder.must(QueryBuilders.termsQuery("site_id", "" + site.getId()));

		String startTime = paraMap.get("start_time") == null ? "" : paraMap
				.get("start_time").toString().trim();
		String endTime = paraMap.get("end_time") == null ? "" : paraMap
				.get("end_time").toString().trim();
		if (!"".equals(startTime) && !"".equals(endTime)) {
			QueryBuilder date = QueryBuilders
					.rangeQuery("date")
					.gte(Double.valueOf(startTime.substring(0, 4)
							+ startTime.substring(5, 7)
							+ startTime.substring(8)))
					.lte(Double.valueOf(endTime.substring(0, 4)
							+ endTime.substring(5, 7) + endTime.substring(8)));
			builder.must(date);
		} else {
			builder.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		}

		String[][] arrs = { { "恶意扫描", "SCANNER" }, { "远程命令", "OS_COMMAND" },
				{ "XSS跨站", "XSS" }, { "SQL注入", "SQLI" }, { "文件注入", "FILEI" },
				{ "webshell", "WEBSHELL" }, { "代码执行", "CODE" },
				{ "恶意采集", "COLLECTOR" }, { "文件包含", "LRFI" },
				{ "特殊攻击", "SPECIAL" }, { "其他", "OTHERS" } };
		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(
				builder);
		srb.setTypes("waf_attack_trend");
		srb.setSearchType(SearchType.QUERY_THEN_FETCH);
		for (int i = 0; i < arrs.length; i++) {
			SumBuilder sumBuilder = AggregationBuilders.sum(arrs[i][1]).field(
					arrs[i][1]);
			srb.addAggregation(sumBuilder);
		}
		SearchResponse sr = srb.execute().get();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < arrs.length; i++) {
			Sum sum = sr.getAggregations().get(arrs[i][1]);
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("name", arrs[i][0]);
			mapData.put("value", sum.getValue());
			list.add(mapData);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(JSON.toJSONString(list));
		return result;
	}

	@SuppressWarnings("deprecation")
	public Result getAttackCount(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}

		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilders.matchAllQuery());
		builder.must(QueryBuilders.termsQuery("site_id", "" + site.getId()));

		String startTime = paraMap.get("start_time") == null ? "" : paraMap
				.get("start_time").toString().trim();
		String endTime = paraMap.get("end_time") == null ? "" : paraMap
				.get("end_time").toString().trim();
		if (!"".equals(startTime) && !"".equals(endTime)) {
			QueryBuilder date = QueryBuilders
					.rangeQuery("date")
					.gte(Double.valueOf(startTime.substring(0, 4)
							+ startTime.substring(5, 7)
							+ startTime.substring(8)))
					.lte(Double.valueOf(endTime.substring(0, 4)
							+ endTime.substring(5, 7) + endTime.substring(8)));
			builder.must(date);
		} else {
			builder.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));
		}

		SearchRequestBuilder srb = esClient.prepareSearch("cloud").setQuery(
				builder);
		srb.setTypes("waf_attack_detail");
		srb.setSearchType(SearchType.COUNT);
		TermsBuilder gradeTermsBuilder = AggregationBuilders.terms("attackIp")
				.field("ip");
		srb.addAggregation(gradeTermsBuilder);
		SearchResponse sr = srb.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		StringTerms gradeTerms = (StringTerms) aggMap.get("attackIp");
		Iterator<Bucket> gradeBucketIt = gradeTerms.getBuckets().iterator();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (gradeBucketIt.hasNext()) {
			Bucket gradeBucket = gradeBucketIt.next();
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("name", gradeBucket.getKey());
			mapData.put("value", gradeBucket.getDocCount());
			list.add(mapData);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(JSON.toJSONString(list));
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	public Result getDayAll(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String date = SignUtil.getDate();
		QueryBuilder qb = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("create_user",
						"" + paraMap.get("createUser")))
				.must(QueryBuilders.termsQuery("date", SignUtil.getDate()));

		MaxBuilder max = AggregationBuilders.max("total").field("attack_total");
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_day_data").setQuery(qb).addAggregation(max)
				.execute().actionGet();

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public void ccDetail(Map<String, Object> paraMap) throws Exception {
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] { "time=" + time,
				"sid=" + site.getSiteWafId() };
		paraMap.put("paras", paras);
		JSONObject topJson = apiService.doWafGet(
				"https://www.yunaq.com/api/v3/report/cc_detail?"
						+ SignUtil.sort(paras), paraMap);
		if (topJson.get("code") == null) {
			return;
		}
		if (!topJson.getString("code").equals("0")) {
			return;
		}
		JSONObject data = topJson.getJSONObject("data");
		if (data == null) {
			return;
		}
		JSONArray detail = data.getJSONArray("detail");
		if (detail == null) {
			return;
		}
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = SignUtil.getDate();
		for (int i = 0; i < detail.size(); i++) {
			JSONObject j = detail.getJSONObject(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("site_id", site.getId());
			map.put("site_waf_id", site.getSiteWafId());
			map.put("date", date);
			map.put("status", j.get("status"));
			Date t = dateFormater1.parse(j.get("date").toString());
			map.put("time", Double.valueOf(dateFormater2.format(t)));
			map.put("location", j.get("location"));
			map.put("url", j.get("url"));
			map.put("ip", j.get("ip"));
			map.put("size", j.get("size"));
			map.put("type", j.get("type"));
			map.put("times", j.get("times"));
			map.put("create_time", SignUtil.getNow());
			map.put("create_user", site.getCreateUser());

			String id = site.getSiteWafId() + dateFormater2.format(t)
					+ j.get("ip");
			elasticUtils.insert(Constance.ESINDEX, "waf_cc_detail", id, map);

		}

	}

	@Override
	public void getReportXml(Map<String, Object> paraMap) throws Exception {
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			return;
		}
		String xmlReport = apiService.doGet(SAFE_URL + "getreport?authcode="
				+ SAFE_CODE + "&type=1&id=" + site.getSiteWafId());
		if (xmlReport == null || xmlReport == "" || xmlReport.startsWith("0:")
				|| !xmlReport.contains("<vuls>")) {
			return;
		}
		Aurora aurora = ReadXmlUtil.xmlToObject(xmlReport);
		if (aurora == null || aurora.getLast_end_time() == null
				|| aurora.getLast_end_time().equals("None")) {
			return;
		}
		Date last = dateFormater1.parse(aurora.getLast_end_time());
		paraMap.put("lastEndTime", Double.valueOf(dateFormater2.format(last)));
		Map<String, Object> scan = siteMapper.getScan(paraMap);
		if (scan != null) {
			return;
		}
		Vuls vuls = aurora.getVuls();
		List<Hole> holes = new ArrayList<Hole>();
		if (vuls == null || vuls.getHole() == null
				|| vuls.getHole().size() == 0) {
			return;
		}
		holes = vuls.getHole();
		for (int i = 0; i < holes.size(); i++) {
			Hole hole = holes.get(i);
			List<Vul> vul = hole.getVul();
			if (vul != null && vul.size() > 0) {
				for (int a = 0; a < vul.size(); a++) {
					Vul v = vul.get(a);
					Map<String, Object> map = getMap(site, aurora);
					map.put("type", "");
					map.put("title", hole.getTitle());
					map.put("vulid", hole.getVulid());
					map.put("vultype", hole.getVultype());
					map.put("description", hole.getDescription());
					map.put("solution", hole.getSolution());
					map.put("cve_id", hole.getCve_id());
					map.put("bugtraq", hole.getBugtraq());
					map.put("severity_points", hole.getSeverity_points());
					map.put("date_found", hole.getDate_found());
					/*
					 * map.put("method", v.getMethod()); map.put("url",
					 * v.getUrl()); map.put("issue", v.getIssue());
					 * map.put("exploit", v.getExploit());
					 */
					List<Param> params = v.getParam();
					for (Param p : params) {
						if (p.getAttr().equals("请求方式")) {
							map.put("method", p.getContent());
						}
						if (p.getAttr().equals("URL")) {
							map.put("url", p.getContent());
						}
						if (p.getAttr().equals("问题参数")) {
							map.put("issue", p.getContent());
						}
						if (p.getAttr().equals("参考(验证)")) {
							map.put("exploit", p.getContent());
						}
					}
					elasticUtils.insertES(Constance.ESINDEX, "site_risk", map);
				}
			} else {
				Map<String, Object> map = getMap(site, aurora);
				map.put("type", "");
				map.put("title", hole.getTitle());
				map.put("vulid", hole.getVulid());
				map.put("vultype", hole.getVultype());
				map.put("description", hole.getDescription());
				map.put("solution", hole.getSolution());
				map.put("cve_id", hole.getCve_id());
				map.put("bugtraq", hole.getBugtraq());
				map.put("severity_points", hole.getSeverity_points());
				map.put("date_found", hole.getDate_found());
				elasticUtils.insertES(Constance.ESINDEX, "site_risk", map);
			}
		}
		List<Keypage> vul_keypage = aurora.getVul_keypages();
		if (vul_keypage != null) {
			for (int i = 0; i < vul_keypage.size(); i++) {
				Keypage keypage = vul_keypage.get(i);
				if (keypage.getMalware() == null) {
					List<Param> params = keypage.getParams();
					for (int j = 0; j < params.size(); j++) {
						Param param = params.get(j);
						Map<String, Object> map = getMap(site, aurora);
						map.put("url", param.getContent());
						elasticUtils.insertES(Constance.ESINDEX, "vul_keypage",
								map);
					}
				} else {
					Malware malware = keypage.getMalware();
					List<Page> pages = malware.getPages();
					for (int j = 0; j < pages.size(); j++) {
						Page page = pages.get(j);
						Map<String, Object> map = getMap(site, aurora);
						map.put("url", page.getUrl());
						map.put("find_time", page.getFind_time());
						map.put("ref_urls", page.getRef_urls());
						map.put("ref_vuls", page.getRef_vuls());
						elasticUtils.insertES(Constance.ESINDEX, "vul_keypage",
								map);
					}
				}
			}
		}
		ChangeUrl change_url = aurora.getPagecrack();
		if (change_url != null) {
			List<Page> pages1 = change_url.getPages();
			for (int i = 0; i < pages1.size(); i++) {
				Page page = pages1.get(i);
				Map<String, Object> map = getMap(site, aurora);
				map.put("url", page.getUrl());
				map.put("find_time", page.getFind_time());
				map.put("find_time_latest", page.getFind_time_latest());
				map.put("info", page.getInfo());
				elasticUtils.insertES(Constance.ESINDEX, "change_url", map);
			}
		}
		ChangeUrl change_url1 = aurora.getDarkchain();
		if (change_url1 != null) {
			List<Page> pages2 = change_url1.getPages();
			for (int i = 0; i < pages2.size(); i++) {
				Page page = pages2.get(i);
				Map<String, Object> map = getMap(site, aurora);
				map.put("url", page.getUrl());
				map.put("find_time", page.getFind_time());
				map.put("find_time_latest", page.getFind_time_latest());
				map.put("info", page.getInfo());
				elasticUtils.insertES(Constance.ESINDEX, "change_url", map);
			}
		}
		Keyword keyword = aurora.getKeyword();
		if (keyword != null) {
			List<Page> pages3 = keyword.getPages();
			for (int i = 0; i < pages3.size(); i++) {
				Page page = pages3.get(i);
				Map<String, Object> map = getMap(site, aurora);
				map.put("url", page.getUrl());
				map.put("find_time", page.getFind_time());
				map.put("find_time_latest", page.getFind_time_latest());
				map.put("info", page.getInfo());
				elasticUtils.insertES(Constance.ESINDEX, "keyword", map);
			}
		}
		Smooth smooth = aurora.getSmooth();
		if (smooth != null) {
			List<Event> event = smooth.getEvent();
			for (int i = 0; i < event.size(); i++) {
				Event event1 = event.get(i);
				Map<String, Object> map = getMap(site, aurora);
				map.put("check_time", event1.getCheck_time());
				map.put("time_out", event1.getTime_out());
				map.put("respone_time", event1.getRespone_time());
				elasticUtils.insertES(Constance.ESINDEX, "smooth", map);
			}
		}
		paraMap.put("siteWafId", site.getSiteWafId());
		siteMapper.insertScan(paraMap);
	}

	public Map<String, Object> getMap(Site site, Aurora aurora)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("site_id", site.getId());
		map.put("site_waf_id", site.getSiteWafId());
		map.put("create_time", SignUtil.getNow());
		map.put("create_user", site.getCreateUser());
		map.put("date", SignUtil.getDoubleDate());
		Date f = dateFormater1.parse(aurora.getFromtime());
		map.put("from_time", Double.valueOf(dateFormater2.format(f)));
		Date e = dateFormater1.parse(aurora.getEndtime());
		Date last1 = dateFormater1.parse(aurora.getLast_end_time());
		map.put("last_end_time", Double.valueOf(dateFormater2.format(last1)));
		map.put("end_time", Double.valueOf(dateFormater2.format(e)));
		map.put("siteUrl", aurora.getSiteurl().split("//")[1]);
		map.put("risk", aurora.getRisk());
		return map;
	}

	@Override
	public Result getSiteRisk(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> all = new HashMap<String, Object>();
		Map<String, Object> msg = new HashMap<String, Object>();
		Map<String, Object> siteRisk = new HashMap<String, Object>();

		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, Object> last = siteMapper.getLast(paraMap);
		msg.put("siteUrl", site.getSiteDomain());
		msg.put("siteTitle", site.getSiteHead());
		msg.put("lastEndTime",
				last == null ? "" : dateFormater.parse(
						last.get("lastEndTime").toString()).getTime());
		all.put("msg", msg);
		if (last == null) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(all);
			result.setMsg("获取成功");
			return result;
		}
		QueryBuilder query = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query).execute().actionGet();
		SearchHits hits = searchResponse.getHits();

		if (hits.getTotalHits() == 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			result.setData(all);
			return result;
		}
		SearchHit[] searchHists = hits.getHits();
		Map<String, Object> data = searchHists[0].getSource();
		if (data == null) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			result.setData(all);
			return result;
		}
		siteRisk.put("fromTime", data.get("from_time"));
		siteRisk.put("endTime", data.get("end_time"));
		siteRisk.put("lastEndTime", data.get("last_end_time"));
		siteRisk.put("risk", data.get("risk"));
		all.put("siteRisk", siteRisk);

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(all);
		return result;
	}

	@Override
	public Result getHoles(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		TermsBuilder aggregationT = AggregationBuilders
				.terms("data")
				.script(new Script(
						"doc['site_id'].value+ '|' + doc['title'].value + '|' + doc['severity_points'].value"))
				.size(1000);

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query1).setSize(1000)
				.addAggregation(aggregationT).execute().actionGet();

		Terms t = searchResponse.getAggregations().get("data");
		List<Bucket> counts = t.getBuckets();
		List<Map<String, Object>> holeList = new ArrayList<Map<String, Object>>();
		for (Bucket b : counts) {
			String[] keys = b.getKey().toString().split("\\|");
			Map<String, Object> hole = new HashMap<String, Object>();
			hole.put("title", keys[1]);
			hole.put("severity_points", keys[2]);
			if (Integer.parseInt(keys[2]) > 7) {
				hole.put("level", "high");
			} else if (Integer.parseInt(keys[2]) < 5) {
				hole.put("level", "low");
			} else {
				hole.put("level", "mid");
			}
			hole.put("num", b.getDocCount());
			holeList.add(hole);
		}
		if (holeList != null) {
			result.setData(holeList);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getVuls(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));

		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query1).setSize(1000).execute()
				.actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();

		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();
			if (Integer.parseInt(data.get("severity_points").toString()) >= 8) {
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("title", data.get("title"));
				d.put("method", data.get("method"));
				d.put("issue", data.get("issue"));
				d.put("url", data.get("url"));
				d.put("exploit", data.get("exploit"));
				d.put("severity_points", data.get("severity_points"));
				vulList.add(d);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		if (vulList != null && vulList.size() > 0) {
			result.setData(vulList);
		}
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getAllSiteHole(String userId) throws Exception {
		Result result = new Result();
		QueryBuilder query = QueryBuilders.boolQuery().must(
				QueryBuilders.termsQuery("create_user", userId));

		TermsBuilder aggregationF = AggregationBuilders.terms("last_end_time")
				.field("last_end_time").size(1);
		TermsBuilder aggregation2 = AggregationBuilders.terms("vuls").script(
				new Script("doc['severity_points'].value"));
		aggregationF.subAggregation(aggregation2);
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query)
				.addAggregation(aggregationF)
				.addSort("last_end_time", SortOrder.DESC).execute().actionGet();

		Map<String, Object> map = new HashMap<String, Object>();
		int high = 0;
		int mid = 0;
		int low = 0;
		Terms t = searchResponse.getAggregations().get("last_end_time");
		List<Bucket> counts = t.getBuckets();
		for (Bucket b : counts) {

			map.put("total", b.getDocCount());

			Terms num = b.getAggregations().get("vuls");

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
		map.put("high", high);
		map.put("mid", mid);
		map.put("low", low);

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(map);
		return result;
	}

	@Override
	public Result getRiskTrend(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		List<Map<String, Object>> allLast = siteMapper.getAllLast(paraMap);
		if (allLast == null || allLast.size() == 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		Map<String, Object> trend = new HashMap<String, Object>();
		for (Map<String, Object> m : allLast) {
			QueryBuilder query = QueryBuilders
					.boolQuery()
					.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
					.must(QueryBuilders.termsQuery("last_end_time",
							m.get("lastEndTime")));
			SearchResponse searchResponse = esClient.prepareSearch("cloud")
					.setTypes("site_risk").setQuery(query).setFrom(0)
					.setSize(1).execute().actionGet();

			SearchHits hits = searchResponse.getHits();

			if (hits.getTotalHits() == 0) {
				BigDecimal bigDecimal = new BigDecimal(m.get("lastEndTime")
						.toString());
				trend.put(
						bigDecimal.toPlainString().substring(0, 8).toString(),
						0);
				continue;
			}
			SearchHit[] searchHists = hits.getHits();
			Map<String, Object> data = searchHists[0].getSource();
			if (data == null || data.get("risk") == null) {
				BigDecimal bigDecimal = new BigDecimal(m.get("lastEndTime")
						.toString());
				trend.put(
						bigDecimal.toPlainString().substring(0, 8).toString(),
						0);
				continue;
			}
			BigDecimal bigDecimal = new BigDecimal(m.get("lastEndTime")
					.toString());
			trend.put(bigDecimal.toPlainString().substring(0, 8),
					data.get("risk"));
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(trend);
		return result;
	}

	@Override
	public Result getVulKeypage(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		int pageNum = (int) paraMap.get("page_num");
		int pageSize = (int) paraMap.get("page_size");
		int num1 = (pageNum - 1) * pageSize;
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("vul_keypage").setQuery(query1).setFrom(num1)
				.setSize(pageSize).execute().actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();
		long totalCount = searchResponse2.getHits().getTotalHits();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", totalCount);
		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();
			{
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("url", data.get("url"));
				d.put("find_time", data.get("find_time"));
				d.put("ref_urls", data.get("ref_urls"));
				d.put("ref_vuls", data.get("ref_vuls"));
				vulList.add(d);
			}
		}
		if (vulList != null && vulList.size() > 0) {
			map.put("VulKeypage", vulList);
			result.setData(map);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getChangeUrl(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		int pageNum = (int) paraMap.get("page_num");
		int pageSize = (int) paraMap.get("page_size");
		int num1 = (pageNum - 1) * pageSize;
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("change_url").setQuery(query1).setFrom(num1)
				.setSize(pageSize).execute().actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();
		long totalCount = searchResponse2.getHits().getTotalHits();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", totalCount);
		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();
			{
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("url", data.get("url"));
				d.put("find_time", data.get("find_time"));
				d.put("find_time_latest", data.get("find_time_latest"));
				d.put("info", data.get("info"));
				vulList.add(d);
			}
		}
		if (vulList != null && vulList.size() > 0) {
			map.put("ChangeUrl", vulList);
			result.setData(map);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getKeyword(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		int pageNum = (int) paraMap.get("page_num");
		int pageSize = (int) paraMap.get("page_size");
		int num1 = (pageNum - 1) * pageSize;
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("keyword").setQuery(query1).setFrom(num1)
				.setSize(pageSize).execute().actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();
		long totalCount = searchResponse2.getHits().getTotalHits();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", totalCount);
		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();

			{
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("url", data.get("url"));
				d.put("find_time", data.get("find_time"));
				d.put("find_time_latest", data.get("find_time_latest"));
				d.put("info", data.get("info"));
				vulList.add(d);

			}
		}
		if (vulList != null && vulList.size() > 0) {
			map.put("getKeyword", vulList);
			result.setData(map);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getSmooth(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		int pageNum = (int) paraMap.get("page_num");
		int pageSize = (int) paraMap.get("page_size");
		int num1 = (pageNum - 1) * pageSize;
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("smooth").setQuery(query1).setFrom(num1)
				.setSize(pageSize).execute().actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();
		long totalCount = searchResponse2.getHits().getTotalHits();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", totalCount);
		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();

			{
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("event", data.get("event"));
				d.put("check_time", data.get("check_time"));
				d.put("time_out", data.get("time_out"));
				d.put("respone_time", data.get("respone_time"));
				vulList.add(d);
			}
		}
		if (vulList != null && vulList.size() > 0) {
			map.put("Smooth", vulList);
			result.setData(map);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getHole(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}

		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		int pageNum = (int) paraMap.get("page_num");
		int pageSize = (int) paraMap.get("page_size");
		int num1 = (pageNum - 1) * pageSize;
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query1).setFrom(num1)
				.setSize(pageSize).execute().actionGet();
		List<Map<String, Object>> vulList = new ArrayList<Map<String, Object>>();
		long totalCount = searchResponse2.getHits().getTotalHits();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", totalCount);
		for (SearchHit hit : searchResponse2.getHits()) {
			Map<String, Object> data = hit.getSource();

			{
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("title", data.get("title"));
				d.put("vultype", data.get("vultype"));
				if (Integer.parseInt(data.get("severity_points").toString()) >= 8) {
					d.put("severity_points", "高");
				} else if (5 <= Integer.parseInt(data.get("severity_points")
						.toString())
						&& Integer.parseInt(data.get("severity_points")
								.toString()) <= 7) {
					d.put("severity_points", "中");
				} else {
					d.put("severity_points", "低");
				}
				d.put("date_found", data.get("date_found"));
				d.put("method", data.get("method"));
				d.put("exploit", data.get("exploit"));
				d.put("url", data.get("url"));
				vulList.add(d);
			}
		}
		if (vulList != null && vulList.size() > 0) {
			map.put("Hole", vulList);
			result.setData(map);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getVulLink(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		List<Map<String, Object>> allLast = siteMapper.getAllLast(paraMap);
		if (allLast == null || allLast.size() == 0 || allLast.size() == 1) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		} else {
			// 最近一个扫描周期
			Map<String, Object> map = new HashMap<String, Object>();
			String last_end_time1 = allLast.get(0).get("lastEndTime")
					.toString();
			QueryBuilder query1 = QueryBuilders
					.boolQuery()
					.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
					.must(QueryBuilders.termsQuery("last_end_time",
							last_end_time1));
			TermsBuilder aggregationT = AggregationBuilders
					.terms("data")
					.script(new Script(
							"doc['site_id'].value+ '|' + doc['title'].value "))
					.size(1000);

			SearchResponse searchResponse = esClient.prepareSearch("cloud")
					.setTypes("site_risk").setQuery(query1).setSize(1000)
					.addAggregation(aggregationT).execute().actionGet();

			Terms t = searchResponse.getAggregations().get("data");
			List<Bucket> counts = t.getBuckets();
			List<Map<String, Object>> holeList = new ArrayList<Map<String, Object>>();
			for (Bucket b : counts) {
				String[] keys = b.getKey().toString().split("\\|");
				Map<String, Object> hole = new HashMap<String, Object>();
				hole.put("title", keys[1]);
				hole.put("vul_totle", b.getDocCount());
				holeList.add(hole);
			}
			if (holeList != null) {
				map.put("data1", holeList);
			}

			// 最近一个扫描周期的上一个周期
			String last_end_time2 = allLast.get(1).get("lastEndTime")
					.toString();
			QueryBuilder query2 = QueryBuilders
					.boolQuery()
					.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
					.must(QueryBuilders.termsQuery("last_end_time",
							last_end_time2));
			TermsBuilder aggregationT2 = AggregationBuilders
					.terms("data")
					.script(new Script(
							"doc['site_id'].value+ '|' + doc['title'].value "))
					.size(1000);

			SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
					.setTypes("site_risk").setQuery(query2).setSize(1000)
					.addAggregation(aggregationT2).execute().actionGet();

			Terms t2 = searchResponse2.getAggregations().get("data");
			List<Bucket> counts2 = t2.getBuckets();
			List<Map<String, Object>> holeList2 = new ArrayList<Map<String, Object>>();
			for (Bucket b : counts2) {
				String[] keys = b.getKey().toString().split("\\|");
				Map<String, Object> hole = new HashMap<String, Object>();
				hole.put("title", keys[1]);
				hole.put("vul_totle", b.getDocCount());
				holeList2.add(hole);
			}
			if (holeList2 != null) {
				map.put("data2", holeList2);
			}
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			return result;
		}

	}

	@Override
	public Result getSmoothData(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		List<Map<String, Object>> allLast = siteMapper.getAllLast(paraMap);
		if (allLast == null || allLast.size() == 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 取最近一個周期的平穩度数据的前十条
		String last_end_time = allLast.get(0).get("lastEndTime").toString();
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time", last_end_time));
		TermsBuilder aggregationT = AggregationBuilders
				.terms("data")
				.script(new Script(
						"doc['respone_time'].value + '|' + doc['check_time'].value "))
				.size(10000);

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("smooth").setQuery(query).setSize(10000)
				.addAggregation(aggregationT).execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		if (hits.getTotalHits() <= 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		if (hits.getTotalHits() > 0 && hits.getTotalHits() <= 10) {
			List<Map<String, Object>> smoothEventList = new ArrayList<Map<String, Object>>();
			SearchHit[] searchHists = hits.getHits();
			for (int i = 0; i < hits.getTotalHits(); i++) {
				Map<String, Object> event = new HashMap<String, Object>();
				event.put("respone_time",
						searchHists[i].getSource().get("respone_time"));
				event.put("check_time",
						searchHists[i].getSource().get("check_time"));
				smoothEventList.add(event);
			}
			map.put("smooth", smoothEventList);
			if (smoothEventList != null) {
				map.put("smooth", smoothEventList);
			}
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			return result;
		} else {
			List<Map<String, Object>> smoothEventList = new ArrayList<Map<String, Object>>();
			SearchHit[] searchHists = hits.getHits();
			for (int i = 0; i < 10; i++) {
				Map<String, Object> event = new HashMap<String, Object>();
				event.put("respone_time",
						searchHists[i].getSource().get("respone_time"));
				event.put("check_time",
						searchHists[i].getSource().get("check_time"));
				smoothEventList.add(event);
			}
			map.put("smooth", smoothEventList);
			if (smoothEventList != null) {
				map.put("smooth", smoothEventList);
			}
			result.setData(map);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			return result;
		}
	}

	@Override
	public Result getHolesType(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		Map<String, Object> last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		QueryBuilder query1 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time",
						last.get("lastEndTime")));
		TermsBuilder aggregationT = AggregationBuilders
				.terms("data")
				.script(new Script(
						"doc['site_id'].value+ '|' + doc['title'].value + '|' + doc['severity_points'].value"))
				.size(1000);

		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query1).setSize(1000)
				.addAggregation(aggregationT).execute().actionGet();

		Terms t = searchResponse.getAggregations().get("data");
		List<Bucket> counts = t.getBuckets();
		List<Map<String, Object>> holeList = new ArrayList<Map<String, Object>>();
		for (Bucket b : counts) {
			String[] keys = b.getKey().toString().split("\\|");
			Map<String, Object> hole = new HashMap<String, Object>();
			hole.put("title", keys[1]);
			hole.put("severity_points", keys[2]);
			if (Integer.parseInt(keys[2]) > 7) {
				hole.put("level", "high");
			} else if (Integer.parseInt(keys[2]) < 5) {
				hole.put("level", "low");
			} else {
				hole.put("level", "mid");
			}
			hole.put("num", b.getDocCount());
			holeList.add(hole);
		}
		if (holeList != null) {
			result.setData(holeList);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	public Result getHighRiskTop(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		List<Map<String, Object>> allLast = siteMapper.getAllLast(paraMap);
		if (allLast == null || allLast.size() == 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String last_end_time = allLast.get(0).get("lastEndTime").toString();
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + site.getId()))
				.must(QueryBuilders.termsQuery("last_end_time", last_end_time));
		TermsBuilder aggregationT = AggregationBuilders
				.terms("data")
				.script(new Script(
						"doc['site_id'].value+ '|' + doc['title'].value "))
				.size(1000);
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query).setSize(1000)
				.addAggregation(aggregationT).execute().actionGet();
		Terms t = searchResponse.getAggregations().get("data");
		List<Bucket> counts = t.getBuckets();
		List<Map<String, Object>> holeList = new ArrayList<Map<String, Object>>();
		for (Bucket b : counts) {
			String[] keys = b.getKey().toString().split("\\|");
			Map<String, Object> hole = new HashMap<String, Object>();
			hole.put("title", keys[1]);
			hole.put("vul_totle", b.getDocCount());
			holeList.add(hole);
		}
		if (holeList != null) {
			Collections.sort(holeList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1,
						Map<String, Object> o2) {
					int ret = 0;
					// 比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
					ret = Integer.parseInt(o2.get("vul_totle").toString())
							- Integer.parseInt(o1.get("vul_totle").toString());// 逆序的话就用o2.compareTo(o1)即可
					return ret;
				}
			});
			map.put("data", holeList);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("获取成功");
			result.setData(map);
		} else {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
		}
		return result;
	}

	@Override
	public Result getAllReport(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 0);
		List<Map<String, Object>> list = siteMapper.getAllReport(paraMap);
		if (list == null || list.size() == 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Long totalNum = siteMapper.countAllReport(paraMap);
		map.put("totalNum", totalNum);
		map.put("data", list);
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public Result getReportBySite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 0);
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		if (!ObjectUtil.isEmpty(paraMap, "from_time")) {
			Date fromTime = dateFormater1.parse(paraMap.get("from_time")
					.toString() + " 00:00:01");
			paraMap.put("fromTime", dateFormater2.format(fromTime));
		}
		if (!ObjectUtil.isEmpty(paraMap, "end_time")) {
			Date fromTime = dateFormater1.parse(paraMap.get("end_time")
					.toString() + " 23:59:59");
			paraMap.put("endTime", dateFormater2.format(fromTime));
		}
		List<Map<String, Object>> list = siteMapper.getReportBySite(paraMap);
		if (list == null || list.size() == 0) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据！");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Long totalNum = siteMapper.countReportBySite(paraMap);
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if (totalNum % Long.parseLong(pageSize) > 0) {
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		} else {
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		map.put("data", list);
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		return result;
	}

	@Override
	public List<Map<String, Object>> getData(Map<String, Object> map)
			throws Exception {
		SimpleDateFormat dateFormater1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = dateFormater1.parse(map.get("lastEndTime").toString());
		String last_end_time = dateFormater2.format(d);
		long siteId = Long.parseLong(map.get("siteId").toString());
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", "" + siteId))
				.must(QueryBuilders.termsQuery("last_end_time", last_end_time));
		TermsBuilder aggregationT = AggregationBuilders
				.terms("data")
				.script(new Script("doc['title'].value+ '|'"
						+ " + doc['siteUrl'].value +'|'"
						+ " + doc['site_id'].value +'|'"
						+ " + doc['vulid'].value +'|'"
						+ " + doc['vultype'].value +'|'"
						+ " + doc['description'].value +'|'"
						+ " + doc['solution'].value +'|'"
						+ " + doc['severity_points'].value +'|'"
						+ " + doc['date_found'].value")).size(1000);
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query).setSize(1000)
				.addAggregation(aggregationT).execute().actionGet();
		Terms t = searchResponse.getAggregations().get("data");
		List<Bucket> counts = t.getBuckets();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Bucket b : counts) {
			String[] keys = b.getKey().toString().split("\\|");
			Map<String, Object> hole = new TreeMap<String, Object>();
			hole.put("title", keys[0]);
			hole.put("siteUrl", keys[1]);
			hole.put("site_id", keys[2]);
			hole.put("vulid", keys[3]);
			hole.put("vultype", keys[4]);
			hole.put("description", keys[5]);
			hole.put("solution", keys[6]);
			hole.put("severity_points", keys[7]);
			hole.put("date_found", keys[8]);
			list.add(hole);
		}
		System.out.println(list.toString());
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Workbook exporttReportXlsx(List<Map<String, Object>> list)
			throws Exception {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("用户工具使用统计表");
		// 设置列宽
		sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 3500);
		sheet.setColumnWidth(2, 3500);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 3500);
		sheet.setColumnWidth(5, 3500);
		sheet.setColumnWidth(6, 3500);
		sheet.setColumnWidth(7, 3500);
		sheet.setColumnWidth(8, 3500);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("漏洞名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("网站URL");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("网站ID");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("漏洞ID");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("漏洞类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("漏洞描叙");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("漏洞解决方法");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("漏洞的风险值");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("漏洞被公布的时间");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Map<String, Object> paraMap = (Map<String, Object>) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(
					paraMap.get("title").toString());
			row.createCell((short) 1).setCellValue(
					paraMap.get("siteUrl").toString());
			row.createCell((short) 2).setCellValue(
					paraMap.get("site_id").toString());
			row.createCell((short) 3).setCellValue(
					paraMap.get("vulid").toString());
			row.createCell((short) 4).setCellValue(
					paraMap.get("vultype").toString());
			row.createCell((short) 5).setCellValue(
					paraMap.get("description").toString());
			row.createCell((short) 6).setCellValue(
					paraMap.get("description").toString());
			row.createCell((short) 7).setCellValue(
					paraMap.get("severity_points").toString());
			row.createCell((short) 8).setCellValue(
					paraMap.get("date_found").toString());
		}
		// 第六步，将文件存到指定位置
		/*
		 * FileOutputStream fout = new
		 * FileOutputStream("E:/tools/students.xls"); wb.write(fout);
		 * fout.close();
		 */
		return wb;
	}

	@SuppressWarnings({ "deprecation" })
	public Result exportWordReport(Map<String, Object> paraMap)
			throws Exception {
		Map<String, Object> maptotle = new HashMap<String, Object>();
		Result result = new Result();
		String siteId = paraMap.get("siteId").toString();
		String type1 = paraMap.get("type1").toString();
		String type2 = paraMap.get("type2").toString();
		String reportType = paraMap.get("reportType").toString();
		String startDate = paraMap.get("startTime").toString();
		String endDate = paraMap.get("endTime").toString();
		String preMonthST = paraMap.get("preMonthST").toString();
		String preMonthET = paraMap.get("preMonthET").toString();
		String fromTime = paraMap.get("fromTime").toString();
		String toTime = paraMap.get("toTime").toString();

		String rtName = "";
		if ("M".equals(reportType)) { // 月报
			rtName = "月报(" + startDate.substring(0, 6) + ")";
		} else {
			rtName = "季报(" + startDate.substring(0, 6) + "-"
					+ endDate.substring(0, 6) + ")";
		}

		QueryBuilder qb = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", siteId))
				.must(QueryBuilders.rangeQuery("date").gte(startDate)
						.lte(endDate));
		SearchResponse searchResponse = esClient.prepareSearch("cloud")
				.setTypes("waf_attack_detail").setQuery(qb)
				.setSearchType(SearchType.COUNT).setSize(0).execute()
				.actionGet();
		long totalCount = searchResponse.getHits().totalHits();

		int high = 0;
		int mid = 0;
		int low = 0;
		QueryBuilder query = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", siteId))
				.must(QueryBuilders.rangeQuery("last_end_time").gte(fromTime)
						.lte(toTime));
		TermsBuilder aggregation1 = AggregationBuilders.terms("vuls").field(
				"severity_points");
		SearchResponse searchResponse1 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query)
				.setSearchType(SearchType.COUNT).addAggregation(aggregation1)
				.execute().actionGet();
		Terms num = searchResponse1.getAggregations().get("vuls");
		List<Bucket> vulN = num.getBuckets();
		for (Bucket vn : vulN) {
			String key = vn.getKeyAsString();
			if (Integer.parseInt(key) >= 8) {
				high += vn.getDocCount();
			} else if (Integer.parseInt(key) < 5) {
				low += vn.getDocCount();
			} else {
				mid += vn.getDocCount();
			}
		}

		TermsBuilder aggregation2 = AggregationBuilders.terms("titleTypes")
				.field("title");
		SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query)
				.setSearchType(SearchType.COUNT)
				.addSort("title", SortOrder.DESC).addAggregation(aggregation2)
				.execute().actionGet();
		Terms num2 = searchResponse2.getAggregations().get("titleTypes");
		List<Bucket> vulN2 = num2.getBuckets();
		maptotle.put("vuls",
				ChartUtils.createBarChart("漏洞类型TOP10", "漏洞类型", vulN2));

		QueryBuilder query3 = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.termsQuery("site_id", siteId))
				.must(QueryBuilders.rangeQuery("last_end_time").gte(preMonthST)
						.lte(preMonthET));
		TermsBuilder aggregation3 = AggregationBuilders.terms("titleTypes")
				.field("title");
		SearchResponse searchResponse3 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query3)
				.setSearchType(SearchType.COUNT)
				.addSort("title", SortOrder.DESC).addAggregation(aggregation3)
				.execute().actionGet();
		Terms num3 = searchResponse3.getAggregations().get("titleTypes");
		List<Bucket> vulN3 = num3.getBuckets();
		maptotle.put("vulf", ChartUtils.createBarChart("最近两个月各漏洞类型变化",
				startDate.substring(0, 6), preMonthST.substring(0, 6), vulN2,
				vulN3));

		int score = high * 3 + mid * 2 + low * 1;
		int safetyScore = (int) totalCount + score > 100 ? 0
				: 100 - ((int) totalCount + score);
		maptotle.put("vuld",
				ChartUtils.createDialplot(safetyScore, "安全得分", "分数"));
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("高危", high);
		data.put("中危", mid);
		data.put("低危", low);
		maptotle.put("vulh", ChartUtils.createPieChart("漏洞情况占比", "漏洞", data));

		TermsBuilder aggregation = AggregationBuilders.terms("lastEndTimes")
				.field("last_end_time");
		SearchResponse sr = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query)
				.setSearchType(SearchType.COUNT)
				.addSort("title", SortOrder.DESC).addAggregation(aggregation)
				.execute().actionGet();
		Terms term = sr.getAggregations().get("lastEndTimes");
		List<Bucket> buckets = term.getBuckets();
		maptotle.put("vulg", ChartUtils.genLineChart("漏洞趋势分析", "漏洞", buckets));

		// 高危漏洞列表
		paraMap.put("siteId", siteId);
		paraMap.put("type", 0);
		Site site = siteMapper.getSiteByIdType(paraMap);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		Date date4 = sdf3.parse(paraMap.get("toTime").toString());
		Date date = sdf.parse(paraMap.get("endTime").toString());
		Date date5 = sdf.parse(paraMap.get("startTime").toString());
		String date1 = sdf4.format(date4);
		String date2 = sdf2.format(date5);
		String date3 = sdf2.format(date);
		if (site == null) {
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("网站未开启服务");
			return result;
		}
		maptotle.put("date", date1);
		maptotle.put("site_title",
				site.getSiteTitle() == null ? "" : site.getSiteTitle());
		maptotle.put("date2", date2);
		maptotle.put("date3", date3);
		maptotle.put("site_domain",
				site.getSiteDomain() == null ? "" : site.getSiteDomain());
		maptotle.put("site_head",
				site.getSiteHead() == null ? "" : site.getSiteHead());
		if (site.getSiteIp() == null || site.getSiteIp().toString().equals("")) {
			maptotle.put("site_ip", " ");
		} else {
			maptotle.put("site_ip", site.getSiteIp());
		}

		SearchResponse searchResponse4 = esClient.prepareSearch("cloud")
				.setTypes("site_risk").setQuery(query).setSize(1000).execute()
				.actionGet();
		List<Vuls> vulList = new ArrayList<Vuls>();
		String[][] arrs = {
				{
						"检测到目标URL存在基于DOM的跨站脚本漏洞",
						"param: s=score><script>alert(696)</script>, append: ><script>alert(696)</script>, 0",
						" " },
				{ "检测到目标URL存在SQL注入漏洞", "参数: orderfield=ReferDate", " " },
				{ "检测到目标URL存在链接注入漏洞", "parameter: s=score, link_inj:", " " },
				{ "检测到目标URL存在框架注入漏洞", "parameter: s=score, frame_inj:", " " },
				{ "检测到目标服务器启用了TRACE方法", "data2.get('url')", " " },
				{ "检测到管理后台登录入口", "/admin", " " },
				{ "检测到目标服务器存在应用程序错误", "500 Internal Server Error", " " },
				{ "检测到错误页面web应用服务器版本信息泄露", "500 Internal Server Error", " " },
				{ "检测到目标源码中可能存在用户名或者密码信息泄露", "ORA-0140,ORA", "data2.get('url')" },
				{ "数据库服务敏感信息泄露", "ORA-0172", " " },
				{ "检测到目标服务器存在有可直接查看文件列表的目录", "cgi-bin/printenv",
						"cgi-bin/printenv" },
				{ "检测到目标服务器上存在web应用默认目录", "cgi-bin/printenv",
						"cgi-bin/printenv" },
				{ "检测到目标URL存在内部IP地址泄露", "192.168.10.21", " " },
				{ "从 Web 站点中除去电话号码，使恶意的用户无从利用",
						"13800138888 020-88888888 0531-1234567", " " },
				{ "检测到目标URL存在电子邮件地址模式", "smallcarrot@163.com", " " } };
		for (SearchHit hit : searchResponse4.getHits()) {
			Map<String, Object> data2 = hit.getSource();
			if (Integer.parseInt(data2.get("severity_points").toString()) >= 8) {
				Vuls v = new Vuls();
				v.setTitle(data2.get("title"));
				v.setUrl(data2.get("url"));
				v.setSolution(data2.get("solution"));
				vulList.add(v);
			}
		}
		maptotle.put("v", vulList);
		for (int i = 0; i < arrs.length; i++) {
			List<Vuls> vulList1 = new ArrayList<Vuls>();
			for (SearchHit hit : searchResponse4.getHits()) {
				Map<String, Object> data2 = hit.getSource();
				if (data2.get("title").toString().equals(arrs[i][0])) {
					Vuls vuls = new Vuls();
					vuls.setVulurl("漏洞 URL");
					vuls.setUrl(data2.get("url"));
					vuls.setParameter("参数");
					vuls.setParameterurl(arrs[i][1]);
					vuls.setVulVerification("漏洞验证");
					vuls.setVulVerificationurl(data2.get("url")+arrs[i][2]);
					vulList1.add(vuls);
				}
			}
			maptotle.put("v" + (i+1), vulList1);
		}
		for (int i = 0; i < arrs.length; i++) {
			if (maptotle.get("v" + (i + 1)) == null) {
				maptotle.put("v" + (i + 1), new ArrayList<Vuls>());
			}
		}
		FreemarkerUtil util = new FreemarkerUtil();
		File outFile = util.createDoc(maptotle, "vulReport.ftl", "安全监测"
				+ rtName + ".doc");
		saveMongoFile(outFile, siteId, type1, type2, startDate.substring(0, 6));
		result.setData(outFile);
		return result;
	}

	/**
	 * 将报表文档存到mongoDB中，并记录在mysql中
	 * 
	 * @param wordFile
	 * @param siteId
	 * @throws Exception
	 */
	private void saveMongoFile(File wordFile, String siteId, String type1,
			String type2, String rptTime) throws Exception {
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
	public Result getVulLevel(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		int high = 0;
		int mid = 0;
		int low = 0;
		String siteId = paraMap.get("id").toString();
		paraMap.put("siteId", siteId);
		Map<String, Object> last = new HashMap<String, Object>();
		last = siteMapper.getLast(paraMap);
		if (last == null) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据！");
		} else {
			String last_end_time = last.get("lastEndTime").toString();
			QueryBuilder query = QueryBuilders
					.boolQuery()
					.must(QueryBuilders.termsQuery("site_id", siteId))
					.must(QueryBuilders.termsQuery("last_end_time",
							last_end_time));
			/*
			 * TermsBuilder aggregation =
			 * AggregationBuilders.terms("vuls").script( new
			 * Script("doc['severity_points'].value"));
			 */
			TermsBuilder aggregation = AggregationBuilders.terms("vuls").field(
					"severity_points");
			SearchResponse searchResponse = esClient.prepareSearch("cloud")
					.setTypes("site_risk").setQuery(query)
					.addAggregation(aggregation).execute().actionGet();
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
		String[] arr = { "高危", "中危", "低危" };
		int[] sum = { high, mid, low };
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("name", arr[i]);
			mapData.put("value", sum[i]);
			list1.add(mapData);
		}
		result.setData(list1);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功！");
		return result;
	}

	@Override
	public Result getTwoDayWafData(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		String today = DateUtil.getDate();
		String yesterday = DateUtil.getYesterday();
		Map<String, Object> detail = new HashMap<String, Object>();
		QueryBuilder builder1 = QueryBuilders.boolQuery().must(
				QueryBuilders.termsQuery("date", yesterday));
		TermsBuilder aggregation1 = AggregationBuilders
				.terms("data1")
				.script(new Script(
						"doc['site_id'].value+ '|' + doc['date'].value + '|' + doc['attack_total'].value"))
				.size(0);
		SearchRequestBuilder sbuilder = esClient.prepareSearch("cloud")
				.setTypes("waf_day_data").setQuery(builder1)
				.addAggregation(aggregation1)
				.addSort("create_time", SortOrder.DESC);
		SearchResponse searchResponse1 = sbuilder.execute().actionGet();
		Terms data1 = searchResponse1.getAggregations().get("data1");
		List<Bucket> buckets1 = data1.getBuckets();
		if(buckets1.size() == 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无数据！");
			return result;
		}
		List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
		for (Bucket bucket : buckets1) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] keys = bucket.getKey().toString().split("\\|");
			String siteId = keys[0];
			paraMap.put("siteId", siteId);
			paraMap.put("type", 1);
			Site site = siteMapper.getSiteByIdType(paraMap);
			map.put("domain", site.getSiteDomain());
			map.put("yesterday", keys[2]);
			QueryBuilder builder2 = QueryBuilders.boolQuery()
					.must(QueryBuilders.termsQuery("date", today))
					.must(QueryBuilders.termQuery("site_id", siteId));
			SearchResponse searchResponse2 = esClient.prepareSearch("cloud")
					.setTypes("waf_day_data").setQuery(builder2).execute()
					.actionGet();
			SearchHits hits = searchResponse2.getHits();
			if (hits == null || hits.getTotalHits() < 1) {
				map.put("today", 0);
				continue;
			}
			SearchHit[] searchHists = hits.getHits();
			Map<String, Object> hit = searchHists[0].getSource();
			map.put("today", hit.get("attack_total"));
			dataMap.add(map);
		}
		int pageSize = Integer.parseInt(paraMap.get("pageSize").toString());
		int pageNum = Integer.parseInt(paraMap.get("pageNum").toString());
		List<Map<String, Object>> list = PagingUtil.fenye(dataMap, pageSize,
				pageNum);
		detail.put("total", buckets1.size());
		detail.put("data", list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(detail);
		result.setMsg("获取成功！");
		return result;
	}
	
	@Override
	public Result getSafeMonitorTotal(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		Map<String, Object> detail = new HashMap<String, Object>();
		TermsBuilder aggregation = AggregationBuilders
				.terms("agg")
				.field("site_id")
				.size(0)
				.subAggregation(AggregationBuilders
						.terms("data")
						.field("severity_points")
						.size(0));
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(
				"cloud").setTypes("site_risk");
		SearchResponse sr = searchRequestBuilder.addAggregation(aggregation).get();
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		Terms agg = sr.getAggregations().get("agg");
		List<Bucket> parents = agg.getBuckets();
		for(Bucket parent: parents){
			int high = 0;
			int mid = 0;
			int low = 0;
			int sum = 0;
			Map<String,Object> map = new HashMap<String,Object>();
			String siteId = parent.getKeyAsString();
			Site site = siteMapper.getSiteById(siteId);
			if(site == null){
				continue;
			}
			map.put("site_domain",site.getSiteDomain().toString());
			Terms node  = parent.getAggregations().get("data"); 
			List<Bucket> childs = node.getBuckets();
			for(Bucket child:childs){			
					String key = child.getKeyAsString();					
					if (Integer.parseInt(key) > 7) {
						high += child.getDocCount();		
					} else if (Integer.parseInt(key) < 5) {
						low += child.getDocCount();
					} else {
						mid += child.getDocCount();
					}				
			}
			sum = high + mid + low;
			map.put("high", high);
			map.put("mid", mid);
			map.put("low", low);
			map.put("sum", sum);
			dataMap.add(map);
		}
		int pageSize = Integer.parseInt(paraMap.get("pageSize").toString());
		int pageNum = Integer.parseInt(paraMap.get("pageNum").toString());
		List<Map<String, Object>> list = PagingUtil.fenye(dataMap, pageSize,
				pageNum);
		detail.put("total", dataMap.size());
		detail.put("data", list);
		result.setData(detail);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功！");
		return result;	
	}
}
