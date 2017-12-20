package com.uway.mobile.mapper;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Mapper {
	String value() default "";
}
