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
public class MistakePathControllerTest extends BaseApplicationTest {

	@Autowired
	MockMvc mvc;

	/**
	 * 整体信息查询
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetTerminalAll() throws Exception {
		String uri = "/mistakepatch/List_MistakePatch";
		String requestJson = "{\"page_size\":\"1\",\"page_num\":\"10\",\"beginTime\":\"\",\"endTime\":\"\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(0)).andDo(MockMvcResultHandlers.print()).andReturn();
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
