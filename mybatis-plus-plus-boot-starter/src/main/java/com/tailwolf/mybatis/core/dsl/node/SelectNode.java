package com.tailwolf.mybatis.core.dsl.node;

/**
 * SELECT字段节点
 * @author tailwolf
 * @date 2020-08-17
 */
public class SelectNode {
    /**
     * 属性名
     */
    private Object filedName;

    /**
     * sql函数名
     * @param filedName
     */
    private String sqlFuncName;

    private SelectNode(Object filedName, String sqlFuncName){
        this.filedName = filedName;
        this.sqlFuncName = sqlFuncName;
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public static SelectNode newInstance(Object filedName, String sqlFuncName){
        return new SelectNode(filedName, sqlFuncName);
    }

    public String getSqlFuncName() {
        return sqlFuncName;
    }

    public void setSqlFuncName(String sqlFuncName) {
        this.sqlFuncName = sqlFuncName;
    }
}
