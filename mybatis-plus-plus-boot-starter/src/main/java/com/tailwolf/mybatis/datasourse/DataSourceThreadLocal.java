package com.tailwolf.mybatis.datasourse;

import javax.sql.DataSource;

/**
 * 使用ThreadLocal保存当前线程的数据源
 * @author tailwolf
 * @date 2020-09-17
 */
public class DataSourceThreadLocal {
    private static final ThreadLocal<DataSource> DATASOURCE_TYPE = new ThreadLocal<>();

    private DataSourceThreadLocal(){}

    public static void setValue(DataSource dataSource){
        DATASOURCE_TYPE.set(dataSource);
    }

    public static DataSource getValue(){
        return DATASOURCE_TYPE.get();
    }

    public static void removeValue(){
        DATASOURCE_TYPE.remove();
    }
}
