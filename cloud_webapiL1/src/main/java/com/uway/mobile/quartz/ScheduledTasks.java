package com.uway.mobile.quartz;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.SafeCategory;
import com.uway.mobile.domain.WordReport;
import com.uway.mobile.mapper.SafeCategoryMapper;
import com.uway.mobile.mapper.SiteMapper;
import com.uway.mobile.mapper.WordReportMapper;
import com.uway.mobile.service.EsService;
import com.uway.mobile.service.MonitorCenterService;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.SignUtil;

@Component
@Configurable
@EnableScheduling
//@Transactional
public class ScheduledTasks extends BaseApplication {

	@Autowired
	private SiteMapper siteMapper;
	@Autowired
	private EsService esService;
	@Autowired
	private SafeCategoryMapper safeCategoryMapper;
	@Autowired
	private WordReportMapper wordReportMapper;
	@Autowired
	private MonitorCenterService monitorCenterService;

	List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> safeIds = new ArrayList<Map<String, Object>>();

	private SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Scheduled(cron = "0 0/5 *  * * * ")
	public void getMessage() throws Exception {
		ids = siteMapper.getAllWAFId();
		log.debug("waf:	waf get all waf site ids	" + ids);
		if (ids.size() > 0) {
			for (Map<String, Object> paraMap : ids) {
				log.debug("waf:	get waf day date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.addToday(paraMap);
				log.debug("waf:	get web trend date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.webTrend(paraMap);
				log.debug("waf:	get web cc date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.webcc(paraMap);
				log.debug("waf:	get attack top date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.attackTop(paraMap);
				log.debug("waf:	get attack trend date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.attackTrend(paraMap);
				log.debug("waf:	get attack detail date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.attackDetail(paraMap);
				log.debug("waf:	get cc detail date "
						+ dateFormat().format(new Date()) + "	pameters	" + ids);
				esService.ccDetail(paraMap);

			}
		}
	}

	// 网站安全监测报表获取
	@Scheduled(cron = "0 0/5 *  * * * ")
	public void getReportXml() throws Exception {
		safeIds = siteMapper.getAllSAFEId();
		log.debug("safe site:	get all safe site ids	" + safeIds);

		if (safeIds.size() > 0) {
			for (Map<String, Object> map : safeIds) {
				esService.getReportXml(map);
			}
		}
	}
	
	/**
	 * 每天凌晨1点遍历所有站点服务，清理过期站点服务，设为未开通状态
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 1 * * * ")
	public void checkServiceEffective() throws Exception{
		List<SafeCategory> safeCategoryList = new ArrayList<SafeCategory>();
		safeCategoryList = safeCategoryMapper.getAllSafeService();
		for (SafeCategory safeCategory : safeCategoryList) {
			if(safeCategory.getCloudWaf() == (short)1){
				long currentTime = System.currentTimeMillis();
				//云WAF
				Date wafStartTime = safeCategory.getWafStartTime();
				Date wafEndTime = safeCategory.getWafEndTime();			
				if(!(wafStartTime.getTime() < currentTime && currentTime < wafEndTime.getTime())){
					Map<String, Object> paraMap = new HashMap<String, Object>();					
					paraMap.put("type", "waf");
					paraMap.put("id", safeCategory.getId());
					safeCategoryMapper.offService1(paraMap);
				}
			}
			if(safeCategory.getAppSafe() == (short)1){
				long currentTime = System.currentTimeMillis();
				//app
				Date appStartTime = safeCategory.getAppStartTime();
				Date appEndTime = safeCategory.getAppEndTime();
				if(!(appStartTime.getTime() < currentTime && currentTime < appEndTime.getTime())){
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("type", "app");
					paraMap.put("id", safeCategory.getId());
					safeCategoryMapper.offService1(paraMap);					
				}
			}
			if(safeCategory.getSiteSafe() == (short)1){
				long currentTime = System.currentTimeMillis();
				//网站安全监测
				Date siteStartTime = safeCategory.getSiteStartTime();
				Date stieEndTime = safeCategory.getSiteEndTime();
				if(!(siteStartTime.getTime() < currentTime && currentTime < stieEndTime.getTime())){
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("type", "site");
					paraMap.put("id", safeCategory.getId());
					safeCategoryMapper.offService1(paraMap);
				}
			}
			if(safeCategory.getExpertSafe() == (short)1){
				long currentTime = System.currentTimeMillis();
				//专家安全检测
				Date expertStartTime = safeCategory.getExpertStartTime();
				Date expertEndTime = safeCategory.getExpertEndTime();
				if(!(expertStartTime.getTime() < currentTime && currentTime < expertEndTime.getTime())){
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("type", "expert");
					paraMap.put("id", safeCategory.getId());
					safeCategoryMapper.offService1(paraMap);
				}
			}
		}
	}	
	
	
	@Scheduled(cron = "0 0 0 1 * * ")
	public void exportMonthWordReport() throws Exception{
		log.info("==========exportMonthWordReport==========");
		String startTime = SignUtil.lastMonthStartTime(-1);
		String endTime = SignUtil.lastMonthEndTime(0);
		String preMonthST = SignUtil.lastMonthStartTime(-2);
		String preMonthET = SignUtil.lastMonthEndTime(-1);
		Map<String, Object> paraMap = new HashMap<String,Object>();
		paraMap.put("reportType", "M");
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		paraMap.put("preMonthST", preMonthST+"000000");
		paraMap.put("preMonthET", preMonthET+"235959");
		paraMap.put("fromTime", startTime+"000000");
		paraMap.put("toTime", endTime+"235959");
		
		//安全防护月报
		List<Map<String,Object>> wafIds = siteMapper.getAllWAFId();
		for(Map<String,Object> site:wafIds){
			paraMap.put("siteId", site.get("siteId"));
			paraMap.put("type1", "1");
			paraMap.put("type2", "1");
			Result rs = monitorCenterService.exportWafWordReport(paraMap);
		}
		
		//安全监测月报
		List<Map<String,Object>> safeIds = siteMapper.getAllSAFEId();
		for(Map<String,Object> site:safeIds){
			paraMap.put("siteId", site.get("siteId"));
			paraMap.put("type1", "1");
			paraMap.put("type2", "2");
			Result rs = esService.exportWordReport(paraMap);
		}
	}
	
	@Scheduled(cron = "0 0 0 1 1,4,7,10 * ")
	public void exportSeasonWordReport() throws Exception{
		log.info("==========exportSeasonWordReport==========");
		String startTime = SignUtil.lastMonthStartTime(-3);
		String endTime = SignUtil.lastMonthEndTime(0);
		String preMonthST = SignUtil.lastMonthStartTime(-6);
		String preMonthET = SignUtil.lastMonthEndTime(-3);
		Map<String, Object> paraMap = new HashMap<String,Object>();
		paraMap.put("reportType", "M");
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		paraMap.put("preMonthST", preMonthST+"000000");
		paraMap.put("preMonthET", preMonthET+"235959");
		paraMap.put("fromTime", startTime+"000000");
		paraMap.put("toTime", endTime+"235959");
		
		//安全防护季报
		List<Map<String,Object>> wafIds = siteMapper.getAllWAFId();
		for(Map<String,Object> site:wafIds){
			paraMap.put("siteId", site.get("siteId"));
			paraMap.put("type1", "2");
			paraMap.put("type2", "1");
			Result rs = monitorCenterService.exportWafWordReport(paraMap);
		}
		
		//安全监测季报
		List<Map<String,Object>> safeIds = siteMapper.getAllSAFEId();
		for(Map<String,Object> site:safeIds){
			paraMap.put("siteId", site.get("siteId"));
			paraMap.put("type1", "2");
			paraMap.put("type2", "2");
			Result rs = esService.exportWordReport(paraMap);
		}
		
	}
		
}
