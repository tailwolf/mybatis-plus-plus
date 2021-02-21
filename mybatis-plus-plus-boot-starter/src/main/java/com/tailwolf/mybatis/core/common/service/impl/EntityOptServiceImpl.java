package com.tailwolf.mybatis.core.common.service.impl;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.tailwolf.mybatis.core.common.dao.EntityOptMapper;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.common.service.EntityOptService;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityUpdate;
import com.tailwolf.mybatis.core.exception.MybatisPlusPlusRuntimeException;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 实体类操作服务
 * @author tailwolf
 * @date 2020-05-29
 */
@Transactional
public class EntityOptServiceImpl<T, K extends EntityOptMapper<T>> implements EntityOptService<T> {

    @Autowired
    private K entityOptMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<T> findList(T t) {
        return entityOptMapper.findList(t);
    }

    @Override
    public T findByPk(Serializable serializable) {
        return entityOptMapper.findByPk(serializable);
    }

    public T findOne(T t) {
        return entityOptMapper.findOne(t);
    }

    public boolean insert(T t) {
        int influence = entityOptMapper.insert(t);
        return influence > 0;
    }

    public boolean updateByPk(T t) {
        int influence = entityOptMapper.updateByPk(t);
        return influence > 0;
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertBatch(Collection<T> collection) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.REUSE);
        try {
            EntityOptMapper<T> mapper = sqlSession.getMapper(EntityOptMapper.class);
            for(T t: collection){
                mapper.insert(t);
            }
            sqlSession.commit(true);
        }catch (Exception ex){
            sqlSession.rollback();
            return false;
        }finally {
            sqlSession.close();
        }
        return true;
    }

    public boolean updateBatchByPk(Collection<T> collection) {
        int influence = entityOptMapper.updateBatchByPk(collection);
        return collection.size() == influence;
    }

    @Override
    public boolean insertOrUpdateBatch(Collection<T> collection) {
        Field pkProperty = ReflectionUtil.getPkProperty(collection.toArray()[0]);
        //筛选出需要更新和需要插入的
        Collection<T> insertBatchList = new ArrayList<>();
        Collection<T> updateBatchList = new ArrayList<>();
        for(T t: collection){
            boolean isUpdate = isUpdate(t, pkProperty);
            if(isUpdate){
                updateBatchList.add(t);
            }else{
                insertBatchList.add(t);
            }
        }

        //批量操作
        this.insertBatch(insertBatchList);
        entityOptMapper.updateBatchByPk(updateBatchList);
        return true;
    }

    private boolean isUpdate(T t, Field pkProperty){
        if(pkProperty == null){
            return false;
        }else{
            pkProperty.setAccessible(true);
            try {
                Object value = pkProperty.get(t);
                if(value == null){
                    return false;
                }else{
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new MybatisPlusPlusRuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public boolean insertOrUpdate(T t) {
        int influence = 0;
        Field pkProperty = ReflectionUtil.getPkProperty(t);
        boolean isUpdate = isUpdate(t, pkProperty);
        if(isUpdate){
            influence = entityOptMapper.insert(t);
        }else{
            influence = entityOptMapper.updateByPk(t);
        }

        return influence == 1;
    }

    public boolean deleteByPk(Serializable serializable) {
        int influence = entityOptMapper.deleteByPk(serializable);
        return influence > 0;
    }

    public boolean deleteBatchByPk(Collection<? extends Serializable> collection) {
        int influence = entityOptMapper.deleteBatchByPk(collection);
        return collection.size() == influence;
    }

    public boolean delete(T t) {
        int influence = entityOptMapper.delete(t);
        return influence > 0;
    }

    public List<T> dslQuery(EntityQuery<T> entityQuery) {
        return entityOptMapper.dslQuery(entityQuery);
    }

    @Override
    public T dslQueryOne(EntityQuery<T> entityQuery) {
        return entityOptMapper.dslQueryOne(entityQuery);
    }

    @Override
    public int dslDelete(EntityDelete<T> entityDelete) {
        return entityOptMapper.dslDelete(entityDelete);
    }

    @Override
    public int dslUpdate(EntityUpdate<T> entityUpdate) {
        return entityOptMapper.dslUpdate(entityUpdate);
    }
}
