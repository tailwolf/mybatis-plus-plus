package com.tailwolf.mybatis.core.dsl.functional.group.entity;

import java.io.Serializable;

/**
 * @author tailwolf
 * @date 2020-08-19
 */
@FunctionalInterface
public interface GroupByFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
