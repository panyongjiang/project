package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import com.uway.mobile.domain.Resource;



/**
 * 资源dao
 * @author lin
 *
 */
@Mapper
public interface ResourceMapper {
	
	
	/**
	 * 获取所有的资源并分页
	 * @param map
	 * @return
	 */
	public List<Resource> getAllResourceLimit(Map<String,Object> map) throws Exception;
	
	
	/**
	 * 获取所有的资源
	 * @param map
	 * @return
	 */
	public List<Resource> getAllResource() throws Exception;
	
	
	/**
	 * 根据id获取后台管理人员所有权限id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllAuthorityByUserId(String id) throws Exception;


	/**统计资源总数
	 * @return
	 */
	public long countAllResources(Map<String,Object> map) throws Exception;
	
	
	
	/**
	 * 根据id获取后台管理人员所有资源
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllResourceByUserId(String id) throws Exception;
	

	
	/**
	 * 根据组id获取组所有的资源
	 * @param id
	 * @throws Exception
	 */
	public List<Resource> getGroupAuthorityByGroupId(String id) throws Exception;
	
	
	/**
	 * 根据组id获取组所有的资源id
	 * @param id
	 * @throws Exception
	 */
	public List<String> getGroupAuthorityIdByGroupId(String id) throws Exception;
	
	
	
	/**
	 * 根据组id获取组所有未拥有的资源
	 * @param id
	 * @throws Exception
	 */
	public List<Resource> getGroupOutOffAuthorityByGroupId(String id) throws Exception;
	
	
	
	/**
	 * 根据资源id获取所有的资源详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllResourceByResourceId(List<String> resource) throws Exception;
	
	
	/**
	 * 根据资源id获取所有的资源详情菜单
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllResourceMenuByResourceId(List<String> resource) throws Exception;
	
	
	/**
	 * 根据父级的资源resource_id获取所有的资源详情菜单
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllResourceMenuByParentResourceId(List<String> resource) throws Exception;
	
	
	
	/**
	 * 根据资源resource_id获取所有的资源详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllResourceByUserResourceId(List<String> list) throws Exception;


	
	
	
	/**
	 * 根据用户id获取所有未拥有的资源
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllOutOffResourceByUserId(String string) throws Exception;
	

}
