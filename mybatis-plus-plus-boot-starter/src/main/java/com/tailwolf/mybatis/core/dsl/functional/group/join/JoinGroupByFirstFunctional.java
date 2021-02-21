package com.tailwolf.mybatis.core.dsl.functional.group.join;

import com.tailwolf.mybatis.core.dsl.functional.group.entity.GroupByFunctional;

/**
 * 用来选择group by后面字段的函数接口
 * @author tailwolf
 * @date 2020-08-19
 */
@FunctionalInterface
public interface JoinGroupByFirstFunctional<T> extends GroupByFunctional<T> {
}
