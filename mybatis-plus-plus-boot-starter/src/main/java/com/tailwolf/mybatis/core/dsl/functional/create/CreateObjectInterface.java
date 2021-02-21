package com.tailwolf.mybatis.core.dsl.functional.create;

/**
 * @author tailwolf
 * @date 2021-01-19
 */
@FunctionalInterface
public interface CreateObjectInterface<R> {
    R create();
}
