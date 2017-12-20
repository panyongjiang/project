package com.uway.mobile.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.uway.mobile.BaseApplication;
import com.uway.mobile.service.ControlEventService;
import com.uway.mobile.service.DownloadEventService;
import com.uway.mobile.service.IndecencyScanResultService;
import com.uway.mobile.service.MalwareSamplesService;
import com.uway.mobile.service.SrvResourceService;
import com.uway.mobile.service.VulnerabilityService;

@Component
public class SheduedTask extends BaseApplication {

	@Resource
	private ControlEventService ControlEventService;
	@Resource
	private DownloadEventService downloadEventService;
	@Resource
	private MalwareSamplesService malwareSamplesService;
	@Resource
	private VulnerabilityService vulnerabilityService;
	@Resource
	private IndecencyScanResultService indecencyScanResultService;
	@Resource
	private SrvResourceService srvResourceService;
/*
	 @Scheduled(cron = "0 0/1 * * * ?")
	public void vul_Excel() {
		log.debug("ftp服务器同步parseVulExcel文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			vulnerabilityService.parseVulExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步parseVulExcel文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}

	 @Scheduled(cron = "0 0/1 * * * ?")
	public void controllerFile() {
		log.debug("ftp服务器同步controllerFile文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			ControlEventService.synchronizedMalwareControlFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步controllerFile文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}

	 @Scheduled(cron = "0 0/1 * * * ?")
	public void downloadEventFile() {
		log.debug("ftp服务器同步downloadEventFile文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			downloadEventService.synchronizedMalwareDownloadFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步downloadEventFile文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}

	 @Scheduled(cron = "0 0/1 * * * ?")
	public void malwareSamplesFile() {
		log.debug("ftp服务器同步malwareSamplesFile文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			malwareSamplesService.synchronizedMalwareFile();
			log.info(malwareSamplesService);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步malwareSamplesFile文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}

	@Scheduled(cron = "0 0 0/1 * * ?")
	public void indecencyScanResultFile() {
		log.debug("ftp服务器同步indecencyScanResultFile文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			indecencyScanResultService.download_indecencyScanResultFile();
			log.info(indecencyScanResultService);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步indecencyScanResultFile文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}

	 @Scheduled(cron="0 0 0/1 * * ?")
	public void srvResourceServiceFile() {
		log.debug("ftp服务器同步srvResourceService文件数据到数据库开始时间：" + dateFormat().format(new Date()));
		try {
			srvResourceService.synchronizedSrvResourceFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("ftp服务器同步srvResourceService文件数据到数据库结束时间：" + dateFormat().format(new Date()));
	}
*/
	/**
	 * 时间格式化
	 * 
	 * @return
	 */
	private SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

}
