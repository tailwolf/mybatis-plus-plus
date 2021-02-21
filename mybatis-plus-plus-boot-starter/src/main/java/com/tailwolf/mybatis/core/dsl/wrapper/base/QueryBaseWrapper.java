package com.tailwolf.mybatis.core.dsl.wrapper.base;

import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.paging.Limiter;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.dsl.node.ExistsOrNotExistsNode;
import com.tailwolf.mybatis.core.dsl.node.InOrNotInNode;
import com.tailwolf.mybatis.core.dsl.node.OrderByNode;

/**
 * dsl查询基础包装类
 * @author tailwolf
 * @date 2020-09-02
 */
public abstract class QueryBaseWrapper extends BaseWrapper {

    /**
     * 页面总记录数
     */
    private Integer pageTotal;

    /**
     * where条件队列
     */
    private NodeIterator<ConditionNode> whereConditionsQueue = new NodeIterator<>();

    private NodeIterator<InOrNotInNode> inOrNotInNodeQueue = new NodeIterator<>();

    private NodeIterator<ExistsOrNotExistsNode> existOrNotExistQueue = new NodeIterator<>();

    /**
     * 排序队列
     */
    private NodeIterator<OrderByNode> orderConditionsQueue = new NodeIterator<>();

    public NodeIterator<ConditionNode> getWhereConditionsQueue() {
        return whereConditionsQueue;
    }

    public void setWhereConditionsQueue(NodeIterator<ConditionNode> whereConditionsQueue) {
        this.whereConditionsQueue = whereConditionsQueue;
    }

    public NodeIterator<OrderByNode> getOrderConditionsQueue() {
        return orderConditionsQueue;
    }

    public void setOrderConditionsQueue(NodeIterator<OrderByNode> orderConditionsQueue) {
        this.orderConditionsQueue = orderConditionsQueue;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public abstract Limiter createLimiter();

    public static final class ConditionKeyworks {
        public static final String DESC = "desc";
        public static final String ASC = "asc";

        public static final String LE = "le";
        public static final String LT = "lt";
        public static final String GE = "ge";
        public static final String GT = "gt";
        public static final String NE = "ne";
        public static final String LIKE = "like";

        public static final String AND = "and";
        public static final String OR = "or";
        public static final String AND_START = "and_start";
        public static final String AND_END = "and_end";
        public static final String OR_START = "or_start";
        public static final String OR_END = "or_end";
    }

    public NodeIterator<InOrNotInNode> getInOrNotInNodeQueue() {
        return inOrNotInNodeQueue;
    }

    public void setInOrNotInNodeQueue(NodeIterator<InOrNotInNode> inOrNotInNodeQueue) {
        this.inOrNotInNodeQueue = inOrNotInNodeQueue;
    }

    public NodeIterator<ExistsOrNotExistsNode> getExistOrNotExistQueue() {
        return existOrNotExistQueue;
    }

    public void setExistOrNotExistQueue(NodeIterator<ExistsOrNotExistsNode> existOrNotExistQueue) {
        this.existOrNotExistQueue = existOrNotExistQueue;
    }
}
