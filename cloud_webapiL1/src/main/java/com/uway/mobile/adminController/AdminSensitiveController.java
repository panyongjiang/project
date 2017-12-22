package com.uway.mobile.adminController;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ArticleService;
import com.uway.mobile.service.SensitiveService;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.SensitiveCheckUtil;

@RestController
@RequestMapping("sensitive")
public class AdminSensitiveController extends BaseApplication {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private SensitiveService sensitiveService;
	@Autowired
	private ArticleService articleService;

	/**
	 * 添加敏感词
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/add_word", method = RequestMethod.POST)
	public Result addWord(
			@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try {
			String word = paraMap.get("word").toString();
			if(ObjectUtil.isEmpty(paraMap, "word")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数不能为空！");
				return result;
			}else{
				String word1 = sensitiveService.checkSensitive(word);
				if (word1 != null && !word1.equals("")) {
					result.setMsg("已存在，请重新添加！");
					return result;
				}
				result.setData(sensitiveService.addSensitive(word));
				redisUtil.lpush(Constance.REDIS_SENSITIVE_WORDS, word);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("添加成功！");
				return result;
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			return result;
		}
	}

	/**
	 * 删除敏感词
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/del_word", method = RequestMethod.POST)
	public Result delWord(
			@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try {
			String word = paraMap.get("word").toString();
			if(ObjectUtil.isEmpty(paraMap, "word")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("参数不能为空！");
				return result;
			}else{
				int num = 0;
				num = sensitiveService.delSensitiveByName(word);
				if (num <= 0) {
					result.setCode(Constance.RESPONSE_INNER_ERROR);
					result.setMsg("内部错误！");
					return result;
				}
				redisUtil.lpull(Constance.REDIS_SENSITIVE_WORDS, word);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("删除成功！");
				return result;
			}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			return result;
		}
	}

	/**
	 * 列出所有的敏感词
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list_word", method = RequestMethod.POST)
	public Result listAllWord() {
		Result result = new Result();
		try {
			long totalSize = redisUtil.lsize(Constance.REDIS_SENSITIVE_WORDS);
			List<String> resultList = redisUtil.lrange(
					Constance.REDIS_SENSITIVE_WORDS, 0, totalSize);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
			result.setData(resultList);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			return result;
		}
	}

	/**
	 * 敏感词检测
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sensitive_filter", method = RequestMethod.POST)
	public Result sensitiveFilter(@RequestBody Map<String, Object> map) {
		Result result = new Result();
		try {
			// 获取所有的敏感词
			long totalSize = redisUtil.lsize(Constance.REDIS_SENSITIVE_WORDS);
			List<String> sensitiveWordList = redisUtil.lrange(
					Constance.REDIS_SENSITIVE_WORDS, 0, totalSize);
			//根据id获取数据
			Map<String,Object> paraMap = articleService.showArticleById(map.get("id").toString());
			// 合并标题和副标题,内容，描述
			String text = paraMap.get("title").toString()
					+ paraMap.get("subTitle").toString()
					+ paraMap.get("description").toString()
					+ paraMap.get("content").toString();

			result = SensitiveCheckUtil.getResultByText(sensitiveWordList,
					text, result);
			
			if (StringUtils.isNotEmpty(result.getMsg())) {
				return result;
			}
			// 修改文章的发布状态
			articleService.articlePub(paraMap.get("id").toString());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("检测通过！");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
			return result;
		}
	}
}
