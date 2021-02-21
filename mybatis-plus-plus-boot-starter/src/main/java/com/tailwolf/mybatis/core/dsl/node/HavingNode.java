package com.tailwolf.mybatis.core.dsl.node;

/**
 * having字段节点
 * @author tailwolf
 * @date 2020-08-20
 */
public class HavingNode {
    /**
     * 属性名
     */
    private Object filedName;

    /**
     * 条件类型关键字
     */
    private String conditionKeywork;

    /**
     * 属性值
     */
    private Object value;

    private HavingNode(String conditionKeywork, Object filedName, Object value){
        this.conditionKeywork = conditionKeywork;
        this.filedName = filedName;
        this.value = value;
    }

    public static HavingNode newInstance(String conditionKeywork, Object filedName, Object value){
        return new HavingNode(conditionKeywork, filedName, value);
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public String getConditionKeywork() {
        return conditionKeywork;
    }

    public void setConditionKeywork(String conditionKeywork) {
        this.conditionKeywork = conditionKeywork;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}