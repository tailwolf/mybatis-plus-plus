package com.tailwolf.mybatis.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库字段模型
 * @author tailwolf
 * @date 2020-03-26
 */
public class ColumnModel {
    //字段属性
    private Field field;

    //属性对应的表名
    private String columnName;

    //是否主键
    private boolean isPk;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPk() {
        return isPk;
    }

    public void setPk(boolean pk) {
        isPk = pk;
    }
}
