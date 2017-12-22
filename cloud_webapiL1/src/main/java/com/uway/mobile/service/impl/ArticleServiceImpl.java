package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Article;
import com.uway.mobile.mapper.ArticleMapper;
import com.uway.mobile.service.ArticleService;
import com.uway.mobile.util.ObjectUtil;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleMapper articleMapper;

	@Override
	public Result listArticle(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "category_id")){
			paraMap.put("categoryId", paraMap.get("category_id").toString());
		}
		Long totalNum = articleMapper.countArticleByCategory(paraMap);	
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
		articleList = articleMapper.listArticleByCategory(paraMap);
		for (Map<String, Object> map2 : articleList) {
			if(map2.get("picId") == null || map2.get("picId").toString().equals("")){
				map2.put("picId", new ObjectId().toString());
			}
		}
		map.put("details", articleMapper.listArticleByCategory(paraMap));
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}
	
	@Override
	public Result listPublishedArticle(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		if(!ObjectUtil.isEmpty(paraMap, "category_id")){
			paraMap.put("categoryId", paraMap.get("category_id").toString());
		}
		Long totalNum = articleMapper.countArticleByCategory(paraMap);	
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
		articleList = articleMapper.listArticleByCategory(paraMap);
		for (Map<String, Object> map2 : articleList) {
			if(map2.get("picId") == null || map2.get("picId").toString().equals("")){
				map2.put("picId", new ObjectId().toString());
			}
		}
		map.put("details", articleMapper.listPublishedArticleByCategory(paraMap));
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if(totalNum % Long.parseLong(pageSize) > 0){
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		}else{
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	@Override
	public List<Map<String, Object>> listArticleCategory() throws Exception {
		// TODO Auto-generated method stub
		return articleMapper.listCategory();
	}

	@Override
	public Map<String, Object> showArticleById(String articleId)
			throws Exception {
		// TODO Auto-generated method stub
		return articleMapper.showArticleById(articleId);
	}

	@Override
	public Result addArticle(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		List<Article> listArticle = articleMapper.validateArticle(paraMap);
		if(listArticle != null && listArticle.size() > 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("该标题已存在！");
			return result;
		}      
		Article article = new Article();
		if(!ObjectUtil.isEmpty(paraMap, "author")){
			article.setAuthor(paraMap.get("author").toString());
		}
		article.setCategoryId(Long.parseLong(paraMap.get("category_id").toString()));
		if(!ObjectUtil.isEmpty(paraMap, "content")){
			article.setContent(paraMap.get("content").toString());
		}
		article.setCreateUser(Long.parseLong(paraMap.get("createUser").toString()));
		if(!ObjectUtil.isEmpty(paraMap, "description")){
			article.setDescription(paraMap.get("description").toString());
		}				
		if(!ObjectUtil.isEmpty(paraMap, "sub_title")){
			article.setSubTitle(paraMap.get("sub_title").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "remark")){
			article.setRemark(paraMap.get("remark").toString());
		}
		article.setTitle(paraMap.get("title").toString());
		article.setPicId(paraMap.get("picId").toString());
		long maxSort = articleMapper.getMaxSort(paraMap.get("category_id").toString());
		article.setSort(maxSort + 1);
		articleMapper.insertArticle(article);		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加成功！");
		return result;
	}
	
	@Override
	public void topArticle(String id) throws Exception{
		// 获取文章对象
		Article article = articleMapper.getArticleById(id);
		// 修改其它文章下移
		articleMapper.moveSortArticle(article.getSort());
		// 获取最大排序号
		long maxSort = articleMapper.getMaxSort("" + article.getCategoryId());
		// 置顶当前文章
		article.setSort(maxSort);
		articleMapper.updateArticle(article);
	}

	@Override
	public Result updArticle(Map<String, Object> paraMap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		Article article = new Article();
		List<Article> listArticle = articleMapper.validateArticle(paraMap);
		if(listArticle != null && listArticle.size() > 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("该标题已存在！");
			return result;
		}
		if(!ObjectUtil.isEmpty(paraMap, "author")){
			article.setAuthor(paraMap.get("author").toString());
		}
		article.setCategoryId(Long.parseLong(paraMap.get("category_id").toString()));
		if(!ObjectUtil.isEmpty(paraMap, "content")){
			article.setContent(paraMap.get("content").toString());
		}
		article.setCreateUser(Long.parseLong(paraMap.get("createUser").toString()));
		if(!ObjectUtil.isEmpty(paraMap, "description")){
			article.setDescription(paraMap.get("description").toString());
		}		
		if(!ObjectUtil.isEmpty(paraMap, "sub_title")){
			article.setSubTitle(paraMap.get("sub_title").toString());
		}
		if(!ObjectUtil.isEmpty(paraMap, "remark")){
			article.setRemark(paraMap.get("remark").toString());
		}
		article.setTitle(paraMap.get("title").toString());
		if(!ObjectUtil.isEmpty(paraMap, "id")){
			article.setId(paraMap.get("id").toString());
		}
		if(paraMap.get("picId") != null && !paraMap.get("picId").toString().equals("")){
			article.setPicId(paraMap.get("picId").toString());
		}		
		articleMapper.updateArticle(article);		
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功！");
		return result;
	}

	@Override
	public int delArticleById(String id) {
		// TODO Auto-generated method stub
		return articleMapper.delArticleById(id);
	}


	@Override
	public Result articlePub(String id) {
		// TODO Auto-generated method stub
		Result result = new Result();
		int num = 0;
		num = articleMapper.updateArticleStatus(id);
		if(num > 0){
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("发布成功！");
		}else{
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
		}
		return result;
	}


}
