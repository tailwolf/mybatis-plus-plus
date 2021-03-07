package com.tailwolf.mybatis.core.dsl.wrapper;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.ConditionInterface;
import com.tailwolf.mybatis.core.dsl.functional.update.UpdateFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.Condition;
import com.tailwolf.mybatis.core.dsl.functional.where.EntityConditionFunctional;
import com.tailwolf.mybatis.core.dsl.wrapper.base.UpdateBaseWrapper;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.dsl.node.ExistsOrNotExistsNode;
import com.tailwolf.mybatis.core.dsl.node.InOrNotInNode;
import com.tailwolf.mybatis.core.dsl.node.SetNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 实体类操作服务
 * @author tailwolf
 * @date 2020-09-02
 */
public class EntityUpdate<T> extends UpdateBaseWrapper<T> {
    private Condition<T> condition = new Condition<>();

    /**
     * 设置字段值
     */
    private NodeIterator<SetNode> setNodeQueue = new NodeIterator<>();

    public EntityUpdate(){
    }

    public EntityUpdate(T entity){
        this.entity = entity;
    }

    public T getEntity() {
        return (T)this.entity;
    }

    public EntityUpdate<T> setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    /**
     * 设置更新字段
     * @param updateFunctional
     * @param value
     * @return
     */
    public EntityUpdate<T> set(UpdateFunctional<T> updateFunctional, Object value){
        this.getSetNodeQueue().add(SetNode.newInstance(updateFunctional, value));
        return this;
    }

    /**
     * 模糊查询
     * @param conditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public EntityUpdate<T> like(EntityConditionFunctional<T> conditionFunctional, String value){
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
    public EntityUpdate<T> ne(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> eq(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> gt(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> ge(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> lt(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> le(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityUpdate<T> isNull(EntityConditionFunctional<T> conditionFunctional){
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
    public EntityUpdate<T> isNotNull(EntityConditionFunctional<T> conditionFunctional){
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
    public EntityUpdate<T> in(EntityConditionFunctional<T> conditionFunctional, Collection collection){
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
    public EntityUpdate<T> in(EntityConditionFunctional<T> conditionFunctional, Object...obj){
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
    public EntityUpdate<T> notIn(EntityConditionFunctional<T> conditionFunctional, Collection collection){
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
    public EntityUpdate<T> notIn(EntityConditionFunctional<T> conditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, conditionFunctional, obj));
        return this;
    }

    /**
     * or条件构造
     * @return
     */
    public EntityUpdate<T> or(ConditionInterface<T> conditionInterface){
        this.orCondition(conditionInterface);
        return this;
    }

    /**
     * and条件构造
     * @return
     */
    public EntityUpdate<T> and(ConditionInterface<T> conditionInterface){
        this.andCondition(conditionInterface);
        return this;
    }

    public EntityUpdate<T> exists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityUpdate<T> notExists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityUpdate<T> exists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, values));
        }
        return this;
    }

    public EntityUpdate<T> notExists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, values));
        }
        return this;
    }

    public Condition<T> getCondition() {
        return condition;
    }

    public void setCondition(Condition<T> condition) {
        this.condition = condition;
    }

//    public NodeIterator<ConditionNode> getWhereConditionsQueue() {
//        return whereConditionsQueue;
//    }
//
//    public void setWhereConditionsQueue(NodeIterator<ConditionNode> whereConditionsQueue) {
//        this.whereConditionsQueue = whereConditionsQueue;
//    }


    public NodeIterator<SetNode> getSetNodeQueue() {
        return setNodeQueue;
    }

    public void setSetNodeQueue(NodeIterator<SetNode> setNodeQueue) {
        this.setNodeQueue = setNodeQueue;
    }
}
