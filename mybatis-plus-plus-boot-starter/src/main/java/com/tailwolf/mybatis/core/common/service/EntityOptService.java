package com.tailwolf.mybatis.core.common.service;

import org.springframework.transaction.annotation.Transactional;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityUpdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 实体类操作服务
 * @author tailwolf
 * @date 2020-05-29
 */
@Transactional
public interface EntityOptService<T> {

    /**
     * 通过实体类查询实体类列表
     * @param t
     * @return
     */
    List<T> findList(T t);

    /**
     * 通过主键查询实体类
     * @param serializable
     * @return
     */
    T findByPk(Serializable serializable);

    /**
     * 获得第一个实体类
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * 插入一条记录
     * @return
     */
    boolean insert(T t);

    /**
     * 更新一条记录
     * @return
     */
    boolean updateByPk(T t);

    /**
     * 批量插入记录
     * @param collection
     * @return
     */
    boolean insertBatch(Collection<T> collection);

    /**
     * 通过主键批量更新
     * @param collection
     * @return
     */
    boolean updateBatchByPk(Collection<T> collection);

    /**
     * 批量插入或者更新。有主键且主键有值的就插入，没的就更新
     * @param collection
     * @return
     */
    boolean insertOrUpdateBatch(Collection<T> collection);

    /**
     * 插入或者更新。有主键且主键有值的就插入，没的就更新
     * @param t
     * @return
     */
    boolean insertOrUpdate(T t);

    /**
     * 通过主键删除
     * @return
     */
    boolean deleteByPk(Serializable serializable);

    /**
     * 通过主键批量删除
     * @param collection
     * @return
     */
    boolean deleteBatchByPk(Collection<? extends Serializable> collection);

    /**
     * 通过实体类属性删除记录。注意，这里不是删除单个实体类，是删除符合实体类属性的所有记录
     * @return
     */
    boolean delete(T t);

    /**
     * dsl查询单表实体类
     * @param entityQuery
     * @return
     */
    List<T> dslQuery(EntityQuery<T> entityQuery);

    /**
     * dsl查询一个对象
     * @param entityQuery
     * @return
     */
    T dslQueryOne(EntityQuery<T> entityQuery);

    /**
     * dsl删除单表实体类
     * @param entityDelete
     * @return
     */
    int dslDelete(EntityDelete<T> entityDelete);

    /**
     * dsl更新单表实体类
     * @param entityUpdate
     * @return
     */
    int dslUpdate(EntityUpdate<T> entityUpdate);
}
