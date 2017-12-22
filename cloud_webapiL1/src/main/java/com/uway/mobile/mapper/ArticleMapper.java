package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Article;

@Mapper
public interface ArticleMapper {
	/**
	 * 通过名称获取相应的文章
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getArticleByName(String title)
			throws Exception;

	/**
	 * 据ID获取相应的文章
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> showArticleById(String id) throws Exception;
	
	/**
	 * 获取文章对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Article getArticleById(String id) throws Exception;
	
	/**
	 * 查看访类型的文章总数
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public long countArticleByCategory(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 图像列表展示，按sort排序
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> headerArticleBanner(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 文章列表，按时间排序
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listArticleByCategory(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 列表展示文章，按时间排序
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listPublishedArticleByCategory(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 展示所有的文章类型列表
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listCategory() throws Exception;
	
	/**
	 * 验证该标题是否已存在
	 * @param sqlMap
	 * @return
	 * @throws Exception
	 */
	public List<Article> validateArticle(Map<String, Object> sqlMap) throws Exception;
	
	/**
	 * 获取最大的排序号
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public long getMaxSort(String categoryId) throws Exception;
	
	/**
	 * 新增文章
	 * @param article
	 * @throws Exception
	 */
	public void insertArticle(Article article) throws Exception;
	
	/**
	 * 修改文章
	 * @param article
	 * @throws Exception
	 */
	public void updateArticle(Article article) throws Exception;
	
	/**
	 * 后至该排序之前的所有文章
	 * @param sort
	 * @throws Exception
	 */
	public void moveSortArticle(long sort) throws Exception;
	
	/**
	 * 根据id删除文章
	 * @param id
	 * @return
	 */
	public int delArticleById(String id);
	
	/**
	 * 更改文章状态
	 * @return
	 */
	public int updateArticleStatus(String id);

}
