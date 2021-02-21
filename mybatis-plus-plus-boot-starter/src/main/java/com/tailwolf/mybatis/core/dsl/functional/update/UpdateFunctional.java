package com.tailwolf.mybatis.core.dsl.functional.update;

import java.io.Serializable;

@FunctionalInterface
public interface UpdateFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
