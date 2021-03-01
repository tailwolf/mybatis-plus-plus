package com.tailwolf.mybatis.core.dsl.wrapper;

import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.functional.create.CreateObjectInterface;
import com.tailwolf.mybatis.core.dsl.functional.where.Condition;
import com.tailwolf.mybatis.core.dsl.wrapper.base.QueryBaseWrapper;
import com.tailwolf.mybatis.core.dsl.wrapper.base.UpdateBaseWrapper;
import com.tailwolf.mybatis.core.dsl.functional.group.join.JoinGroupBy;
import com.tailwolf.mybatis.core.dsl.functional.group.join.JoinGroupByInterface;
import com.tailwolf.mybatis.core.dsl.functional.having.join.JoinHaving;
import com.tailwolf.mybatis.core.dsl.functional.having.join.JoinHavingConditionInterface;
import com.tailwolf.mybatis.core.dsl.functional.order.join.OrderByFirstFunctional;
import com.tailwolf.mybatis.core.dsl.functional.order.join.OrderBySecondFunctional;
import com.tailwolf.mybatis.core.dsl.functional.select.join.JoinSelect;
import com.tailwolf.mybatis.core.dsl.functional.select.join.JoinSelectInterface;
import com.tailwolf.mybatis.core.dsl.functional.where.FromConditionFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.join.JoinCondition;
import com.tailwolf.mybatis.core.dsl.functional.where.join.JoinConditionFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.join.JoinConditionInterface;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.paging.Limiter;
import com.tailwolf.mybatis.core.dsl.node.*;
import com.tailwolf.mybatis.core.proxy.CommonQueryHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 实体类查询组件
 * @author tailwolf
 * @date 2020-08-14
 */
public class JoinQuery<T, E> extends QueryBaseWrapper implements Serializable {

    private static final String FROM_OBJECT = "fromObject";

    private static final String JOIN_OBJECT = "joinObject";

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页面序号
     */
    private Integer currentPage;

    /**
     * from对象
     */
    private T fromObject;

    /**
     * join对象
     */
    private E joinObject;

    /**
     * join关键字
     */
    private String joinKeyWord;

    private JoinCondition<T, E> joinCondition = new JoinCondition<>();

    private JoinSelect<T, E> selectCondition = new JoinSelect<>();

    private JoinGroupBy<T, E> groupBy = new JoinGroupBy<>();

    private JoinHaving<T, E> having = new JoinHaving<>();

    /**
     * 数据源名字
     */
    private String dataSourceName;

    /**
     * on条件队列
     */
    private NodeIterator<ConditionNode> onConditionsQueue = new NodeIterator<>();

    /**
     * select字段队列
     */
    private NodeIterator<SelectNode> selectNodeQueue = new NodeIterator<>();

    /**
     * group by字段队列
     */
    private NodeIterator<GroupByNode> groupByNodeQueue = new NodeIterator<>();

    /**
     * having字段队列
     */
    private NodeIterator<HavingNode> havingNodeNodeQueue = new NodeIterator<>();

    public JoinQuery(){}

