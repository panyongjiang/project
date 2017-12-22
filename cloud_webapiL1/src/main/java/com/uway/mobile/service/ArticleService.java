package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface ArticleService {
	/**
	 * 文章列表
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result listArticle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 列出所有可展示的文章
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result listPublishedArticle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 列表展示文章类型
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listArticleCategory() throws Exception;
	
	/**
	 * 据文章ID获取相应的文章
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> showArticleById(String articleId) throws Exception;
	
	/**
	 * 新增文章
	 * @param paraMap
	 * @return
	 * @throws Exceptin
	 */
	public Result addArticle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 置顶该文章
	 * @param id
	 * @throws Exception
	 */
	public void topArticle(String id) throws Exception;
	
	/**
	 * 修改文章信息
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result updArticle(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 根据文章Id删除文章
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public int delArticleById(String id);
	
	/**
	 * 文章发布
	 * @return
	 */
	public Result articlePub(String id);


}
