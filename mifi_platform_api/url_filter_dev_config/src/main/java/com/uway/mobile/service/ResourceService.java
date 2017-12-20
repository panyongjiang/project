package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Resource;


@Transactional
public interface ResourceService {
	/**
	 * 获取所有的资源
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Resource> getAllResource(Map<String,Object> map) throws Exception;
	
	
	/**
	 * 将权限赋予所有被选中的用户
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Result saveUserAuthority(Map<String,Object> map) throws Exception;
	
	
	
	/**
	 * 根据用户id获取所有的资源信息MENU = 0 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllAuthorityByUser(String id) throws Exception;
	
	
	/**
	 * 根据用户id和资源节点id获取用户父级节点
	 * @param id
	 * @return
	 * @throws Exception
	 */
	//public long getAuthorityByUserResourceId(String userId,String resourceId) throws Exception;
	
	
	
	/**
	 * 统计所有资源数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public long countResources(Map<String,Object> map) throws Exception;
	
	
	
    /**
     * 创建并保存admin用户信息
     * @param map
     * @return
     * @throws Exception 
     */
    public Result creartAndSaveAdminDetail(Map<String,Object> map,Result result) throws Exception;
    
    
    /**
     * 修改并保存admin用户信息
     * @param map
     * @return
     * @throws Exception 
     */
    public Result updateAndSaveAdminDetail(Map<String,Object> map,Result result) throws Exception;


	/**获取所有的资源
	 * @return
	 */
	public Result getAllResource(Result result) throws Exception;


	/**
	 * 将权限赋予所有被选中的用户组
	 * @param paramap
	 * @return
	 * @throws Exception
	 */
	public Result saveGroupAuthority(Map<String, Object> paramap,Result result) throws Exception;


	/**
	 * 根据用户id获取所有的资源信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Resource> getAllAuthorityByUserALL(String id) throws Exception;
}
