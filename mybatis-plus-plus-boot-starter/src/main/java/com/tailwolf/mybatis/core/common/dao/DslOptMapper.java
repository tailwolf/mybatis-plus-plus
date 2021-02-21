package com.tailwolf.mybatis.core.common.dao;

import org.apache.ibatis.annotations.Param;
import com.tailwolf.mybatis.core.dsl.wrapper.base.QueryBaseWrapper;

import java.util.List;

/**
 * 自定义dsl操作Mapper
 * @author tailwolf
 * @date 2020-08-23
 */
public interface DslOptMapper extends java.io.Serializable{

    /**
     * dsl查询
     * @param query
     * @param <T>
     * @return
     */
    <T> List<T> joinQuery(@Param("dslWrapper") QueryBaseWrapper query, @Param("returnEntity") Class clazz);
}
