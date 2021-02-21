package com.tailwolf.mybatis.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MyBatis++返回结果的嵌套集合注解
 * @author tailwolf
 * @date 2020-09-10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collection {
    //由于java目前没有真正的泛型，所以该属性是必须的，用于指定类型
    Class clazz();
}
