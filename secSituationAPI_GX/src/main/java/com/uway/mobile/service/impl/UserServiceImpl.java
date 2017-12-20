package com.uway.mobile.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public Result getUserByName(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		String userName = paraMap.get("userName").toString();
		List<Map<String, Object>> userList = userMapper.getUserByName(userName);

		if (userList.size() < 1) {
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("用户名错误");
			return result;
		}
		String md5pwd = DigestUtils.md5DigestAsHex(paraMap.get("passwd").toString().getBytes());
		paraMap.put("passwd", md5pwd);
		if (userList.get(0).get("passwd").equals(md5pwd)) {
			result.setCode(Constance.RESPONSE_SUCCESS);
			result.setMsg("登录成功");
			result.setData(userList.get(0));
			return result;
		} else {
			result.setCode(Constance.RESPONSE_PWD_ERROR);
			result.setMsg("密码错误");
			return result;
		}
	}

	@Override
	public Result updPwd(Map<String, Object> paramap) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();
		String md5pwd = DigestUtils.md5DigestAsHex(paramap.get("passwd_new").toString().getBytes());
		paramap.put("passwd_new", md5pwd);
		userMapper.updPwd(paramap);
		result.setCode(Constance.RESPONSE_PARAM_ERROR);
		result.setMsg("修改成功");
		return result;

	}

}
