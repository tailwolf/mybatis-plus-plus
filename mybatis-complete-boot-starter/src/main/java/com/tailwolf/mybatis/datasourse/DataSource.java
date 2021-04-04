package com.tailwolf.mybatis.datasourse;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 设置数据源注解
 * @author tailwolf
 * @date 2020-01-11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface DataSource {
    String name();
}
