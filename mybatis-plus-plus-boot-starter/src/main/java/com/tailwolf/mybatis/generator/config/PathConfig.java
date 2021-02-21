package com.tailwolf.mybatis.generator.config;

/**
 * 关于路径的配置
 * @author tailwolf
 * @date 2021-01-27
 */
public class PathConfig {
    //java目录
    private String rootPath;
    //xmlMapper目录
    private String xmlRootPath;
    //包名
    private String packageName;
    //mapper xml文件的包名
    private String xmlMapperPackageName;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getXmlMapperPackageName() {
        return xmlMapperPackageName;
    }

    public void setXmlMapperPackageName(String xmlMapperPackageName) {
        this.xmlMapperPackageName = xmlMapperPackageName;
    }

    public String getXmlRootPath() {
        return xmlRootPath;
    }

    public void setXmlRootPath(String xmlRootPath) {
        this.xmlRootPath = xmlRootPath;
    }
}
