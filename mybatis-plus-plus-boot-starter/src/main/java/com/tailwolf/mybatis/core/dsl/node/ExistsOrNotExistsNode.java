package com.tailwolf.mybatis.core.dsl.node;

/**
 * exist或者not exist节点
 * @author tailwolf
 * @date 2020-10-13
 */
public class ExistsOrNotExistsNode {
    /**
     * 条件关键字
     */
    private String keywork;

    /**
     * sql脚本
     */
    private String sqlScript;

    /**
     * 属性值
     */
    private Object[] value;

    private ExistsOrNotExistsNode(String keywork, String sqlScript, Object[] value){
        this.keywork = keywork;
        this.sqlScript = sqlScript;
        this.value = value;
    }

    private ExistsOrNotExistsNode(){}

    public static ExistsOrNotExistsNode newInstance(String conditionKeywork, String sqlScript, Object[] value){
        return new ExistsOrNotExistsNode(conditionKeywork, sqlScript, value);
    }

    public String getKeywork() {
        return keywork;
    }

    public void setKeywork(String keywork) {
        this.keywork = keywork;
    }

    public String getSqlScript() {
        return sqlScript;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public Object[] getValue() {
        return value;
    }

    public void setValue(Object[] value) {
        this.value = value;
    }
}
