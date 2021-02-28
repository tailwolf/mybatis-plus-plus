package com.tailwolf.mybatis.core.dsl.node;

/**
 * 查询条件节点
 * @author tailwolf
 * @date 2020-06-22
 */
public class ConditionNode {
    /**
     * 条件关键字
     */
    private String conditionKeywork;

    /**
     * 属性名
     */
    private Object filedName;

    /**
     * 属性值
     */
    private Object value;

    private ConditionNode(String conditionKeywork, Object filedName, Object value){
        this.conditionKeywork = conditionKeywork;
        this.filedName = filedName;
        this.value = value;
    }

    private ConditionNode(){}

    public static ConditionNode newInstance(String conditionKeywork, Object filedName, Object value){
        return new ConditionNode(conditionKeywork, filedName, value);
    }

    public String getConditionKeywork() {
        return conditionKeywork;
    }

    public void setConditionKeywork(String conditionKeywork) {
        this.conditionKeywork = conditionKeywork;
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
