package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.AppCheck;
import com.uway.mobile.mapper.AppCheckMapper;
import com.uway.mobile.service.AppCheckService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@Service("appCheckService")
public class AppCheckServiceImpl implements AppCheckService {
	@Value("${app.upload.path}")
	public String APK_PATH;
	//上传app检测报告存储路径
	@Value("${app.report.path}")
    public String APK_REPORT_PATH;
	
	@Autowired
	private AppCheckMapper appCheckMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public Result addAppCheck(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		AppCheck appCheck = new AppCheck();
		Result result = new Result();
		String app_name = paraMap.get("app_name").toString();
		String app_url = paraMap.get("app_url").toString();
		appCheck.setAppName(app_name);
		appCheck.setAppUrl(app_url);
		appCheck.setCreateUser(Integer.parseInt(paraMap.get("createUser")
				.toString()));
		appCheckMapper.insert(appCheck);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("上传成功");
		return result;

	}

	/**
	 * 获取appCheck列表（分页）
	 */
	@Override
	public Result listAppCheck(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		long totalNum = appCheckMapper.countAppCheck(paraMap);

		if (ObjectUtil.isEmpty(paraMap, "page_size")) {
			paraMap.put("pageSize", Constance.PAGE_SIZE);
		}else{
			paraMap.put("pageSize", paraMap.get("page_size").toString());
		}

		String pageSize = paraMap.get("pageSize").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNum = (Long.parseLong(paraMap.get("page_num").toString()) - 1)
				* Long.parseLong(pageSize);
		sqlMap.put("pageNum", pageNum);
		sqlMap.put("pageSize", Integer.parseInt(pageSize));
		sqlMap.put("userId", paraMap.get("userId"));
		map.put("details", appCheckMapper.listAppCheck(sqlMap));
		map.put("totalNum", totalNum);
		if (totalNum % Long.parseLong(pageSize) > 0) {
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		} else {
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;

	}

	/**
	 * 上传app检测报告
	 */
	@Override
	public Result addAppReport(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appCheckUser", paraMap.get("appCheckUser"));
		map.put("id", paraMap.get("id"));
		map.put("appCheckUrl", paraMap.get("app_check_url"));
		map.put("remark", paraMap.get("remark"));	
		appCheckMapper.addAppReport(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("上传成功");
		return result;
	}

	/**
	 * 下载app检测报告
	 */
	@Override
	public String downloadReport(HttpServletResponse response,Map<String, Object> paraMap) throws Exception {
		String appCheckId = paraMap.get("id").toString();
		//String sessionId = paraMap.get("sid").toString();
		
		/*if (StringUtils.isEmpty(sessionId)) {
			return "";
		}
		sessionId = Constance.REDIS_USER_PRE + sessionId;
		if (redisUtil.get(sessionId) == null
				|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
			return "";
		}else{
			String userId = redisUtil.get(sessionId).toString();
			paraMap.put("userId", userId);
		}*/
		AppCheck appCheck = appCheckMapper.getAppById(appCheckId);
		
		if(appCheck==null){
			return "";
		}
		/*if(paraMap.get("userId").toString().equals("" + appCheck.getCreateUser())){
			return appCheck.getAppChkUrl();
		}else{
			return "";
		}*/
		return appCheck.getAppChkUrl();
	}

	@Override
	public String downloadApp(HttpServletResponse response,Map<String, Object> paraMap) throws Exception {
		String appCheckId = paraMap.get("id").toString();
		String sessionId = paraMap.get("sid").toString();
		
		if (StringUtils.isEmpty(sessionId)) {
			return "";
		}
		sessionId = Constance.REDIS_USER_PRE + sessionId;
		if (redisUtil.get(sessionId) == null
				|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
			return "";
		}else{
			String userId = redisUtil.get(sessionId).toString();
			paraMap.put("userId", userId);
		}
		AppCheck appCheck = appCheckMapper.getAppById(appCheckId);
		
		if(appCheck==null){
			return "";
		}
		return appCheck.getAppUrl();
	}
}
