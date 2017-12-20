package com.uway.mobile.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserDepartment;

@Transactional
public interface UserDepartmentService {
	
	/**
	 * 根据分组ID获取分组下的所有用户
	 * @param departmentId
	 * @return
	 */
	public List<User> getUsersByDepartmentId(int departmentId);
	
	/**
	 * 新增用户分组
	 * @param userDepartment
	 * @return
	 */
	public int insertUserDepartment(UserDepartment userDepartment);
	
	/**
	 * 查询不在任何组的用户
	 * @param param
	 * @return
	 */
	public List<User> getOtherUsers(Integer companyId);

}
