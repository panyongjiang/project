package com.uway.mobile.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Result;
import com.uway.mobile.service.AssetInfoService;
import com.uway.mobile.service.MacService;

/***
 * 测试去重**
 * 
 * @author 我
 *
 */
@RestController
public class ExcelTempleteController {
	@Autowired
	private AssetInfoService assetInfoService;
	@Autowired
	private MacService macService;

	@RequestMapping(value = "/testuploadFile", method = RequestMethod.GET)
	public Result testuploadFile() throws Exception {
		Result result = new Result();
		String fileName = "D:\\广西移动数据\\9．2017年广西公司互联网露暴面资产报备汇总表（2017年2月）.xls";
		FileInputStream in = new FileInputStream(new File(fileName));
		result = assetInfoService.importAssetInfos(fileName, in);

		return result;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public Result uploadFile() throws Exception {
		Result result = new Result();
		String fileName = "D:\\广西移动数据\\2017广西移动MAS系统资产汇总表_20170719.xlsx";
		FileInputStream in = new FileInputStream(new File(fileName));
		result = macService.importMasInfos(fileName, in);

		return result;
	}
}
