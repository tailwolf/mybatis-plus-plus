package com.tailwolf.mybatis.core.dsl.functional.where;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;

import java.util.LinkedList;

/**
 * 嵌套条件构造器
 * @author tailwolf
 * @date 2020-05-29
 */
public class Condition<R> {

    /**
     * 条件节点
     */
    private LinkedList<ConditionNode> conditionsQueue = new LinkedList<>();

    /**
     * from对象
     */
    private Object fromObject;

    /**
     * join对象
     */
    private Object joinObject;

    public Condition<R> like(EntityConditionFunctional<R> conditionFunctional, String value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LIKE, conditionFunctional, value));
        return this;
    }
    public Condition<R> ne(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.NE, conditionFunctional, value));
        return this;
    }
    public Condition<R> gt(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GT, conditionFunctional, value));
        return this;
    }
    public Condition<R> ge(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GE, conditionFunctional, value));
        return this;
    }
    public Condition<R> lt(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LT, conditionFunctional, value));
        return this;
    }
    public Condition<R> eq(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.EQ, conditionFunctional, value));
        return this;
    }
    public Condition<R> le(EntityConditionFunctional<R> conditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LE, conditionFunctional, value));
        return this;
    }

    public LinkedList<ConditionNode> getConditionsQueue() {
        return conditionsQueue;
    }

    public void setConditionsQueue(LinkedList<ConditionNode> conditionsQueue) {
        this.conditionsQueue = conditionsQueue;
    }

    public void clean(){
        this.conditionsQueue.clear();
    }

    public Object getFromObject() {
        return fromObject;
    }

    public void setFromObject(Object fromObject) {
        this.fromObject = fromObject;
    }

    public Object getJoinObject() {
        return joinObject;
    }

    public void setJoinObject(Object joinObject) {
        this.joinObject = joinObject;
    }
}
