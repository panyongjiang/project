package com.uway.mobile.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.util.ElasticSearchUtils;
import com.uway.mobile.util.JPushUtil;

/*@RestController
@RequestMapping("jpush")*/
public class JPushController extends BaseApplication {
	@Autowired
	private JPushUtil jPushUtil;
	@Autowired
	private JPushClient jPushClient;
	@Autowired
	private ElasticSearchUtils elasticUtils;
	
	@RequestMapping(value = "/send_msg")
	public void sendMsg() throws Exception{
		String message = "20170313集成测试！";
	    try {
	    	PushPayload payload = jPushUtil.buildPushObject_all_all_alert(message);
	        PushResult result = jPushClient.sendPush(payload);
	        if(result.getResponseCode() == 200){
	        	log.info("推送成功！");
	        	log.info("Got result - " + result);
	        }else{
	        	log.error("send message error,the message is '" + message + "'");
	        }
	    } catch (Exception e) {
	        // Connection error, should retry later
	        log.error("send message Exception,the message is '" + message + "' Connection error, should retry later", e);
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/send_msg_to_user")
	public void sendMsgToUser(@RequestParam(value="user_name", required = true) String userName) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String message = "20170313集成测试！" + sdf.format(new Date());
	    try {
	    	PushPayload payload = jPushUtil.buildPushObject_all_alias_alert(userName, message);
	        PushResult result = jPushClient.sendPush(payload);
	        if(result.getResponseCode() == 200){
	        	log.info("推送成功！");
	        	log.info("Got result - " + result);
	        }else{
	        	log.info("推送出错！");
	        	log.error("send message error,the message is '" + message + "'");
	        }
	    } catch (Exception e) {
	        // Connection error, should retry later
	        log.error("send message Exception,the message is '" + message + "' Connection error, should retry later", e);
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/send_define_msg")
	public void sendDefineMsg(@RequestParam(value="user_name", required = true) String userName) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String message = "http://www.baidu.com";
		String title = "测试标题" + sdf.format(new Date());
	    try {
	    	PushPayload payload = jPushUtil.buildPushMessage_all_alias_alert(userName, title, message);
	        PushResult result = jPushClient.sendPush(payload);
	        if(result.getResponseCode() == 200){
	        	log.info("推送成功！");
	        	log.info("Got result - " + result);
	        }else{
	        	log.info("推送出错！");
	        	log.error("send message error,the message is '" + message + "'");
	        }
	    } catch (Exception e) {
	        // Connection error, should retry later
	        log.error("send message Exception,the message is '" + message + "' Connection error, should retry later", e);
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/es_add")
	public void ESAdd() throws Exception{
		try {
			Map<String, Object> json = new HashMap<String, Object>();// 构建json
			json.put("role", "0");
			json.put("user_name", "ym");
			json.put("password", "89c05b65b0112ffb03758251b48ad5ba");
			json.put("industry", "1");
			json.put("business", "互联网");
			json.put("url", "www.uway.cn");
			json.put("province", "140000");
			json.put("city", "140500");
			json.put("address", "东城区中海地产广场");
			json.put("email", "888888888@qq.com");
			json.put("phone", "18233189798");
			json.put("position", "项目经理");
			json.put("company", "优网科技");
			json.put("person", "总经理");
			json.put("is_deleted", "0");
			json.put("create_time", "20170316");
			
			//String[] arr = new String[1];
			//arr[0] = "music";
			//json.put("interests", arr);
			
			elasticUtils.insertES("cloud","user",json);
			//elasticUtils.createMapping("cloud", "user");
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@RequestMapping(value = "/get_user")
	public void getUser() throws Exception{
		 QueryBuilder queryBuilder = QueryBuilders.typeQuery("user");
	        
	        List<Map<String, Object>> result = elasticUtils.searcher(queryBuilder, "cloud", "");
	        for(int i=0;i<result.size();i++){
	        	
	        }
	}
	@RequestMapping(value = "/upd_user")
	public void updUser()throws Exception{
		elasticUtils.upMethod();
	}
	@RequestMapping(value = "/create_mapping")
	public void createMapping()throws Exception{
		elasticUtils.createMapping("cloud", "waf_attack_top");
	}
}
