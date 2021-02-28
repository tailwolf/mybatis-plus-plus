package com.tailwolf.mybatis.core.common.dao;

import org.apache.ibatis.annotations.Param;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityUpdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 实体类查询dao层
 * @author tailwolf
 * @date 2020-05-29
 */
public interface EntityOptMapper<T> extends java.io.Serializable{
    /**
     * 插入一个Entity
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 通过主键更新
     * @param t
     * @return
     */
    int updateByPk(T t);

    /**
     * 通过主键批量更新
     * @param collection
     * @return
     */
    int updateBatchByPk(Collection<T> collection);

    /**
     * 通过主键批量删除
     * @param collection
     * @return
     */
    int deleteBatchByPk(Collection<? extends Serializable> collection);

    /**
     * 删除Entity通过主键
     * @param serializable
     * @return
     */
    int deleteByPk(Serializable serializable);

    /**
     * 删除Entity
     * @param t
     * @return
     */
    int delete(T t);

    /**
     * 查询Entity集合
     * @param t
     * @return
     */
    List<T> findList(T t);

    /**
     * 查询第一个Entity
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * 通过主键查询实体类
     * @param serializable
     * @return
     */
    T findByPk(Serializable serializable);

    /**
     * dsl查询
     * @param entityQuery
     * @return
     */
    <T> List<T> dslQuery(@Param("dslWrapper") EntityQuery<T> entityQuery);

    /**
     * dsl查询一个对象
     * @param entityQuery
     * @return
     */
    T dslQueryOne(@Param("dslWrapper") EntityQuery<T> entityQuery);

    /**
     * dsl删除
     * @param entityDelete
     * @return
     */
    int dslDelete(@Param("dslWrapper") EntityDelete<T> entityDelete);

    /**
     * dsl更新
     * @param entityUpdate
     * @return
     */
    int dslUpdate(@Param("dslWrapper") EntityUpdate<T> entityUpdate);
}
