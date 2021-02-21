package com.tailwolf.mybatis.core.dsl.functional.having.entity;


/**
 * @author tailwolf
 * @date 2020-09-15
 */
public interface HavingConditionInterface<T> {
    void having(Having<T> having);
}
