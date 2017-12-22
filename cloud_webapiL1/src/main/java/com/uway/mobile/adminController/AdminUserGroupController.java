package com.uway.mobile.adminController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.service.ResourceService;
import com.uway.mobile.service.UserGroupService;
import com.uway.mobile.util.ObjectUtil;

/**
 * 用户组控制层
 * 
 * @author lin
 *
 */
@RestController
@RequestMapping("user_group")
public class AdminUserGroupController {

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private UserGroupService userGroupService;

	/**
	 * 获取所有用户组信息并分页
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/statistics_all_group_limit", method = RequestMethod.POST)
	public Result getAllUserGroupDetailLimit(
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
			return userGroupService.getAllUserGroupDetailLimit(paraMap);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 添加用户组 ,并分配组权限
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_usergroup", method = RequestMethod.POST)
	public Result addUserGroupDetail(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paraMap, "group_name")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.addUserGroupDetail(paraMap, result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 根据id修改用户组 ,并分配组权限
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update_usergroup", method = RequestMethod.POST)
	public Result updateUserGroupDetail(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paraMap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		if (ObjectUtil.isEmpty(paraMap, "resource_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.updateUserGroupDetail(paraMap, result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 根据id获取用户组 ,并获取权限
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get_usergroupbyid", method = RequestMethod.POST)
	public Result getUserGroupDetailById(
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paraMap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.getUserGroupDetailById(paraMap, result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 根据组id删除用户组，组权限信息，该组用户组信息
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete_usergroupbyid", method = RequestMethod.POST)
	public Result deleteUserGroupDetailById(
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
			return userGroupService.deleteUserGroupDetailById(paraMap, result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 获取所有用户组信息
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/statistics_all_group", method = RequestMethod.POST)
	public Result getAllUserGroupDetail() throws Exception {
		Result result = new Result();
		try {
			return userGroupService.getAllUserGroupDetail(result);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

	/**
	 * 将多个管理员批量分配到某个组
	 * 
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batch_allusers_togroups", method = RequestMethod.POST)
	public Result batchAllUsersToGroups(@RequestBody Map<String, Object> paramap)
			throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paramap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		if (ObjectUtil.isEmpty(paramap, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.batchAllUsersToGroups(result, paramap);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};
	
	
	
	/**
	 * 根据组id获取旗下所有管理员
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/statistics_all_adminbygroupid", method = RequestMethod.POST)
	public Result statisticsAllAdminByGroupId(@RequestBody Map<String,Object> paramap) throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paramap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.statisticsAllAdminByGroupId(result,paramap);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};
	
	
	/**
	 * 根据组id（添加）修改旗下所有管理员
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update_all_adminbygroupid", method = RequestMethod.POST)
	public Result updateAllAdminByGroupId(@RequestBody Map<String,Object> paramap) throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(paramap, "group_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		if (ObjectUtil.isEmpty(paramap, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		try {
			return userGroupService.updateAllAdminByGroupId(result,paramap);

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	};

}
