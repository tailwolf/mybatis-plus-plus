package com.tailwolf.mybatis.generator.config;

/**
 * 关于数据源的配置
 * @author tailwolf
 * @date 2021-01-27
 */
public class DsConfig {
    //数据库驱动路径
    private String driverClassName;
    //数据库链接
    private String url;
    //数据库账号
    private String username;
    //数据库密码
    private String password;

    //表名数组
    private String[] tableNames;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }
}
