package com.uway.mobile.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Company;
import com.uway.mobile.domain.Department;
import com.uway.mobile.domain.DeviceCompany;
import com.uway.mobile.domain.User;
import com.uway.mobile.service.CompanyService;
import com.uway.mobile.service.DepartmentService;
import com.uway.mobile.service.DeviceService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.EncryptUtils;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.RedisUtil;
import com.uway.mobile.util.SignUtil;

@RestController
@RequestMapping("user")
@Api(value="用户controlle",tags={"用户操作接口"})
public class UserController {
	@Autowired
	public UserService userService;
	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private CompanyService companyService;
	
	/**
	 * 验证用户是否存在
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/validate_user", method = RequestMethod.POST)
	public Result validateUser(@RequestBody Map<String, Object> paraMap) {
		Result result = new Result();
		try{
			if(ObjectUtil.isEmpty(paraMap, "email")){
				result.setMsg("邮箱不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			String email = paraMap.get("email").toString();
			List<User> listUser = userService.getUserByEmail(email);
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

	/**
	 * 用户注册
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ApiOperation(value="用户注册:/user/register",notes="邮箱做为用户名",httpMethod="POST",response=Result.class)
	@ApiResponse(code=200,message="注册成功")
	public Result register(HttpServletRequest request, @RequestBody @ApiParam(name="paraMap",value="注册请求参数:{\"email\":\"邮箱\","
			+ "\"password\":\"密码\","
			+ "\"companyName\":\"公司名称\","
			+ "\"user_name\":\"昵称\"}",required=true)  Map<String, String> paraMap) {
		Result result = new Result();
		try {
			String email = paraMap.get("email");
			String password = EncryptUtils.decryption(paraMap.get("password"),EncryptUtils.desKey);
			if (StringUtils.isEmpty(email)) {
				result.setMsg("邮箱不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (StringUtils.isEmpty(password)) {
				result.setMsg("密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			//正则表达式的模式
			String rule_psd="(?![^a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,}$";
	        Pattern p = Pattern.compile(rule_psd);
	        //正则表达式的匹配器
	        Matcher m = p.matcher(password);
	        if(!m.matches()){
	        	result.setCode(Constance.RESPONSE_PARAM_ERROR);
	        	result.setMsg("密码必须为8位以上，包含字母、数字或特殊字符");
	        	return result;
	        }
			String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
			//正则表达式的模式
	        Pattern p1 = Pattern.compile(RULE_EMAIL);
	        //正则表达式的匹配器
	        Matcher m1 = p1.matcher(paraMap.get("email").toString());
	        if(!m1.matches()){
	        	result.setMsg("邮箱格式不正确！");
				result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
				return result;
	        }
			
			List<User> listUser = userService.getUserByEmail(paraMap.get("email").toString());
			List<Company> listCompany=companyService.getCompanyByName(paraMap.get("companyName").toString());
			if (listUser != null && listUser.size() > 0) {
				if(listUser.get(0).getStatus().equals("1")){
					result.setMsg("邮箱已被注册！");
					result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
					return result;
				}
			}
			if(listCompany.size()>0){
				result.setMsg("该企业已有管理员账号！");
				result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
				return result;
			}
			result = userService.insertAdminUser(paraMap);
		} catch (Exception e) {
			result.setMsg("内部错误！");
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 邮箱验证
	 */
	@ApiOperation(value="邮箱验证:/user/checkMail",httpMethod="GET")
	
