package com.uway.mobile.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ApiService {
    public Map<String, Object> doPost(String url, Map<String, Object> paraMap) throws IOException;
    public Map<String, Object> doGet(String url) throws Exception;
}
