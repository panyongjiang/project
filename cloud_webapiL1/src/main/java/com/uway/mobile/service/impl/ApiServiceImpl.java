package com.uway.mobile.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.util.ArrayToJsonUtil;
import com.uway.mobile.util.HttpDeleteWithBody;
import com.uway.mobile.util.SignUtil;

@Service("apiService")
@Scope("prototype")
public class ApiServiceImpl implements ApiService {
	@Autowired
	private CloseableHttpClient httpClientObj;
	@Autowired
	private RequestConfig httpClientRequestConfig;

	public static final Logger log = Logger.getLogger(ApiService.class);

	@Override
	public String doPost(String url, Map<String, String> paraMap) throws IOException {

		log.debug("Post url " + url + "	paraMap " + paraMap.toString());
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		// 设置请求参数
		httpPost.setConfig(httpClientRequestConfig);
		if (paraMap != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String s : paraMap.keySet()) {
				parameters.add(new BasicNameValuePair(s, paraMap.get(s)));
			}
			// 构建一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8"));
			// 将请求实体放入到httpPost中
			httpPost.setEntity(formEntity);
		}
		// 创建httpClient对象
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClientObj.execute(httpPost);
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			log.debug("Post Request " + resCode);
			return resCode;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public String doGet(String url) throws Exception {

		log.debug("Get url " + url);
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpGet httpget = new HttpGet(url);
			response = httpClientObj.execute(httpget);

			// 获取响应实体
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			log.debug("Get Request " + resCode);

			// if(resCode.startsWith("0:")){ return null; }

			return resCode;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	private String wafCheck(Map<String, Object> paraMap) throws Exception {
		String apiId = Constance.WAF_API_ID;
		String paras[] = (String[]) paraMap.get("paras");
		String sign = SignUtil.getSign(paras);
		String up = apiId + ":" + sign;
		String encode = Base64.byteArrayToBase64(up.getBytes());
		return encode;
	}

	@Override
	public JSONObject doWafPost(String url, Map<String, Object> paraMap) throws Exception {
		log.debug("WafPost url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = wafCheck(paraMap);

		CloseableHttpResponse response = null;
		StringEntity entity = ArrayToJsonUtil.toJson(paras);
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Authorization", "Basic " + encode);
			httpPost.setEntity(entity);
			httpPost.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpPost);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("WafPost Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doWafGet(String url, Map<String, Object> paraMap) throws Exception {
		log.debug("WafGet url " + url + "	paraMap " + paraMap.toString());
		String encode = wafCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Authorization", "Basic " + encode);
			httpGet.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpGet);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("WafGet Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doWafDelete(String url, Map<String, Object> paraMap) throws Exception {

		log.debug("WafDelete url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = wafCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			StringEntity entity = ArrayToJsonUtil.toJson(paras);
			HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);
			httpdelete.setHeader("Authorization", "Basic " + encode);
			httpdelete.setEntity(entity);
			httpdelete.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpdelete);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("WafDelete Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doWafPatch(String url, Map<String, Object> paraMap) throws Exception {

		log.debug("WafPatch url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = wafCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			StringEntity entity = ArrayToJsonUtil.toJson(paras);
			HttpPatch httpPatch = new HttpPatch(url);
			httpPatch.setHeader("Authorization", "Basic " + encode);
			httpPatch.setEntity(entity);
			httpPatch.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpPatch);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("WafGet Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

}
