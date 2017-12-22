package com.uway.mobile.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.uway.mobile.filter.AdminInterceptor;
import com.uway.mobile.filter.UserInterceptor;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private UserInterceptor userInterceptor;
	@Autowired
	private AdminInterceptor adminInterceptor;
	
	/**
	 * 配置拦截器
	 * 
	 * @param registry
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_app/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_article/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_note/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_safe_trial/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_user/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_product/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_code/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/user_group/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/resource/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/admin_domainQue/**");
		registry.addInterceptor(adminInterceptor).addPathPatterns("/sensitive/**");
		
		
		registry.addInterceptor(userInterceptor).addPathPatterns("/appCheck/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/article/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/cer/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/note/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/safe_service/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/safe_trial/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/site/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/user/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/waf/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/es/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/monitorCenter/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/wafConfig/**");
		//registry.addInterceptor(userInterceptor).addPathPatterns("/test/**");
	}
}
