package com.uway.mobile.controller;

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
import com.uway.mobile.domain.User;
import com.uway.mobile.service.IndustryService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.EncryptUtils;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.SignUtil;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	public UserService userService;
	@Autowired
	public IndustryService industryService;
	@Autowired
	public RedisUtil redisUtil;
	
	/**
	 * 验证用户是否存在
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/validate_user", method = RequestMethod.POST)
	public Result validateUser(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "user_name")){
				result.setMsg("用户名不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String userName = paraMap.get("user_name").toString();
			List<User> listUser = userService.getUserByName(userName);
			if (listUser != null && listUser.size() > 0) {
				result.setMsg("用户已存在！");
				result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
				return result;
			}
			
		}catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/validate_code", method = RequestMethod.POST)
	public Result validateCode(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		if(ObjectUtil.isEmpty(paraMap, "code")){
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("邀请码为空");
			return result;
		}
		try{
		    result = userService.validateCode(paraMap);
		}catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 用户注册
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Result register(HttpServletRequest request, @RequestBody Map<String, String> paraMap) {
		Result result = new Result();
		try {
			String userName = paraMap.get("user_name");
			String password = EncryptUtils.decryption(paraMap.get("password"),EncryptUtils.desKey);
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
			if (StringUtils.isEmpty(paraMap.get("ver_code"))) {
				result.setMsg("验证码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if(!StringUtils.isEmpty(paraMap.get("uid"))){
				Object obj = redisUtil.get("ver:images:" + paraMap.get("uid").toString());
				if(obj == null || "".equals(obj.toString())){
					result.setMsg("请刷新验证码！");
					result.setCode(Constance.RESPONSE_PARAM_ERROR);
					return result;
				}else{
					if(!paraMap.get("ver_code").toLowerCase().equals(obj.toString().toLowerCase())){
						result.setMsg("验证码不正确！");
						result.setCode(Constance.RESPONSE_PARAM_ERROR);
						return result;
					}
				}
			}else{
				result.setMsg("参数错误！");
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				return result;
			}
			
			List<User> listUser = userService.getUserByName(userName);
			if (listUser != null && listUser.size() > 0) {
				result.setMsg("用户已存在！");
				result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
				return result;
			}
			
			result = userService.insertUser(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 登陆接口
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(HttpServletRequest request, @RequestBody Map<String, String> paraMap)
			throws IOException, SQLException {
		Result result = new Result();
		try {
			HttpSession session = request.getSession();
			String userName = paraMap.get("user_name");
			String password = paraMap.get("password").toString();
			try{
				password = EncryptUtils.decryption(password,EncryptUtils.desKey);
			}catch(Exception e){
				result.setMsg("用户名或密码错误！");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			}
			//String loginIp = request.getRemoteAddr();
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
			
			/*String redisIpKey = Constance.REDIS_LOGIN_IP+loginIp;
			int loginTimes = 0;
			if (redisUtil.get(redisIpKey)!=null) {
				loginTimes = Integer.parseInt(redisUtil.get(redisIpKey).toString());
				if(loginTimes >=3){//登录超过3次就要做验证码验证
					if(StringUtils.isEmpty(paraMap.get("ver_code"))){
						result.setMsg("验证码不能为空！");
						result.setCode(Constance.RESPONSE_PARAM_EMPTY);
						return result;
					}else{
						Object obj = redisUtil.get("ver:images:" + paraMap.get("uid").toString());
						if(obj == null || "".equals(obj.toString())){
							result.setMsg("请刷新验证码！");
							result.setCode(Constance.RESPONSE_PARAM_ERROR);
							return result;
						}else{
							if(!paraMap.get("ver_code").toLowerCase().equals(obj.toString().toLowerCase())){
								result.setMsg("验证码不正确！");
								result.setCode(Constance.RESPONSE_PARAM_ERROR);
								return result;
							}
						}
					}
				}
			}
			loginTimes = loginTimes + 1;
			redisUtil.set(redisIpKey, loginTimes, Long.valueOf(10 * 60));*/
			
			List<User> listUser = userService.getUserByName(userName);
			if (listUser == null || listUser.size() <= 0) {
				result.setMsg("用户名或密码错误！");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			} else {
				User user = listUser.get(0);
				// 查看是否已锁定
				int errorTimes = 0;
				Map<String, Object> mapInfo = new HashMap<String, Object>();
				if(redisUtil.get(Constance.REDIS_USER_INFO + user.getId()) == null){
					mapInfo.put("error_times", "" + errorTimes);
					redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo, Long.valueOf(30 * 60));
				}else{
					mapInfo = (Map<String, Object>) redisUtil.get(Constance.REDIS_USER_INFO + user.getId());
					errorTimes = Integer.parseInt(mapInfo.get("error_times").toString());
					if(errorTimes >3 ){//密码错误超过3次就要做验证码验证
						if(StringUtils.isEmpty(paraMap.get("ver_code"))){
							result.setMsg("验证码不能为空！");
							result.setCode(Constance.RESPONSE_PARAM_EMPTY);
							return result;
						}else{
							Object obj = redisUtil.get("ver:images:" + paraMap.get("uid").toString());
							if(obj == null || "".equals(obj.toString())){
								result.setMsg("请刷新验证码！");
								result.setCode(Constance.RESPONSE_PARAM_ERROR);
								return result;
							}else{
								if(!paraMap.get("ver_code").toLowerCase().equals(obj.toString().toLowerCase())){
									result.setMsg("验证码不正确！");
									result.setCode(Constance.RESPONSE_PARAM_ERROR);
									return result;
								}
							}
						}
					}
					if(errorTimes >= 6){
						result.setMsg("密码输入次数过多，冻结半小时！");
						result.setCode(Constance.RESPONSE_USER_FROZEN);
						return result;
					}
				}
				
				String fullPwd = Constance.KEY + userName + password;
				if (user.getPassword().equals(DigestUtils.md5DigestAsHex(fullPwd.getBytes()))) {
					result.setMsg("登陆成功！");
					result.setCode(Constance.RESPONSE_SUCCESS);

					session.setAttribute(session.getId(), user.getId());
					// 过期时间为一天
					redisUtil.set(Constance.REDIS_USER_PRE + session.getId(), "" + user.getId(), Long.valueOf(10 * 60));

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("sid", session.getId());
					map.put("userName", user.getUserName());
					result.setData(map);
					redisUtil.remove(Constance.REDIS_USER_INFO + user.getId());
					return result;
				} else {
					mapInfo.put("error_times", "" + (errorTimes + 1));
					redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo, Long.valueOf(30 * 60));		
					result.setData(mapInfo);
					result.setMsg("用户名或密码错误！");
					result.setCode(Constance.RESPONSE_PWD_ERROR);
					return result;
				}
			}
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
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
			String sessionId = Constance.REDIS_USER_PRE + request.getHeader("sid");
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
	 * 修改密码接口
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "upd_pwd", method = RequestMethod.POST)
	public Result updatePassword(HttpServletRequest request, @RequestBody Map<String, Object> paraMap)
			throws Exception {
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
			String password = EncryptUtils.decryption(paraMap.get("password").toString(),EncryptUtils.desKey);
			String changePwd = EncryptUtils.decryption(paraMap.get("change_pwd").toString(),EncryptUtils.desKey);

			String userId = request.getAttribute("userId").toString();
			if (StringUtils.isEmpty(userId)) {
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("请登陆！");
				return result;
			}
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
	 * 查看用户所有信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view_user", method = RequestMethod.POST)
	public Result viewUser(HttpServletRequest request){
		Result result = new Result();
		String userId = request.getAttribute("userId").toString();
		try {
			Map<String, Object> map = userService.getInfoById(userId);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setData(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "upd_user", method = RequestMethod.POST)
	public Result doUpdUser(HttpServletRequest request, @RequestBody Map<String, String> paraMap){
		Result result = new Result();
		String userId = request.getAttribute("userId").toString();
		try {
			User user = userService.getUserById(userId);
			if(user==null){
				result.setCode(Constance.RESPONSE_USER_ERROR);
				result.setMsg("请登录");
				return result;
			}
			if(paraMap.get("industry")!=null&&paraMap.get("industry")!=""){
				
				Pattern pattern = Pattern.compile("[0-9]*"); 
				   Matcher isNum = pattern.matcher(paraMap.get("industry"));
				   if( !isNum.matches() ){
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
	
	@RequestMapping(value = "test")
	public Result test() throws Exception{
		Result result = new Result();
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("测试成功！");
		return result;
	}
	
	@RequestMapping(value = "getDesKey")
	public Result getDesKey(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		long time = SignUtil.getTime();
		map.put("desKey", EncryptUtils.desKey);
		map.put("time", time);
		String loginIp = request.getRemoteAddr();
		String redisIpKey = Constance.REDIS_LOGIN_IP+loginIp;
		int loginTimes = 0;
		if (redisUtil.get(redisIpKey)!=null) {
			loginTimes = Integer.parseInt(redisUtil.get(redisIpKey).toString());
		}
		map.put("repeat_times", loginTimes);
		Result result = new Result();
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("返回成功！");
		result.setData(map);
		return result;
	}
	
	/**
	 * 前端页面登录时是否需要显示验证码
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value = "isDisplayValidateCode")
	public Result isDisplayValidateCode(HttpServletRequest request){
		Result result = new Result();
		try{
			String loginIp = request.getRemoteAddr();
			String key = Constance.REDIS_LOGIN_IP+loginIp;
			if(redisUtil.get(key)!=null){
				int loginTimes = Integer.parseInt(redisUtil.get(key).toString());
				if(loginTimes >=3){
					result.setData(1);
				}else{
					result.setData(0);
				}
			}else{
				result.setData(0);
			}
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("返回成功！");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}*/
	
	
}