    public JoinQuery<T, E> select(JoinSelectInterface<E, T> selectConditionFunctional){
        selectConditionFunctional.selectCondition(this.getSelectCondition());
        LinkedList<SelectNode> conditionsQueue = this.getSelectCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.selectNodeQueue.addAll(conditionsQueue);
        }
        this.getSelectCondition().clean();
        return this;
    }

    /**
     * from表
     * @param fromObject    实体类对象
     * @return
     */
    public JoinQuery<T, E> from(T fromObject) {
        this.fromObject = fromObject;
        return this;
    }

    /**
     * from表
     * @param createObjectInterface    函数对象
     * @return
     */
    public JoinQuery<T, E> from(CreateObjectInterface<T> createObjectInterface) {
        T o = createObjectInterface.create();
        this.fromObject = o;
        return this;
    }

    public JoinQuery<T, E> from(T table1, E table2) {
        this.fromObject = table1;
        this.joinObject = table2;
        this.joinKeyWord = MontageSqlConstant.BLANK;
        return this;
    }

    public JoinQuery<T, E> from(CreateObjectInterface<T> table1, CreateObjectInterface<E> table2) {
        this.fromObject = table1.create();
        this.joinObject = table2.create();
        this.joinKeyWord = MontageSqlConstant.BLANK;
        return this;
    }

    /**
     * 左join表
     * @param joinObject
     * @return
     */
    public JoinQuery<T, E> leftJoin(E joinObject) {
        this.joinObject = joinObject;
        this.joinKeyWord = MontageSqlConstant.LEFT_JOIN;
        return this;
    }

    /**
     * 右join表
     * @param joinObject
     * @return
     */
    public JoinQuery<T, E> rightJoin(E joinObject) {
        this.joinObject = joinObject;
        this.joinKeyWord = MontageSqlConstant.RIGHT_JOIN;
        return this;
    }

    /**
     * 内联表
     * @param joinObject
     * @return
     */
    public JoinQuery<T, E> innerJoin(E joinObject) {
        this.joinObject = joinObject;
        this.joinKeyWord = MontageSqlConstant.INNER_JOIN;
        return this;
    }

    /**
     * 左join表
     * @param createObjectInterface
     * @return
     */
    public JoinQuery<T, E> leftJoin(CreateObjectInterface<E> createObjectInterface) {
        this.joinObject = createObjectInterface.create();
        this.joinKeyWord = MontageSqlConstant.LEFT_JOIN;
        return this;
    }

    /**
     * 右join表
     * @param createObjectInterface
     * @return
     */
    public JoinQuery<T, E> rightJoin(CreateObjectInterface<E> createObjectInterface) {
        this.joinObject = createObjectInterface.create();
        this.joinKeyWord = MontageSqlConstant.RIGHT_JOIN;
        return this;
    }

    /**
     * 内联表
     * @param createObjectInterface
     * @return
     */
    public JoinQuery<T, E> innerJoin(CreateObjectInterface<E> createObjectInterface) {
        this.joinObject = createObjectInterface.create();
        this.joinKeyWord = MontageSqlConstant.INNER_JOIN;
        return this;
    }

    /**
     * on条件
     * @param joinConditionInterface
     * @return
     */
    public JoinQuery<T, E> on(JoinConditionInterface<T,E> joinConditionInterface){
        joinConditionInterface.joinCondition(this.getJoinCondition());
        LinkedList<ConditionNode> conditionsQueue = this.getJoinCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.onConditionsQueue.addAll(conditionsQueue);
        }
        this.getJoinCondition().clean();
        return this;
    }

    /**
     * 降序
     * @param orderByFirstFunctional
     * @return
     */
    public JoinQuery<T, E> desc(OrderByFirstFunctional<T> orderByFirstFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.DESC, orderByFirstFunctional));
        return this;
    }

    /**
     * 降序
     * @param orderBySecondFunctional
     * @return
     */
    public JoinQuery<T, E> desc(OrderBySecondFunctional<E> orderBySecondFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.DESC, orderBySecondFunctional));
        return this;
    }

    /**
     * 升序
     * @param orderByFirstFunctional
     * @return
     */
    public JoinQuery<T, E> asc(OrderByFirstFunctional<T> orderByFirstFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.ASC, orderByFirstFunctional));
        return this;
    }

    /**
     * 升序
     * @param joinConditionFunctional
     * @return
     */
    public JoinQuery<T, E> asc(JoinConditionFunctional<E> joinConditionFunctional){
        this.getOrderConditionsQueue().add(OrderByNode.newInstance(MontageSqlConstant.ASC, joinConditionFunctional));
        return this;
    }

    /**
     * 模糊查询
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> like(FromConditionFunctional<T> fromConditionFunctional, String value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LIKE, fromConditionFunctional, value));
        return this;
    }

    /**
     * 模糊查询
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> like(JoinConditionFunctional<E> joinConditionFunctional, String value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LIKE, joinConditionFunctional, value));
        return this;
    }

    /**
     * 不等于
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> ne(FromConditionFunctional<T> fromConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.NE, fromConditionFunctional, value));
        return this;
    }

    /**
     * 不等于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> ne(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.NE, joinConditionFunctional, value));
        return this;
    }

    /**
     * 不等于
     * @param fromConditionFunctional
     *        字段名
     * @param joinConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> ne(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.NE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }

    /**
     * 等于
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> eq(FromConditionFunctional<T> fromConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.EQ, fromConditionFunctional, value));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> eq(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.EQ, joinConditionFunctional, value));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param fromConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> eq(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.EQ, fromConditionFunctional, joinConditionFunctional));
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
    public JoinQuery<T, E> gt(FromConditionFunctional<T> conditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GT, conditionFunctional, value));
        return this;
    }

    /**
     * 大于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> gt(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GT, joinConditionFunctional, value));
        return this;
    }

    /**
     * 大于
     * @param fromConditionFunctional
     *        字段名
     * @param joinConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> gt(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GT, fromConditionFunctional, joinConditionFunctional));
        return this;
    }

    /**
     * 大于等于
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> ge(FromConditionFunctional<T> fromConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GE, fromConditionFunctional, value));
        return this;
    }

    /**
     * 大于等于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> ge(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GE, joinConditionFunctional, value));
        return this;
    }

    /**
     * 大于等于
     * @param fromConditionFunctional
     *        字段名
     * @param joinConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> ge(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.GE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }

    /**
     * 小于
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> lt(FromConditionFunctional<T> fromConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LT, fromConditionFunctional, value));
        return this;
    }

    /**
     * 小于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> lt(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LT, joinConditionFunctional, value));
        return this;
    }

    /**
     * 小于
     * @param fromConditionFunctional
     *        字段名
     * @param joinConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> lt(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LT, fromConditionFunctional, joinConditionFunctional));
        return this;
    }

    /**
     * 小于等于
     * @param fromConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> le(FromConditionFunctional<T> fromConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LE, fromConditionFunctional, value));
        return this;
    }

    /**
     * 小于等于
     * @param joinConditionFunctional
     *        字段名
     * @param value
     *        值
     * @return
     */
    public JoinQuery<T, E> le(JoinConditionFunctional<E> joinConditionFunctional, Object value){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LE, joinConditionFunctional, value));
        return this;
    }

    /**
     * 小于等于
     * @param fromConditionFunctional
     *        字段名
     * @param joinConditionFunctional
     *        值
     * @return
     */
    public JoinQuery<T, E> le(FromConditionFunctional<T> fromConditionFunctional, JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.LE, fromConditionFunctional, joinConditionFunctional));
        return this;
    }

    /**
     * 等于Null
     * @param fromConditionFunctional
     *        字段名
     * @return
     */
    public JoinQuery<T, E> isNull(FromConditionFunctional<T> fromConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, fromConditionFunctional, "NULL"));
        return this;
    }

    /**
     * 等于Null
     * @param joinConditionFunctional
     *        字段名
     * @return
     */
    public JoinQuery<T, E> isNull(JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, joinConditionFunctional, "NULL"));
        return this;
    }

    /**
     * 不等于Null
     * @param fromConditionFunctional
     *        字段名
     * @return
     */
    public JoinQuery<T, E> isNotNull(FromConditionFunctional<T> fromConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, fromConditionFunctional, "NOT NULL"));
        return this;
    }

    /**
     * 不等于Null
     * @param joinConditionFunctional
     *        字段名
     * @return
     */
    public JoinQuery<T, E> isNotNull(JoinConditionFunctional<E> joinConditionFunctional){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.IS, joinConditionFunctional, "NOT NULL"));
        return this;
    }

    /**
     * 等于
     * @param fromConditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public JoinQuery<T, E> in(FromConditionFunctional<T> fromConditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, fromConditionFunctional, collection));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public JoinQuery<T, E> in(JoinConditionFunctional<E> joinConditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, joinConditionFunctional, collection));
        return this;
    }

    /**
     * 等于
     * @param fromConditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public JoinQuery<T, E> in(FromConditionFunctional<T> fromConditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, fromConditionFunctional, obj));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public JoinQuery<T, E> in(JoinConditionFunctional<E> joinConditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.IN, joinConditionFunctional, obj));
        return this;
    }

    /**
     * 等于
     * @param fromConditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public JoinQuery<T, E> notIn(FromConditionFunctional<T> fromConditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, fromConditionFunctional, collection));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param collection
     *        值
     * @return
     */
    public JoinQuery<T, E> notIn(JoinConditionFunctional<E> joinConditionFunctional, Collection collection){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, joinConditionFunctional, collection));
        return this;
    }

    /**
     * 等于
     * @param fromConditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public JoinQuery<T, E> notIn(FromConditionFunctional<T> fromConditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, fromConditionFunctional, obj));
        return this;
    }

    /**
     * 等于
     * @param joinConditionFunctional
     *        字段名
     * @param obj
     *        值
     * @return
     */
    public JoinQuery<T, E> notIn(JoinConditionFunctional<E> joinConditionFunctional, Object...obj){
        this.getInOrNotInNodeQueue().add(InOrNotInNode.newInstance(MontageSqlConstant.NOT_IN, joinConditionFunctional, obj));
        return this;
    }

    /**
     * or条件构造
     * @return
     */
    public JoinQuery<T, E> or(JoinConditionInterface<T, E> joinConditionInterface){
        joinConditionInterface.joinCondition(this.getJoinCondition());
        LinkedList<ConditionNode> conditionsQueue = this.getJoinCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR_END, null, null));
        }
        this.getJoinCondition().clean();
        return this;
    }

    /**
     * or条件构造
     * @return
     */
    public JoinQuery<T, E> or(){
        this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.OR, null, null));
        return this;
    }

    /**
     * and条件构造
     * @return
     */
    public JoinQuery<T, E> and(JoinConditionInterface<T, E> joinConditionInterface){
        joinConditionInterface.joinCondition(this.getJoinCondition());
        LinkedList<ConditionNode> conditionsQueue = this.getJoinCondition().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_START, null, null));
            this.getWhereConditionsQueue().addAll(conditionsQueue);
            this.getWhereConditionsQueue().add(ConditionNode.newInstance(MontageSqlConstant.AND_END, null, null));
        }
        this.getJoinCondition().clean();
        return this;
    }

    public JoinQuery<T, E> exists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, null));
        }
        return this;
    }

    public JoinQuery<T, E> notExists(String sqlScript){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, null));
        }
        return this;
    }

    public JoinQuery<T, E> exists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.EXISTS, sqlScript, values));
        }
        return this;
    }

    public JoinQuery<T, E> notExists(String sqlScript, Object...values){
        if(!StringUtils.isEmpty(sqlScript)){
            this.getExistOrNotExistQueue().add(ExistsOrNotExistsNode.newInstance(MontageSqlConstant.NOT_EXISTS, sqlScript, values));
        }
        return this;
    }

    public JoinQuery<T, E> groupBy(JoinGroupByInterface<T, E> groupByInterface){
        groupByInterface.groupBy(this.getGroupBy());
        LinkedList<GroupByNode> conditionsQueue = this.getGroupBy().getConditionsQueue();
        if(!conditionsQueue.isEmpty()){
            this.groupByNodeQueue.addAll(conditionsQueue);
        }
        this.getGroupBy().clean();
        return this;
    }

    /**
     * having条件
     * @param havingConditionInterface
     * @return
     */
    public JoinQuery<T, E> having(JoinHavingConditionInterface<T,E> havingConditionInterface){
        havingConditionInterface.having(this.getHaving());
        LinkedList<HavingNode> havingNodeQueue = this.getHaving().getHavingNodeQueue();
        if(!havingNodeQueue.isEmpty()){
            this.havingNodeNodeQueue.addAll(havingNodeQueue);
        }
        this.getHaving().clean();
        return this;
    }

    @Override
    public Limiter createLimiter() {
        return new Limiter(currentPage, pageSize);
    }

    public Object getFromObject() {
        return fromObject;
    }

    public void setFromObject(T fromObject) {
        this.fromObject = fromObject;
    }

    public Object getJoinObject() {
        return joinObject;
    }

    public void setJoinObject(E joinObject) {
        this.joinObject = joinObject;
    }

    public String getJoinKeyWord() {
        return joinKeyWord;
    }

    public void setJoinKeyWord(String joinKeyWord) {
        this.joinKeyWord = joinKeyWord;
    }

    public NodeIterator<ConditionNode> getOnConditionsQueue() {
        return onConditionsQueue;
    }

    public void setOnConditionsQueue(NodeIterator<ConditionNode> onConditionsQueue) {
        this.onConditionsQueue = onConditionsQueue;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public JoinQuery<T, E> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public JoinQuery<T, E> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public JoinCondition<T, E> getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(JoinCondition<T, E> joinCondition) {
        this.joinCondition = joinCondition;
    }

    public JoinQuery<T, E> setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public JoinSelect<T, E> getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(JoinSelect<T, E> selectCondition) {
        this.selectCondition = selectCondition;
    }

    public NodeIterator<SelectNode> getSelectNodeQueue() {
        return selectNodeQueue;
    }

    public void setSelectNodeQueue(NodeIterator<SelectNode> selectNodeQueue) {
        this.selectNodeQueue = selectNodeQueue;
    }

    public JoinGroupBy<T, E> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(JoinGroupBy<T, E> groupBy) {
        this.groupBy = groupBy;
    }

    public NodeIterator<GroupByNode> getGroupByNodeQueue() {
        return groupByNodeQueue;
    }

    public void setGroupByNodeQueue(NodeIterator<GroupByNode> groupByNodeQueue) {
        this.groupByNodeQueue = groupByNodeQueue;
    }

    public NodeIterator<HavingNode> getHavingNodeNodeQueue() {
        return havingNodeNodeQueue;
    }

    public void setHavingNodeNodeQueue(NodeIterator<HavingNode> havingNodeNodeQueue) {
        this.havingNodeNodeQueue = havingNodeNodeQueue;
    }

    public JoinHaving<T, E> getHaving() {
        return having;
    }

    public void setHaving(JoinHaving<T, E> having) {
        this.having = having;
    }
}
