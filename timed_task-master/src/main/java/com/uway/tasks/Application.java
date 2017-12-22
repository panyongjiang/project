package com.uway.tasks;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("com.uway.tasks.mapper")
@ImportResource({"classpath*:spring.xml"})
public class Application{
	public static final Logger log = Logger.getLogger("Application.class");
	
	static {
		try {
			// 初始化log4j
			String log4jPath = Application.class.getClassLoader().getResource("").getPath()	+ "log4j.properties";
			PropertyConfigurator.configure(log4jPath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.debug("============= SpringBoot Start Success =============");
	}
}
