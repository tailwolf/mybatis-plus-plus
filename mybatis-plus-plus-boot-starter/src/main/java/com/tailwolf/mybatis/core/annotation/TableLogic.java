package com.tailwolf.mybatis.core.annotation;

import java.lang.annotation.*;

/**
 * 逻辑删除注解，该注解作用于
 * @author tailwolf
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableLogic {
    String value() default "";

    String delval() default "";
}
