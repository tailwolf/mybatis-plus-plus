package com.tailwolf.mybatis.core.dsl.functional.having;

import java.io.Serializable;

/**
 * @author tailwolf
 * @date 2020-08-20
 */
@FunctionalInterface
public interface HavingConditionFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
