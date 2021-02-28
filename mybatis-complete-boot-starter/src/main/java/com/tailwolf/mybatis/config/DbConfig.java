package com.tailwolf.mybatis.config;

/**
 * mybatis-complete关于数据库的配置
 * @author tailwolf
 * @date 2020-09-06
 */
public class DbConfig {

    /**逻辑删除属性**/
    private String logicDeleteField;
    /**逻辑删除值**/
    private String logicDeleteValue;
    /**逻辑非删除值**/
    private String logicNotDeleteValue;

    public String getLogicDeleteField() {
        return logicDeleteField;
    }

    public void setLogicDeleteField(String logicDeleteField) {
        this.logicDeleteField = logicDeleteField;
    }

    public String getLogicDeleteValue() {
        return logicDeleteValue;
    }

    public void setLogicDeleteValue(String logicDeleteValue) {
        this.logicDeleteValue = logicDeleteValue;
    }

    public String getLogicNotDeleteValue() {
        return logicNotDeleteValue;
    }

    public void setLogicNotDeleteValue(String logicNotDeleteValue) {
        this.logicNotDeleteValue = logicNotDeleteValue;
    }
}
