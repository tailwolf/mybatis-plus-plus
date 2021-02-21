package com.tailwolf.mybatis.config;

/**
 * mybatis++关于sql日志打印的配置
 * @author tailwolf
 * @date 2020-09-06
 */
public class LogConfig {
    //是否打印完整的sql语句的功能，默认false
    private boolean completeSql = false;

    public boolean isCompleteSql() {
        return completeSql;
    }

    public void setCompleteSql(boolean completeSql) {
        this.completeSql = completeSql;
    }

    public boolean getCompleteSql() {
        return this.completeSql;
    }
}
