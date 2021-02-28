package com.tailwolf.mybatis.core.dsl.functional.select.join;


@FunctionalInterface
public interface JoinSelectInterface<E, T> {
    void selectCondition(JoinSelect<T,E> selectCondition);
}
