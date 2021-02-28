package com.tailwolf.mybatis.core.registrar;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MybatisCompleteMapperScannerRegistrar extends MapperScannerRegistrar {
    private ResourceLoader resourceLoader;

    public MybatisCompleteMapperScannerRegistrar() {
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator((BeanNameGenerator) BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBean((MapperFactoryBean)BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        scanner.setSqlSessionTemplateBeanName(annoAttrs.getString("sqlSessionTemplateRef"));
        scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));
        List<String> basePackages = new ArrayList<>();
        String[] var10 = annoAttrs.getStringArray("value");
        int var11 = var10.length;

        int var12;
        String pkg;
        for(var12 = 0; var12 < var11; ++var12) {
            pkg = var10[var12];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        var10 = annoAttrs.getStringArray("basePackages");
        var11 = var10.length;

        for(var12 = 0; var12 < var11; ++var12) {
            pkg = var10[var12];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        Class[] var14 = annoAttrs.getClassArray("basePackageClasses");
        var11 = var14.length;

        for(var12 = 0; var12 < var11; ++var12) {
            Class<?> clazz = var14[var12];
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        basePackages.add("com.tailwolf.mybatis.core.common.dao");

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
