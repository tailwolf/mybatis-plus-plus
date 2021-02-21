package com.tailwolf.mybatis.core.dsl.functional.group.join;

/**
 * @author tailwolf
 * @date 2020-08-19
 */
@FunctionalInterface
public interface JoinGroupByInterface<T, E> {
    void groupBy(JoinGroupBy<T,E> groupBy);
}
