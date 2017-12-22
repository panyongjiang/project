package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.mapper.ResourceMapper;

@Component
public class AuthorityUtil {
	@Autowired
	private ResourceMapper resourceMapper;

	/**
	 * 去掉所有重复的权限,并返回无重复权限的id
	 * 
	 * @param allauthority
	 *            所有已拥有的权限
	 * @param resourceids
	 *            需要添加的权限
	 * @return
	 * @throws Exception
	 */
	public List<String> replaceAllHavedAuthority(List<String> resourceids)
			throws Exception {
		// 根据需要添加的资源获取所有资源详情
		List<Resource> resources = resourceMapper
				.getAllResourceByResourceId(resourceids);
		Set<String> need = new HashSet<String>();
		//本地资源所对应的父级resource_id及子级resource_id
		StringBuilder sb = new StringBuilder();
		//本地资源resource_id
		List<String> localresource = new ArrayList<String>();
        //菜单级资源resource_id
		StringBuilder parentsb = new StringBuilder();
		for (Resource resource : resources) {
			localresource.add(resource.getResourceId());
			// 获取父级关联的resource_id
			if (!StringUtils.isEmpty(resource.getResourceParentid())) {

				parentsb.append(resource.getResourceParentid());
				parentsb.append(",");

				sb.append((resource.getResourceParentid()));
				// 拼接数据
				sb.append(",");
			}
			if (!StringUtils.isEmpty(resource.getResourceChildid())) {
				// 获取子级关联的resource_id
				sb.append(resource.getResourceChildid());
				sb.append(",");
			}
		}
		// 拼接所有菜单父级依赖菜
		sb.append(getAllparentChildResourceId(parentsb.toString()));
		String[] allresourceid = sb.toString().split(",");

		List<String> temporary = Arrays.asList(allresourceid);
		List<String> authority = new ArrayList<String>(temporary);
		// 添加所有父级、子级resource_id和自身resource_id
		need.addAll(authority);
		need.addAll(localresource);
		List<String> needauthority = new ArrayList<String>();
		needauthority.addAll(need);

		if (needauthority.size() > 0) {
			return resourceMapper.getAllResourceByUserResourceId(needauthority);
		}
		return null;

	}

	/**
	 * 获取父级下的所有子级resource
	 * 
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	private String getAllparentChildResourceId(String parentid)
			throws Exception {
		String[] parentresourceid = parentid.split(",");
		Set<String> need = new HashSet<String>();
		List<String> temporary = Arrays.asList(parentresourceid);
		List<String> authority = new ArrayList<String>(temporary);
		// 过滤所有重复的父级resouce_id
		need.addAll(authority);
		List<String> needauthority = new ArrayList<String>();
		needauthority.addAll(need);
		// 获取所有父级菜单id依赖的资源
		List<Resource> resources = resourceMapper
				.getAllResourceMenuByParentResourceId(needauthority);
		StringBuilder sb = new StringBuilder();
		for (Resource resource : resources) {
			if (!StringUtils.isEmpty(resource.getResourceChildid())) {
				// 获取父级下子级关联的resource_id
				sb.append(resource.getResourceChildid());
				sb.append(",");
			}
		}
		return sb.toString();

	}
}
