package com.uway.mobile.filter;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uway.mobile.BaseApplication;
import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;
import com.uway.mobile.domain.User;
import com.uway.mobile.mapper.UserMapper;
import com.uway.mobile.service.ResourceService;
import com.uway.mobile.util.RedisUtil;


@Component
public class AdminInterceptor extends BaseApplication implements
		HandlerInterceptor {

	@Autowired
	private RedisUtil redisUtil;
	@Resource
	private UserMapper userMapper;
	@Autowired
	private ResourceService resourceService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String sessionId = request.getHeader("sid");
		if (sessionId == null || "".equals(sessionId)) {
			sessionId = request.getParameter("sid");
		}
		String url = request.getRequestURL().toString();
		if (url.contains("admin_user/login") || url.contains("admin_user/test")
				|| url.contains("admin_app/download_app")
				|| url.contains("admin_user/login_out")
				|| url.contains("admin_app/add_app_report")
				|| url.contains("admin_user/getDesKey")) {
			return true;
		}
		Result result = new Result();
		ObjectMapper om = new ObjectMapper();
		if (StringUtils.isEmpty(sessionId)) {
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));
			return false;
		}
		sessionId = Constance.REDIS_USER_PRE + sessionId;
		if (redisUtil.get(sessionId) == null
				|| StringUtils.isEmpty(redisUtil.get(sessionId).toString())) {
			result.setCode(Constance.RESPONSE_USER_ERROR);
			result.setMsg("请登陆！");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));
			return false;
		}
		String userId = redisUtil.get(sessionId).toString();
		User user = userMapper.getUserById(userId);

		if (user.getRole() == (short) 1) {
			request.setAttribute("userId", redisUtil.get(sessionId).toString());
			redisUtil.set(sessionId, request.getAttribute("userId").toString(),
					Long.valueOf(10 * 60));
			// 校验权限
			boolean bool = checkedAuthorityByAdminAcount(userId, url);
			if (!bool) {
				result.setCode(Constance.RESPONSE_AUTH_ERROR);
				result.setMsg("您无权进行此操作！");
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().print(om.writeValueAsString(result));
				return false;
			}
			return true;
		} else {
			result.setCode(Constance.RESPONSE_AUTH_ERROR);
			result.setMsg("您无权进行此操作！");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(om.writeValueAsString(result));
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 校验管理员权限
	 * 
	 * @return
	 */
	private boolean checkedAuthorityByAdminAcount(String userId, String url) {
		try {
			List<com.uway.mobile.domain.Resource> resources = resourceService
					.getAllAuthorityByUserALL(userId);
			if (resources != null) {
				for (com.uway.mobile.domain.Resource resource : resources) {
					if ((!StringUtils.isEmpty(resource.getResourceUrl()))) {
						if (url.contains(resource.getResourceUrl())) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}


}
