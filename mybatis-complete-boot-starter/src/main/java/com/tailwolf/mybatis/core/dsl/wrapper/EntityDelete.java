package com.tailwolf.mybatis.core.dsl.wrapper;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.ConditionInterface;
import com.tailwolf.mybatis.core.dsl.functional.where.Condition;
import com.tailwolf.mybatis.core.dsl.functional.where.EntityConditionFunctional;
import com.tailwolf.mybatis.core.dsl.wrapper.base.UpdateBaseWrapper;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.dsl.node.ExistsOrNotExistsNode;
import com.tailwolf.mybatis.core.dsl.node.InOrNotInNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 删除操作的dsl包装类
 * @param <T>
 */
public class EntityDelete<T> extends UpdateBaseWrapper<T> {

    public EntityDelete(){
    }

    public EntityDelete(T entity){
        this.entity = entity;
    }

    public T getEntity() {
        return (T)this.entity;
    }

    public EntityDelete<T> setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    /**
     * where条件队列
     */
    private NodeIterator<ConditionNode> whereConditionsQueue = new NodeIterator<>();

    /**
     * 模糊查询
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> like(EntityConditionFunctional<T> conditionFunctional, String value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LIKE, conditionFunctional, value));
        return this;
    }

    /**
     * 不等于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> ne(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.NE, conditionFunctional, value));
        return this;
    }

    /**
     * 等于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> eq(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.EQ, conditionFunctional, value));
        return this;
    }

    /**
     * 大于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> gt(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GT, conditionFunctional, value));
        return this;
    }

    /**
     * 大于等于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> ge(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GE, conditionFunctional, value));
        return this;
    }

    /**
     * 小于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> lt(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LT, conditionFunctional, value));
        return this;
    }

    /**
     * 小于等于
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityDelete<T> le(EntityConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LE, conditionFunctional, value));
        return this;
    }

    /**
     * 等于Null
     * @param conditionFunctional
     *        字段名
     *        值
     * @return
     */
    public EntityDelete<T> isNull(EntityConditionFunctional<T> conditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, conditionFunctional, "NULL"));
        return this;
    }

    /**
     * 不等于Null
     * @param conditionFunctional
     *        字段名
     *        值
     * @return
     */
    public EntityDelete<T> isNotNull(EntityConditionFunctional<T> conditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, conditionFunctional, "NOT NULL"));
        return this;
    }

    /**
     * @param conditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public EntityDelete<T> in(EntityConditionFunctional<T> conditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, conditionFunctional, collection));
        return this;
    }

    /**
     * @param conditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public EntityDelete<T> in(EntityConditionFunctional<T> conditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, conditionFunctional, obj));
        return this;
    }

    /**
     * @param conditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public EntityDelete<T> notIn(EntityConditionFunctional<T> conditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, conditionFunctional, collection));
        return this;
    }

    /**
     * @param conditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public EntityDelete<T> notIn(EntityConditionFunctional<T> conditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, conditionFunctional, obj));
        return this;
    }

    public EntityDelete<T> exists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityDelete<T> notExists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityDelete<T> exists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, values));
        }
        return this;
    }

    public EntityDelete<T> notExists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, values));
        }
        return this;
    }

    /**
     * or条件构造
     * @return
     */
    public EntityDelete<T> or(ConditionInterface<T> conditionInterface){
        this.orCondition(conditionInterface);
        return this;
    }

    /**
     * and条件构造
     * @return
     */
    public EntityDelete<T> and(ConditionInterface<T> conditionInterface){
        this.andCondition(conditionInterface);
        return this;
    }

    public Condition<T> getCondition() {
        return condition;
    }

    public void setCondition(Condition<T> condition) {
        this.condition = condition;
    }

    public NodeIterator<ConditionNode> getWhereConditionsQueue() {
        return whereConditionsQueue;
    }

    public void setWhereConditionsQueue(NodeIterator<ConditionNode> whereConditionsQueue) {
        this.whereConditionsQueue = whereConditionsQueue;
    }
}
