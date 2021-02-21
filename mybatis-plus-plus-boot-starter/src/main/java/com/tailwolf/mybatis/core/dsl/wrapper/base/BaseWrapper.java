package com.tailwolf.mybatis.core.dsl.wrapper.base;

/**
 * 基础包装类
 * @author tailwolf
 * @date 2020-01-03
 */
public class BaseWrapper {
    /**
     * 数据源名称
     */
    private String dataSource;

    private Object parameterObject;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object parameterObject) {
        this.parameterObject = parameterObject;
    }
}
