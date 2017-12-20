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
public class MasControllerTest extends BaseApplicationTest {

	@Autowired
	MockMvc mvc;

	/**
	 * MAS系统资产汇总信息列表
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetAssetInfos() throws Exception {
		String uri = "/MasAssets/List_MasAssets";
		// page_size：每页页数
		// page_num：起始页号
		String requestJson = "{\"page_size\":\"10\",\"page_num\":\"1\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 按地市，时间条件统计月份资产量
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgroupByTime() throws Exception {
		String uri = "/MasAssets/groupByTime";
		// timeRange：时间段
		// size：根据时间段分时
		String requestJson = "{\"timeRange\":\"\",\"size\":\"6\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);

	}

	/**
	 * 统计城市mas资产量
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgroupByParm() throws Exception {
		String uri = "/MasAssets/groupByParm";
		// 按时间字段分组统计
		String requestJson = "{\"groupfields\":\"time\"}";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 上传文件
	 * 
	 * @throws Exception
	 */
	@Test
	public void testupload() throws Exception {
		String uri = "/MasAssets/uploadFile";
		String fileName = "D:\\广西移动数据\\mas资产数据.xls";
		RequestBuilder request = null;
		request = MockMvcRequestBuilders.post(uri).header("sid", "xxxx").contentType(MediaType.APPLICATION_JSON)
				.content(fileName);
		MvcResult mvcResult = mvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.total_num").value(5)).andDo(MockMvcResultHandlers.print()).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(status + "," + content);
	}

	/**
	 * 下载文件
	 * 
	 * @throws Exception
	 */
	@Test
	public void testexportExcel() throws Exception {
		String uri = "/MasAssets/exportExcel";
		// address： “”或者"address\":\"百\"以及其它条件等
		String requestJson = "{\"address\":\"百\"}";
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
