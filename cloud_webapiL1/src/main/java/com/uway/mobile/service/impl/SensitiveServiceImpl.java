package com.uway.mobile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.SensitiveWord;
import com.uway.mobile.mapper.SensitiveMapper;
import com.uway.mobile.service.SensitiveService;
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.SensitiveCheckUtil;

@Service
public class SensitiveServiceImpl implements SensitiveService {
	@Autowired
	private SensitiveMapper sensitiveMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public Result addSensitive(String word) throws Exception {
		Result result = new Result();
		if (word == null || word.equals("")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数不能为空！");
		}
		SensitiveWord sensitive = new SensitiveWord();
		sensitive.setName(word);
		try {
			sensitiveMapper.insertSensitive(sensitive);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("添加到mysql数据库成功！");
		} catch (Exception e) {
			// TODO: handle exception
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误！");
		}
		return result;
	}

	@Override
	public int delSensitiveByName(String name) throws Exception {
		// TODO Auto-generated method stub
		return sensitiveMapper.delSensitive(name);
	}

	@Override
	public String checkSensitive(String word) {
		// TODO Auto-generated method stub
		return sensitiveMapper.checkWord(word);
	}

	@Override
	public Result getResultSensitiveCheck(Result result,String text) throws Exception {
		// 获取所有的敏感词
		long totalSize = redisUtil.lsize(Constance.REDIS_SENSITIVE_WORDS);
		List<String> sensitiveWordList = redisUtil.lrange(
				Constance.REDIS_SENSITIVE_WORDS, 0, totalSize);
		result = SensitiveCheckUtil.getResultByText(sensitiveWordList, text,
				result);
		return result;
	}

}
