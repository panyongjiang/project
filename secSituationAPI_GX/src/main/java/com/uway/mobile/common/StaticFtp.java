package com.uway.mobile.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticFtp {
	public static String host;
	public static Integer port;
	public static String username;
	public static String password;
	// 漏洞
	public static String dir_vulnerability = "/vulnerability/";
	public static String download_vulnerability = "福建漏洞";
	public static String vulnerability_his = "/vulnerability_his/";
	// 恶意
	public static String sourceFilePath = "/malware/";
	public static String destFilePath = "/malware_his/";
	// 不良信息扫描结果
	public static String indecencyScan = "/indecencyScan/";
	public static String indecencySacn_his = "/indecencyScan_his/";
	public static String file_indecencyScanResult = "不良信息扫描结果";
	// 福建资产报表
	public static String srvResource = "/srvResource/";
	public static String srvResource_his = "/srvResource_his/";
	public static String srvResource_file = "福建资产报表";

	@SuppressWarnings("static-access")
	@Value("${StaticFtp.host}")
	public void setHost(String host) {
		this.host = host;
	}

	@SuppressWarnings("static-access")
	@Value("${StaticFtp.port}")
	public void setPort(Integer port) {
		this.port = port;
	}

	@SuppressWarnings("static-access")
	@Value("${StaticFtp.username}")
	public void setUsername(String username) {
		this.username = username;
	}

	@SuppressWarnings("static-access")
	@Value("${StaticFtp.password}")
	public void setPassword(String password) {
		this.password = password;
	}

	@Value("${StaticFtp.dir_vulnerability}")
	public static String getDirVulnerability() {
		return dir_vulnerability;
	}

	@Value("${StaticFtp.download_vulnerability}")
	public static String getDownloadVulnerability() {
		return download_vulnerability;
	}

	@Value("${StaticFtp.vulnerability_his}")
	public static String getVulnerabilityHis() {
		return vulnerability_his;
	}

	@Value("${StaticFtp.sourceFilePath}")
	public static String getSourcefilepath() {
		return sourceFilePath;
	}

	@Value("${StaticFtp.destFilePath}")
	public static String getDestfilepath() {
		return destFilePath;
	}

	@Value("${StaticFtp.indecencyScan}")
	public static String getIndecencyscan() {
		return indecencyScan;
	}

	@Value("${StaticFtp.indecencySacn_his}")
	public static String getIndecencysacnHis() {
		return indecencySacn_his;
	}

	@Value("${StaticFtp.file_indecencyScanResult}")
	public static String getFileIndecencyscanresult() {
		return file_indecencyScanResult;
	}

	@Value("${StaticFtp.srvResource}")
	public static String getSrvresource() {
		return srvResource;
	}

	@Value("${StaticFtp.srvResource_his}")
	public static String getSrvresourceHis() {
		return srvResource_his;
	}

	@Value("${StaticFtp.srvResource_file}")
	public static String getSrvresourceFile() {
		return srvResource_file;
	}

}
