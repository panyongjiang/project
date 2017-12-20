package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserGroup;
import com.uway.mobile.mapper.AuthorityMapper;
import com.uway.mobile.mapper.ResourceMapper;
import com.uway.mobile.mapper.UserGroupMapper;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.UserGroupService;
import com.uway.mobile.util.AuthorityUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.PagingUtil;

@Service
public class UserGroupServiceImpl implements UserGroupService {
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private AuthorityMapper authorityMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private UserMapper userMapper;
    @Autowired
	private AuthorityUtil authorityUtil;

	private static final Logger log = Logger
			.getLogger(ResourceServiceImpl.class);

	@Override
	public Result getAllUserGroupDetailLimit(Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		// 验证分页参数
		result = PagingUtil.validatePagination(result, paraMap);
		if (StringUtils.isNotBlank(result.getMsg())) {
			return result;
		}
		// 设置分页具体参数
		paraMap = PagingUtil.installParameters(paraMap);
		// 设置模糊查询条件
		if (!ObjectUtil.isEmpty(paraMap, "group_name")) {
			paraMap.put("group_name", "%"
					+ paraMap.get("group_name").toString() + "%");
		}
		if (!ObjectUtil.isEmpty(paraMap, "description")) {
			paraMap.put("description", "%"
					+ paraMap.get("description").toString() + "%");
		}
		if (!ObjectUtil.isEmpty(paraMap, "group_id")) {
			paraMap.put("group_id", paraMap.get("group_id").toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("detail", userGroupMapper.getAllGroupDetailLimit(paraMap));
		map.put("total_num", userGroupMapper.getAllGroupCount());
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Result addUserGroupDetail(Map<String, Object> map, Result result)
			throws Exception {
		List<Integer> resources = (List<Integer>) map.get("resource_id");
		// 判断是否存在该用户
		UserGroup excuteruser = userGroupMapper.getUserGroupByName(map.get(
				"group_name").toString());
		if (excuteruser != null) {
			result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
			result.setMsg("该用户已存在");
			return result;
		}
		// 添加用户组
		UserGroup group = new UserGroup();
		group.setGroupName(map.get("group_name").toString());
		group.setDescription(map.get("description").toString());
		userGroupMapper.saveGroupDetail(group);
		long id = group.getId();
		log.debug("返回的id为：" + id);
		// 创建所有资源集合
		List<Map<String, Object>> resourceMap = new ArrayList<Map<String, Object>>();

		List<String> allresource = new ArrayList<String>();
		for (int resource : resources) {
			allresource.add(resource + "");
		}
		if (allresource.size() <= 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("添加成功");
			return result;
		}
		List<String> needauthority = authorityUtil
				.replaceAllHavedAuthority(allresource);

		for (String resource : needauthority) {
			Map<String, Object> paramap = new HashMap<String, Object>();
			paramap.put("resource_id", resource);
			paramap.put("group_id", id);
			resourceMap.add(paramap);
		}
		if (resourceMap.size() > 0) {
			// 添加组权限
			authorityMapper.saveAllAuthorityByGroup(resourceMap);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加成功");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result updateUserGroupDetail(Map<String, Object> map, Result result)
			throws Exception {
		
		List<Integer> resources = (List<Integer>) map.get("resource_id");
		// 根据groupid获取组
		UserGroup group = new UserGroup();
		int id = Integer.valueOf(map.get("group_id").toString());
		group.setId(id);
		group.setGroupName(map.get("group_name").toString());
		group.setDescription(map.get("description").toString());
		userGroupMapper.updateGroupDetail(group);
		// 删除组权限
		authorityMapper.deleteGroupAuthorityByGroupId(id + "");
		// 创建所有资源集合
		List<Map<String, Object>> resourceMap = new ArrayList<Map<String, Object>>();
		
		List<String> allresource = new ArrayList<String>();
		for (int resource : resources) {
			allresource.add(resource + "");
		}
		if (allresource.size() <= 0) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功");
			return result;
		}
		List<String> needauthority = authorityUtil.replaceAllHavedAuthority(allresource);

		for (String resource : needauthority) {
			Map<String, Object> paramap = new HashMap<String, Object>();
			paramap.put("resource_id", resource);
			paramap.put("group_id", id);
			resourceMap.add(paramap);
		}
		// 添加组权限
		authorityMapper.saveAllAuthorityByGroup(resourceMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功");
		return result;
	}

	@Override
	public Result getUserGroupDetailById(Map<String, Object> map, Result result)
			throws Exception {
		
		int id = Integer.valueOf(map.get("group_id").toString());
		// 查询用户组
		UserGroup group = userGroupMapper.getUserGroupById(id + "");
		// 根据组id获取组下所有的资源权限
		List<Resource> resources = resourceMapper.getGroupAuthorityByGroupId(id
				+ "");
		// 获取所有的资源权限
		List<Resource> allresources = resourceMapper.getAllResource();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("group_detail", group);
		paraMap.put("resource_detail", resources);
		paraMap.put("all_resource_detail", allresources);
		result.setData(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		return result;
	}

	@Override
	public Result deleteUserGroupDetailById(Map<String, Object> map,
			Result result) throws Exception {
		if (ObjectUtil.isEmpty(map, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		int id = Integer.valueOf(map.get("group_id").toString());

		// 删除用户组信息
		userGroupMapper.deleteGroupDetailByGroupId(id + "");
		// 根据组id删除组下所有的资源权限
		authorityMapper.deleteGroupAuthorityByGroupId(id + "");
		// 根据组id删除组下的所有用户关联信息
		userGroupMapper.deleteGroupMappingDetailByGroupId(id + "");
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功");
		return result;
	}

	@Override
	public Result getAllUserGroupDetail(Result result) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("detail", userGroupMapper.getAllGroupDetail());

		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result batchAllUsersToGroups(Result result, Map<String, Object> map)
			throws Exception {
		// 组id集合
		List<Integer> groupids = (List<Integer>) map.get("group_id");
		// 用户id集合
		List<Integer> userids = (List<Integer>) map.get("user_id");

		for (int userid : userids) {
			// 获取个人所有分组信息
			List<Integer> groups = userGroupMapper.getAllGroupIdByUserId(userid
					+ "");
			List<Map<String, Object>> needgroups = new ArrayList<Map<String, Object>>();

			for (int groupid : groupids) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				if (!groups.contains(groupid)) {
					paramap.put("group_id", groupid);
					paramap.put("user_id", userid);

				}
				if (paramap != null && paramap.size() > 0) {
					needgroups.add(paramap);
				}
			}
			if (needgroups != null && needgroups.size() > 0) {
				// 添加管理员未拥有的组
				userGroupMapper.saveAllGroup(needgroups);
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("添加成功");
		return result;

	}

	@Override
	public Result statisticsAllAdminByGroupId(Result result,
			Map<String, Object> paramap) throws Exception {
		// 获取该组下所有用户
		List<User> user = userGroupMapper.statisticsAllAdminByGroupId(paramap
				.get("group_id").toString());
		// 所有的后端管理员
		List<User> alluser = userMapper.getAllAdminUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_detail", user);
		map.put("all_user_detail", alluser);
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result updateAllAdminByGroupId(Result result,
			Map<String, Object> paramap) throws Exception {
		String groupid = paramap.get("group_id").toString();
		// 先删除用户组映射信息
		userGroupMapper.deleteGroupMappingDetailByGroupId(paramap.get(
				"group_id").toString());

		// 获取所有需要添加的用户
		List<Integer> adminids = (List<Integer>) paramap.get("user_id");
		// 添加用户与用户组映射
		List<Map<String, Object>> listGroups = new ArrayList<Map<String, Object>>();
		for (int adminid : adminids) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("group_id", groupid);
			map.put("user_id", adminid);
			listGroups.add(map);
		}
		if (listGroups.size() > 0) {
			userGroupMapper.saveAllGroup(listGroups);
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功");
		return result;
	}

}
