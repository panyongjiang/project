package com.uway.mobile.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ArticleService;
import com.uway.mobile.util.ObjectUtil;

@RestController
@RequestMapping("article")
public class ArticleController {
	@Autowired
	private ArticleService articleService;

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
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			if (ObjectUtil.isEmpty(paraMap, "category_id")) {
				result.setMsg("文章类别不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put("pageNum", (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if(Integer.parseInt(paraMap.get("pageNum").toString()) < 0 || Integer.parseInt(paraMap.get("pageSize").toString()) <= 0){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			paraMap.put("categoryId", paraMap.get("category_id").toString());
			return articleService.listPublishedArticle(paraMap);
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
	@RequestMapping(value = "/get_article", method = RequestMethod.GET)
	public Result getArticle(@RequestParam Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
				if(ObjectUtil.isEmpty(paraMap, "id")){
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
}
