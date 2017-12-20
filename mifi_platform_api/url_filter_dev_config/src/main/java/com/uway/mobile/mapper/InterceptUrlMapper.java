package com.uway.mobile.mapper;

import java.util.List;

import com.uway.mobile.domain.Route;
import com.uway.mobile.domain.User;

@Mapper
public interface InterceptUrlMapper {

	public User selectById(String userId);

	public List<Route> getRouteId(int id);

}
