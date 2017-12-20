package com.uway.mobile.controller;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.uway.mobile.Application;


@RunWith(SpringJUnit4ClassRunner.class) // 引入Spring-Test框架支持
@SpringBootTest(classes = Application.class) // 指定SpringBoot-Application启动类
// 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@Rollback(value=true)
@Transactional(transactionManager = "transactionManager")
public abstract class BaseApplicationTest extends MockMvcResultMatchers {

}
