package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserDepartment;

@Mapper
public interface UserDepartmentMapper {
	
	/**
	 * 根据分组ID查询分组的成员
	 * @param departmentId
	 * @return
	 */
	public List<User> getUsersByDepartmentId(int departmentId);
	
	/**
	 * 新增分组用户表的记录
	 * @param userDepartment
	 * @return
	 */
	public int insertUserDepartment(UserDepartment userDepartment);
	
	/**
	 * 根据分组ID删除分组的成员
	 * @param departmentId
	 * @return
	 */
	public int deleteByDepartmentId(int departmentId);
	
	/**
	 * 根据分组和用户删除
	 * @param param
	 * @return
	 */
	public int deleteByDepartAndUser(Map<String,Integer> param);
	
	/**
	 * 查询不在任何组的用户
	 * @param param
	 * @return
	 */
	public List<User> getOtherUsers(Integer companyId);

}
