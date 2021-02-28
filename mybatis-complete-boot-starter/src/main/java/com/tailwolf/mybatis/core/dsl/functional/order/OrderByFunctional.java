package com.tailwolf.mybatis.core.dsl.functional.order;

import java.io.Serializable;

/**
 * @author tailwolf
 * @date 2020-08-29
 */
@FunctionalInterface
public interface OrderByFunctional<R> extends Serializable {
    Object getFieldName(R r);
}
