package com.tailwolf.mybatis.generator.properties;

/**
 * 所有代码生成的Properties都集成该类
 * @author tailwolf
 * @date 2021-01-28
 */
public class BaseProperties {
    //包名
    protected String packageName;
    //实体类名
    protected String entityName;

    public BaseProperties(String packageName, String entityName){
        this.packageName = packageName;
        this.entityName = entityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
