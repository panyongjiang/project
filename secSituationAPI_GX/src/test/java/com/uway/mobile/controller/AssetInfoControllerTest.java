package com.uway.mobile.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@AutoConfigureMockMvc
public class AssetInfoControllerTest extends BaseApplicationTest {

	@Autowired
	MockMvc mvc;

	/**
	 * 测试暴露面资产信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetAssetInfos() throws Exception {
		String uri = "/assetInfo/list_AssetInfos";
		String requestJson = "{\"page_size\":\"10\",\"page_num\":\"10\",\"timeRange\":\"\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(1107)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	@Test
	public void testgroupByTime() throws Exception {
		String uri = "/assetInfo/groupByTime";
		String requestJson = "{\"topn\":\"10\",\"page_num\":\"10\",\"timeRange\":\"6 MONTH\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson);
		@SuppressWarnings("unused")
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(6)).andDo(MockMvcResultHandlers.print())
				.andReturn();

	}

	@Test
	public void testgroupByParm() throws Exception {
		String uri = "/assetInfo/groupByParm";
		String requestJson = "{\"page_size\":\"10\",\"page_num\":\"10\",\"timeRange\":\"6 MONTH\",\"groupfields\":\"port\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON);
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(5)).andDo(MockMvcResultHandlers.print())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

}
