package com.tailwolf.mybatis.core.dsl.functional.where.join;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.functional.where.FromConditionFunctional;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;

import java.util.LinkedList;

/**
 * 连表嵌套条件构造器
 * @author tailwolf
 * @date 2020-08-16
 */
public class JoinCondition<T,E> {
    /**
     * 条件节点
     */
    private LinkedList<ConditionNode> conditionsQueue = new LinkedList<>();

    public JoinCondition<T,E> like(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LIKE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
    public JoinCondition<T,E> like(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LIKE, joinConditionFunctional, fromConditionFunctional));
        return this;
    }

    public JoinCondition<T,E> ne(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.NE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> ne(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.NE, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
    public JoinCondition<T,E> ne(FromConditionFunctional<T> fromConditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.NE, fromConditionFunctional, value));
        return this;
    }

    public JoinCondition<T,E> gt(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GT, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> gt(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GT, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
    public JoinCondition<T,E> gt(FromConditionFunctional<T> fromConditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GT, fromConditionFunctional, value));
        return this;
    }

    public JoinCondition<T,E> ge(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> ge(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GE, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
public JoinCondition<T,E> ge(FromConditionFunctional<T> fromConditionFunctional, Object value){
    conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.GE, fromConditionFunctional, value));
    return this;
}

    public JoinCondition<T,E> lt(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LT, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> lt(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LT, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
    public JoinCondition<T,E> lt(FromConditionFunctional<T> fromConditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LT, fromConditionFunctional, value));
        return this;
    }

    public JoinCondition<T,E> eq(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.EQ, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> eq(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.EQ, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
    public JoinCondition<T,E> eq(FromConditionFunctional<T> fromConditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.EQ, fromConditionFunctional, value));
        return this;
    }

    public JoinCondition<T,E> le(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }
//    public JoinCondition<T,E> le(JoinConditionFunctional<E> joinConditionFunctional, FromConditionFunctional<T> fromConditionFunctional){
//        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LE, joinConditionFunctional, fromConditionFunctional));
//        return this;
//    }
    public JoinCondition<T,E> le(FromConditionFunctional<T> fromConditionFunctional, Object value){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.LE, fromConditionFunctional, value));
        return this;
    }

    public JoinCondition<T, E> or(){
        conditionsQueue.add(ConditionNode.newInstance(MontageSqlConstant.OR, null, null));
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
}
