package com.tailwolf.mybatis.core.config;

import com.tailwolf.mybatis.config.DbConfig;
import com.tailwolf.mybatis.config.LogConfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * mybatis++自动配置属性
 * @author tailwolf
 * @date 2020-09-06
 */
@ConfigurationProperties(
        prefix = "mybatis.plus-plus"
)
public class MybatisPlusPlusProperties {
    private DbConfig dbConfig;

    private LogConfig logConfig;

    private List<DataSourceProperties> propertiesList;

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public List<DataSourceProperties> getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(List<DataSourceProperties> propertiesList) {
        this.propertiesList = propertiesList;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }
}
