package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Site;
import com.uway.mobile.mapper.SiteMapper;
import com.uway.mobile.mapper.SiteSonMapper;
import com.uway.mobile.service.ApiService;
import com.uway.mobile.service.SiteService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.PagingUtil;
import com.uway.mobile.util.SignUtil;

@Service
public class SiteServiceImpl implements SiteService {
	
	// 网站安全监测接口URL前缀
	@Value("${site.safe.prefix.url}")
    public String SAFE_URL;
    // 网站安全监测接口授权码
	@Value("${site.safe.code}")
    public String SAFE_CODE;
		
	@Autowired
	private SiteMapper siteMapper;
	@Autowired
	private SiteSonMapper siteSonMapper;
	@Autowired
	private ApiService apiService;
	
	@Override
	public Result addSite(Map<String, Object> paraMap) throws Exception {
		
		Result result = new Result();
		Site site = new Site();
		site.setSiteDomain(paraMap.get("site_domain").toString());
		site.setSiteIp(null);
		site.setSiteTitle(paraMap.get("site_title").toString());
		site.setSiteHead(paraMap.get("site_head").toString());
		if(!ObjectUtil.isEmpty(paraMap, "remark"))
			site.setRemark(paraMap.get("remark").toString());
		if(!ObjectUtil.isEmpty(paraMap, "createUser"))
			site.setCreateUser(Integer.parseInt(paraMap.get("createUser").toString()));
		if(!ObjectUtil.isEmpty(paraMap, "site_repo"))
		   site.setSiteRepo(paraMap.get("site_repo").toString());
		if(!ObjectUtil.isEmpty(paraMap, "email"))
		site.setEmail(paraMap.get("email").toString());
		if(!ObjectUtil.isEmpty(paraMap, "type"))
			site.setType(Short.parseShort(paraMap.get("type").toString()));
		String week = paraMap.get("week").toString();
		switch (week) {
			case "1": week = "周一  ";
				break;
			case "2": week = "周二  ";
				break;
			case "3": week = "周三  ";
				break;
			case "4": week = "周四  ";
				break;
			case "5": week = "周五  ";
				break;
			case "6": week = "周六  ";
				break;
			case "7": week = "周日  ";
				break;
		}
		String sweep_time = week + paraMap.get("hour").toString();
		site.setSweepTime(sweep_time);
		String url = SAFE_URL + "/newsite?authcode="
				+ SAFE_CODE + "&name=" + site.getSiteHead()
				+ "&target=" + site.getSiteTitle()
				+ "&autopage=100"
				+ "&manageid=0"
				+ "&manager=admin"
				+ "&email=2929501196@qq.com";
		
		String wafCode = apiService.doGet(url);
       if(wafCode==null){
			   result.setCode(Constance.RESPONSE_INNER_ERROR);
	    	   result.setMsg("内部错误");
	    	   return result;
		}
       String[] wafCodes = wafCode.split(":");
       if(wafCodes.length==2&&!wafCodes[0].equals("1")){
    	   result.setCode(Constance.RESPONSE_INNER_ERROR);
    	   result.setMsg(wafCodes[1]);
    	   return result;
       }
       if(wafCodes.length==2&&wafCodes[0].equals("1")){
    	   site.setSiteWafId(wafCodes[1]);
       }
        String curl = SAFE_URL+"/newpolicy?authcode="+ SAFE_CODE +"&siteid="+site.getSiteWafId()+"&webscan_type=2&webscan_args="+paraMap.get("week")+"&webscan_time="+paraMap.get("hour")+"&malware=10080&smooth=10080&pagecrack=10080&darkchain=10080&keyword=10080";
        
        String policyCode = apiService.doGet(curl);
        if(policyCode==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			return result;
        }
		if(policyCode.startsWith("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(policyCode);
			return result;
		}
		
		siteMapper.insertSite(site);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("新增站点成功");
		return result;

	}
	
	@Override
	public Result getAllSaftSite(String userId) throws Exception {
		Result result = new Result();
		result.setData(siteMapper.getAllSafeSite(Long.parseLong(userId)));
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	/**
	 * 删除site
	 */
	@Override
	public Result deleteSite(Map<String, Object> paraMap) throws Exception {
		
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请选择要删除的站点！");
			return result;
		}
		String id = paraMap.get("id").toString();
		Site site = siteMapper.getSiteById(id);
		if(site.getSiteWafId()!=null&&site.getSiteWafId()!=""){
			String url = SAFE_URL+"/delsite?authcode=" + SAFE_CODE +"&siteid="+site.getSiteWafId();
			String wafCode = apiService.doGet(url);
			
			if(wafCode==null){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("删除失败");
				return result;
			}
			
			if(wafCode.startsWith("0")){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg(wafCode);
				return result;
			}
		}
		siteMapper.deleteSite(id);

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功！");
		return result;
	}

	@Override
	public Result startCheck(String id) throws Exception {
		Result result = new Result();
		
		String wafCode = apiService.doGet(SAFE_URL+"/startmonitor?authcode=" + SAFE_CODE +"&siteid="+id+"&type=0");
		if(wafCode==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("开启失败");
			return result;
		}
		if(wafCode.startsWith("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("开启失败");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("开启成功");
		return result;
	}

	@Override
	public Result stopCheck(String id) throws Exception {
		Result result = new Result();
		
		String wafCode = apiService.doGet(SAFE_URL+"/stopmonitor?authcode=" + SAFE_CODE +"&siteid="+id);
		if(wafCode==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("关闭失败");
			return result;
		}
		if(wafCode.startsWith("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafCode);
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("关闭成功");
		return result;
	}

	@Override
	public List<Map<String, Object>> getSiteByUrlType(Map<String, Object>paraMap) throws Exception {
		List<Map<String, Object>> map = siteMapper.getSiteByUrlType(paraMap);
		return map;
	}

	@Override
	public Result addWafSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		String strs[] = new String[] { "domain="+paraMap.get("site_domain"), "cdn_type=cname",
				"time=" + time };
		
		paraMap.put("paras", strs);
		String url = "https://www.yunaq.com/api/v3/site";
		
		JSONObject wafCode = apiService.doWafPost(url, paraMap);
		if(wafCode.get("code")==null || !wafCode.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafCode.getString("message"));
			return result;
		}
		
		Site site = new Site();
		site.setSiteDomain(paraMap.get("site_domain").toString());
		site.setCdn_type("cname");
		
		if(!ObjectUtil.isEmpty(paraMap, "createUser")){
			site.setCreateUser(Integer.parseInt(paraMap.get("createUser").toString()));
		}
		site.setSweepTime(paraMap.get("sweep_time")==null?"":paraMap.get("sweep_time").toString());
		site.setRemark(paraMap.get("remark")==null?"":paraMap.get("remark").toString());
		if(!ObjectUtil.isEmpty(paraMap, "type")){
			site.setType(Short.parseShort(paraMap.get("type").toString()));
		}
		JSONObject data = wafCode.getJSONObject("data");
		if(data==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增失败");
			return result;
		}
		
		
		if(data.getString("id")==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增失败");
			return result;
		}
		site.setSiteWafId(data.getString("id"));
		Map<String, Object> view = new HashMap<String, Object>();
		
	String domain = site.getSiteDomain();
	domain = domain.replace("http://", "");
	domain = domain.replace("https://", "");
	domain = domain.replace("www.", "");
	String strv[] = new String[] {"time=" + time,"domain="+domain};
	
	view.put("paras", strv);
	
	JSONObject viewJson = apiService.doWafGet("https://www.yunaq.com/api/v3/site?"+SignUtil.sort(strv),view);
	if(viewJson.getString("code")==null){
		result.setCode(Constance.RESPONSE_INNER_ERROR);
		result.setMsg("获取信息失败");
		return result;
	}
	if(!viewJson.getString("code").equals("0")){
		result.setCode(Constance.RESPONSE_INNER_ERROR);
		result.setMsg(viewJson.getString("message"));
		return result;
	}
	JSONObject datav = viewJson.getJSONObject("data");
	if(datav==null||datav.get("verify_txt")==null||datav.getString("verify_txt")==""){
		result.setCode(Constance.RESPONSE_INNER_ERROR);
		result.setMsg("获取信息失败");
		return result;
	}
	
	site.setVerify_txt(datav.getString("verify_txt"));
		siteMapper.insertSite(site);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("新增成功");
		result.setData(site.getId());
		return result;
	}

	@Override
	public Result deleteWafSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Site site = siteMapper.getSiteById(paraMap.get("id").toString());
		if(site==null){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("已删除");
			return result;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] {"time=" + time, "sid=" + site.getSiteWafId()};
		paraMap.put("paras", paras);
		String url = "https://www.yunaq.com/api/v3/site";
		JSONObject wafJson = apiService.doWafDelete(url, paraMap);
		
		String code = wafJson.getString("code");
		if(code==null||!code.equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafJson.getString("message"));
			return result;
		}
		siteSonMapper.deleteSonBySite(paraMap.get("id").toString());
		siteMapper.deleteSite(paraMap.get("id").toString());
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功");
		return result;
	}

	@Override
	public Result getAllWafSite(String userId) throws Exception {
		Result result = new Result();
		List<Map<String, Object>> sites = siteMapper.getAllWAFSite(Long.parseLong(userId));
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(sites);
		return result;
	}

	@Override
	public Result activeSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		String siteId = paraMap.get("id").toString();
		Site site = siteMapper.getSiteById(siteId);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		String sid = site.getSiteWafId();
		String paras[] = new String[] {"time=" + time, "sid=" + sid};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/site/ns_active", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null||data.get("task_id")==null||data.getString("task_id")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
			return result;
		}
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("激活成功");
		return result;
	}

	@Override
	public Result addSonSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		String paras[] = new String[] {"time="+SignUtil.getTime(),"sid="+site.getSiteWafId(),"type="+paraMap.get("model"),"host="+paraMap.get("host"),"point="+paraMap.get("point"),"isp="+paraMap.get("isp"),"use_cdn=1"};
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paras", paras);
		String url = "https://"
				+ "www.yunaq.com/api/v3/dns";
		JSONObject wafCode = apiService.doWafPost(url, map);
		
		if(wafCode.get("code")==null || !wafCode.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafCode.getString("message"));
			return result;
		}
		JSONObject data = wafCode.getJSONObject("data");
		if(data==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("添加失败");
			return result;
		}
		paraMap.put("site_waf_id", site.getSiteWafId());
		paraMap.put("site_id", site.getId());
		if(data.getString("id")==null){
			
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("添加失败");
			return result;
		}
		paraMap.put("son_site_id", data.getString("id"));
        String sonStrs[] = new String[] {"time=" + SignUtil.getTime(),"sid="+site.getSiteWafId(),"id="+paraMap.get("son_site_id")};
		
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("paras", sonStrs);
		JSONObject sonJson = apiService.doWafGet("https://www.yunaq.com/api/v3/dns?"+SignUtil.sort(sonStrs), m);
		if(sonJson.get("code")==null || !sonJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(sonJson.getString("message"));
			return result;
		}
		JSONObject datas = sonJson.getJSONObject("data");
		if(datas==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取返回数据失败");
			return result;
		}
		JSONArray subdomains = datas.getJSONArray("subdomains");
		if(subdomains==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("获取返回数据失败");
			return result;
		}
		String cname = subdomains.getJSONObject(0).getString("cname");
		if(cname==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("获取返回数据失败");
			return result;
		}
		paraMap.put("cname", cname);
		
		siteSonMapper.insertSiteSon(paraMap);
		
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setData(cname);
		result.setMsg("新增成功");
		return result;
	}
	
	@Override
	public Result deleteSonSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Map<String, Object> son = siteSonMapper.getSiteSonById(paraMap);
		if(son==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("已删除");
			return result;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] {"time="+time,"sid="+son.get("sid"),"id="+son.get("id")};
		paraMap.put("paras", paras);
		
		JSONObject wafJson = apiService.doWafDelete("https://www.yunaq.com/api/v3/dns", paraMap);
		String code = wafJson.getString("code");
		if(code==null||!code.equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafJson.getString("message"));
			return result;
		}
		siteSonMapper.deleteSon(paraMap.get("siteSonId").toString());
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功");
		return result;
	}

	@Override
	public Result activeCname(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		String siteId = paraMap.get("id").toString();
		Site site = siteMapper.getSiteById(siteId);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		String sid = site.getSiteWafId();
		List<Map<String, Object>> sons = siteSonMapper.getSons(sid);
		if(sons==null||sons.size()==0){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("请添加子域名");
			return result;
		}
		String ids = "";
		for(int i=0;i<sons.size();i++){
			ids +="\""+sons.get(i).get("id")+"\",";
		}
		ids = "["+ids.substring(0,ids.length()-1)+"]";
		
		String paras[] = new String[] {"time=" + time, "sid=" + sid, "ids="+ids};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/dns/cname_active", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null||data.get("task_id")==null||data.getString("task_id")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("激活成功");
		return result;
	}

	@Override
	public Result viewSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		
		String domain = paraMap.get("domain").toString();
		domain = domain.replace("http://", "");
		domain = domain.replace("https://", "");
		domain = domain.replace("www.", "");
		long time = SignUtil.getTime();
		String strs[] = new String[] {"time=" + time,"domain="+domain};
		
		paraMap.put("paras", strs);
		
		JSONObject strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/site?"+SignUtil.sort(strs),paraMap);
		if(strJson.getString("code")==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			return result;
		}
		if(!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null||data.get("sites")==null||data.getString("sites")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		result.setData(strJson);
		return result;
	}

	@Override
	public Result viewSonSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Site site = siteMapper.getSiteById(paraMap.get("id").toString());
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		
		long time = SignUtil.getTime();
		
		String strs[] = new String[] {"sid="+site.getSiteWafId(),"time=" + time};
		
		paraMap.put("paras", strs);
		JSONObject strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/dns?"+SignUtil.sort(strs), paraMap);
		if(strJson.getString("code")==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			return result;
		}
		if(!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		result.setData(strJson);
		return result;
	}

	@Override
	public Result listSonSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未开启服务");
			return result;
		}
		List<Map<String, Object>> list = siteSonMapper.listSon(paraMap);
		Map<String, Object> ispType = new HashMap<String,Object>();
		ispType.put("0", "默认");
		ispType.put("1", "联通");
		ispType.put("2", "电信");
		ispType.put("3", "移动");
		ispType.put("5", "搜索引擎");
		ispType.put("6", "教育网");
		ispType.put("7", "铁通");
		ispType.put("8", "国外");
		ispType.put("9", "长城宽带");		
		for(Map<String, Object> m:list){
			m.put("ispName", ispType.get(m.get("isp")==null?"":m.get("isp").toString()));
		}
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("获取成功");
		result.setData(list);
		return result;
	}
	
	@Override
	public void receiveXml(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Result updSonSite(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		paraMap.put("siteSonId", paraMap.get("id"));
		Map<String, Object> sonSite = siteSonMapper.getSiteSonById(paraMap);
		if(sonSite==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("子域名不存在");
			return result;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] {"time=" + time, "sid=" + sonSite.get("sid"), "id=" + sonSite.get("id"), "point=" + paraMap.get("point")};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/dns", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		siteSonMapper.updSonSite(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功");
		return result;
	}

	@Override
	public Result activeSon(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		Map<String, Object> son = siteSonMapper.getSiteSonById(paraMap);
		if(son==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的子域名");
			return result;
		}
		String ids = "[\""+son.get("id")+"\"]";
		
		String paras[] = new String[] {"time=" + time, "sid=" + son.get("sid"), "ids="+ids};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/dns/cname_active", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null||data.get("task_id")==null||data.getString("task_id")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("激活成功");
		return result;
	}

	@Override
	public Result cnameActive(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("siteId", paraMap.get("id"));
		paraMap.put("type", 1);
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的域名");
			return result;
		}
		
		String paras[] = new String[] {"time=" + time, "sid=" + site.getSiteWafId()};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/site/cname_active", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
			return result;
		}
		if(data.get("task_id")==null||data.getString("task_id")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		site.setTaskId(data.getString("task_id"));
		site.setStatus("1");
		siteMapper.updSite(site);
		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("激活成功");
		return result;
	}

	@Override
	public Result viewTask(Map<String, Object> paraMap) throws Exception {
		
		Result result = new Result();
		paraMap.put("type", 1);
		paraMap.put("siteId", paraMap.get("id"));
		
		Site site = siteMapper.getSiteByIdType(paraMap);
		
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		if(site.getTaskId()==null||site.getTaskId()==""){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未激活");
			return result;
		}
		
		long time = SignUtil.getTime();
		
		String paras[] = new String[] {"time=" + time, "task_id="+site.getTaskId()};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafGet("https://www.yunaq.com/api/v3/site/cname_active?"+SignUtil.sort(paras), paraMap);
		if(strJson.get("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
			return result;
		}
		JSONObject detail = data.getJSONObject("detail");
		if(detail==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("查询失败");
			return result;
		}
		String status = detail.getString("success");
		if(status.equals("true")){
			site.setStatus("2");
		}
		if(status.equals("false")){
			site.setStatus("3");
		}
		siteMapper.updSite(site);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		result.setData(site.getStatus());
		return result;
	}

	@Override
	public Result switchLock(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("time", time);
		if(ObjectUtil.isEmpty(paraMap, "sid")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("主站ID不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("子域名ID不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "value")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("状态不能为空！");
			return result;
		}
		if(ObjectUtil.isEmpty(paraMap, "keyword")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("开关字段不能为空！");
			return result;
		}
		if(!"0".equals(paraMap.get("value").toString()) && !"1".equals(paraMap.get("value").toString())){
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("开关字段不正确！");
			return result;
		}
		
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		if(site.getTaskId()==null||site.getTaskId()==""){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未激活！");
			return result;
		}
		
		String paras[] = new String[] {"time="+time,"sid="+paraMap.get("sid").toString(),"id="+paraMap.get("id"),"value="+paraMap.get("value"),"keyword="+paraMap.get("keyword")};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/switch", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Integer.parseInt(strJson.getString("code")));
			result.setMsg(strJson.getString("message"));
		}
		
		if(!strJson.getString("status").equals("success")){
			result.setCode(Integer.parseInt(strJson.getString("code")));
			result.setMsg(strJson.getString("message"));
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("操作成功！");
		return result;
	}

	
	
	

	@Override
	public Result getAll() throws Exception {
		Result result = new Result();
		Site site = siteMapper.getSiteById("285");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (site != null) {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			BeanUtils.copyProperties(site, paraMap);
			paraMap.put("type", 1);
			long time = SignUtil.getTime();
			String paras[] = new String[]{"time="+time, "sid="+site.getSiteWafId(), "search_date=20170607"};
			paraMap.put("paras", paras);
			JSONObject attackJson = apiService.doWafGet("https://www.yunaq.com/api/v3/report/attack_detail?"+SignUtil.sort(paras), paraMap);
			if(attackJson.get("code")==null || !attackJson.getString("code").equals("0")){
				return null;
			}
			JSONObject data = attackJson.getJSONObject("data");
			if(data==null){
				return null;
			}
			JSONArray detail = data.getJSONArray("detail");
			if(detail==null){
				return null;
			}
			for(int j=0;j<detail.size();j++){
				JSONObject d = detail.getJSONObject(j);
				if(d.get("url").toString().startsWith("http://yxxt")){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("site_id", site.getId());
					map.put("site_waf_id", site.getSiteWafId());
					map.put("date", SignUtil.getDoubleDate());
					map.put("status", d.get("status"));
					map.put("url", d.get("url"));
					map.put("ip", d.get("ip"));
					map.put("times", d.get("times"));
					map.put("location", d.get("location"));
					
					resultList.add(map);
				}else{
					continue;
				}
			}
		}
		result.setCode(200);
		result.setData(resultList);
		return result;
	}
	
	@Override
	public Result portSet(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		long time = SignUtil.getTime();
		paraMap.put("time", time);
		if(ObjectUtil.isEmpty(paraMap, "id")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("子域名ID不能为空！");
			return result;
		}
		Site site = siteMapper.getSiteByIdType(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的网站");
			return result;
		}
		if(site.getTaskId()==null||site.getTaskId()==""){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("网站未激活！");
			return result;
		}
		String paras[] = new String[] {"time="+time,"sid="+site.getSiteWafId(),"id="+paraMap.get("id"),"values="+paraMap.get("values")};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPost("https://www.yunaq.com/api/v3/dns/ports_set", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		if(!strJson.getString("status").equals("success")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("操作成功！");
		return result;
	}

	
	@Override
	public Result getSiteDomainByUser(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = siteMapper.getSiteDomainByUser(paraMap);
		result.setData(list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("操作成功！");
		return result;
	}

	@Override
	public Result getSaftSiteByUser(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "site_url")){
			paraMap.put("siteUrl",paraMap.get("site_url"));
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = siteMapper.saftSiteByUser(paraMap);
		result.setData(list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	@Override
	public Result getSafeSiteByDomain(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "site_domain")){
			paraMap.put("siteDomain",paraMap.get("site_domain").toString());
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();		
		list = siteMapper.getSafeSiteByDomain(paraMap);
		if(list == null || list.size() == 0){
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无监测任务！");
			return result;
		}
		result.setData(list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	@Override
	public Result getWafSiteByDomain(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "site_domain")){
			paraMap.put("siteDomain",paraMap.get("site_domain").toString());
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = siteMapper.getSonSiteByDomain(paraMap);
		if(list == null || list.size() == 0){
			result.setData(null);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("暂无防护任务！");
			return result;
		}
		Map<String, Object> ispType = new HashMap<String,Object>();
		ispType.put("0", "默认");
		ispType.put("1", "联通");
		ispType.put("2", "电信");
		ispType.put("3", "移动");
		ispType.put("5", "搜索引擎");
		ispType.put("6", "教育网");
		ispType.put("7", "铁通");
		ispType.put("8", "国外");
		ispType.put("9", "长城宽带");		
		for(Map<String, Object> m:list){
			m.put("ispName", ispType.get(m.get("isp")==null?"":m.get("isp").toString()));
		}
		result.setData(list);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	@Override
	public Result deleteWafSiteByDomain(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "site_domain")){
			paraMap.put("siteDomain",paraMap.get("site_domain").toString());
		}
		Site site = siteMapper.getWafSiteByDomain(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("已删除");
			return result;
		}
		long time = SignUtil.getTime();
		String paras[] = new String[] {"time=" + time, "sid=" + site.getSiteWafId()};
		paraMap.put("paras", paras);
		String url = "https://www.yunaq.com/api/v3/site";
		JSONObject wafJson = apiService.doWafDelete(url, paraMap);
		
		String code = wafJson.getString("code");
		if(code==null||!code.equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(wafJson.getString("message"));
			return result;
		}
		siteMapper.deleteSite(paraMap.get("id").toString());
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功");
		return result;
	}

	@Override
	public Result getAllSiteList(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		// 验证分页参数
		result = PagingUtil.validatePagination(result, paraMap);
		if (StringUtils.isNotBlank(result.getMsg())) {
			return result;
		}
		// 设置分页具体参数
		paraMap = PagingUtil.installParameters(paraMap);
		// 设置模糊查询条件
		if (!ObjectUtil.isEmpty(paraMap, "site_domain")) {
			paraMap.put("site_domain", "%"
					+ paraMap.get("site_domain").toString() + "%");
		}
		if (!ObjectUtil.isEmpty(paraMap, "user_name")) {
			paraMap.put("userName", paraMap.get("user_name").toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail",siteMapper.getAllSiteList(paraMap));
		map.put("total_num",siteMapper.getAllSiteCount(paraMap));
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("操作成功！");
		return result;
	}

	@Override
	public Result cnameActiveDomain(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		long time = SignUtil.getTime();
		if(!ObjectUtil.isEmpty(paraMap, "site_domain")){
			paraMap.put("siteDomain",paraMap.get("site_domain").toString());
		}		
		Site site = siteMapper.getWafSiteByDomain(paraMap);
		if(site==null){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("无效的域名");
			return result;
		}
		
		String paras[] = new String[] {"time=" + time, "sid=" + site.getSiteWafId()};
		paraMap.put("paras", paras);
		JSONObject strJson = apiService.doWafPatch("https://www.yunaq.com/api/v3/site/cname_active", paraMap);
		if(strJson.getString("code")==null||!strJson.getString("code").equals("0")){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		JSONObject data = strJson.getJSONObject("data");
		if(data==null){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("激活失败");
			return result;
		}
		if(data.get("task_id")==null||data.getString("task_id")==""){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg(strJson.getString("message"));
			return result;
		}
		
		site.setTaskId(data.getString("task_id"));
		site.setStatus("2");
		siteMapper.updSite(site);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("激活成功");
		return result;
	}

	@Override
	public Map<String, Object> getSiteServiceMsg(Map<String, Object> paraMap)
			throws Exception {				
		paraMap.put("siteDomain", paraMap.get("site_domain").toString());	
		List<Map<String, Object>> safeSiteList = new ArrayList<Map<String, Object>>();
		Map<String, Object> siteServiceMsgMap = new HashMap<String, Object>();
		safeSiteList = siteMapper.getSafeSiteByDomain(paraMap);
		if(safeSiteList.size() == 0){
			siteServiceMsgMap.put("safe",null);
		}else{
			List<Map<String, Object>> safeList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : safeSiteList) {
				map.put("userId", paraMap.get("userId").toString());
				Map<String, Object> map1 = siteMapper.getSiteSafeServiceMsg(map);
				map1.put("site_title",map.get("site_title") == null ?"" : map.get("site_title").toString());
				safeList.add(map1);
			}
			siteServiceMsgMap.put("safe",safeList);
		}		
		Site site = siteMapper.getWafSiteByDomain(paraMap);
		if(site == null){
			siteServiceMsgMap.put("waf", null);
		}else{
			List<Map<String, Object>> wafList = new ArrayList<Map<String, Object>>();
			paraMap.put("siteId", site.getId());			
			wafList = siteMapper.getSiteWafServiceMsg(paraMap);
			if(wafList.size() > 0){
				siteServiceMsgMap.put("waf", wafList);
			}
		}
		return siteServiceMsgMap;
	}
	@Override
	public String getsiteIdBysiteDomain(Map<String, Object> paraMap) throws Exception {
		paraMap.put("siteDomain", paraMap.get("site_domain").toString());
		paraMap.put("userId", paraMap.get("create_user").toString());
		Site site = siteMapper.getWafSiteByDomain(paraMap);
		if(site == null){
			return null;
		}
		return String.valueOf(siteMapper.getWafSiteByDomain(paraMap).getId());
	}

	@Override
	public List<Map<String,Object>> getSitesByUserAndType(Map<String, Object> paraMap) throws Exception {
		return siteMapper.getSitesByUserAndType(paraMap);
	}
	
}
