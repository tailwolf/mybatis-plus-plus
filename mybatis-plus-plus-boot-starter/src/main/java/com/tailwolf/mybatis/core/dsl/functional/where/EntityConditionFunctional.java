package com.tailwolf.mybatis.core.dsl.functional.where;

import java.io.Serializable;

@FunctionalInterface
public interface EntityConditionFunctional<T> extends Serializable {
    Object getFieldName(T t);
}
