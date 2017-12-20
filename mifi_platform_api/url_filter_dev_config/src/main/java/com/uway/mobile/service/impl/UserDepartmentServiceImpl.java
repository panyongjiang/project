package com.uway.mobile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserDepartment;
import com.uway.mobile.mapper.UserDepartmentMapper;
import com.uway.mobile.service.UserDepartmentService;

@Service("userDepartmentService")
public class UserDepartmentServiceImpl implements UserDepartmentService {
	
	@Autowired
	private UserDepartmentMapper userDepartmentMapper;

	@Override
	public List<User> getUsersByDepartmentId(int departmentId) {
		return userDepartmentMapper.getUsersByDepartmentId(departmentId);
	}

	@Override
	public int insertUserDepartment(UserDepartment userDepartment) {
		// TODO Auto-generated method stub
		return userDepartmentMapper.insertUserDepartment(userDepartment);
	}

	@Override
	public List<User> getOtherUsers(Integer companyId) {
		// TODO Auto-generated method stub
		return userDepartmentMapper.getOtherUsers(companyId);
	}

	

}
