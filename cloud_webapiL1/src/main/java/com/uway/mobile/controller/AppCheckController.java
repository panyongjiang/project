package com.uway.mobile.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("appCheck")
public class AppCheckController {

	@Autowired
	AppCheckService appCheckService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private MongoUtil mu;
	
	@Value("${spring.data.mongodb.filedb}")	
	public String FILE_DB;
		
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
			paraMap.put("userId", request.getAttribute("userId").toString());
			result = appCheckService.listAppCheck(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("获取失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 上传app
	 * @param request
	 * @param paraMap
	 * @return
	 *  GridFS：核心类，提供了对文件的创建、查找和删除操作。  方法：createFile
  
     GridFSDBFile：从数据库中读取的文件，提供了文件的输出、删除操作。  方法：findOne取到文件名---file.write（）
  
     GridFSFile：要保存的文件，提供了文件的保存、校验、获取文件基本信息等操作。  方法：findOne取到文件名---file.remove（）
  
     DBCollection的两个实例：  
  
     filesCollection：文件集合，文件的基本信息。  
  
     _chunkCollection：块集合，文件的数据  
	 */
	@RequestMapping(value = "/add_appCheck", method = RequestMethod.POST)
	public Result addAppCheck(HttpServletRequest request,
			@RequestParam(value = "apk_file", required = true) MultipartFile file) throws Exception{		
		Result result = new Result();
		InputStream in = null;
		DB db = null;
		GridFS gridFS = null;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			String createUser = request.getAttribute("userId").toString();//根据用户id获取用户名称
			String fileName = file.getOriginalFilename();//文件流
			String prefix=fileName.substring(fileName.lastIndexOf(".")+1);//获取文件后缀
			prefix=prefix.toLowerCase();//前缀转换成小写
			//校验文件格式请上传APK格式的文件
			if("apk".equals(prefix) || "APK".equals(prefix)){
			//用户放入map中
			paraMap.put("createUser", createUser);
			paraMap.put("app_name", fileName);
			//将文件存储到MongoDB gridFS上
			in = file.getInputStream();
			db = mu.getDB(FILE_DB);//取数据库名称
			gridFS =new GridFS(db);//
			String contentType = file.getContentType();//文件头类型
			String app_url = UUID.randomUUID().toString();
			paraMap.put("app_url", app_url);
			DBObject query  = new BasicDBObject("_id", app_url);//根据对象id的值，查询是否存在
			GridFSDBFile gridFSDBFile = gridFS.findOne(query);  //获取文件
			//如果文件为空
	        if(gridFSDBFile == null){  
	             GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
	             gridFSInputFile.setId(app_url);  
	             gridFSInputFile.setFilename(fileName); 
	             gridFSInputFile.setContentType(contentType);
	             gridFSInputFile.put("createUser", createUser);
	             gridFSInputFile.save();//保存文件
	        }else{
	        	 gridFS.remove(query);//移除_id
	        	 GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
	             gridFSInputFile.setId(app_url);
	             gridFSInputFile.setContentType(contentType);
	             gridFSInputFile.setFilename(fileName);  		    	            
	             gridFSInputFile.save();//保存文件
	        }  
			result = appCheckService.addAppCheck(paraMap);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("上传成功");}
			else{
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("上传文件格式不正确，请上传APK格式的文件");
			}
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * APK检测报告下载
	 * @param response
	 * @param paraMap
	 * @throws Exception
	 */
	@RequestMapping(value = "/download_report")
	public void downloadReport(HttpServletResponse response,
			@RequestParam(value = "sid", required = true) String sid,
			@RequestParam(value = "appChkUrl", required = true) String appChkUrl) throws Exception{
		DB db = null;
		GridFS gridFS = null;
		OutputStream out = null;
		try{
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("sid", sid);
			
			db = mu.getDB(FILE_DB);
			gridFS =new GridFS(db);
			DBObject query  = new BasicDBObject("_id", appChkUrl);
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
		}catch (Exception e) {
			//清空response
			response.reset();
			//设置response的header
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().print("<script type='text/javascript'>alert('内部错误！');</script>");
			response.getWriter().close();
			e.printStackTrace();
		}
	}
}
