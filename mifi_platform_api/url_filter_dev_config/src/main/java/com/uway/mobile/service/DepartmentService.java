package com.uway.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.common.Result;
import com.uway.mobile.domain.Department;

@Transactional
public interface DepartmentService {

	/**
	 * 新增分组
	 * @param department
	 */
	public Result insertDepartment(Map<String, Object> paraMap) throws Exception;
	
	/**
	 * 更新分组
	 * @param department
	 */
	public void updateDepartment(Department department,List<Integer> userIds) throws Exception;
	
	/**
	 * 根据ID查找分组
	 * @param departmentId
	 * @return
	 */
	public Department findDepartmentById(Integer departmentId);
	
	/**
	 * 根据主键删除分组
	 * @param id
	 */
	public void deleteDepartment(int id) throws Exception;
	
	/**
	 * 根据管理员的公司ID查所有分组
	 * @param companyId
	 * @return
	 */
	public List<Department> getDepartmentsByCompId(Integer companyId) throws Exception;
	
	/**
	 * 查询所有的分组及其用户，没有被分组的用户归到"其他"组
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public List<Department> getDepartmentUsers(Integer companyId) throws Exception;

	public List<Integer> checkDepartmentUserIds(int companyId,int userId)throws Exception;

	public List<String> checkDepartmentName(Map<String, Object> paraMap)throws Exception;

	
}
