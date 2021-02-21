package com.tailwolf.mybatis.core.dsl.node;

/**
 * 排序节点
 * @author tailwolf
 * @date 2020-08-29
 */
public class OrderByNode {
    /**
     * 属性名
     */
    private Object filedName;

    /**
     * 排序关键字
     */
    private String orderByKeywork;

    private OrderByNode(Object filedName, String orderByKeywork){
        this.filedName = filedName;
        this.orderByKeywork = orderByKeywork;
    }

    public Object getFiledName() {
        return filedName;
    }

    public void setFiledName(Object filedName) {
        this.filedName = filedName;
    }

    public String getOrderByKeywork() {
        return orderByKeywork;
    }

    public void setOrderByKeywork(String orderByKeywork) {
        this.orderByKeywork = orderByKeywork;
    }

    public static OrderByNode newInstance(String orderByKeywork, Object filedName){
        return new OrderByNode(filedName, orderByKeywork);
    }
}
