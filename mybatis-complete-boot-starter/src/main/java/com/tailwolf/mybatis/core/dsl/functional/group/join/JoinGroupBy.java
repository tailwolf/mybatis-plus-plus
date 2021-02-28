package com.tailwolf.mybatis.core.dsl.functional.group.join;

import com.tailwolf.mybatis.core.dsl.node.GroupByNode;

import java.util.LinkedList;

/**
 * group by查询
 * @author tailwolf
 * @date 2020-06-22
 */
public class JoinGroupBy<T, E> {
    /**
     * 条件节点
     */
    private LinkedList<GroupByNode> conditionsQueue = new LinkedList<>();

    /**
     * 选择列名
     * @param groupByFromFunctional
     * @return
     */
    public JoinGroupBy<T, E> column(JoinGroupByFirstFunctional<T> groupByFromFunctional) {
        conditionsQueue.add(GroupByNode.newInstance(groupByFromFunctional));
        return this;
    }

    /**
     * 选择列名
     * @param groupByJoinFunctional
     * @return
     */
    public JoinGroupBy<T, E> column(JoinGroupBySecondFunctional<E> groupByJoinFunctional) {
        conditionsQueue.add(GroupByNode.newInstance(groupByJoinFunctional));
        return this;
    }

    public LinkedList<GroupByNode> getConditionsQueue() {
        return conditionsQueue;
    }

    public void setConditionsQueue(LinkedList<GroupByNode> conditionsQueue) {
        this.conditionsQueue = conditionsQueue;
    }

    public void clean(){
        this.conditionsQueue.clear();
    }
}
