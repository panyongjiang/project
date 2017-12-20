package com.uway.mobile.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;

@Transactional
public interface UserGroupService {
	
	
	
	 /**
	  * 获取用户组详情并分页
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Result getAllUserGroupDetailLimit(Map<String,Object> map) throws Exception;
	
	

	 /**
	  * 添加用户组信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Result addUserGroupDetail(Map<String,Object> map,Result result) throws Exception;



	/**
	 * 修改用户组信息
	 * @param paraMap
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	public Result updateUserGroupDetail(Map<String, Object> paraMap,
			Result result) throws Exception;



	/**
	 * 根据id获取用户组信息
	 * @param paraMap
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	public Result getUserGroupDetailById(Map<String, Object> paraMap,
			Result result) throws Exception;



	/**
	 * 根据id删除用户组信息
	 * @param paraMap
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public Result deleteUserGroupDetailById(Map<String, Object> paraMap,
			Result result) throws Exception;



	/**
	 * 获取所有权限组信息
	 * @return
	 * @throws Exception
	 */
	public Result getAllUserGroupDetail(Result result) throws Exception;



	/**
	 * 将多个管理员批量分配到某个组
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public Result batchAllUsersToGroups(Result result,Map<String,Object> map) throws Exception;



	/**
	 * 根据组id获取旗下所有管理员
	 * @param result
	 * @param paramap
	 * @return
	 * @throws Exception
	 */
	public Result statisticsAllAdminByGroupId(Result result,
			Map<String, Object> paramap) throws Exception;



	/**
	 * 根据组id修改旗下管理员
	 * @param result
	 * @param paramap
	 * @return
	 * @throws Exception
	 */
	public Result updateAllAdminByGroupId(Result result,Map<String, Object> paramap) throws Exception;



	

}
