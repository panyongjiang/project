package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
	 * 通过邮箱查找用户群
	 * @param email
	 * @return
	 */
	public List<User> getUserByEmail(Map<String,Object> paraMap) throws Exception;
	
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
	
	/**
	 * 根据条件查找用户
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<User>  findUsersByCondition(Map<String,Object> map) throws Exception;

	/**
	 * 根据ID查询用户
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserId(Map<String,Object> paraMap)throws Exception;
	
	/**
	 * 根据角色查询用户信息
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsersByRole(Map<String,Object> paraMap)throws Exception;
	
	/**
	 * 查询某个角色的用户总数
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public int getUsersByRoleCount(String role)throws Exception;

	/**
	 * 邮箱激活更新状态
	 * @param email
	 * @throws Exception
	 */
	public void updateStatus(String email)throws Exception;

	public void rePWD(@Param(value="password") String fullpwd,@Param(value="userId") int userId);

	public void updateDepartment(User user);

	public List<Integer> selectUserIdByDepartmentId(int id);

	public void deleteDepartment(Integer userId);

	public void deleteUserInDepartment(String userId);
	
}
