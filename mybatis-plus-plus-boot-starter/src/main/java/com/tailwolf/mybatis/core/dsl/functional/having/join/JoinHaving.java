package com.tailwolf.mybatis.core.dsl.functional.having.join;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.node.HavingNode;

import java.util.LinkedList;

/**
 * having条件
 * @author tailwolf
 * @date 2020-08-20
 */
public class JoinHaving<T, E> {
    /**
     * 条件节点
     */
    private LinkedList<HavingNode> havingNodeQueue = new LinkedList<>();

    public JoinHaving<T,E> like(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> like(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> like(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> like(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LIKE, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> ne(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> ne(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> ne(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> ne(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.NE, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> gt(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> gt(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> gt(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> gt(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GT, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> ge(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> ge(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> ge(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> ge(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.GE, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> lt(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> lt(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> lt(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> lt(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LT, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> eq(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> eq(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> eq(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> eq(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.EQ, havingJoinConditionFunctional, havingFromConditionFunctional));
        return this;
    }

    public JoinHaving<T,E> le(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingFromConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> le(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, Object value){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingJoinConditionFunctional, value));
        return this;
    }
    public JoinHaving<T,E> le(JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional, JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingFromConditionFunctional, havingJoinConditionFunctional));
        return this;
    }
    public JoinHaving<T,E> le(JoinHavingConditionSecondFunctional<E> havingJoinConditionFunctional, JoinHavingConditionFirstFunctional<T> havingFromConditionFunctional){
        havingNodeQueue.add(HavingNode.newInstance(MontageSqlConstant.LE, havingJoinConditionFunctional, havingFromConditionFunctional));
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
