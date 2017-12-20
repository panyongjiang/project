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

import com.uway.mobile.common.Result;

@AutoConfigureMockMvc
public class SecurityIncidentsControllerTest extends BaseApplicationTest {

	@Autowired
	MockMvc mvc;

	/**
	 * 一般事件安全信息列表
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetSecurityIncidentss() throws Exception {
		String uri = "/SecurityIncidents/list_SecurityIncidents";
		// page_size：每页页数
		// page_num：起始页数
		String requestJson = "{\"page_size\":\"10\",\"page_num\":\"1\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(1107)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 按需求时间查询
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgroupByTime() throws Exception {
		String uri = "/SecurityIncidents/groupByTime";
		// 按照时间段查询6个月
		String requestJson = "{\"timeRange\":\"6 MONTH\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(6)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 按需求分组统计
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgroupByParm() throws Exception {
		String uri = "/SecurityIncidents/groupByParm";
		// timeRange按照时间段查询
		// groupfields根据event事件分组查询统计
		String requestJson = "{\"timeRange\":\"\",\"groupfields\":\"event\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(5)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 上传文件
	 * 
	 * @throws Exception
	 */
	@Autowired
	private SecurityIncidentsController testContrOller;

	@Test
	public void testupload() throws Exception {
		Result result = testContrOller.testuploadFile();
		System.out.println(result);
	}

	/**
	 * 安全事件汇总数据接口
	 * 
	 * @throws Exception
	 */
	@Test
	public void testqueryCount() throws Exception {
		String uri = "/SecurityIncidents/queryCount";
		// isRegisted ：true 表示用户已处理
		// isRegisted ：false 表示用户未处理
		// unitAddess ：“” 表示获取所有城市
		// period_month ：“” 表示根据选择某个年月时间去查询
		String requestJson = "{\"unitAddess\":\"\",\"isRegisted\":\"true\",\"period_month\":\"201702\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(5)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

}
