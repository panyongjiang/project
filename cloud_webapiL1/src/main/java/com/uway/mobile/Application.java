package com.uway.mobile;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SuppressWarnings("deprecation")
@SpringBootApplication
@MapperScan("com.uway.mobile.mapper")
public class Application extends SpringBootServletInitializer {
	public static final Logger log = Logger.getLogger(Application.class);
	
	static {
		try {
			// 初始化log4j
			String log4jPath = Application.class.getClassLoader().getResource("").getPath()
					+ "log4j.properties";
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
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.debug("============= SpringBoot Start Success =============");
	}
}
