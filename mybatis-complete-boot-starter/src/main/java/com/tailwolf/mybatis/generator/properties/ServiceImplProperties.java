package com.tailwolf.mybatis.generator.properties;

public class ServiceImplProperties extends BaseProperties{
    //实体类引用路径
    private String entityReference;
    //mapper类引用路径
    private String mapperReference;
    //service类引用路径
    private String sericeReference;
    //mapper文件名
    private String mapperName;
    //service文件名
    private String serviceName;
    //serviceImp文件名
    private String serviceImplName;

    public ServiceImplProperties(String packageName, String entityReference, String mapperReference, String sericeReference, String serviceName, String mapperName, String serviceImplName, String entityName){
        super(packageName, entityName);
        this.entityReference = entityReference;
        this.mapperReference = mapperReference;
        this.sericeReference = sericeReference;
        this.mapperName = mapperName;
        this.serviceImplName = serviceImplName;
        this.serviceName = serviceName;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getMapperReference() {
        return mapperReference;
    }

    public void setMapperReference(String mapperReference) {
        this.mapperReference = mapperReference;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getSericeReference() {
        return sericeReference;
    }

    public void setSericeReference(String sericeReference) {
        this.sericeReference = sericeReference;
    }
}
