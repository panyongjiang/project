package com.uway.mobile.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import com.uway.mobile.service.CertificateService;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;


@RestController
@RequestMapping("cer")
public class CertificateController {
	
	@Autowired
	CertificateService cerService;
	@Autowired
	private MongoUtil mu;
	@Value("${spring.data.mongodb.filedb}")	
	public String FILE_DB;
	
	
	/**
	 * 获取站点
	 */
	@RequestMapping(value="/get_site", method = RequestMethod.POST)
	public Result getSite(HttpServletRequest request){
		Result result = new Result();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String createUser = request.getAttribute("userId").toString();
			paraMap.put("createUser", createUser);
			if (createUser == null || "".equals(createUser)) {
				result.setMsg("请登录！");
				return result;
			}
			result = cerService.getSite(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据站点获取子域名
	 */
	@RequestMapping(value="/get_site_son", method = RequestMethod.POST)
	public Result getSiteSon(@RequestBody Map<String, Object> paraMap){
		Result result=new Result();
		try {
			result = cerService.getSiteSon(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 上传证书
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/add_cer", method = RequestMethod.POST)
	public Result addCerCheck(HttpServletRequest request,
			@RequestParam(value = "cer_file", required = true) MultipartFile file,
			@RequestParam(value = "siteId", required = true) String siteId,
			@RequestParam(value = "ssid",required = true)String ssid) throws Exception {		
		Result result = new Result();
		InputStream in=null;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		DB db = null;
		GridFS gridFS = null;
		try {
			in = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data=null;
			String pem="";
			while((data=br.readLine())!=null){
				pem+=data;
			}
			paraMap.put("pem",pem);
			paraMap.put("siteId", siteId);
			paraMap.put("ssid", ssid);
			//存入mongodb
			String createUser = request.getAttribute("userId").toString();
			String fileName = file.getOriginalFilename();
			String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
			prefix=prefix.toLowerCase();
			//校验文件格式请上传CER或者是KEYSTOTR格式的文件
			if("cer".equals(prefix) || "CER".equals(prefix)||"keystore".equals(prefix)||"KEYSTORE".equals(prefix)||"CRT".equals(prefix)||"crt".equals(prefix)){
				paraMap.put("createUser", createUser);
				paraMap.put("cer_name", fileName);
				db = mu.getDB(FILE_DB);
				gridFS =new GridFS(db);
				String contentType = file.getContentType();
				String cer_url = UUID.randomUUID().toString();
				paraMap.put("cer_url", cer_url);
				DBObject query  = new BasicDBObject("_id", cer_url);
				GridFSDBFile gridFSDBFile = gridFS.findOne(query); 
				if(gridFSDBFile == null){  
		             GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
		             gridFSInputFile.setId(cer_url);  
		             gridFSInputFile.setFilename(fileName); 
		             gridFSInputFile.setContentType(contentType);
		             gridFSInputFile.put("createUser", createUser);
		            /* gridFSInputFile.save();*/
		        }else{
		        	 gridFS.remove(query);
		        	 GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
		             gridFSInputFile.setId(cer_url);
		             gridFSInputFile.setContentType(contentType);
		             gridFSInputFile.setFilename(fileName);  		    	            
		            /* gridFSInputFile.save();*/
		        }
				in.close();
				result = cerService.addCer(paraMap);
			}else{
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("上传文件格式不正确，请上传证书格式的文件");
			}
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		} finally{
			if(in!=null){
				try {
					in.close();
				} catch (Exception e2) {
					result.setCode(Constance.RESPONSE_INNER_ERROR);
					result.setMsg("读取文件错误");
					e2.printStackTrace();
				}
			}
		}
		return result;	
	}
	

	/**
	 * 获取证书列表
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping("/get_cer")
	public Result getCer(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result = cerService.getCer(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("读取文件错误");
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 删除证书
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping("/del_cer")
	public Result delCer(@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try {
			result=cerService.delCer(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("读取文件错误");
			e.printStackTrace();
		}
		return result;
	}
	

}
