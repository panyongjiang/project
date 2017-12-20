package com.uway.mobile.filter;

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
import com.uway.mobile.util.RedisUtil;

@Component
public class UserInterceptor extends BaseApplication implements
		HandlerInterceptor {

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String sessionId = request.getHeader("sid");
		if(sessionId == null || "".equals(sessionId)){				
			sessionId = request.getParameter("sid");
		}
		// log.debug("sessionId================" + sessionId);
		String url = request.getRequestURL().toString();
		if(url.contains("user/validate_user") || url.contains("user/register")
				|| url.contains("user/login") || url.contains("user/test")
				|| url.contains("user/login_out") || url.contains("download")
				|| url.contains("article/list_article") || url.contains("article/get_article")
				|| url.contains("user/validate_code")|| url.contains("test/attack_trend")
				|| url.contains("test/get_today")|| url.contains("test/attack_top")
				|| url.contains("test/get_attack_top")|| url.contains("test/view_site")
				|| url.contains("test/view_son_site")|| url.contains("test/es_delete")
				|| url.contains("test/es_insert")|| url.contains("test/get_xml")
				|| url.contains("test/active_site")|| url.contains("test/active_cname")
				|| url.contains("test/get_day")|| url.contains("test/get_web")
				|| url.contains("site/receive_xml")|| url.contains("user/getDesKey")
				|| url.contains("test/readEs")|| url.contains("test/writeEs")
				|| url.contains("user/isDisplayValidateCode")|| url.contains("es/export_user_xlsx")
				|| url.contains("checkMail/checkMail")/*true*/
				){
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
		request.setAttribute("userId", redisUtil.get(sessionId).toString());
		redisUtil.set(sessionId, request.getAttribute("userId").toString(), Long.valueOf(10 * 60));
		return true;
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

}
