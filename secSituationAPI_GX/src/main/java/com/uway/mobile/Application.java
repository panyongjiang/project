package com.uway.mobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.uway.mobile.filter.XssFilter;

@SpringBootApplication
@MapperScan("com.uway.mobile.mapper")
@EnableScheduling
public class Application extends SpringBootServletInitializer {
	public static final Logger log = Logger.getLogger(Application.class);

	static {
		try {
			// 初始化log4j
			String log4jPath = Application.class.getClassLoader().getResource("").getPath() + "log4j.properties";
			// log.debug("初始化Log4j。。。。");
			// log.debug("path is " + log4jPath);
			PropertyConfigurator.configure(log4jPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * 注册过滤器bean
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		XssFilter xssFilter = new XssFilter();
		registrationBean.setFilter(xssFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/user/*");// 拦截路径
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.debug("============= SpringBoot Start Success =============");
	}
}
