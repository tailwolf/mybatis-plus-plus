package com.tailwolf.mybatis.core.dsl.functional.having.entity;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.functional.having.HavingConditionFunctional;
import com.tailwolf.mybatis.core.dsl.node.HavingNode;

import java.util.LinkedList;

/**
 * having条件
 * @author tailwolf
 * @date 2020-09-15
 */
public class Having<T> {
    /**
     * 条件节点
     */
    private LinkedList<HavingNode> havingNodeQueue = new LinkedList<>();

    public Having<T> like(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingConditionFunctional, value));
        return this;
    }
    public Having<T> like(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> ne(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingConditionFunctional, value));
        return this;
    }
    public Having<T> ne(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> gt(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingConditionFunctional, value));
        return this;
    }
    public Having<T> gt(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> ge(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingConditionFunctional, value));
        return this;
    }
    public Having<T> ge(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> lt(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingConditionFunctional, value));
        return this;
    }
    public Having<T> lt(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> eq(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingConditionFunctional, value));
        return this;
    }
    public Having<T> eq(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public Having<T> le(HavingConditionFunctional<T> havingConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingConditionFunctional, value));
        return this;
    }
    public Having<T> le(HavingConditionFunctional<T> havingConditionFirstFunctional, HavingConditionFunctional<T> havingConditionSecondFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingConditionFirstFunctional, havingConditionSecondFunctional));
        return this;
    }

    public LinkedList<HavingNode> getHavingNodeQueue() {
        return havingNodeQueue;
    }

    public void setHavingNodeQueue(LinkedList<HavingNode> havingNodeQueue) {
        this.havingNodeQueue = havingNodeQueue;
    }

    public void clean(){
        this.havingNodeQueue.clear();
    }
}
