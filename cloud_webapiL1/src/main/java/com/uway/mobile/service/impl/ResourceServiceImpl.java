package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.domain.User;
import com.uway.mobile.mapper.AuthorityMapper;
import com.uway.mobile.mapper.ResourceMapper;
import com.uway.mobile.mapper.UserGroupMapper;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.ResourceService;
import com.uway.mobile.util.AuthorityUtil;
import com.uway.mobile.util.ObjectUtil;

@Service
public class ResourceServiceImpl implements ResourceService {
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AuthorityMapper authorityMapper;
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private AuthorityUtil authorityUtil;

	private static final Logger log = Logger
			.getLogger(ResourceServiceImpl.class);

	@Override
	public List<Resource> getAllResource(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return resourceMapper.getAllResourceLimit(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result saveUserAuthority(Map<String, Object> paramap)
			throws Exception {
		Result result = new Result();
		List<String> resources = (List<String>) paramap.get("resource_id");
		List<String> users = (List<String>) paramap.get("user_id");
		if (resources.size() <= 0 || users.size() <= 0) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("资源或用户不得为空！");
			return result;
		}
		// 遍历所有被选中的用户
		for (String id : users) {
			// 获取所有的资源id
			List<String> authoritys = resourceMapper
					.getAllAuthorityByUserId(id);
			List<Map<String, Object>> needauthoritys = new ArrayList<Map<String, Object>>();
			
			List<String> needauthority = authorityUtil.replaceAllHavedAuthority(resources);
			for (String resource : needauthority) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (!authoritys.contains(resource)) {
					map.put("resource_id", resource);
					map.put("user_id", id);
				}
				if (map != null && map.size() > 0) {
					needauthoritys.add(map);
				}
			}
			if (needauthoritys != null && needauthoritys.size() > 0) {
				// 添加管理员未拥有的权限
				authorityMapper.saveAllAuthorityByResource(needauthoritys);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加权限成功！");
		return result;
	}

	@Override
	public List<Resource> getAllAuthorityByUser(String id) throws Exception {
		// TODO Auto-generated method stub
		List<String> resource = authorityMapper.getAllAuthorityIdByUser(id);
		if (resource != null && resource.size() > 0) {
			// 获取该用户所有的菜单权限
			return resourceMapper.getAllResourceMenuByResourceId(resource);
		}
		return null;
	}
	
	@Override
	public List<Resource> getAllAuthorityByUserALL(String id) throws Exception {
		// TODO Auto-generated method stub
		List<String> resource = authorityMapper.getAllAuthorityIdByUser(id);
		if (resource != null && resource.size() > 0) {
			// 获取该用户所有的菜单权限
			return resourceMapper.getAllResourceByResourceId(resource);
		}
		return null;
	}
	

	@Override
	public long countResources(Map<String, Object> map) throws Exception {
		return resourceMapper.countAllResources(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result creartAndSaveAdminDetail(Map<String, Object> map,
			Result result) throws Exception {
		if (ObjectUtil.isEmpty(map, "user_name")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		List<User> users = userMapper.getAdminByName(map.get("user_name")
				.toString());
		if (users.size() > 0) {
			result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
			result.setMsg("该用户名已存在！");
			return result;
		}
		// 添加用户信息
		User user = new User();
		user.setRole((short) 1);
		user.setUserName(map.get("user_name").toString());
		// 拼接密码信息
		String fullPwd = Constance.KEY + map.get("user_name").toString()
				+ Constance.DEFAULT_PASSWORD;
		// 存入密码
		user.setPassword(DigestUtils.md5DigestAsHex(fullPwd.getBytes()));
		userMapper.insertAdmin(user);
		long id = user.getId();
		log.debug("返回的id为：" + id);
		if (!ObjectUtil.isEmpty(map, "group_id")) {
			List<String> groups = (List<String>) map.get("group_id");
			// 添加用户组
			List<Map<String, Object>> listGroups = new ArrayList<Map<String, Object>>();
			for (String group : groups) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("group_id", group);
				paramap.put("user_id", id);
				listGroups.add(paramap);
			}
			if (listGroups.size() > 0) {
				userGroupMapper.saveAllGroup(listGroups);
			}
		}
		if (!ObjectUtil.isEmpty(map, "resource_id")) {
			List<Integer> resources = (List<Integer>) map.get("resource_id");
			List<String> allresource = new ArrayList<String>();
			for(int resource : resources){
				allresource.add(resource +"");
			}
			if (allresource.size() <= 0) {
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("添加成功");
				return result;
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			List<String> needauthority = authorityUtil.replaceAllHavedAuthority(allresource);
			// 添加资源权限
			for (String resource : needauthority) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("resource_id", resource);
				paramap.put("user_id", id);
				list.add(paramap);
			}
			if (list.size() > 0) {
				authorityMapper.saveAllAuthorityByResource(list);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加成功");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result updateAndSaveAdminDetail(Map<String, Object> map,
			Result result) throws Exception {
		if (ObjectUtil.isEmpty(map, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		// 修改用户信息
		User user = new User();
		user.setId((Long.valueOf(map.get("user_id").toString())));
		// user.setRole((short)1);
		user.setUserName(map.get("user_name").toString());
		// user.setPassword(Constance.DEFAULT_PASSWORD);
		userMapper.updateAdmin(user);
		long id = user.getId();
		// 删除用户组信息
		userGroupMapper.deleteAllGroupByUserId(map.get("user_id").toString());
		// 删除用户权限信息
		authorityMapper.deleteAuthorityByUser(map.get("user_id").toString());
		if (!ObjectUtil.isEmpty(map, "group_id")) {
			List<String> groups = (List<String>) map.get("group_id");
			// 添加用户组
			List<Map<String, Object>> listGroups = new ArrayList<Map<String, Object>>();
			for (String group : groups) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("group_id", group);
				paramap.put("user_id", id);
				listGroups.add(paramap);
			}
			if (listGroups.size() > 0) {
				userGroupMapper.saveAllGroup(listGroups);
			}
		}
		if (!ObjectUtil.isEmpty(map, "resource_id")) {
			List<Integer> resources = (List<Integer>) map.get("resource_id");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> allresource = new ArrayList<String>();
			for(int resource : resources){
				allresource.add(resource +"");
			}
			if (allresource.size() <= 0) {
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("修改成功");
				return result;
			}
			List<String> needauthority = authorityUtil.replaceAllHavedAuthority(allresource);
			// 添加资源权限
			for (String resource : needauthority) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("resource_id", resource);
				paramap.put("user_id", id);
				list.add(paramap);
			}
			if (list.size() > 0) {
				authorityMapper.saveAllAuthorityByResource(list);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功");
		return result;

	}

	@Override
	public Result getAllResource(Result result) throws Exception {
		// TODO Auto-generated method stub
		List<Resource> resources = resourceMapper.getAllResource();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resource_detail", resources);
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Result saveGroupAuthority(Map<String, Object> paramap, Result result)
			throws Exception {
		List<String> resourceids = (List<String>) paramap.get("resource_id");
		List<String> usergroupids = (List<String>) paramap.get("group_id");
		if (resourceids.size() <= 0 || usergroupids.size() <= 0) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("资源或用户组不得为空！");
			return result;
		}
		// 遍历所有被选中的用户
		for (String id : usergroupids) {
			// 根据组id获取组下所有的资源权限
			List<String> resources = resourceMapper
					.getGroupAuthorityIdByGroupId(id + "");
			
			List<String> needauthority = authorityUtil.replaceAllHavedAuthority(resourceids);
			
			List<Map<String, Object>> needauthoritys = new ArrayList<Map<String, Object>>();
			for (String resource : needauthority) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (!resources.contains(resource)) {
					map.put("resource_id", resource);
					map.put("group_id", id);
				}
				if (map != null && map.size() > 0) {
					needauthoritys.add(map);
				}
			}
			if (needauthoritys != null && needauthoritys.size() > 0) {
				// 添加用户组未拥有的权限
				authorityMapper.saveAllAuthorityByGroup(needauthoritys);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加权限成功！");
		return result;
	}

}