	@RequestMapping(value="/checkMail",method=RequestMethod.GET)
	public Result checkMail(HttpServletRequest request){
		Result result = new Result();
		try {
			String action=request.getParameter("action");
			if(!action.equals("activate")){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("激活失败,请重新注册!");
				return result;
			}
			String email=request.getParameter("email");
			String validateCode=request.getParameter("validateCode");
			result=userService.checkMail(email,validateCode);
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
	@ApiOperation(value="登录接口:user/login",httpMethod="POST")
	@ApiResponse(code=200,message="登录成功")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(HttpServletRequest request, @RequestBody 
			@ApiParam(name="控制台页面登录",value="登录所需参数:{\"email\":\"邮箱\","
					+ "\"password\":\"密码\"}") Map<String, String> paraMap)
			throws IOException, SQLException {
		Result result = new Result();
		try {
 			HttpSession session = request.getSession();
			String email = paraMap.get("email");
			String password = EncryptUtils.decryption(paraMap.get("password"),EncryptUtils.desKey);
			String loginIp = request.getRemoteAddr();
			if (StringUtils.isEmpty(email)) {
				result.setMsg("邮箱不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			if (StringUtils.isEmpty(password)) {
				result.setMsg("密码不能为空！");
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				return result;
			}
			
			String redisIpKey = Constance.REDIS_LOGIN_IP+loginIp;
			int loginTimes = 0;
			if (redisUtil.get(redisIpKey)!=null) {
				loginTimes = Integer.parseInt(redisUtil.get(redisIpKey).toString());
				if(loginTimes >=300){
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
			redisUtil.set(redisIpKey, loginTimes, Long.valueOf(10 * 60));
			
			List<User> listUser = userService.getUserByEmail(email);
			if (listUser == null || listUser.size() <= 0) {
				result.setMsg("邮箱或密码错误！");
				result.setCode(Constance.RESPONSE_USER_ERROR);
				return result;
			} else {
				User user = listUser.get(0);
				// 查看是否已锁定
				int errorTimes = 1;
				Map<String, Object> mapInfo = new HashMap<String, Object>();
				if(redisUtil.get(Constance.REDIS_USER_INFO + user.getId()) == null){
					mapInfo.put("error_times", "" + errorTimes);
					redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo, Long.valueOf(30 * 60));
				}else{
					mapInfo = (Map<String, Object>) redisUtil.get(Constance.REDIS_USER_INFO + user.getId());
					errorTimes = Integer.parseInt(mapInfo.get("error_times").toString());
					if(errorTimes >= 100){
						result.setMsg("密码输入次数过多，冻结半小时！");
						result.setCode(Constance.RESPONSE_USER_FROZEN);
						return result;
					}else{
						mapInfo.put("error_times", "" + (errorTimes + 1));
						redisUtil.set(Constance.REDIS_USER_INFO + user.getId(), mapInfo, Long.valueOf(30 * 60));
					}
				}
				
				String fullPwd = Constance.KEY + email + password;
				if (user.getPassword().equals(DigestUtils.md5DigestAsHex(fullPwd.getBytes()))) {
					if(user.getStatus()==null||"".equals(user.getStatus().toString())||user.getStatus().equals("0")){
						result.setCode(Constance.RESPONSE_PARAM_EMPTY);
						result.setMsg("账户未激活，请前往邮箱进行激活");
						return result;
					}
					result.setMsg("登陆成功！");
					result.setCode(Constance.RESPONSE_SUCCESS);

					session.setAttribute(session.getId(), user.getId());
					// 过期时间为一天
					redisUtil.set(Constance.REDIS_USER_PRE + session.getId(), "" + user.getId(), Long.valueOf(10 * 60));
					Map<String, Object> map = new HashMap<String, Object>();
					List<DeviceCompany> deviceCompany = deviceService.getCompanyId(user.getId());
                    if(deviceCompany.size()>0){
                    	String deviceCompanyId=deviceCompany.get(0).getDeviceCompanyId();
                    	map.put("device_company_id", deviceCompanyId);
                    }else{
                    	map.put("device_company_id", user.getManufacturer());
                    }
					map.put("sid", session.getId());
					map.put("userName", user.getUserName());
					map.put("role", user.getRole());
					map.put("company", user.getCompanyName());
					map.put("companyId", user.getCompanyId());
					result.setData(map);
					//记录登录时间
					user.setLastLoginTime(new Date());
					userService.updateUser(user);
					redisUtil.remove(Constance.REDIS_USER_INFO + user.getId());
					return result;
				} else {
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
	@ApiOperation(value="登出接口:user/login_out",httpMethod="POST")
	@ApiResponse(code=200,message="登出成功")
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
	@ApiOperation(value="修改密码接口:/user/upd_pwd",httpMethod="POST")
	@ApiResponse(code=200,message="修改成功")
	@RequestMapping(value = "upd_pwd", method = RequestMethod.POST)
	public Result updatePassword(HttpServletRequest request, @RequestBody 
			@ApiParam(name="修改密码",value="修改密码参数:{\"password\":\"密码\","
					+ "\"change_pwd\":\"新密码\"}") Map<String, Object> paraMap)
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
			//正则表达式的模式
			String rule_psd="(?![^a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,}$";
	        Pattern p = Pattern.compile(rule_psd);
	        //正则表达式的匹配器
	        Matcher m = p.matcher(changePwd);
	        if(!m.matches()){
	        	result.setCode(Constance.RESPONSE_PARAM_ERROR);
	        	result.setMsg("密码必须为8位以上，包含字母、数字或特殊字符");
	        	return result;
	        }

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

			String oldPwd = Constance.KEY + user.getEmail() + password;
			String newPwd = Constance.KEY + user.getEmail() + changePwd;
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

	
	@RequestMapping(value = "test")
	public Result test() throws Exception{
		Result result = new Result();
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("测试成功！");
		return result;
	}
	
	@RequestMapping(value = "getDesKey")
	public Result getDesKey(){
		Map<String, Object> map = new HashMap<String, Object>();
		long time = SignUtil.getTime();
		map.put("desKey", EncryptUtils.desKey);
		map.put("time", time);
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
	@RequestMapping(value = "isDisplayValidateCode")
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
	}
	
	/**
	 * 用户导入
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "importUsers")
	public Result importUsers(HttpServletRequest request
			,@RequestParam(value = "file", required = true) MultipartFile file){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User user = userService.getUserById(userId);
			result=userService.importUsers(file,user);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据条件查询用户
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "findUsersByCondition")
	public Result findUsersByCondition(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User user = userService.getUserById(userId);
			if(user!=null){
				paraMap.put("companyId", user.getCompanyId());
				List<User> userList = userService.findUsersByCondition(paraMap);
				result.setData(userList);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("返回成功！");
			}else{
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("用户不存在");
			}
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 新增分组
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "addDepartment")
	public Result addDepartment(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			paraMap.put("userId", userId);
			result=departmentService.insertDepartment(paraMap);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 跳转到分组编辑页
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "toEditDepartment")
	public Result toEditDepartment(HttpServletRequest request,@RequestBody Map<String, String> paraMap){
		Result result = new Result();
		try{
			String departmentId = paraMap.get("departmentId");
			Department department = departmentService.findDepartmentById(Integer.parseInt(departmentId));
			result.setData(department);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("编辑成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 编辑分组
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "editDepartment")
	public Result editDepartment(HttpServletRequest request,@RequestBody Map<String, Object> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User user = userService.getUserById(userId);
			String departmentId = paraMap.get("departmentId").toString();
			String departName = paraMap.get("departName").toString();
			List<Integer> userIds = (List<Integer>)paraMap.get("userIds");
			Department department = new Department();
			department.setId(Integer.parseInt(departmentId));
			department.setName(departName);
			department.setCompanyId(user.getCompanyId());
			departmentService.updateDepartment(department, userIds);
			
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("编辑成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 移除分组
	 * @param request
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "deleteDepartment")
	public Result deleteDepartment(HttpServletRequest request,@RequestBody Map<String, String> paraMap){
		Result result = new Result();
		try{
			String departmentId = paraMap.get("departmentId");
			departmentService.deleteDepartment(Integer.parseInt(departmentId));
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("移除成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询分组列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findDepartments")
	public Result findDepartments(HttpServletRequest request){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User user = userService.getUserById(userId);
			List<Department> departments = departmentService.getDepartmentsByCompId(user.getCompanyId());
			result.setData(departments);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询所有的分组及其用户，没有被分组的用户归到"其他"组
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findDepartmentUsers")
	public Result findDepartmentUsers(HttpServletRequest request){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User user = userService.getUserById(userId);
			List<Department> departments = departmentService.getDepartmentUsers(user.getCompanyId());
			result.setData(departments);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("查询成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("内部错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除组员
	 */
	@RequestMapping(value="/delUserInDepartment")
	public Result delUserInDepartment(HttpServletRequest request,@RequestBody Map<String,String> paraMap){
		Result result = new Result();
		try{
			if(!paraMap.containsKey("userId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setData("");
				result.setMsg("参数为空");
				return result;
			}
			String userId = paraMap.get("userId");
			String msg = userService.deleteUserInDepartment(userId);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg(msg);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("更新错误");
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 新增用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "addUser")
	public Result addUser(HttpServletRequest request,@RequestBody Map<String,Object> paraMap){
		Result result = new Result();
		try{
			String userId = request.getAttribute("userId").toString();
			User adminUser = userService.getUserById(userId);
			if(adminUser.getRole()!=1){
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("无权进行该操作");
				return result;
			}
			paraMap.put("companyId", adminUser.getCompanyId());
			result=userService.insertUser(paraMap,adminUser);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("新增错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 更新用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "updUser")
	public Result updUser(HttpServletRequest request,@RequestBody User user){
		Result result = new Result();
		try{
			userService.updateUser(user);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("更新成功");
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("更新错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据用户用户ID删除用户
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteUser")
	public Result deleteUser(HttpServletRequest request,@RequestBody Map<String,String> paraMap){
		Result result = new Result();
		try{
			if(!paraMap.containsKey("userId")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setData("");
				result.setMsg("参数为空");
				return result;
			}
			String userId = paraMap.get("userId");
			User user=userService.getUserById(userId);
			if(user.getRole()==1){
				result.setCode(Constance.RESPONSE_PARAM_ERROR);
				result.setMsg("管理员不能删除管理员用户");
				return result;
			}
			String msg = userService.deleteUser(userId);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg(msg);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("更新错误");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "getUsersByRoleWithPage")
	public Result getUsersByRoleWithPage(HttpServletRequest request,@RequestBody Map<String,String> param){
		Result result = new Result();
		try{
			if(param.get("pageNum")==null){
				param.put("pageNum", "1");
			}
			if(param.get("pageSize")==null){
				param.put("pageSize", "10");
			}
			String userId = param.get("userId");
			String msg = userService.deleteUser(userId);
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg(msg);
		}catch(Exception e){
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("更新错误");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 密码重置
	 */
	@RequestMapping("/rePWD")
	public Result rePWD(@RequestBody Map<String,String> paraMap){
		Result result = new Result();
		try {
			if(!paraMap.containsKey("email")){
				result.setCode(Constance.RESPONSE_PARAM_EMPTY);
				result.setData("");
				result.setMsg("参数为空");
				return result;
			}
			result=userService.rePWD(paraMap);
		} catch (Exception e) {
			result.setCode(Constance.RESPONSE_INNER_ERROR);
			result.setMsg("更新错误");
			e.printStackTrace();
		}
		return result;
	}
	
}
