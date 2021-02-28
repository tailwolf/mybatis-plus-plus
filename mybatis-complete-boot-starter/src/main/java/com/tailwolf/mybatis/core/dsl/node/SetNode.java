package com.tailwolf.mybatis.core.dsl.node;

/**
 * SET字段节点
 * @author tailwolf
 * @date 2020-09-05
 */
public class SetNode {
    /**
     * 属性值
     */
    private Object value;

    /**
     * 字段
     * @param filedName
     */
    private Object filedName;

    private SetNode(Object filedName, Object value){
        this.filedName = filedName;
        this.value = value;
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public static SetNode newInstance(Object filedName, Object value){
        return new SetNode(filedName, value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
