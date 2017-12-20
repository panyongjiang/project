package com.uway.mobile.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.service.ApiService;

@RestController
@RequestMapping("test")
public class CheckHttpController {
	
	@Autowired
	public ApiService apiService;
	
	@RequestMapping("/test")
	public void checkHttp() throws IOException{
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("ads", 123);
		Map<String,Object> par=apiService.doPost("192.168.28.184:8000/test/test", paraMap);
		System.out.println("*********"+par.toString());
	}

}
