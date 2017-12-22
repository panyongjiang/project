package com.uway.mobile.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.domain.Manage;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.ManageService;

@SuppressWarnings("deprecation")
@Service("manageService")
public class ManageServiceImpl implements ManageService {

	@Autowired
	private ApiService apiService; 
	@Override
	public String add(String msg) throws Exception {
		JSONObject jsStr = JSONObject.parseObject(msg);
		Manage manage = new Manage();
		manage.setAuthcode(jsStr.getString("authcode"));
		manage.setName(jsStr.getString("name"));
		manage.setParentid(jsStr.getString("parentid"));
		manage.setManager(jsStr.getString("manager"));
		manage.setEmail(jsStr.getString("email"));
		
		String url = String.format("https://192.168.20.200/httprpc/newmanage?authcode=%s&name=%s&manager=%s&parentid=%s&email=%s", 
				manage.getAuthcode(),manage.getName(),manage.getManager(),manage.getParentid(),manage.getEmail()); 
		
		String re = apiService.doGet(url);
		return re;
	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		return null;
	}
	


	//授权验证
	@SuppressWarnings("unused")
	public void NotAuthTestPost() throws IOException {
	Map<String, String> params = new HashMap<String, String>();  
	params.put("client_id", "123456"); //ID
	params.put("client_secret", "abcdef");  //密码
	params.put("grant_type", "client_credentials");//授权类型
	String xml =post_method( params); 
	
	}
	
	public String post_method(Map<String, String> params )
	{
	DefaultHttpClient httpclient = new DefaultHttpClient();  
	String body = null; 
	HttpPost post = postForm(params);  
	body = invoke(httpclient, post);  
	httpclient.getConnectionManager().shutdown();  
	return body;  
	}
	
	private HttpPost postForm(Map<String, String> params){  
		HttpPost httpost = new HttpPost("http://www.baidu.com");  //自己本地的(授权)URL地址
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
		Set<String> keySet = params.keySet();  
		for(String key : keySet) {  
		nvps.add(new BasicNameValuePair(key, params.get(key)));  
		}  
		try {  
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		} catch (UnsupportedEncodingException e) {  
		e.printStackTrace();  
		}  
		return httpost;  
		}
	
	private String invoke(DefaultHttpClient httpclient,HttpUriRequest httpost) {  
		HttpResponse response = sendRequest(httpclient, httpost);  
		String body = paseResponse(response);  
		return body;  
		} 
	
	
	private HttpResponse sendRequest(DefaultHttpClient httpclient,    HttpUriRequest httpost) {  
		HttpResponse response = null;  
		try {  
		response = httpclient.execute(httpost);  
		} catch (ClientProtocolException e) {  
		e.printStackTrace();  
		} catch (IOException e) {  
		e.printStackTrace();  
		}  
		return response;  
		}  
	
	private String paseResponse(HttpResponse response) {  
		HttpEntity entity = response.getEntity();  
		//String charset = EntityUtils.getContentCharSet(entity);  
		String body = null;  
		try {
		body = EntityUtils.toString(entity);
		} catch (ParseException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
		} catch (IOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
		}  
		return body;  
		}  
	
	//获取返回值验证
	public String get(String url) {  
	DefaultHttpClient httpclient = new DefaultHttpClient();  
	String body = null;  
	HttpGet get = new HttpGet(url);  
	//Authorization:http验证的头，第二个参数：token类型+空格+连接令牌(我们用这个"access_token")
	get.setHeader("Authorization", "Bearer " + "Rp_H4RaozPWO2JMZv4Y1r7McVlWPSZ30thmCvD_eLPHOoIsRfxjgDy_OCwy5sgN7R7fc1XXde3qviFC8gBC2NCPDvgNsXTY9BRdVOs0nJIUbcpCMEVwSc2p7I0uAbzmBph0z2oGYKB2EVwqHttXlZ7l-ah_Z4zu7QxuJFCa-UtyMSZNLiLlSsatD6C4oxBxpcfUKpDKJZcvHZOeoEAADK0nhhqj-Qf71qArYr-Y-Z_7-bsz4");
	body = invoke(httpclient, get);
	httpclient.getConnectionManager().shutdown();  
	return body;  
	}  


}
