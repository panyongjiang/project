package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.User;

@Mapper
public interface UserMapper {
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int insertUser(User user) throws Exception;
	
	/**
	 * 通过用户名查找用户群
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
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId) throws Exception;
	
	/**
	 * 据用户名获取用户信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getInfoById(String userId) throws Exception;
	
	public Map<String, Object> getInfoByAdmin(String userId) throws Exception;
	
	/**
	 * 列出符合条件的所有用户
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listUser(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 获取符合条件的用户个数
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public long countUser(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 修改用户密码
	 * @param user
	 */
	public void updPwd(User user) throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 */
	public void updUser(User user) throws Exception;
	
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
	public List<User> getAllLimitAdminUser(Map<String,Object> map) throws Exception;
	
	
   
	/**
	 * 添加管理员
	 * @param user
	 * @return
	 */
	public int insertAdmin(User user) throws Exception;
	
	
	/**
	 * 修改管理员
	 * @param user
	 * @return
	 */
	public int updateAdmin(User user) throws Exception;

	
	/**
	 * 统计所有admin
	 * @return
	 */
	public long  countAllAdmin(Map<String,Object> map) throws Exception;

	
}
