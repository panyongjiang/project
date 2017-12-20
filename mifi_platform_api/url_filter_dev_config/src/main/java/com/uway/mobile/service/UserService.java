package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;
import com.uway.mobile.util.Pagination;

@Transactional
public interface UserService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public Result insertAdminUser(Map<String, String> paraMap)throws Exception;
	
	/**
	 * 邮箱验证
	 * @param email
	 * @param validateCode
	 * @return
	 */
	public Result checkMail(String email, String validateCode)throws Exception;
	
	/**
	 * 通过邮箱查找用户群
	 * 
	 * @param userName
	 * @return
	 */
	public List<User> getUserByEmail(String email) throws Exception;
	
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
	
	/**
	 * 使用excel导入用户数据
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Result importUsers(MultipartFile file,User user) throws Exception;
	
	/**
	 * 根据条件查找用户
	 * @param map
	 * @return
	 * @throws Exception
	 */
	
	public List<User> findUsersByCondition(Map<String, Object> map) throws Exception;
	
	public List<User> getUserId(Map<String,Object> paraMap)throws Exception;
	
	/**
	 * 新增用户
	 * @param user
	 * @throws Exception
	 */
	public Result insertUser(Map<String,Object> paraMap,User adminUser) throws Exception;
	
	/**
	 * 更新用户信息
	 * @param user
	 * @throws Exception
	 */
	public void updateUser(User user) throws Exception;
	
	/**
	 * 删除不拥有设备的用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String deleteUser(String userId) throws Exception;
	
	/**
	 * 根据角色分页查询用户信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Pagination getUsersByRoleWithPage(Map<String,Object> param) throws Exception;

	public Result rePWD(Map<String, String> paraMap)throws Exception;

	public void updateDepartment(User user)throws Exception;

	public List<Integer> selectUserIdByDepartmentId(int id)throws Exception;

	public void deleteDepartment(Integer userId)throws Exception;

	public String deleteUserInDepartment(String userId)throws Exception;
	
	
}
