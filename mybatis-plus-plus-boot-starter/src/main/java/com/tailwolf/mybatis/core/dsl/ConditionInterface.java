package com.tailwolf.mybatis.core.dsl;


import com.tailwolf.mybatis.core.dsl.functional.where.Condition;

@FunctionalInterface
public interface ConditionInterface<T> {
    void condition(Condition<T> t);
}
