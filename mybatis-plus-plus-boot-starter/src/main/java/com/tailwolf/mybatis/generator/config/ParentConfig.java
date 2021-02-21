package com.tailwolf.mybatis.generator.config;

/**
 * 配置生成java代码类的父类
 * @author tailwolf
 * @date 2021-01-31
 */
public class ParentConfig {
    //实体类的父类
    private Class<?> entityParent;
    //controller类的父类
    private Class<?> controllerParent;

    public void setEntityParent(Class<?> entityParent) {
        this.entityParent = entityParent;
    }

    public void setControllerParent(Class<?> controllerParent) {
        this.controllerParent = controllerParent;
    }

    public Class<?> getEntityParent() {
        return entityParent;
    }

    public Class<?> getControllerParent() {
        return controllerParent;
    }
}
