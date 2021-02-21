package com.tailwolf.mybatis.core.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import com.tailwolf.mybatis.core.registrar.PlusPlusMapperScannerRegistrar;

import java.lang.annotation.*;

/**
 * MyBatis++启用注解，该注解作用于SpringBoot启动类，开始MyBatis++
 * @author tailwolf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({PlusPlusMapperScannerRegistrar.class})
@ComponentScans(value = {@ComponentScan(value = "com.tailwolf.mybatis")})
public @interface MybatisPlusPlusScans{
}
