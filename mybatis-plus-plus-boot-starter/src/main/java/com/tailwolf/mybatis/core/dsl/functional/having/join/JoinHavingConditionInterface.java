package com.tailwolf.mybatis.core.dsl.functional.having.join;

/**
 * @author tailwolf
 * @date 2020-08-20
 */
@FunctionalInterface
public interface JoinHavingConditionInterface<T,E> {
    void having(JoinHaving<T,E> having);
}
