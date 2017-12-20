package com.uway.mobile.mapper;


import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.User;



@Mapper
public interface UserMapper {
	
	
	/**
	 * 查找用户
	 * @param userName
	 * @return
	 * @throws Exception
	 * @create_time 2017年12月12日 上午10:07:15
	 */
	public List<Map<String, Object>> getUserByName(String userName) throws Exception;
	
	
	/**
	 * 修改密码
	 * @param user
	 * @throws Exception
	 * @create_time 2017年12月12日 上午10:09:22
	 */
	public void updPwd(Map<String, Object> paramap) throws Exception;
}
