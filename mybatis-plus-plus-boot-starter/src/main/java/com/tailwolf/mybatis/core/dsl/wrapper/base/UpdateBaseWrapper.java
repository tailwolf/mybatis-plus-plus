package com.tailwolf.mybatis.core.dsl.wrapper.base;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.ConditionInterface;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import net.sf.cglib.proxy.Enhancer;
import com.tailwolf.mybatis.core.dsl.functional.where.Condition;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.dsl.node.ExistsOrNotExistsNode;
import com.tailwolf.mybatis.core.dsl.node.InOrNotInNode;
import com.tailwolf.mybatis.core.dsl.node.OrderByNode;
import com.tailwolf.mybatis.core.proxy.CommonQueryHandler;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.util.*;

/**
 * 查询接口
 * @author tailwolf
 * @date 2020-08-14
 */
public abstract class UpdateBaseWrapper<T>  extends BaseWrapper {
    protected T entity;

    protected T proxyEntity;

    protected Condition<T> condition = new Condition<>();

    /**
     * 页面总记录数
     */
    private Integer pageTotal;

    /**
     * where条件队列
     */
    private NodeIterator<ConditionNode> whereConditionsQueue = new NodeIterator<>();

    private NodeIterator<InOrNotInNode> InOrNotInNodeQueue = new NodeIterator<>();

    private NodeIterator<ExistsOrNotExistsNode> existOrNotExistQueue = new NodeIterator<>();

    /**
     * 排序队列
     */
    private NodeIterator<OrderByNode> orderConditionsQueue = new NodeIterator<>();

    protected void setProxyEntity(Object subInstance){
        Object property = ReflectionUtil.getProperty(subInstance, "entity");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(property.getClass());
        enhancer.setCallback(CommonQueryHandler.getInterceptor());
        this.proxyEntity = (T)enhancer.create();
    }

    public void orCondition(ConditionInterface<T> conditionInterface){
        conditionInterface.condition(this.condition);
        LinkedList<ConditionNode> conditionsQueue = this.condition.getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_END, null, null));
        }
        this.condition.clean();
    }

    public void andCondition(ConditionInterface<T> conditionInterface){
        conditionInterface.condition(this.condition);
        LinkedList<ConditionNode> conditionsQueue = this.condition.getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_END, null, null));
        }
        this.condition.clean();
    }

    public NodeIterator<ConditionNode> getWhereConditionsQueue() {
        return whereConditionsQueue;
    }

    public void setWhereConditionsQueue(NodeIterator<ConditionNode> whereConditionsQueue) {
        this.whereConditionsQueue = whereConditionsQueue;
    }

    public NodeIterator<InOrNotInNode> getInOrNotInNodeQueue() {
        return InOrNotInNodeQueue;
    }

    public void setInOrNotInNodeQueue(NodeIterator<InOrNotInNode> inOrNotInNodeQueue) {
        InOrNotInNodeQueue = inOrNotInNodeQueue;
    }

    public NodeIterator<OrderByNode> getOrderConditionsQueue() {
        return orderConditionsQueue;
    }

    public void setOrderConditionsQueue(NodeIterator<OrderByNode> orderConditionsQueue) {
        this.orderConditionsQueue = orderConditionsQueue;
    }

    public NodeIterator<ExistsOrNotExistsNode> getExistOrNotExistQueue() {
        return existOrNotExistQueue;
    }

    public void setExistOrNotExistQueue(NodeIterator<ExistsOrNotExistsNode> existOrNotExistQueue) {
        this.existOrNotExistQueue = existOrNotExistQueue;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public T getEntity() {
        return entity;
    }

    public T getProxyEntity() {
        return proxyEntity;
    }
}
