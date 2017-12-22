package com.uway.mobile.adminController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
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
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ArticleService;
import com.uway.mobile.service.SensitiveService;
import com.uway.mobile.util.MongoUtil;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("admin_article")
public class AdminArticleController extends BaseApplication {
	@Autowired
	private ArticleService articleService;
    @Autowired
	private MongoUtil mu;
	@Autowired
	private SensitiveService sensitiveService;
	

	@Value("${spring.data.mongodb.imagedb}")	
	public String IMAGE_DB;
	
	/**
	 * 列表展示所有的文章类型
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_article_category", method = RequestMethod.POST)
	public Result listArtilceCategory() throws Exception {
		Result result = new Result();
		try {
			log.debug("admin_article/list_article_category interface's parameter is null!");
			result.setData(articleService.listArticleCategory());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 文章列表展示
	 * 
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/list_article", method = RequestMethod.POST)
	public Result listArticle(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			log.debug("list_article interface's parameter paraMap=" + paraMap);
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put(
					"pageNum",
					(Integer.parseInt(pageNum) - 1)
							* Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if (Integer.parseInt(paraMap.get("pageNum").toString()) < 0
					|| Integer.parseInt(paraMap.get("pageSize").toString()) <= 0) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			return articleService.listArticle(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 据ID显示出对应的文章
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_article", method = RequestMethod.POST)
	public Result getArticle(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			log.debug("get_article interface's parameter paraMap=" + paraMap);
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("文章ID不能为空！");
				return result;
			}
			String articleId = paraMap.get("id").toString();
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			result.setData(articleService.showArticleById(articleId));
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 新增文章
	 * 
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/add_article", method = RequestMethod.POST)
	public Result addArticle(
			HttpServletRequest request,
			@RequestParam(value = "pic_file", required = true) MultipartFile file,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "sub_title", required = false) String subTitle,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "category_id", required = true) long categoryId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "remark", required = false) String remark)
			throws Exception {
		Result result = new Result();
		InputStream in = null;
		DB db = null;
		GridFS gridFS = null;
		Object picId = null;
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("title", title);
			paraMap.put("sub_title", subTitle);
			paraMap.put("content", content);
			paraMap.put("category_id", categoryId);
			paraMap.put("description", description);
			paraMap.put("author", author);
			paraMap.put("createUser", request.getAttribute("userId").toString());
			paraMap.put("pic_file", file);
			String fileName = file.getOriginalFilename();
			paraMap.put("fileName", fileName);
			paraMap.put("root_path", request.getRealPath("/"));
			paraMap.put("remark",remark);
			log.debug("add_article interface's parameter paraMap=" + paraMap);

			String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(fileName.toLowerCase());
			if (!matcher.find()) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("上传文件格式错误！");
				return result;
			}
			if (file != null) {
				// 获取文件流
				in = file.getInputStream();
				db = mu.getDB(IMAGE_DB);
				gridFS = new GridFS(db);
				GridFSInputFile gridFSInputFile = gridFS.createFile(in);
				gridFSInputFile.setFilename(fileName);
				gridFSInputFile.save();
				picId = gridFSInputFile.getId();
			}
			paraMap.put("picId", picId);
			return articleService.addArticle(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 置顶文章
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/top_article", method = RequestMethod.POST)
	public Result topArticle(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			log.debug("top_article interface's parameter paraMap=" + paraMap);
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("文章ID不能为空！");
				return result;
			}
			articleService.topArticle(paraMap.get("id").toString());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("置顶成功！");
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 修改文章信息
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/upd_article", method = RequestMethod.POST)
	public Result updArticle(
			HttpServletRequest request,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "pic_id", required = true) String picId,
			@RequestParam(value = "pic_file", required = false) MultipartFile file,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "sub_title", required = false) String subTitle,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "category_id", required = true) long categoryId,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "remark", required = false) String remark)
			throws Exception {
		Result result = new Result();
		InputStream in = null;
		DB db = null;
		GridFS gridFS = null;
		Object pic_id = null;
		try {
			//首先判断该文章是否已经发布
			Map<String, Object> article =  articleService.showArticleById(id);
		    if(article.size() > 0 && (article.get("status").toString().equals("1"))){
				// 合并标题和副标题,内容，描述
				String text = title + subTitle + content + description;
				result = sensitiveService.getResultSensitiveCheck(result, text);
				if (StringUtils.isNotEmpty(result.getMsg())) {
					return result;
				}
			}
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("title", title);
			paraMap.put("sub_title", subTitle);
			paraMap.put("content", content);
			paraMap.put("category_id", categoryId);
			paraMap.put("description", description);
			paraMap.put("author", author);
			paraMap.put("id", id);
			paraMap.put("createUser", request.getAttribute("userId").toString());
			paraMap.put("root_path", request.getRealPath("/"));
			paraMap.put("remark",remark);
			if (file != null) {
				paraMap.put("file", file);
				String fileName = file.getOriginalFilename();
				paraMap.put("fileName", fileName);
				log.debug("add_article interface's parameter paraMap="
						+ paraMap);

				String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
				Pattern pattern = Pattern.compile(reg);
				Matcher matcher = pattern.matcher(fileName.toLowerCase());
				if (!matcher.find()) {
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					result.setMsg("上传文件格式错误！");
					return result;
				}
			}
			if (file != null) {
				// 获取文件流
				in = file.getInputStream();
				db = mu.getDB(IMAGE_DB);
				gridFS = new GridFS(db);
				String fileName = file.getOriginalFilename();
				GridFSInputFile gridFSInputFile = gridFS.createFile(in);
				gridFSInputFile.setFilename(fileName);				
				BasicDBObject query = new BasicDBObject();
				query.put("_id", new ObjectId(picId));
				GridFSDBFile gridFSDBFile = gridFS.findOne(query);
				if (gridFSDBFile == null) {
					gridFSInputFile.save();
					pic_id = gridFSInputFile.getId();
					log.debug("=========================上传到gridFS成功!");
				} else {
					gridFS.remove(query);
					gridFSInputFile.save();
					pic_id = gridFSInputFile.getId();
					log.debug("=========================上传到gridFS成功!");
				}

			}
			paraMap.put("picId", pic_id);
			return articleService.updArticle(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 根据文章Id删除文章
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/del_article", method = RequestMethod.POST)
	public Result delArticleById(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			log.debug("del_article interface's parameter paraMap=" + paraMap);
			if (ObjectUtil.isEmpty(paraMap, "id")) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("文章ID不能为空！");
				return result;
			}
			String id = paraMap.get("id").toString();
			int num = 0;
			num = articleService.delArticleById(id);
			if (num > 0) {
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("删除成功！");
			} else {
				result.setMsg("内部错误！");
				result.setCode(Constance.RESPONSE_INNER_ERROR);
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
		return result;
	}
}
