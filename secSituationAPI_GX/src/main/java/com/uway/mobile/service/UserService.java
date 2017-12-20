package com.uway.mobile.service;


import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;

@Transactional
public interface UserService {

	/**
	 * 用户查找
	 * @param userName
	 * @return
	 * @throws Exception
	 * @create_time 2017年12月12日 上午10:11:22
	 */
	public Result getUserByName(Map<String, Object> parMap) throws Exception;
	/**
	 * 修改密码
	 * @param user
	 * @throws Exception
	 * @create_time 2017年12月12日 上午10:11:32
	 */
	public Result updPwd(Map<String, Object> paramap) throws Exception;
}
