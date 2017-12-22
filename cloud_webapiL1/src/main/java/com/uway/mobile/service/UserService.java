package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;

@Transactional
public interface UserService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public Result insertUser(Map<String, String> paraMap)throws Exception;
	/**
	 * 验证邀请码
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result validateCode(Map<String, Object> paraMap) throws Exception;
	/**
	 * 通过用户名查找用户群
	 * 
	 * @param userName
	 * @return
	 */
	public List<User> getUserByName(String userName) throws Exception;
	
	/**
	 * 通过用户名查找管理员
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public List<User> getAdminByName(String userName) throws Exception;
	
	/**
	 * 通过用户ID查找用户群
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId) throws Exception;
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getInfoById(String userId) throws Exception;
	
	public Map<String, Object> getInfoByAdmin(String userId) throws Exception;
	
	/**
	 * 获取所有符合条件的用户
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public Result listUser(Map<String, Object> paraMap) throws Exception;

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 */
	public void updPwd(User user) throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @throws Exception
	 */
	public Result updUser(User user, Map<String, String> paraMap) throws Exception;

	/**
	 * 删除用户
	 * @param userId
	 * @throws Exception
	 */
	public void delUser(String userId) throws Exception;
	
	
	/**
	 * 获取所有的后台管理人员
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<User> getAllAdminUser() throws Exception;
	
	
	/**
	 * 获取所有的后台管理人员并分页
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Result getAllLimitAdminUser(Map<String,Object> map) throws Exception;
	
	
	

	/**
	 * 获取所有管理员添加信息（组，资源）
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Result getAllDetailsByAdmin() throws Exception;
	
	
	
	/**
	 * 根据管理员id获取所有详情
	 * @return
	 * @throws Exception
	 */
	public Result getAllDetailsByAdminId(Map<String,Object> map) throws Exception;
	
	
	
	
	/**
	 * 根据管理员id删除管理员信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Result deleteAdminDetailsByAdminId(Map<String, Object> map,Result result) throws Exception;
	
}
