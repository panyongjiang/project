package com.uway.mobile.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uway.mobile.domain.User;
import com.uway.mobile.service.UserService;

@Component
public class AuthorityUser {
	
	@Autowired
	UserService us;
	
	/**
	 * 获取权限下的
	 * 所有userID
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> getUserId(Map<String,Object> paraMap) throws Exception {
			List<User> userIds=us.getUserId(paraMap);
			List<Integer> list = new ArrayList<Integer>();
			for(User user:userIds){
				list.add(user.getId());
			}
			return list;
	}
	
	public List<User> getUsers(Map<String,Object> paraMap) throws Exception{
		List<User> userIds=us.getUserId(paraMap);
		return userIds;
	}
}
