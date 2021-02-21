package com.tailwolf.mybatis.core.dsl.functional.where.join;

@FunctionalInterface
public interface JoinConditionInterface<T,E> {
    void joinCondition(JoinCondition<T,E> joinCondition);
}
