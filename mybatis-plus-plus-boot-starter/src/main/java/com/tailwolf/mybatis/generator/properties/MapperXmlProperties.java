package com.tailwolf.mybatis.generator.properties;

/**
 * 生成MapperXml的配置属性
 * @author tailwolf
 * @date 2021-01-27
 */
public class MapperXmlProperties extends BaseProperties{

    private String mapperReference;
    private String mapperXmlName;

    public MapperXmlProperties(String packageName, String entityName, String mapperXmlName, String mapperReference){
        super(packageName, entityName);
        this.mapperReference = mapperReference;
        this.mapperXmlName = mapperXmlName;
    }

    public String getMapperReference() {
        return mapperReference;
    }

    public void setMapperReference(String mapperReference) {
        this.mapperReference = mapperReference;
    }

    public String getMapperXmlName() {
        return mapperXmlName;
    }

    public void setMapperXmlName(String mapperXmlName) {
        this.mapperXmlName = mapperXmlName;
    }
}
