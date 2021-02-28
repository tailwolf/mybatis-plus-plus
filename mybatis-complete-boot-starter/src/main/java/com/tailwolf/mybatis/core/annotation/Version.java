package com.tailwolf.mybatis.core.annotation;

import java.lang.annotation.*;

/**
 * 乐观锁注解
 * @author tailwolf
 * @date 2020-12-12
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Version {
}
