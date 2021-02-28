package com.tailwolf.mybatis.core.dsl.functional.where;

import java.io.Serializable;

@FunctionalInterface
public interface ConditionFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
