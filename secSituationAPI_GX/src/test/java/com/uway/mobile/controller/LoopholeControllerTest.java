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
public class LoopholeControllerTest extends BaseApplicationTest {

	@Autowired
	MockMvc mvc;

	/**
	 * 查询汇总信息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetLoopholeSummary() throws Exception {
		String uri = "/loophole/getLoopholeSummary";
		String requestJson = "{\"beginTime\":\"\",\"endTime\":\"\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").value("查询成功！")).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 占比Top5
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCountloopholebyunits() throws Exception {
		String uri = "/loophole/Countloopholebyunits";
		String requestJson = "{\"beginTime\":\"\",\"endTime\":\"\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		@SuppressWarnings("unused")
		MvcResult mvcResult = mvc.perform(request).andExpect(jsonPath("$.msg").value("查询成功！"))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}

	/**
	 * 风险等级占比
	 * 
	 * @throws Exception
	 */
	@Test
	public void testreskpropor() throws Exception {
		String uri = "/loophole/reskpropor";
		String requestJson = "{\"beginTime\":\"\",\"endTime\":\"\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.msg").value("查询成功！")).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 上传文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void testupload() throws Exception {
		String uri = "/SecurityIncidents/uploadFile";
		String fileName = "D:\\广西移动数据\\1．广西移动2月份互联网一般安全事件汇总.xls";

	}

}
