package com.tailwolf.mybatis.core.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import com.tailwolf.mybatis.core.registrar.MybatisCompleteMapperScannerRegistrar;

import java.lang.annotation.*;

/**
 * mybatis-complete启用注解，该注解作用于SpringBoot启动类，开始mybatis-complete
 * @author tailwolf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MybatisCompleteMapperScannerRegistrar.class})
@ComponentScans(value = {@ComponentScan(value = "com.tailwolf.mybatis")})
public @interface MybatisCompleteScans {
}
