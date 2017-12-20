package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.config.JedisClusterConfig;
import com.uway.mobile.domain.Company;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.Resource;
import com.uway.mobile.domain.User;
import com.uway.mobile.mapper.AuthorityMapper;
import com.uway.mobile.mapper.CompanyMapper;
import com.uway.mobile.mapper.DeviceMapper;
import com.uway.mobile.mapper.ResourceMapper;
import com.uway.mobile.mapper.UserGroupMapper;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.DeviceConfigService;
import com.uway.mobile.service.UserService;
import com.uway.mobile.util.EncryptUtils;
import com.uway.mobile.util.ExcelUtil;
import com.uway.mobile.util.ObjectUtil;
import com.uway.mobile.util.Pagination;
import com.uway.mobile.util.PagingUtil;
import com.uway.mobile.util.SendEmailUtil;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AuthorityMapper authorityMapper;
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private SendEmailUtil sendMailUtil;
	@Autowired
	public JedisClusterConfig jedis;
	@Autowired
	private DeviceConfigService dcs;
	@Value("${admin.email}")
	private String email;
	@Value("${site.safe.email}")
	private String ip;

	@Override
	public List<User> getUserByEmail(String email) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("email", email);
		return userMapper.getUserByEmail(paraMap);
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

	@SuppressWarnings("static-access")
	@Override
	public Result insertAdminUser(Map<String, String> paraMap) throws Exception {
		Result result = new Result();
		User user = new User();

		String fullpwd = Constance.KEY
				+ paraMap.get("email")
				+ EncryptUtils.decryption(paraMap.get("password"),
						EncryptUtils.desKey);
		String md5pwd = DigestUtils.md5DigestAsHex(fullpwd.getBytes());
		//生成uuid
		String uuid=UUID.randomUUID().toString().replace("-", "");
		Company company=new Company();
		company.setCompanyId(uuid);
		company.setCompanyName(paraMap.get("companyName").toString());
		companyMapper.insertCompany(company);
		
		user.setUserName(paraMap.get("user_name"));
		user.setPassword(md5pwd);
		user.setEmail(paraMap.get("email"));
		user.setRole((short)1);
		user.setCompanyId(company.getId());
		user.setCompanyName(paraMap.get("companyName").toString());
		user.setStatus("0");
		user.setManufacturer("2");
		
		userMapper.insertUser(user);
		//发送验证邮件
		String validateCode=DigestUtils.md5DigestAsHex(user.getEmail().getBytes());
		Session session=sendMailUtil.SimpleEmail();
		String content=paraMap.get("email").toString()+"您好，请点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>"
                + "<a href=\"http://"+ip+":8080/dist/#/active?action=activate&email="+paraMap.get("email").toString()+"&validateCode="+validateCode
                + "\">http://"+ip+":8080/dist/#/active?action=activate&email="+paraMap.get("email").toString()+"&validateCode="+validateCode
                + "</a>";
		MimeMessage message=sendMailUtil.createMimeMessage(session, email, paraMap.get("email").toString(), paraMap.get("email").toString(), content);
		sendMailUtil.sendMail(session, email, message);
		jedis.jedisConnectionFactory().getConnection().setEx(("validateCode"+user.getEmail()).getBytes(), 1*60*60*24*2, validateCode.getBytes());
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("注册成功,请先激活");
		return result;
	}
	
	@SuppressWarnings("unused")
	@Override
	public Result checkMail(String email, String validateCode) throws Exception {
		Result result = new Result();
		String code=new String(jedis.jedisConnectionFactory().getConnection().get(("validateCode"+email).getBytes()));
		if(code!=null||""!=code){
			if(validateCode.equals(code)){
				userMapper.updateStatus(email);
				result.setCode(Constance.RESPONSE_SUCCESS);
				result.setMsg("已激活");
			}else{
				result.setCode(Constance.RESPONSE_INNER_ERROR);
				result.setMsg("激活失败,请重新注册");
				return result;
			}
		}else{
			result.setCode(Constance.RESPONSE_PARAM_EMPTY);
			result.setMsg("激活邮件已过期");
		}
		return result;
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

	@Override
	public Result importUsers(MultipartFile file,User admin) throws Exception {
		Result result = new Result();
		String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		//正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
		String[] strCol = {"email"};
		String resStr = ExcelUtil.exportListFromExcel(file.getInputStream(),FilenameUtils.getExtension(file.getOriginalFilename()),0,strCol);
		List<User> users = new ArrayList<User>();
		users = JSONObject.parseArray(resStr, User.class);
		StringBuilder sb= new StringBuilder("[");
		StringBuilder sbmail=new StringBuilder("[");
		for(User user:users){
			if(StringUtils.isBlank(user.getEmail())){
				continue;
			}
	        //正则表达式的匹配器
	        Matcher m = p.matcher(user.getEmail());
	        if(!m.matches()){
	        	sb.append(user.getEmail()).append(",");
	        }else{
	        	Map<String,Object> paraMap=new HashMap<String,Object>();
	        	paraMap.put("email", user.getEmail());
	        	paraMap.put("companyId", admin.getCompanyId());
	        	List<User> listUser = userMapper.getUserByEmail(paraMap);
	        	if(listUser.size()>0){
	        		sbmail.append(user.getEmail()).append(",");
	        	}else{
	        		String fullpwd = Constance.KEY+ user.getEmail() + "2017SECGW";
					String userName=user.getEmail().substring(0,user.getEmail().indexOf("@"));
					user.setPassword(DigestUtils.md5DigestAsHex(fullpwd.getBytes()));
					user.setCompanyId(admin.getCompanyId());
					user.setCompanyName(admin.getCompanyName());
					user.setUserName(userName);
					user.setManufacturer(admin.getManufacturer());
					user.setRole((short)2);
					user.setManufacturer("2");
					user.setStatus("1");
					userMapper.insertUser(user);
	        	}
	        }
		}
		sb.append("]");
		sbmail.append("]");
		System.out.println(sb.toString());
		result.setCode(Constance.RESPONSE_SUCCESS);
		String msg="";
		if(!sb.toString().equals("[]")){
			msg+="邮箱"+sb.toString()+"的邮箱格式错误，未导入到系统.";
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
		}else if(!sbmail.toString().equals("[]")){
			msg+= "邮箱"+sbmail.toString()+"已被注册，未导入系统.";
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
		}else{
			msg="导入成功1";
			result.setCode(Constance.RESPONSE_SUCCESS);
		}
		result.setMsg(msg);
		return result;
	}

	@Override
	public List<User> findUsersByCondition(Map<String, Object> map) throws Exception {
		return userMapper.findUsersByCondition(map);
	}

	@Override
	public List<User> getUserId(Map<String,Object> paraMap) throws Exception {
		return userMapper.getUserId(paraMap);
	}
	
	@SuppressWarnings("unchecked")
	public Result insertUser(Map<String,Object> paraMap,User adminUser) throws Exception{
		Result result = new Result();
		User user = new User();
		String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		//正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(paraMap.get("email").toString());
        if(!m.matches()){
        	result.setMsg("邮箱格式不正确！");
			result.setCode(Constance.RESPONSE_USER_EXISTED_ERROR);
			return result;
        }
        List<User> listUser = userMapper.getUserByEmail(paraMap);
        if(listUser.size()>0){
        	result.setCode(Constance.RESPONSE_PARAM_ERROR);
        	result.setMsg("该邮箱已注册");
        	return result;
        }
        String fullpwd = Constance.KEY+ paraMap.get("email").toString() + "2017SECGW";
		String userName=paraMap.get("email").toString().substring(0,paraMap.get("email").toString().indexOf("@"));
		user.setPassword(DigestUtils.md5DigestAsHex(fullpwd.getBytes()));
		user.setCompanyId(adminUser.getCompanyId());
		user.setCompanyName(adminUser.getCompanyName());
		user.setUserName(userName);
		user.setManufacturer(adminUser.getManufacturer());
		user.setRole((short)2);
		user.setEmail(paraMap.get("email").toString());
		user.setStatus("1");
		user.setManufacturer("2");
		userMapper.insertUser(user);
		if(paraMap.containsKey("deviceIds")){
			List<String> deviceids = (List<String>)paraMap.get("deviceIds");
			if(deviceids.size()>0){
				for(String i:deviceids){
					paraMap.put("userId", user.getId());
					paraMap.put("deviceId", i);
					dcs.bindRoute(paraMap);
				}
			}
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("新增成功");
        return result;
	}
	
	public void updateUser(User user) throws Exception{
		userMapper.updUser(user);
	}
	
	public String deleteUser(String userId) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", userId);
		List<Device> list = deviceMapper.getDevicesByUserId(map);
		if(list.size()>0){
			for(Device di:list){
				Map<String,Object> paraMap=new HashMap<String,Object>();
				paraMap.put("deviceId", di.getDeviceId());
				dcs.bindRoute(paraMap);
			}
		}
		userMapper.deleteUserInDepartment(userId);
		userMapper.delUser(userId);
		return "删除成功";
	}
	
	public Pagination getUsersByRoleWithPage(Map<String,Object> param) throws Exception{
		Integer pageNum = Integer.parseInt(param.get("pageNum").toString());
		Integer pageSize = Integer.parseInt(param.get("pageSize").toString());
		String role = param.get("role").toString();
		Pagination pagination = new Pagination();
		pagination.setPageNo(pageNum);
		pagination.setPageSize(pageSize);
		param.put("pageStart", (pageNum-1)*pageSize);
		List<User> list = userMapper.getUsersByRole(param);
		pagination.setDetails(list);
		int total = userMapper.getUsersByRoleCount(role);
		pagination.setTotal_num(total);
		return pagination;
	}

	@Override
	public Result rePWD(Map<String, String> paraMap) throws Exception {
		Result result = new Result();
		String fullpwd = Constance.KEY+ paraMap.get("email").toString() + "2017SECGW";
		int userId=Integer.parseInt(paraMap.get("userId"));
		userMapper.rePWD(fullpwd,userId);
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg("密码重置成功!");
		return result;
	}

	@Override
	public void updateDepartment(User user) throws Exception {
		userMapper.updateDepartment(user);
	}

	@Override
	public List<Integer> selectUserIdByDepartmentId(int id) throws Exception {
		
		return userMapper.selectUserIdByDepartmentId(id);
	}

	@Override
	public void deleteDepartment(Integer userId) throws Exception {
		userMapper.deleteDepartment(userId);
		
	}

	@Override
	public String deleteUserInDepartment(String userId) throws Exception {
		userMapper.deleteUserInDepartment(userId);
		String msg="删除组员成功";
		return msg;
	}

	
}
