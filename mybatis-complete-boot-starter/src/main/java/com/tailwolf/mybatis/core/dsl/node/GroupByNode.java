package com.tailwolf.mybatis.core.dsl.node;

/**
 * group by字段节点
 * @author tailwolf
 * @date 2020-08-19
 */
public class GroupByNode {
    /**
     * 属性名
     */
    private Object filedName;

    private GroupByNode(Object filedName){
        this.filedName = filedName;
    }

    public static GroupByNode newInstance(Object filedName){
        return new GroupByNode(filedName);
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }
}
