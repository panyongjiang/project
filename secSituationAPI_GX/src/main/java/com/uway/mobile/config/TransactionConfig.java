package com.uway.mobile.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@EnableTransactionManagement
@Configuration
public class TransactionConfig implements TransactionManagementConfigurer {
	@Resource(name = "txManager1")
	private PlatformTransactionManager txManager1;

	@Bean(name = "txManager1")
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {

		return txManager1;
	}

}
