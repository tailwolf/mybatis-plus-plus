package com.tailwolf.mybatis.core.dsl.functional.select.join;

import java.io.Serializable;

@FunctionalInterface
public interface SelectTableFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
