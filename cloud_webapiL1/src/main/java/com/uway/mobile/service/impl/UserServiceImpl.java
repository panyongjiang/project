package com.uway.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.domain.SafeTrial;
import com.uway.mobile.domain.User;
import com.uway.mobile.mapper.AuthorityMapper;
import com.uway.mobile.mapper.CodeMapper;
import com.uway.mobile.mapper.ResourceMapper;
import com.uway.mobile.mapper.SafeCategoryMapper;
import com.uway.mobile.mapper.SafeTrialMapper;
import com.uway.mobile.mapper.UserGroupMapper;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.EncryptUtils;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.PagingUtil;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SafeTrialMapper safeTrialMapper;
	@Autowired
	private SafeCategoryMapper safeCategoryMapper;
	@Autowired
	private CodeMapper codeMapper;
	@Autowired
	private AuthorityMapper authorityMapper;
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public List<User> getUserByName(String userName) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserByName(userName);
	}

	@Override
	public List<User> getAdminByName(String userName) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getAdminByName(userName);
	}

	@Override
	public User getUserById(String userId) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserById(userId);
	}

	@Override
	public Result listUser(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		if (!ObjectUtil.isEmpty(paraMap, "user_name")) {
			paraMap.put("userName", "%" + paraMap.get("user_name").toString()
					+ "%");
		}
		long totalNum = userMapper.countUser(paraMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("details", userMapper.listUser(paraMap));
		map.put("totalNum", totalNum);
		String pageSize = paraMap.get("pageSize").toString();
		if (totalNum % Long.parseLong(pageSize) > 0) {
			map.put("totalPage", totalNum / Long.parseLong(pageSize) + 1);
		} else {
			map.put("totalPage", totalNum / Long.parseLong(pageSize));
		}
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功！");
		return result;
	}

	@Override
	public void updPwd(User user) throws Exception {
		// TODO Auto-generated method stub
		userMapper.updPwd(user);
	}

	@Override
	public Result insertUser(Map<String, String> paraMap) throws Exception {
		User user = new User();
		Result result = new Result();

		String fullpwd = Constance.KEY
				+ paraMap.get("user_name")
				+ EncryptUtils.decryption(paraMap.get("password"),
						EncryptUtils.desKey);
		String md5pwd = DigestUtils.md5DigestAsHex(fullpwd.getBytes());

		user.setUserName(paraMap.get("user_name"));
		user.setPassword(md5pwd);
		Pattern pattern = Pattern.compile("[0-9]*");
		if (!StringUtils.isEmpty(paraMap.get("industry"))) {
			Matcher iisNum = pattern.matcher(paraMap.get("industry"));
			if (!iisNum.matches()) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("行业参数格式错误!");
				return result;
			}
			user.setIndustry((long) Integer.parseInt(paraMap.get("industry")));
		} else {
			user.setIndustry((long) 0);
		}
		if (!StringUtils.isEmpty(paraMap.get("business"))) {
			user.setBusiness(paraMap.get("business"));
		} else {
			user.setBusiness("");
		}
		if (!StringUtils.isEmpty(paraMap.get("province"))) {
			Matcher pisNum = pattern.matcher(paraMap.get("province"));
			if (!pisNum.matches()) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("省份参数格式错误!");
				return result;
			}
			user.setProvince(paraMap.get("province"));
		} else {
			user.setProvince("");
		}
		if (!StringUtils.isEmpty(paraMap.get("city"))) {
			Matcher cisNum = pattern.matcher(paraMap.get("city"));
			if (!cisNum.matches()) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("城市参数格式错误!");
				return result;
			}
			user.setCity(paraMap.get("city"));
		} else {
			user.setCity("");
		}
		userMapper.insertUser(user);
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("code", paraMap.get("code"));
		c.put("userId", user.getId());

		codeMapper.updCode(c);
		safeCategoryMapper.insertSafeCategory(user.getId());

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("注册成功");
		return result;
	}

	@Override
	public Result updUser(User user, Map<String, String> paraMap)
			throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();

		if (paraMap.get("industry") != null) {
			user.setIndustry((long) Integer.parseInt(paraMap.get("industry")));
		}
		if (paraMap.get("business") != null) {
			user.setBusiness(paraMap.get("business"));
		}
		if (paraMap.get("province") != null) {
			user.setProvince(paraMap.get("province"));
		}
		if (paraMap.get("city") != null) {
			user.setCity(paraMap.get("city"));
		}
		if (paraMap.get("address") != null) {
			user.setAddress(paraMap.get("address"));
		}
		if (paraMap.get("url") != null) {
			user.setUrl(paraMap.get("url"));
		}
		if (paraMap.get("phone") != null) {
			user.setPhone(paraMap.get("phone"));
		}
		if (paraMap.get("person") != null) {
			user.setPerson(paraMap.get("person"));
		}
		if (paraMap.get("email") != null) {
			user.setEmail(paraMap.get("email"));
		}
		if (paraMap.get("company") != null) {
			user.setCompany(paraMap.get("company"));
		}
		if (paraMap.get("position") != null) {
			user.setPosition(paraMap.get("position"));
		}
		if (StringUtils.isEmpty(paraMap.get("waf"))
				&& StringUtils.isEmpty(paraMap.get("app"))
				&& StringUtils.isEmpty(paraMap.get("site"))
				&& StringUtils.isEmpty(paraMap.get("expert"))) {

		} else {
			boolean flag = false;
			SafeTrial st = new SafeTrial();			
			st.setUserId(user.getId());
			if (!StringUtils.isEmpty(paraMap.get("waf"))) {
				st.setWaf((short) 1);
				flag = true;
			}
			if (!StringUtils.isEmpty(paraMap.get("app"))) {
				st.setApp((short) 1);
				flag = true;
			}
			if (!StringUtils.isEmpty(paraMap.get("site"))) {
				st.setSite((short) 1);
				flag = true;
			}
			if (!StringUtils.isEmpty(paraMap.get("expert"))) {
				st.setExpert((short) 1);
				flag = true;
			}
			if(flag){
				safeTrialMapper.insertSafeTrial(st);
			}else{
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("至少选择一个产品！");
				return result;
			}
		}
		userMapper.updUser(user);

		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("修改成功");

		return result;
	}

	@Override
	public Map<String, Object> getInfoById(String userId) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getInfoById(userId);
	}

	@Override
	public void delUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		userMapper.delUser(userId);
	}

	@Override
	public Map<String, Object> getInfoByAdmin(String userId) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getInfoByAdmin(userId);
	}

	@Override
	public Result validateCode(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		List<Map<String, Object>> map = codeMapper.getCode(paraMap);
		if (map == null || map.size() == 0) {
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("无效的邀请码");
			return result;
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("有效的邀请码");
		return result;
	}

	@Override
	public List<User> getAllAdminUser() throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getAllAdminUser();
	}

	@Override
	public Result getAllLimitAdminUser(Map<String, Object> paraMap)
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
		if (!ObjectUtil.isEmpty(paraMap, "user_name")) {
			paraMap.put("user_name", "%"
					+ paraMap.get("user_name").toString() + "%");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total_num", userMapper.countAllAdmin(paraMap));
		map.put("detail", userMapper.getAllLimitAdminUser(paraMap));
		result.setData(map);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");

		return result;
	}

	

	@Override
	public Result getAllDetailsByAdmin() throws Exception {
		Result result = new Result();
		// 获取所有分组信息
		List<Map<String, Object>> groups = userGroupMapper.getAllGroupDetail();
		// 获取所有资源信息
		List<Resource> resources = resourceMapper.getAllResource();

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("group_detail", groups);
		paraMap.put("resource_detail", resources);
		result.setData(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");

		return result;
	}

	@Override
	public Result getAllDetailsByAdminId(Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		if (ObjectUtil.isEmpty(map, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		// 获取后台管理员信息
		User user = userMapper.getUserById(map.get("user_id").toString());
		// 获取个人所有分组信息
		List<Map<String, Object>> groups = userGroupMapper
				.getAllGroupDetailByUserId(map.get("user_id").toString());
		// 获取个人所有资源信息
		List<Resource> resources = resourceMapper.getAllResourceByUserId(map
				.get("user_id").toString());
		// 获取所有的资源信息
		List<Resource> allresources = resourceMapper.getAllResource();

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("user_detail", user);
		paraMap.put("group_detail", groups);
		paraMap.put("resource_detail", resources);
		paraMap.put("all_resource_detail", allresources);
		result.setData(paraMap);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("查询成功");

		return result;
	}

	@Override
	public Result deleteAdminDetailsByAdminId(Map<String, Object> map,
			Result result) throws Exception {
		if (ObjectUtil.isEmpty(map, "user_id")) {
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("参数错误");
			return result;
		}
		// 删除用户信息
		userMapper.delUser(map.get("user_id").toString());
		// 删除用户组信息
		userGroupMapper.deleteAllGroupByUserId(map.get("user_id").toString());
		// 删除用户权限信息
		authorityMapper.deleteAuthorityByUser(map.get("user_id").toString());
		
        result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("删除成功");
		return result;
	}
}
