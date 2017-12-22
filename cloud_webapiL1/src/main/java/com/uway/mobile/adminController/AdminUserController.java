package com.uway.mobile.adminController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.IndustryService;
import com.uway.mobile.service.ResourceService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.EncryptUtils;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;

@RestController
@RequestMapping("admin_user")
public class AdminUserController {
	@Autowired
	public UserService userService;
	@Autowired
	private IndustryService industryService;
	@Autowired
	public RedisUtil redisUtil;
	@Autowired
    public ResourceService resourceService;

	/**
	 * 登陆接口
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(HttpServletRequest request,
			@RequestBody Map<String, String> paraMap) throws IOException,
			SQLException {
		Result result = new Result();
		try {
			HttpSession session = request.getSession();
			String userName = paraMap.get("user_name");
			String password = paraMap.get("password");
			if (StringUtils.isEmpty(userName)) {
				result.setMsg("用户名不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (StringUtils.isEmpty(password)) {
				result.setMsg("密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			// des解密
			try {
				password = EncryptUtils.decryption(password, EncryptUtils.desKey);
			} catch (Exception e) {
				result.setMsg("用户或密码错误!");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			}
			List<User> listUser = userService.getAdminByName(userName);
			if (listUser == null || listUser.size() <= 0) {
				result.setMsg("用户名或密码错误！");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			} else {
				User user = listUser.get(0);
				// 查看是否已锁定
				int errorTimes = 1;
				Map<String, Object> mapInfo = new HashMap<String, Object>();
				if (redisUtil.get(Constance.REDIS_USER_INFO + user.getId()) == null) {
					mapInfo.put("error_times", errorTimes);
					redisUtil.set(Constance.REDIS_USER_INFO + user.getId(),
							mapInfo, Long.valueOf(30 * 60));
				} else {
					mapInfo = (Map<String, Object>) redisUtil
							.get(Constance.REDIS_USER_INFO + user.getId());
					errorTimes = Integer.parseInt(mapInfo.get("error_times")
							.toString());
					if (errorTimes >= 6) {
						result.setMsg("密码输入次数过多，冻结半小时！");
						result.setCode(Constance.RESPONSE_USER_FROZEN);
						return result;
					} else {
						mapInfo.put("error_times", errorTimes + 1);
						redisUtil.set(Constance.REDIS_USER_INFO + user.getId(),
								mapInfo, Long.valueOf(30 * 60));
					}
				}
				// 定义登录人的登录次数
				int repeatTimes = 1;
				// 获取登录者用户的ip地址
				// 锁定同一ip地址 重复登录的用户
				String ip = request.getRemoteAddr();
				result = checkRepeatCountByIp(ip, repeatTimes, result, paraMap);
				if (StringUtils.isNotEmpty(result.getMsg())) {
					return result;
				}
				String fullPwd = Constance.KEY + userName + password;
				if (user.getPassword().equals(
						DigestUtils.md5DigestAsHex(fullPwd.getBytes()))) {
					List<Resource> resources = resourceService.getAllAuthorityByUser(user.getId()+"");
					//List<Resource> resources = resourceMapper.getAllResource();
					if(resources == null){
						result.setMsg("该管理员暂未分配权限组或权限！");
						result.setCode(Constance.RESPONSE_AUTH_ERROR);
						return result;
						
					}
					result.setMsg("登陆成功！");
					result.setCode(Constance.RESPONSE_SUCCESS);
					// 过期时间为一天
					redisUtil.set(Constance.REDIS_USER_PRE + session.getId(),
							user.getId(), Long.valueOf(10 * 60));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("sid", session.getId());
					map.put("user_id", user.getId());
					map.put("userName", user.getUserName());
					//返回用户所有权限
				    map.put("authority", resources);
					result.setData(map);
					redisUtil.remove(Constance.REDIS_USER_INFO + user.getId());
					return result;
				} else {
					result.setMsg("密码错误！");
					result.setCode(Constance.RESPONSE_PWD_ERROR);
					return result;
				}
			}
			/*
			 * String fullPwd = Constance.KEY + userName + password; if
			 * (user.getPassword().equals(
			 * DigestUtils.md5DigestAsHex(fullPwd.getBytes()))) {
			 * result.setMsg("登陆成功！");
			 * result.setCode(Constance.RESPONSE_SUCCESS);
			 * 
			 * // session.setAttribute(session.getId(), user.getId()); //
			 * 过期时间为一天 redisUtil.set(Constance.REDIS_USER_PRE + session.getId(),
			 * user.getId(), Long.valueOf(24 * 60 * 60));
			 * 
			 * Map<String, Object> map = new HashMap<String, Object>();
			 * map.put("sid", session.getId()); map.put("user_id",
			 * user.getId()); map.put("userName", user.getUserName());
			 * result.setData(map); redisUtil.remove(Constance.REDIS_USER_INFO +
			 * user.getId()); return result; } else {
			 * result.setMsg("用户名或密码错误！");
			 * result.setCode(Constance.RESPONSE_PWD_ERROR); return result; } }
			 */
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 查询相应的用户
	 * 
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/list_user", method = RequestMethod.POST)
	public Result listUser(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "page_num")) {
				result.setMsg("页码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String pageNum = paraMap.get("page_num").toString();
			String pageSize = "" + Constance.PAGE_SIZE;
			if (!ObjectUtil.isEmpty(paraMap, "page_size")) {
				pageSize = paraMap.get("page_size").toString();
			}
			paraMap.put(
					"pageNum",
					(Integer.parseInt(pageNum) - 1)
							* Integer.parseInt(pageSize));
			paraMap.put("pageSize", Integer.parseInt(pageSize));
			if (Integer.parseInt(paraMap.get("pageNum").toString()) < 0
					|| Integer.parseInt(paraMap.get("pageSize").toString()) <= 0) {
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("参数格式不正确！");
				return result;
			}
			return userService.listUser(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 通过userId查用户详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/view_user", method = RequestMethod.POST)
	public Result viewUser(HttpServletRequest request,
			@RequestBody Map<String, String> paraMap) {
		Result result = new Result();
		try {
			if (StringUtils.isEmpty(paraMap.get("userId"))) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("请传userId");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", userService.getInfoByAdmin(paraMap.get("userId")));
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(map);
		} catch (Exception e) {

			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用户信息修改
	 * 
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "upd_user", method = RequestMethod.POST)
	public Result updUser(HttpServletRequest request,
			@RequestBody Map<String, String> paraMap) {
		Result result = new Result();
		try {
			if (StringUtils.isEmpty(paraMap.get("user_id"))) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("用户ID不能为空！");
				return result;
			}
			User user = userService.getUserById(paraMap.get("user_id")
					.toString());
			if (paraMap.get("industry") != null
					&& paraMap.get("industry") != "") {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(paraMap.get("industry"));
				if (!isNum.matches()) {
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					result.setMsg("参数格式错误");
					return result;
				}
			}
			result = userService.updUser(user, paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取行业列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/industry_list")
	public Result getIndustryList() {
		Result result = new Result();
		try {
			result.setData(industryService.getIndustry());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功！");
		} catch (Exception e) {
			// TODO: handle exception
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 修改管理员自己的密码
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "upd_admin_pwd", method = RequestMethod.POST)
	public Result updAdminPwd(HttpServletRequest request,
			@RequestBody Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		try {
			if (paraMap.get("password") == null) {
				result.setMsg("原密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (paraMap.get("change_pwd") == null) {
				result.setMsg("新密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String password = paraMap.get("password").toString();
			String changePwd = paraMap.get("change_pwd").toString();

			String userId = request.getAttribute("userId").toString();
			if (StringUtils.isEmpty(password)) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("原密码不能为空！");
				return result;
			}
			if (StringUtils.isEmpty(changePwd)) {
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setMsg("新密码不能为空！");
				return result;
			}
			User user = userService.getUserById(userId);
			if (user == null) {
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("用户不存在！");
				return result;
			}

			String oldPwd = Constance.KEY + user.getUserName() + password;
			String newPwd = Constance.KEY + user.getUserName() + changePwd;
			if (DigestUtils.md5DigestAsHex(oldPwd.getBytes()).equals(
					user.getPassword())) {
				user.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
				userService.updPwd(user);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("修改成功！");
			} else {
				result.setCode(Constance.RESPONSE_PWD_ERROR);
				result.setMsg("密码错误！");
			}
			return result;
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("出错啦！");
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 修改用户的密码
	 * 
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "upd_user_pwd", method = RequestMethod.POST)
	public Result updUserPwd(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (StringUtils.isEmpty(paraMap.get("password").toString())) {
				result.setMsg("密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (StringUtils.isEmpty(paraMap.get("user_id").toString())) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			User user = userService.getUserById(paraMap.get("user_id")
					.toString());
			String newPwd = Constance.KEY + user.getUserName()
					+ paraMap.get("password").toString();
			user.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
			userService.updPwd(user);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("修改成功！");
			return result;
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("出错啦！");
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 删除用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "del_user", method = RequestMethod.POST)
	public Result delUser(@RequestBody Map<String, Object> paraMap)
			throws Exception {
		Result result = new Result();
		try {
			if (ObjectUtil.isEmpty(paraMap, "user_id")) {
				result.setMsg("用户ID不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			userService.delUser(paraMap.get("user_id").toString());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("删除成功！");
			return result;
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("出错啦！");
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @param paraMap
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/login_out", method = RequestMethod.POST)
	public Result loginOut() throws IOException, SQLException {
		Result result = new Result();
		try {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes sra = (ServletRequestAttributes) ra;
			HttpServletRequest request = sra.getRequest();
			String sessionId = Constance.REDIS_USER_PRE
					+ request.getHeader("sid");
			if (redisUtil.get(sessionId) == null) {
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("用户不存在！");
				return result;
			}
			redisUtil.remove(sessionId);
			result.setMsg("登出成功！");
			result.setCode(Constance.RESPONSE_SUCCESS);
			return result;
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 根据ip检查重复的次数
	 * 
	 * @param ip
	 * @param mapInfo
	 * @param repeatTimes
	 * @param result
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Result checkRepeatCountByIp(String ip, int repeatTimes,
			Result result, Map<String, String> paraMap) {
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		if (redisUtil.get(Constance.REDIS_USER_INFO + ip) == null) {
			// 重复登录次数
			mapInfo.put("repeat_times", "" + repeatTimes);
			// 将该ip地址缓存10分钟
			redisUtil.set(Constance.REDIS_USER_INFO + ip, mapInfo,
					Long.valueOf(10 * 60));
		} else {
			mapInfo = (Map<String, Object>) redisUtil
					.get(Constance.REDIS_USER_INFO + ip);
			repeatTimes = Integer.parseInt(mapInfo.get("repeat_times")
					.toString());
			if (repeatTimes >= 3) {
				// 验证码检查校验
				result = checkVerifyCode(result, paraMap);
				if (StringUtils.isNotEmpty(result.getMsg())) {
					return result;
				}
			} else {
				mapInfo.put("repeat_times", "" + (repeatTimes + 1));
				redisUtil.set(Constance.REDIS_USER_INFO + ip, mapInfo,
						Long.valueOf(10 * 60));
			}
		}
		return result;
	}

	/**
	 * 
	 * 验证码检查
	 * 
	 * @return
	 */
	private Result checkVerifyCode(Result result, Map<String, String> paraMap) {
		if (!StringUtils.isEmpty(paraMap.get("uid"))) {
			if (paraMap.get("verifyCode") == null
					|| paraMap.get("verifyCode").toString().equals("")) {
				result.setMsg("验证码不得为空！");
				result.setCode(Constance.RESPONSE_USER_VERIFICATION_ERROR);
				return result;
			}
			Object obj = redisUtil.get("ver:images:"
					+ paraMap.get("uid").toString());
			if (obj == null || "".equals(obj.toString())) {
				result.setMsg("请刷新验证码！");
				result.setCode(Constance.RESPONSE_USER_VERIFICATION_ERROR);
				return result;
			} else {
				if (!paraMap.get("verifyCode").toLowerCase()
						.equals(obj.toString().toLowerCase())) {
					result.setMsg("验证码不正确！");
					result.setCode(Constance.RESPONSE_USER_VERIFICATION_ERROR);
					return result;
				}
			}
		} else {
			result.setMsg("参数错误！");
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			return result;
		}
		return result;

	}

	/**
	 * 统计错误登录的请求
	 * 
	 * @param result
	 * @param mapInfo
	 * @param errorTimes
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void checkErrorLoginRequest(int errorTimes, User user) {
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		if (redisUtil.get(Constance.REDIS_USER_INFO + user.getId()) == null) {
			mapInfo.put("error_times", "" + errorTimes);
			redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo,
					Long.valueOf(30 * 60));
		} else {
			mapInfo = (Map<String, Object>) redisUtil
					.get(Constance.REDIS_USER_INFO + user.getId());
			errorTimes = Integer
					.parseInt(mapInfo.get("error_times").toString());
			mapInfo.put("error_times", "" + (errorTimes + 1));
			redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo,
					Long.valueOf(30 * 60));
		}
	}

	/**
	 * 返回密钥和登录次数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getDesKey")
	public Result getDesKey(HttpServletRequest request) {
		Result result = new Result();
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		String ip = request.getRemoteAddr();
		int repeatTimes = 0;
		if (redisUtil.get(Constance.REDIS_USER_INFO + ip) == null) {
			mapInfo.put("repeat_times", "" + repeatTimes);
		} else {
			mapInfo = (Map<String, Object>) redisUtil
					.get(Constance.REDIS_USER_INFO + ip);
			repeatTimes = Integer.parseInt(mapInfo.get("repeat_times")
					.toString());
		}
		mapInfo.put("desKey", EncryptUtils.desKey);
		redisUtil.get(Constance.REDIS_USER_INFO + ip);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("返回成功");
		result.setData(mapInfo);
		return result;
	}

	@RequestMapping(value = "test")
	public Result test() throws Exception {
		Result result = new Result();
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("测试成功！");
		return result;
	}

	/**
	 * 获取所有的后台管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "get_all_adminuser", method = RequestMethod.POST)
	public Result getAllAdminUser() throws Exception {
		Result result = new Result();
		try {
			result.setData(userService.getAllAdminUser());
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");

		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 获取所有的后台管理员并分页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "get_all_limit_adminuser", method = RequestMethod.POST)
	public Result getAllLimitAdminUser(@RequestBody Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		try {
			return userService.getAllLimitAdminUser(map);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 添加后端管理员，并分组分配权限
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "create_adminuser", method = RequestMethod.POST)
	public Result createAndGiveAuthorityToAdmin(@RequestBody Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		try {
			
			return resourceService.creartAndSaveAdminDetail(map,result);
		
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	
	
	/**
	 * 修改后端管理员，并分组分配权限
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "update_adminuser", method = RequestMethod.POST)
	public Result updateAndGiveAuthorityToAdmin(@RequestBody Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		try {
			return resourceService.updateAndSaveAdminDetail(map,result);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	
	
	/**
	 * 获取所有管理员添加信息（组，资源）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "get_all_admindetail", method = RequestMethod.POST)
	public Result getAllAdminDetail()
			throws Exception {
		Result result = new Result();
		try {
			
			return userService.getAllDetailsByAdmin();
		
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	
	/**
	 * 根据id获取管理员信息（个人信息,组，资源）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "get_admin_detail", method = RequestMethod.POST)
	public Result getAdminDetailByUserId(@RequestBody Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		try {
			
			return userService.getAllDetailsByAdminId(map);
		
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	
	/**
	 * 根据id删除管理员信息（个人信息,组，资源）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delete_admin_detail", method = RequestMethod.POST)
	public Result deleteAdminDetailByUserId(@RequestBody Map<String, Object> map)
			throws Exception {
		Result result = new Result();
		try {
			
			return userService.deleteAdminDetailsByAdminId(map,result);
		
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
			return result;
		}
	}
	
	
	
	
}
