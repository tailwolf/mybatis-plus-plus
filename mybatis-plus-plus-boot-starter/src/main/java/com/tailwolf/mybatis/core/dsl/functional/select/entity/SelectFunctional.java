package com.tailwolf.mybatis.core.dsl.functional.select.entity;

import java.io.Serializable;

@FunctionalInterface
public interface SelectFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
