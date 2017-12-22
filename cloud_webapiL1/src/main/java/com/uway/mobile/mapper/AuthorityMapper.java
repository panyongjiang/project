package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;



@Mapper
public interface AuthorityMapper {
	
	/**
	 * 添加用户权限
	 * @param list
	 * @throws Exception
	 */
	public void saveAllAuthorityByResource(List<Map<String,Object>> list) throws Exception;
	
	
	/**
	 * 根据用户id获取所在组及个人所有权限资源id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllAuthorityIdByUser(String id) throws Exception;
	
	
	
	/**
	 * 根据用户id删除权限
	 * @param id
	 * @throws Exception
	 */
	public void deleteAuthorityByUser(String id) throws Exception;

	/**
	 * 添加用户组权限
	 * @param list
	 * @throws Exception
	 */
	public void saveAllAuthorityByGroup(List<Map<String,Object>> list) throws Exception;
	
	
	/**
	 * 根据组id删除组权限
	 * @param id
	 * @throws Exception
	 */
	public void deleteGroupAuthorityByGroupId(String id) throws Exception;
	
	
	

}
