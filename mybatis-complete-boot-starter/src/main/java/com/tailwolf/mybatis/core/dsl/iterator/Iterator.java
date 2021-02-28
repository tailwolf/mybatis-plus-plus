package com.tailwolf.mybatis.core.dsl.iterator;

/**
 * 迭代器
 * @author tailwolf
 * @date 2020-06-23
 */
public interface Iterator<T> {
    /**
     * 是否为空
     */
    boolean isEmpty();

    /**
     * 获取当前节点
     * @return
     */
    T currentNode();

    /**
     * 游标往下一个移动
     */
    Iterator nextNonius();

    /**
     * 游标往上一个移动
     */
    Iterator preNonius();

    /**
     * 获取上一个节点
     */
    T preNode();

    /**
     * 获取下一个节点
     */
    T nextNode();
}
