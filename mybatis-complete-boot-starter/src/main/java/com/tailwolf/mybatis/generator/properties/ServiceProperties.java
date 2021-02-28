package com.tailwolf.mybatis.generator.properties;

/**
 * 生成Service层的配置属性
 * @author tailwolf
 * @date 2021-01-27
 */
public class ServiceProperties extends BaseProperties{
    //实体类引用路径
    private String entityReference;
    //文件名
    private String serviceName;

    public ServiceProperties(String packageName, String entityReference, String serviceName, String entityName) {
        super(packageName, entityName);
        this.entityReference = entityReference;
        this.serviceName = serviceName;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
