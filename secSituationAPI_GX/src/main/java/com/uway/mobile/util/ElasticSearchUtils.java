package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.alibaba.fastjson.JSONObject;

//@Component
public class ElasticSearchUtils {
	// @Autowired
	private Client esClient;

	/**
	 * 创建mapping(feid("indexAnalyzer","ik")该字段分词IK索引；
	 * feid("searchAnalyzer","ik")该字段分词ik查询；具体分词插件请看IK分词插件说明)
	 * 
	 * @param indices
	 *            索引名称；
	 * @param mappingType
	 *            类型
	 * @throws Exception
	 */
	public void createMapping(String indices, String mappingType) throws Exception {
		esClient.admin().indices().prepareCreate(indices).execute().actionGet();
		new XContentFactory();
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject(mappingType)
				.startObject("properties").startObject("id").field("type", "integer").field("store", "yes").endObject()
				.startObject("kw").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik")
				.field("searchAnalyzer", "ik").endObject().startObject("edate").field("type", "date")
				.field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
				.endObject().endObject().endObject();
		PutMappingRequest mapping = Requests.putMappingRequest(indices).type(mappingType).source(builder);

		esClient.admin().indices().putMapping(mapping).actionGet();
		esClient.close();
	}

	@SuppressWarnings("unused")
	public void insertES(String index, String type, Map<String, Object> json) throws Exception {
		// TODO Auto-generated method stub
		try {
			IndexResponse response = esClient.prepareIndex(index, type)// 插入文档，参数依次为index、type、id
					.setSource(json).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// esClient.close();

	}

	@SuppressWarnings("unused")
	public void insert(String index, String type, String id, Map<String, Object> json) throws Exception {
		// TODO Auto-generated method stub
		try {
			IndexResponse response = esClient.prepareIndex(index, type, id)// 插入文档，参数依次为index、type、id
					.setSource(json).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// esClient.close();

	}

	public List<Map<String, Object>> searcher(QueryBuilder queryBuilder, String indexname, String type) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		SortOrder sort = SortOrder.DESC;

		SearchResponse searchResponse = esClient.prepareSearch(indexname)

				.setQuery(queryBuilder).setFrom(0).setSize(1).addSort("create_time", sort).execute().actionGet();
		SearchHits hits = searchResponse.getHits();

		SearchHit[] searchHists = hits.getHits();
		if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				Map<String, Object> source = hit.getSource();
				list.add(source);
			}
		}
		return list;
	}

	public void upMethod() {
		try {
			// 方法一:创建一个UpdateRequest,然后将其发送给client.
			JSONObject json = new JSONObject();
			UpdateRequest uRequest = new UpdateRequest();
			uRequest.index("cloud");
			uRequest.type("user");
			uRequest.id("2");

			uRequest.doc(json);
			esClient.update(uRequest).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
