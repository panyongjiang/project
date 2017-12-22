package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserGroup;



@Mapper
public interface UserGroupMapper {
	
	
	/**
	 * 添加所有的用户组信息
	 * @param list
	 */
	public void saveAllGroup(List<Map<String,Object>> list) throws Exception;	
	

	/**
	 * 根据id删除所有的用户组信息
	 * @param list
	 */
	public void  deleteAllGroupByUserId(String id) throws Exception;
	
	
	
	/**
	 * 获取所有组信息
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getAllGroupDetail() throws Exception;
	
	
	/**
	 * 获取所有组数量
	 * @return
	 * @throws Exception
	 */
	public long getAllGroupCount() throws Exception;
	
	
	/**
	 * 获取所有组信息并分页
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getAllGroupDetailLimit(Map<String,Object> map) throws Exception;
	
	
	/**
	 * 根据用户id获取组信息
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getAllGroupDetailByUserId(String id) throws Exception;
	
	
	/**
	 * 根据用户id获取组id
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getAllGroupIdByUserId(String id) throws Exception;
	
	
	/**
	 * 添加用户组信息
	 * @param userGroup
	 * @return
	 */
	public int saveGroupDetail(UserGroup userGroup) throws Exception;


	/**
	 * 修改用户组信息
	 * @param group
	 */
	public void updateGroupDetail(UserGroup group) throws Exception;
	
	
	
	/**
	 * 根据组id获取组信息
	 * @param id
	 * @return
	 */
	public UserGroup getUserGroupById(String id) throws Exception;
	
	
	/**
	 * 根据组名称获取组信息
	 * @param name
	 * @return
	 */
	public UserGroup getUserGroupByName(String name) throws Exception;
	
	
	
	
	/**
	 * 根据组id删除组信息
	 * @param id
	 */
	public void deleteGroupDetailByGroupId(String id) throws Exception;
	
	
	/**
	 * 根据组id删除组与用户映射信息
	 * @param id
	 */
	public void deleteGroupMappingDetailByGroupId(String id) throws Exception;


	/**
	 * 根据用户批量添加组映射
	 * @param needauthoritys
	 * @throws Exception
	 */
	public void saveAllGroupByUsers(List<Map<String, Object>> needauthoritys) throws Exception;
	
	
	
	/**
	 * 根据组id获取旗下所有用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<User> statisticsAllAdminByGroupId(String id) throws Exception;
	
	
}
