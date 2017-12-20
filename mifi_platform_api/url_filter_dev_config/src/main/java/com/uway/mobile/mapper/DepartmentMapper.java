package com.uway.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uway.mobile.domain.Department;

@Mapper
public interface DepartmentMapper {
	
	/**
	 * 新增分组
	 * @param department
	 * @return
	 */
	public int insertDepartment(Department department);
	
	/**
	 * 更新分组
	 * @param department
	 * @return
	 */
	public int updateDepartment(Department department);
	
	/**
	 * 根据主键删除分组
	 * @param id
	 * @return
	 */
	public int deleteById(int id);
	
	/**
	 * 根据ID查找
	 * @param departmentId
	 * @return
	 */
	public Department getDepartmentById(Integer departmentId);
	
	/**
	 * 根据管理员的公司ID查所有分组
	 * @param m
	 * @return
	 */
	public List<Department> getDepartmentsByCompId(Map<String,Object> m);

	public List<Integer> checkDepartmentUserIds(@Param(value="companyId")int companyId,@Param(value="userId")int userId);

	public List<String> checkDepartmentName(Map<String, Object> paraMap);

}
