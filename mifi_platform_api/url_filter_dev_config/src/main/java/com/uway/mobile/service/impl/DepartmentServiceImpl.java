package com.uway.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Department;
import com.uway.mobile.domain.Device;
import com.uway.mobile.domain.User;
import com.uway.mobile.domain.UserDepartment;
import com.uway.mobile.mapper.DepartmentMapper;
import com.uway.mobile.mapper.UserDepartmentMapper;
import com.uway.mobile.service.DepartmentService;
import com.uway.mobile.service.DeviceService;
import com.uway.mobile.service.UserDepartmentService;
import com.uway.mobile.service.UserService;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private UserDepartmentMapper userDepartmentMapper;
	@Autowired
	private UserDepartmentService userDepartmentService;
	@Autowired
	public UserService userService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	public DeviceService deviceService;

	@SuppressWarnings("unchecked")
	@Override
	public Result insertDepartment(Map<String, Object> paraMap) throws Exception {
		Result result = new Result();
		Department department = new Department();
		String msg="";
		User user = userService.getUserById(paraMap.get("userId").toString());
		String departName = paraMap.get("departName").toString();
		List<String> departNameLists=departmentService.checkDepartmentName(paraMap);
		if(departNameLists.size()>0){
			result.setCode(Constance.RESPONSE_PARAM_ERROR);
			result.setMsg("组名已存在");
			return result;
		}
		List<Integer> userIds = (List<Integer>)paraMap.get("userIds");
		List<Integer> uid=new ArrayList<Integer>();
		StringBuilder sb= new StringBuilder("[");
		for(Integer i:userIds){
			List<Integer> checkIds=departmentService.checkDepartmentUserIds(Integer.parseInt(paraMap.get("companyId").toString()),i);
			if(checkIds.size()>0){
				User users=userService.getUserById(String.valueOf(i));
				sb.append(users.getUserName()).append(",");
			}else{
				uid.add(i);
			}
		}
		sb.append("]");
		department.setName(departName);
		department.setCompanyId(user.getCompanyId());
		departmentMapper.insertDepartment(department);
		if(uid.size()>0){
			for(Integer userId:uid){
				UserDepartment userDepartment = new UserDepartment();
				User users = new User();
				userDepartment.setDepartmentId(department.getId());
				userDepartment.setUserId(userId);
				userDepartmentMapper.insertUserDepartment(userDepartment);
				users.setId(userId);
				users.setDepartment(departName);
				userService.updateDepartment(users);
			}
		}
		if(!sb.toString().equals("[]")){
			msg+="用户"+sb.toString()+"已分组.";
		}else{
			msg+="新增成功";
		}
		result.setCode(Constance.RESPONSE_SUCCESS);
		result.setMsg(msg);
		return result;
	}

	@Override
	public void updateDepartment(Department department,List<Integer> userIds) throws Exception {
		Map<String,Integer> param = new HashMap<String,Integer>();
		List<User> duser=userDepartmentMapper.getUsersByDepartmentId(department.getId());
		List<Integer> uIds=new ArrayList<Integer>();
		for(Integer ids:userIds){
			uIds.add(ids);
		}
		if(duser.size()>0){
			for(User u:duser){
				Boolean flag =false;
				for(Integer i:userIds){
					if(u.getId()==i){
						flag=true;
					}
				}
				if(!flag){
					uIds.add(u.getId());
				}
			}
		}
		for(Integer id:uIds){
				param.put("userId", id);
				userDepartmentMapper.deleteByDepartAndUser(param);
				userService.deleteDepartment(id);
		}
		if(userIds.size()>0){
			for(Integer userId:userIds){
				UserDepartment userDepartment = new UserDepartment();
				User users = new User();
				userDepartment.setDepartmentId(department.getId());
				userDepartment.setUserId(userId);
				userDepartmentMapper.insertUserDepartment(userDepartment);
				users.setId(userId);
				users.setDepartment(department.getName());
				userService.updateDepartment(users);
			}
		}
		departmentMapper.updateDepartment(department);
	}

	@Override
	public void deleteDepartment(int id) throws Exception {
		List<Integer> userIds = userService.selectUserIdByDepartmentId(id);
		userDepartmentMapper.deleteByDepartmentId(id);
		departmentMapper.deleteById(id);
		for(Integer userId:userIds){
			userService.deleteDepartment(userId);
		}
		
	}

	@Override
	public List<Department> getDepartmentsByCompId(Integer companyId) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		List<Department> departments = departmentMapper.getDepartmentsByCompId(param);
		for(Department department:departments){
			List<User> userList = userDepartmentMapper.getUsersByDepartmentId(department.getId());
			department.setUserList(userList);
		}
		return departments;
	}
	
	@Override
	public List<Department> getDepartmentUsers(Integer companyId) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		List<Department> departments = departmentMapper.getDepartmentsByCompId(param);
		for(Department department:departments){
			List<User> userList = userDepartmentMapper.getUsersByDepartmentId(department.getId());
			department.setUserList(userList);
			for(User user:userList){
				Map<String,String> paraMap = new HashMap<String,String>();
				paraMap.put("userId", String.valueOf(user.getId()));
				List<Device> device=deviceService.getDevicesByUserId(paraMap);
				user.setDevice(device);
			}
		}
		List<User> userList = userDepartmentService.getOtherUsers(companyId);
		for(User user:userList){
			Map<String,String> paraMap = new HashMap<String,String>();
			paraMap.put("userId", String.valueOf(user.getId()));
			List<Device> device=deviceService.getDevicesByUserId(paraMap);
			user.setDevice(device);
		}
		Department dept = new Department();
		dept.setId(0);
		dept.setName("其他");
		dept.setUserList(userList);
		departments.add(dept);
		return departments;
	}

	@Override
	public Department findDepartmentById(Integer departmentId) {
		Department department = departmentMapper.getDepartmentById(departmentId);
		if(department!=null){
			List<User> userList = userDepartmentMapper.getUsersByDepartmentId(department.getId());
			department.setUserList(userList);
		}
		return department;
	}

	@Override
	public List<Integer> checkDepartmentUserIds(int companyId,int userId) throws Exception {
		
		return departmentMapper.checkDepartmentUserIds(companyId,userId);
	}

	@Override
	public List<String> checkDepartmentName(Map<String, Object> paraMap) throws Exception {
		
		return departmentMapper.checkDepartmentName(paraMap);
	}

}
