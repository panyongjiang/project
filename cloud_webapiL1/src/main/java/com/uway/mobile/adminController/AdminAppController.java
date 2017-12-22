package com.uway.mobile.adminController;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.AppCheckService;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("admin_app")
public class AdminAppController {
	@Autowired
	private AppCheckService appCheckService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private MongoUtil mu;
	
	@Value("${spring.data.mongodb.filedb}")	
	public String FILE_DB;
	
	private static final Logger log = Logger.getLogger(AdminAppController.class);
	
	/**
	 * 获取app列表
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/list_appCheck", method = RequestMethod.POST)
	public Result listAppCheck(HttpServletRequest request, @RequestBody Map<String, Object> paraMap) throws Exception{
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (Integer.parseInt(paraMap.get("page_num").toString()) < 1){
				result.setMsg("页码错误！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			result = appCheckService.listAppCheck(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 添加APP检测报告
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_app_report", method = RequestMethod.POST)
	public Result addAppReport(HttpServletRequest request,
			@RequestParam(value = "sid", required = true) String sid,
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "remark") String remark) throws Exception{
		Result result = new Result();
		InputStream in = null;
		DB db = null;
		GridFS gridFS = null;
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			if(StringUtils.isEmpty(sid)){
				result.setMsg("请登录");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			}
			String sessionId = Constance.REDIS_USER_PRE + sid;
			if (redisUtil.get(sessionId) == null
					|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
				result.setMsg("请登录");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			}
			//将文件存储到MongoDB gridFS上
			if(file !=null){				
		        // 获取文件流
				//MongoUtil mu = new MongoUtil();
				in = file.getInputStream();
				db = mu.getDB(FILE_DB);
				gridFS =new GridFS(db);
				String fileName = file.getOriginalFilename();
				String contentType = file.getContentType();
				String app_check_url = UUID.randomUUID().toString();
				paraMap.put("app_check_url", app_check_url);
				DBObject query  = new BasicDBObject("_id", app_check_url); 
		    	GridFSDBFile gridFSDBFile = gridFS.findOne(query);  
	    	        if(gridFSDBFile == null){  
	    	             GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
	    	             gridFSInputFile.setId(app_check_url);  
	    	             gridFSInputFile.setFilename(fileName); 
	    	             gridFSInputFile.setContentType(contentType);
	    	             gridFSInputFile.save();
	    	             log.debug("add_app_report=========================上传APK检测报告到gridFS成功!");
	    	        }else{
	    	        	 gridFS.remove(query);
	    	        	 GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
	    	             gridFSInputFile.setId(app_check_url);
	    	             gridFSInputFile.setContentType(contentType);
	    	             gridFSInputFile.setFilename(fileName);  		    	            
	    	             gridFSInputFile.save();
	    	             log.debug("add_app_report=========================上传APK检测报告到gridFS成功!");
	    	        }  
		    }			
			paraMap.put("appCheckUser", redisUtil.get(sessionId).toString());			
			paraMap.put("id", id);
			paraMap.put("remark", remark);
			result = appCheckService.addAppReport(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}finally{			
			in.close();
		}
		return result;
	}
	
	/**
	 * 下载APP
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/download_app", method = RequestMethod.GET)
	public void downloadApp(HttpServletResponse response,
			@RequestParam(value = "sid", required = true) String sid,
			@RequestParam(value = "appUrl", required = true) String appUrl) throws Exception{
		DB db = null;
		GridFS gridFS = null;
		OutputStream out = null;
		try{
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("sid", sid);
			boolean flag = false;
			if(StringUtils.isEmpty(sid)){
				flag = true;
			}
			String sessionId = Constance.REDIS_USER_PRE + sid;
			if (redisUtil.get(sessionId) == null
					|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
				flag = true;
			}			
			if(flag){
				//清空response
				response.reset();
				//设置response的header
				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.getWriter().print("<script type='text/javascript'>alert('请登录！');</script>");
				response.getWriter().close();
			}else{
				db = mu.getDB(FILE_DB);
				gridFS =new GridFS(db);
				DBObject query  = new BasicDBObject("_id", appUrl);
				GridFSDBFile gridFSDBFile = gridFS.findOne(query);
				String fileName = gridFSDBFile.get("filename").toString();
				//清空response
				response.reset();
				//设置response的header
				response.setContentType("application/octet-stream");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
				out = response.getOutputStream();
				gridFSDBFile.writeTo(out);
				out.flush();
				out.close();			
			}
		}catch (Exception e) {
			//清空response
			response.reset();
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().print("<script type='text/javascript'>alert('内部错误！');</script>");
			response.getWriter().close();
			e.printStackTrace();
		}
	}
}
