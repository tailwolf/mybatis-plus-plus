package com.tailwolf.mybatis.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段注解
 * @author tailwolf
 * @date 2020-03-26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    //字段名
    String name() default "";

    //是否映射数据库字段，默认true
    boolean exist() default true;
}
