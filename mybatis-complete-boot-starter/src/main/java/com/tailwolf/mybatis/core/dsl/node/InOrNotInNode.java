package com.tailwolf.mybatis.core.dsl.node;

/**
 * IN或者NOT IN条件节点
 * @author tailwolf
 * @date 2020-10-07
 */
public class InOrNotInNode {
    private String keywork;

    /**
     * 属性名
     */
    private Object filedName;

    private Object object;

    private InOrNotInNode(String keywork, Object filedName, Object object){
        this.keywork = keywork;
        this.filedName = filedName;
        this.object = object;
    }

    public static InOrNotInNode newInstance(String keywork, Object filedName, Object object){
        return new InOrNotInNode(keywork, filedName, object);
    }

    public String getKeywork() {
        return keywork;
    }

    public void setKeywork(String keywork) {
        this.keywork = keywork;
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
