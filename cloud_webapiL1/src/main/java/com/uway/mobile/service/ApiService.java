package com.uway.mobile.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

@Transactional
public interface ApiService {
    public String doPost(String url, Map<String, String> paraMap) throws IOException;
    public String doGet(String url) throws Exception;
    public JSONObject doWafPost(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doWafGet(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doWafDelete(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doWafPatch(String url, Map<String, Object> paraMap) throws Exception;
}
