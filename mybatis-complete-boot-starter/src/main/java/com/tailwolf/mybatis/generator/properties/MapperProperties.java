package com.tailwolf.mybatis.generator.properties;

/**
 * 生成Mapper类的配置属性
 * @author tailwolf
 * @date 2021-01-27
 */
public class MapperProperties extends BaseProperties{
    //实体类引用路径
    private String entityReference;
    //文件名
    private String mapperName;

    public MapperProperties(String packageName, String entityReference, String mapperName, String entityName){
        super(packageName, entityName);
        this.entityReference = entityReference;
        this.mapperName = mapperName;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }
}
