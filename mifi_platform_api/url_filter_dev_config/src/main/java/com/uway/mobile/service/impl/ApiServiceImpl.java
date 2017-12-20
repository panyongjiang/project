package com.uway.mobile.service.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.uway.mobile.service.ApiService;

@Service("apiService")
@Scope("prototype")
public class ApiServiceImpl implements ApiService {
	@Autowired
	private HttpClient httpClientObj;
	@Autowired
	private RequestConfig httpClientRequestConfig;

	public static final Logger log = Logger.getLogger(ApiService.class);

	@SuppressWarnings({ "unchecked"})
	@Override
	public Map<String, Object> doPost(String url,Map<String, Object> paraMap) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		 // 创建httpClient对象
	    HttpResponse response = null;
		try {
		   // 设置请求参数
		   httpPost.setConfig(httpClientRequestConfig);
		   if (paraMap != null) {
			   List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			   for (String s : paraMap.keySet()) {
				   parameters.add(new BasicNameValuePair(s, paraMap.get(s).toString()));
			   }
			   // 构建一个form表单式的实体
			   UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8"));
			   // 将请求实体放入到httpPost中
			   httpPost.setEntity(formEntity);
		    }
		
			// 执行请求
			response = httpClientObj.execute(httpPost);
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			Map<String,Object> map = (Map<String,Object>)JSON.parse(resCode);
			return map;
		} catch(SocketTimeoutException cte){
			cte.printStackTrace();
			log.error("连接超时"+cte);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("HttpException", "连接超时");
			return map;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			if (response!=null) {
				response.getEntity().getContent().close();
			}
			httpPost.releaseConnection();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> doGet(String url) throws Exception {

		log.debug("Get url " + url);
		HttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(httpClientRequestConfig);
			
//			httpClientObj=HttpClientUtil.getInstance();
			response = httpClientObj.execute(httpGet);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			String resCode =EntityUtils.toString(entity, "utf-8");
			Map<String,Object> map = (Map<String,Object>)JSON.parse(resCode);
			log.debug("Get Request " + resCode);
			return map;

			// if(resCode.startsWith("0:")){ return null; }

		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
//				if (response != null)
//					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

}
