package com.tailwolf.mybatis.core.dsl.wrapper;

import com.tailwolf.mybatis.core.dsl.wrapper.base.QueryBaseWrapper;
import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.ConditionInterface;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.dsl.functional.group.entity.GroupByFunctional;
import com.tailwolf.mybatis.core.dsl.functional.having.entity.Having;
import com.tailwolf.mybatis.core.dsl.functional.having.entity.HavingConditionInterface;
import com.tailwolf.mybatis.core.dsl.functional.order.OrderByFunctional;
import com.tailwolf.mybatis.core.dsl.functional.select.entity.SelectFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.Condition;
import com.tailwolf.mybatis.core.dsl.functional.where.EntityConditionFunctional;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.paging.Limiter;
import com.tailwolf.mybatis.core.dsl.node.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 实体类查询组件
 * @author tailwolf
 * @date 2020-08-14
 */
public class EntityQuery<T> extends QueryBaseWrapper implements Serializable {
    private T entity;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页面序号
     */
    private Integer currentPage;

    private Condition<T> condition = new Condition<>();

    private NodeIterator<SelectNode> selectNodeQueue = new NodeIterator<>();

    private NodeIterator<GroupByNode> groupByNodeQueue = new NodeIterator<>();

    private NodeIterator<HavingNode> havingNodeQueue = new NodeIterator<>();

    private Having<T> having = new Having<>();

    public EntityQuery(){
    }

    public EntityQuery(T entity){
        this.entity = entity;
    }

    public T getEntity() {
        return (T)entity;
    }

    public EntityQuery<T> setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    /**
     * 降序
     * @param orderByFunctional
     * @return
     */
    public EntityQuery<T> desc(OrderByFunctional<T> orderByFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.DESC, orderByFunctional));
        return this;
    }

    /**
     * 升序
     * @param orderByFunctional
     * @return
     */
    public EntityQuery<T> asc(OrderByFunctional<T> orderByFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.ASC, orderByFunctional));
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
    public EntityQuery<T> like(EntityConditionFunctional<T> conditionFunctional, String value){
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
    public EntityQuery<T> ne(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> eq(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> gt(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> ge(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> lt(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> le(EntityConditionFunctional<T> conditionFunctional, Object value){
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
    public EntityQuery<T> isNull(EntityConditionFunctional<T> conditionFunctional){
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
    public EntityQuery<T> isNotNull(EntityConditionFunctional<T> conditionFunctional){
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
    public EntityQuery<T> in(EntityConditionFunctional<T> conditionFunctional, Collection collection){
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
    public EntityQuery<T> in(EntityConditionFunctional<T> conditionFunctional, Object...obj){
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
    public EntityQuery<T> notIn(EntityConditionFunctional<T> conditionFunctional, Collection collection){
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
    public EntityQuery<T> notIn(EntityConditionFunctional<T> conditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, conditionFunctional, obj));
        return this;
    }

    /**
     * or条件构造
     * @return
     */
    public EntityQuery<T> or(ConditionInterface<T> conditionInterface){
        conditionInterface.condition(this.getCondition());
        LinkedList<ConditionNode> conditionsQueue = this.getCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_END, null, null));
        }
        this.getCondition().clean();
        return this;
    }

    public EntityQuery<T> or(){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR, null, null));
        return this;
    }

    /**
     * and条件构造
     * @return
     */
    public EntityQuery<T> and(ConditionInterface<T> conditionInterface){
        conditionInterface.condition(this.getCondition());
        LinkedList<ConditionNode> conditionsQueue = this.getCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_END, null, null));
        }
        this.getCondition().clean();
        return this;
    }

    /**
     * select字段
     * @param selectFunctionals
     * @return
     */
    @SafeVarargs
    public final EntityQuery<T> select(SelectFunctional<T>...selectFunctionals){
        if(selectFunctionals.length < 1){
            throw new MybatisCompleteRuntimeException("无select字段！");
        }
        List<SelectNode> selectNodeList = new LinkedList<>();
        Arrays.stream(selectFunctionals).forEach(selectFunctional -> {
            selectNodeList.add(SelectNode.newInstance(selectFunctional, null));
        });
        this.getSelectNodeQueue().addAll(selectNodeList);
        return this;
    }

    public EntityQuery<T> exists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityQuery<T> notExists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, null));
        }
        return this;
    }

    public EntityQuery<T> exists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, values));
        }
        return this;
    }

    public EntityQuery<T> notExists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, values));
        }
        return this;
    }

    @SafeVarargs
    public final EntityQuery<T> groupBy(GroupByFunctional<T>...groupByInterfaces){
        if(groupByInterfaces.length < 1){
            throw new MybatisCompleteRuntimeException("无group字段！");
        }
        List<GroupByNode> groupNodeList = new LinkedList<>();
        Arrays.stream(groupByInterfaces).forEach(groupByInterface -> {
            groupNodeList.add(GroupByNode.newInstance(groupByInterface));
        });
        this.getGroupByNodeQueue().addAll(groupNodeList);
        return this;
    }

    public EntityQuery<T> having(HavingConditionInterface<T> havingConditionInterface){
        havingConditionInterface.having(this.getHaving());
        LinkedList<HavingNode> havingNodeQueue = this.getHaving().getHavingNodeQueue();
        if(!havingNodeQueue.isEmpty()){
            this.havingNodeQueue.addAll(havingNodeQueue);
        }
        this.getHaving().clean();
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public EntityQuery<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public EntityQuery<T> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public Condition<T> getCondition() {
        return condition;
    }

    public void setCondition(Condition<T> condition) {
        this.condition = condition;
    }

    @Override
    public Limiter createLimiter() {
        return new Limiter(currentPage, pageSize);
    }

    public NodeIterator<SelectNode> getSelectNodeQueue() {
        return selectNodeQueue;
    }

    public void setSelectNodeQueue(NodeIterator<SelectNode> selectNodeQueue) {
        this.selectNodeQueue = selectNodeQueue;
    }

    public NodeIterator<GroupByNode> getGroupByNodeQueue() {
        return groupByNodeQueue;
    }

    public void setGroupByNodeQueue(NodeIterator<GroupByNode> groupByNodeQueue) {
        this.groupByNodeQueue = groupByNodeQueue;
    }

    public NodeIterator<HavingNode> getHavingNodeQueue() {
        return havingNodeQueue;
    }

    public void setHavingNodeQueue(NodeIterator<HavingNode> havingNodeQueue) {
        this.havingNodeQueue = havingNodeQueue;
    }

    public Having<T> getHaving() {
        return having;
    }

    public void setHaving(Having<T> having) {
        this.having = having;
    }
}
