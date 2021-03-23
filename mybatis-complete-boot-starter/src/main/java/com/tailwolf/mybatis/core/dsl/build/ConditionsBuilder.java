package com.tailwolf.mybatis.core.dsl.build;

import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.wrapper.base.UpdateBaseWrapper;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;
import com.tailwolf.mybatis.core.dsl.functional.group.join.JoinGroupByFirstFunctional;
import com.tailwolf.mybatis.core.dsl.functional.group.join.JoinGroupBySecondFunctional;
import com.tailwolf.mybatis.core.dsl.functional.having.HavingConditionFunctional;
import com.tailwolf.mybatis.core.dsl.functional.having.join.JoinHavingConditionFirstFunctional;
import com.tailwolf.mybatis.core.dsl.functional.having.join.JoinHavingConditionSecondFunctional;
import com.tailwolf.mybatis.core.dsl.functional.order.join.OrderByFirstFunctional;
import com.tailwolf.mybatis.core.dsl.functional.order.join.OrderBySecondFunctional;
import com.tailwolf.mybatis.core.dsl.functional.select.entity.SelectFunctional;
import com.tailwolf.mybatis.core.dsl.functional.select.join.JoinSelectFromFunctional;
import com.tailwolf.mybatis.core.dsl.functional.select.join.JoinSelectJoinFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.EntityConditionFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.FromConditionFunctional;
import com.tailwolf.mybatis.core.dsl.functional.where.join.JoinConditionFunctional;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.core.dsl.node.*;
import com.tailwolf.mybatis.core.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拼接条件
 * @author tailwolf
 * @date 2020-08-31
 */
public class ConditionsBuilder {

    private StringBuffer selectTableBuffer;
    private StringBuffer updateTableBuffer;
    private StringBuffer deleteTableBuffer;

    private StringBuffer selectColumnBuffer;
    private StringBuffer setColumnBuffer;
    private StringBuffer onBuffer;
    private StringBuffer whereBuffer;
    private StringBuffer mapBuffer;
    private StringBuffer inBuffer;
    private StringBuffer existsOrNotExistsBuffer;
    private StringBuffer groupByBuffer;
    private StringBuffer havingBuffer;
    private StringBuffer orderBuffer;

    public ConditionsBuilder(String tableName) {
        initBuffer();
//        this.selectColumnBuffer.append(MontageSqlConstant.SELECT);
        this.selectTableBuffer.append(MontageSqlConstant.SPACE).append(MontageSqlConstant.FROM).append(tableName).append(MontageSqlConstant.SPACE);
    }

    public ConditionsBuilder(UpdateBaseWrapper updateBaseWrapper, String tableName) {
        initBuffer();
        if(updateBaseWrapper instanceof EntityDelete){
            this.deleteTableBuffer.append(MontageSqlConstant.DELETE).append(MontageSqlConstant.FROM).append(tableName).append(MontageSqlConstant.SPACE);
        }else{
            this.updateTableBuffer.append(MontageSqlConstant.UPDATE).append(tableName).append(MontageSqlConstant.SPACE);
        }
    }

    public ConditionsBuilder(JoinQuery joinQuery, String fromObjectTableName, String joinObjectTableName) {
        initBuffer();
        String joinKeyWord = joinQuery.getJoinKeyWord();
        this.selectTableBuffer.append(MontageSqlConstant.SPACE).append(MontageSqlConstant.FROM).append(fromObjectTableName).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.T1);
        if(StringUtils.isEmpty(joinKeyWord)){
            this.selectTableBuffer.append(MontageSqlConstant.COMMA).append(joinObjectTableName).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.T2).append(MontageSqlConstant.SPACE);
        }else{
            this.selectTableBuffer.append(MontageSqlConstant.SPACE).append(joinQuery.getJoinKeyWord()).append(joinObjectTableName).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.T2).append(MontageSqlConstant.SPACE);
        }
    }

    private void initBuffer(){
        this.deleteTableBuffer = new StringBuffer();
        this.updateTableBuffer = new StringBuffer();
        this.selectTableBuffer = new StringBuffer();
        this.selectColumnBuffer = new StringBuffer();
        this.setColumnBuffer = new StringBuffer();
        this.onBuffer = new StringBuffer();
        this.inBuffer = new StringBuffer();
        this.whereBuffer = new StringBuffer();
        this.mapBuffer = new StringBuffer();
        this.existsOrNotExistsBuffer = new StringBuffer();
        this.groupByBuffer = new StringBuffer();
        this.havingBuffer = new StringBuffer();
        this.orderBuffer = new StringBuffer();
    }

    /**
     * 拼接Update语句的Set关键字字段
     * @param setNodeIterator
     * @param filedColumnMap
     * @param filedColumnMap
     * @param filedColumnNameMap
     */
    public void montageSetNode(NodeIterator<SetNode> setNodeIterator, Map<String, Object> dynamicContextMap, Map<String, Map<String, Object>> filedColumnMap, Map<String, String> filedColumnNameMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SetNode setNode = setNodeIterator.currentNode();
        if(setNode == null){
            return;
        }

        Object filedNameObject = setNode.getFiledName();
        SerializedLambda lambda = SerializedLambdaUtil.getSerializedLambda((Serializable) filedNameObject);
        String filedName = this.analyseGetterName(lambda.getImplMethodName());
        String columnName = filedColumnNameMap.get(filedName);
        Object value = setNode.getValue();
        Object templateName = value;
        String uUid = createUUid();
        templateName = "#{" + uUid + "}";
        dynamicContextMap.put(uUid, value);
        if(setNodeIterator.isFirst()){
            this.setColumnBuffer.append(MontageSqlConstant.SET);
        }
        this.setColumnBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.EQ).append(templateName).append(MontageSqlConstant.SPACE);
        if(!setNodeIterator.isLast()){
            this.setColumnBuffer.append(MontageSqlConstant.COMMA);
        }
        montageSetNode(setNodeIterator.nextNonius(), dynamicContextMap, filedColumnMap, filedColumnNameMap);
    }

    /**
     * 生成随机uuid
     * @return
     */
    private String createUUid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 递归拼接where条件查询
     *        组装sql的可变字符串类
     * @param queryEntity
     *        查询实体类
     * @param nodeIterator
     * @param filedColumnMap
     * @param filedColumnNameMap
     */
    public void montageWhereConditions(Object queryEntity,
                                              NodeIterator<ConditionNode> nodeIterator, Map<String, Map<String, Object>> filedColumnMap,
                                              Map<String, String> filedColumnNameMap) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        ConditionNode conditionNode = nodeIterator.currentNode();
        //当前节点为空的时候，就是递归终止
        if(conditionNode == null){
            return;
        }

        String conditionKeywork = conditionNode.getConditionKeywork();
        //如果是and_start
        if(nodeIterator.isAndStart(conditionKeywork)){
            //如果不是第一个节点，则应该应该先加AND再加左括号
            if(!nodeIterator.isFirst()){
                this.whereBuffer.append(MontageSqlConstant.AND);
            }
            this.whereBuffer.append(MontageSqlConstant.LEFT_BRACKET);
            montageWhereConditions(queryEntity, nodeIterator.nextNonius(), filedColumnMap, filedColumnNameMap);
            return;
        }
        //如果是or_start
        if(nodeIterator.isOrStart(conditionKeywork)){
            //如果不是第一个节点，则应该应该先加OR再加左括号
            if(!nodeIterator.isFirst()){
                this.whereBuffer.append(MontageSqlConstant.OR);
            }
            this.whereBuffer.append(MontageSqlConstant.LEFT_BRACKET);
            montageWhereConditions(queryEntity, nodeIterator.nextNonius(), filedColumnMap, filedColumnNameMap);
            return;
        }
        //如果是右括号
        else if(nodeIterator.isEnd(conditionKeywork)){
            this.whereBuffer.append(MontageSqlConstant.RIGHT_BRACKET);
            montageWhereConditions(queryEntity, nodeIterator.nextNonius(), filedColumnMap, filedColumnNameMap);
            return;
        }

        //判断or的类型
        if(MontageSqlConstant.OR.equals(conditionKeywork) && conditionNode.getFiledName() == null && conditionNode.getValue() == null){
            this.whereBuffer.append(MontageSqlConstant.OR);
            montageWhereConditions(queryEntity, nodeIterator.nextNonius(), filedColumnMap, filedColumnNameMap);
            return;
        }

        //判断是否应该加OR
//        else if(nodeIterator.isOr(nodeIterator)){
//            this.whereBuffer.append(MontageSqlConstant.OR);
//        }
        //判断是否应该加AND
        else if(nodeIterator.isAnd(nodeIterator)){
            this.whereBuffer.append(MontageSqlConstant.AND);
        }

        Object filedNameObject = conditionNode.getFiledName();
        Object value = conditionNode.getValue();
        SerializedLambda lambda = SerializedLambdaUtil.getSerializedLambda((EntityConditionFunctional)filedNameObject);
        String filedName = this.analyseGetterName(lambda.getImplMethodName());
        String columnName = filedColumnNameMap.get(filedName);//数据库字段名
        Object templateName = whereTemplateName(filedName, value, conditionNode.getConditionKeywork(), queryEntity);

        if(filedColumnMap.get(filedName) != null){
            filedColumnMap.remove(filedName);
        }
        this.whereBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(templateName).append(MontageSqlConstant.SPACE);
        montageWhereConditions(queryEntity, nodeIterator.nextNonius(), filedColumnMap, filedColumnNameMap);
    }

    public void montageJoinWhereConditions(Object dynamicContextMap,
                                       NodeIterator<ConditionNode> nodeIterator,
                                       Map<String, String> filedColumnNameMap) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        montageConditions(this.whereBuffer, dynamicContextMap, nodeIterator, filedColumnNameMap);
    }

    /**
     * 获取表名
     * @param lambda
     * @return
     */
    private String getTableName(SerializedLambda lambda) throws ClassNotFoundException {
        String implClass = lambda.getImplClass().replace("/", ".");
        Class<?> clazz = Class.forName(implClass);
        Table tableAnnot = clazz.getAnnotation(Table.class);
        return tableAnnot.name();
    }

    private String whereTemplateName(Object value, String conditionKeywork, Object queryEntity) {
        String templateName;
        if(MontageSqlConstant.LIKE.equals(conditionKeywork)){
            templateName = "'" + value + "'";
        }else if(MontageSqlConstant.IS.equals(conditionKeywork)){
            templateName = (String)value;
        }else{
            String param = createUUid();
            templateName = "#{" + param + "}";
            Map<String, Object> queryMap = (Map) queryEntity;
            queryMap.put(param, value);
        }
        return templateName;
    }

    private String whereTemplateName(String param, Object value, String conditionKeywork, Object queryEntity) {
        String templateName;
        if(MontageSqlConstant.IS.equals(conditionKeywork)){
            templateName = (String)value;
            return templateName;
        } else if(MontageSqlConstant.LIKE.equals(conditionKeywork)){
            templateName = "concat('%', #{" + value + "}, '%')";
        }else if(MontageSqlConstant.LEFT_LIKE.equals(conditionKeywork)){
            templateName = "concat('%', #{" + value + "})";
        }else if(MontageSqlConstant.RIGHT_LIKE.equals(conditionKeywork)){
            templateName = "concat(#{" + value + "}, '%')";
        }else{
            templateName = "#{" + param + "}";
        }
        Map<String, Object> queryMap = (Map) queryEntity;
        queryMap.put(param, value);
        return templateName;
    }

    private void montageConditions(StringBuffer buffer, Object dynamicContextMap,
                                   NodeIterator<ConditionNode> nodeIterator,
                                   Map<String, String> filedColumnNameMap) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConditionNode conditionNode = nodeIterator.currentNode();
        //当前节点为空的时候，就是递归终止
        if(conditionNode == null){
            return;
        }

        String conditionKeywork = conditionNode.getConditionKeywork();
        //如果是and_start
        if(nodeIterator.isAndStart(conditionKeywork)){
            //如果不是第一个节点，则应该应该先加AND再加左括号
            if(!nodeIterator.isFirst()){
                buffer.append(MontageSqlConstant.AND);
            }
            buffer.append(MontageSqlConstant.LEFT_BRACKET);
            montageConditions(buffer, dynamicContextMap, nodeIterator.nextNonius(), filedColumnNameMap);
            return;
        }
        //如果是or_start
        if(nodeIterator.isOrStart(conditionKeywork)){
            //如果不是第一个节点，则应该应该先加OR再加左括号
            if(!nodeIterator.isFirst()){
                buffer.append(MontageSqlConstant.OR);
            }
            this.whereBuffer.append(MontageSqlConstant.LEFT_BRACKET);
            montageConditions(buffer, dynamicContextMap, nodeIterator.nextNonius(), filedColumnNameMap);
            return;
        }
        //如果是右括号
        else if(nodeIterator.isEnd(conditionKeywork)){
            buffer.append(MontageSqlConstant.RIGHT_BRACKET);
            montageConditions(buffer, dynamicContextMap, nodeIterator.nextNonius(), filedColumnNameMap);
            return;
        }

        //判断or的类型
        if(MontageSqlConstant.OR.equals(conditionKeywork) && conditionNode.getFiledName() == null && conditionNode.getValue() == null){
            buffer.append(MontageSqlConstant.OR);
            montageConditions(buffer, dynamicContextMap, nodeIterator.nextNonius(), filedColumnNameMap);
            return;
        }

        //判断是否应该加OR
        else if(nodeIterator.isOr(nodeIterator)){
            buffer.append(MontageSqlConstant.OR);
        }
        //判断是否应该加AND
        else if(nodeIterator.isAnd(nodeIterator)){
            buffer.append(MontageSqlConstant.AND);
        }

        Object filedNameObject = conditionNode.getFiledName();
        String filedName = "";
        String columnName = "";
        Object templateName = "";
        Object value = conditionNode.getValue();
        if(filedNameObject instanceof FromConditionFunctional && value instanceof JoinConditionFunctional){
            columnName = filedColumnNameMap.get(tableFieldName((FromConditionFunctional)filedNameObject));
            templateName = filedColumnNameMap.get(tableFieldName((JoinConditionFunctional)value));
        }else if(filedNameObject instanceof FromConditionFunctional){
            columnName = filedColumnNameMap.get(tableFieldName((FromConditionFunctional)filedNameObject));
            templateName = whereTemplateName(createUUid(), value, conditionNode.getConditionKeywork(), dynamicContextMap);

        }else {
            columnName = filedColumnNameMap.get(tableFieldName((JoinConditionFunctional)filedNameObject));
            templateName = whereTemplateName(createUUid(), value, conditionNode.getConditionKeywork(), dynamicContextMap);
        }

//        if(filedColumnMap.get(filedName) != null){
//            filedColumnMap.remove(filedName);
//        }
        buffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(templateName).append(MontageSqlConstant.SPACE);
        montageConditions(buffer, dynamicContextMap, nodeIterator.nextNonius(), filedColumnNameMap);
    }

    /**
     * 递归拼接on条件查询
     *        组装sql的可变字符串类
     * @param dynamicContextMap
     *        查询实体类
     * @param nodeIterator
     * @param filedColumnNameMap
     */
    public void montageOnConditions(Map<String, Object> dynamicContextMap, NodeIterator<ConditionNode> nodeIterator, Map<String, String> filedColumnNameMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        ConditionNode conditionNode = nodeIterator.currentNode();

        if(conditionNode != null){
            this.onBuffer.append(MontageSqlConstant.ON);
            montageConditions(this.onBuffer, dynamicContextMap, nodeIterator, filedColumnNameMap);
        }
    }

    /**
     * @param fn
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private String tableFieldName(Serializable fn) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SerializedLambda fromLambdaSeri = SerializedLambdaUtil.getSerializedLambda(fn);
        String fieldName = this.analyseGetterName(fromLambdaSeri.getImplMethodName());
        String tableName = getTableName(fromLambdaSeri);
        return tableName + "." + fieldName;
    }


    /**
     * join查询拼接select字段
     * @param nodeIterator
     */
    public void montageJoinSelectNode(NodeIterator<SelectNode> nodeIterator, Map<String, String> allColumnNameMap, String fromObjectTableName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        selectColumnBuffer.append(MontageSqlConstant.SELECT);
        SelectNode selectNode = nodeIterator.currentNode();
        if(selectNode == null){
            List<String> select = new ArrayList<>();
            for(Map.Entry<String, String> entity: allColumnNameMap.entrySet()){
                select.add(entity.getValue() + " as " + entity.getValue().replace(".", "_"));
            }
            selectColumnBuffer.append(StringUtils.join(select, ", "));
            return;
        }

        montageSelectNode(nodeIterator.nextNonius(), allColumnNameMap, fromObjectTableName);
    }

    /**
     * 单表查询拼接select字段
     * @param nodeIterator
     */
    public void montageSingleSelectNode(NodeIterator<SelectNode> nodeIterator, Map<String, String> columnNameMap, String fromObjectTableName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        selectColumnBuffer.append(MontageSqlConstant.SELECT);
        SelectNode selectNode = nodeIterator.currentNode();
        if(selectNode == null){
            List<String> select = new ArrayList<>();
            for(Map.Entry<String, String> entity: columnNameMap.entrySet()){
                select.add(entity.getValue());
            }
            selectColumnBuffer.append(StringUtils.join(select, ", "));
            return;
        }

        montageSelectNode(nodeIterator.nextNonius(), columnNameMap, fromObjectTableName);
    }

    /**
     * 拼接select字段
     * @param nodeIterator
     */
    public void montageSelectNode(NodeIterator<SelectNode> nodeIterator, Map<String, String> allColumnNameMap, String fromObjectTableName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SelectNode selectNode = nodeIterator.currentNode();
        if(selectNode == null){
            return;
        }

        Object filedNameObject = selectNode.getFiledName();
        String sqlFuncName = selectNode.getSqlFuncName();
        String column;
        if(filedNameObject instanceof JoinSelectFromFunctional){
            column = allColumnNameMap.get(this.tableFieldName((JoinSelectFromFunctional)filedNameObject));
            if(!StringUtils.isEmpty(sqlFuncName)){
                column = sqlFuncName.replace("$", column);
            }
            column = column + " as " + column.replace(".", "_");
        }else if(filedNameObject instanceof JoinSelectJoinFunctional){
            column = allColumnNameMap.get(this.tableFieldName((JoinSelectJoinFunctional)filedNameObject));
            if(!StringUtils.isEmpty(sqlFuncName)){
                column = sqlFuncName.replace("$", column);
            }
            column = column + " as " + column.replace(".", "_");
        }else if(filedNameObject instanceof String){
            column = (String)filedNameObject;
        }else if(filedNameObject instanceof SelectFunctional){
            SerializedLambda lambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObject);
            column = this.analyseGetterName(lambda.getImplMethodName());
        } else{
            Table tableAnnot = filedNameObject.getClass().getAnnotation(Table.class);
            String tableName = tableAnnot.name().trim();
            if(tableName.equals(fromObjectTableName)){
                column = MontageSqlConstant.T1 + ".*";
            }else{
                column = MontageSqlConstant.T2 + ".*";
            }
            if(!StringUtils.isEmpty(sqlFuncName)){
                column = sqlFuncName.replace("$", column);
            }
        }

        this.selectColumnBuffer.append(column);
        if(!nodeIterator.isLast()){
            this.selectColumnBuffer.append(MontageSqlConstant.COMMA);
        }else{
            this.selectColumnBuffer.append(MontageSqlConstant.SPACE);
        }
        montageSelectNode(nodeIterator.nextNonius(), allColumnNameMap, fromObjectTableName);
    }

    /**
     * 拼接group by字段
     * @param groupByNodeIterator
     * @param allColumnNameMap
     */
    public void montageGroupByNode(NodeIterator<GroupByNode> groupByNodeIterator, Map<String, String> allColumnNameMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        GroupByNode groupByNode = groupByNodeIterator.currentNode();
        if(groupByNode == null){
            return;
        }
        if(groupByNodeIterator.isFirst()){
            groupByBuffer.append(MontageSqlConstant.GROUP_BY);
        }

        Object filedNameObject = groupByNode.getFiledName();
        String column;
        if(filedNameObject instanceof JoinGroupByFirstFunctional){
            column = allColumnNameMap.get(this.tableFieldName((JoinGroupByFirstFunctional)filedNameObject));
        }else if(filedNameObject instanceof JoinGroupBySecondFunctional){
            column = allColumnNameMap.get(this.tableFieldName((JoinGroupBySecondFunctional)filedNameObject));
        }else{
            SerializedLambda serializedLambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObject);
            String filedName = this.analyseGetterName(serializedLambda.getImplMethodName());
            column = allColumnNameMap.get(filedName);
        }

        this.groupByBuffer.append(column);
        if(!groupByNodeIterator.isLast()){
            this.groupByBuffer.append(MontageSqlConstant.COMMA);
        }else{
            this.groupByBuffer.append(MontageSqlConstant.SPACE);
        }
        montageGroupByNode(groupByNodeIterator.nextNonius(), allColumnNameMap);
    }

    /**
     * 拼接having字段
     * @param queryEntity
     * @param havingNodeIterator
     * @param filedColumnNameMap
     */
    public void montageHavingNode(Object queryEntity, NodeIterator<HavingNode> havingNodeIterator, Map<String, String> filedColumnNameMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        HavingNode havingNode = havingNodeIterator.currentNode();
        if(havingNode == null){
            return;
        }
        if(havingNodeIterator.isFirst()){
            this.havingBuffer.append(MontageSqlConstant.HAVING);
        }else{
            this.havingBuffer.append(MontageSqlConstant.AND);
        }

        String conditionKeywork = havingNode.getConditionKeywork();
        Object filedNameObect = havingNode.getFiledName();
        String filedName;
        if(filedNameObect instanceof JoinHavingConditionFirstFunctional){
            filedName = this.tableFieldName((JoinHavingConditionFirstFunctional)filedNameObect);

        }else if(filedNameObect instanceof JoinHavingConditionSecondFunctional){
            filedName = this.tableFieldName((JoinHavingConditionSecondFunctional)filedNameObect);
        }else{
            SerializedLambda serializedLambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObect);
            filedName = this.analyseGetterName(serializedLambda.getImplMethodName());
        }


        Object value = havingNode.getValue();
        if(value instanceof JoinHavingConditionFirstFunctional){
            String filedName2 = this.tableFieldName((JoinHavingConditionFirstFunctional)value);
            String columnName = filedColumnNameMap.get(filedName);
            String columnName2 = filedColumnNameMap.get(filedName2);
            this.havingBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(columnName2).append(MontageSqlConstant.SPACE);
        }else if(value instanceof JoinHavingConditionSecondFunctional){
            String filedName2 = this.tableFieldName((JoinHavingConditionSecondFunctional)value);
            String columnName = filedColumnNameMap.get(filedName);
            String columnName2 = filedColumnNameMap.get(filedName2);
            this.havingBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(columnName2).append(MontageSqlConstant.SPACE);
        } else if(value instanceof HavingConditionFunctional){
            SerializedLambda serializedLambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObect);
            String filedName2 = this.analyseGetterName(serializedLambda.getImplMethodName());
            String columnName = filedColumnNameMap.get(filedName);
            String columnName2 = filedColumnNameMap.get(filedName2);
            this.havingBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(columnName2).append(MontageSqlConstant.SPACE);
        } else{
            String columnName = filedColumnNameMap.get(filedName);
            Object templateName;
            if(queryEntity instanceof Map){
                templateName = whereTemplateName(value, havingNode.getConditionKeywork(), queryEntity);
            }else{
                if(MontageSqlConstant.LIKE.equals(conditionKeywork)){
                    templateName = "'" + value + "'";
                }else{
                    templateName = "#{" + filedName + "}";
                    ReflectionUtil.setProperty(queryEntity, filedName, value);
                }
            }
            this.havingBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(conditionKeywork).append(templateName).append(MontageSqlConstant.SPACE);
        }
        montageHavingNode(queryEntity, havingNodeIterator.nextNonius(), filedColumnNameMap);
    }

    /**
     * 排序
     * @param nodeIterator
     * @param columnNameMap
     * @throws NoSuchFieldException
     */
    public void order(NodeIterator<OrderByNode> nodeIterator, Map<String, String> columnNameMap) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        OrderByNode orderByNode = nodeIterator.currentNode();
        if(orderByNode == null){
            return;
        }
        if(nodeIterator.isFirst()){
            this.orderBuffer.append(MontageSqlConstant.ORDER_BY);
        }
        Object filedNameObject = orderByNode.getFiledName();
        Object columnName;
        if(filedNameObject instanceof OrderByFirstFunctional){
            String filedName = this.tableFieldName((OrderByFirstFunctional)filedNameObject);
            columnName = columnNameMap.get(filedName);
        }else if(filedNameObject instanceof OrderBySecondFunctional){
            String filedName = this.tableFieldName((OrderBySecondFunctional)filedNameObject);
            columnName = columnNameMap.get(filedName);
        }else{
            SerializedLambda serializedLambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObject);
            String filedName = this.analyseGetterName(serializedLambda.getImplMethodName());
            columnName = columnNameMap.get(filedName);
        }
        String orderByKeywork = orderByNode.getOrderByKeywork();
        this.orderBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(orderByKeywork);
        if(!nodeIterator.isLast()){
            this.orderBuffer.append(MontageSqlConstant.COMMA);
        }
        order(nodeIterator.nextNonius(), columnNameMap);
    }

    public void inOrNotIn(NodeIterator<InOrNotInNode> inNodeQueue, Map<String, Object> dynamicContextMap, Map<String, String> columnNameMap) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        InOrNotInNode inOrNotInNode = inNodeQueue.currentNode();
        if(inOrNotInNode == null){
            return;
        }
        if(!inNodeQueue.isFirst()){
            this.inBuffer.append(MontageSqlConstant.AND);
        }

        Object filedNameObject = inOrNotInNode.getFiledName();
        String columnName;
        if(filedNameObject instanceof FromConditionFunctional){
            String filedName = this.tableFieldName((FromConditionFunctional)filedNameObject);
            columnName = columnNameMap.get(filedName);
        }else if(filedNameObject instanceof JoinConditionFunctional){
            String filedName = this.tableFieldName((JoinConditionFunctional)filedNameObject);
            columnName = columnNameMap.get(filedName);
        }else{
            SerializedLambda serializedLambda = SerializedLambdaUtil.getSerializedLambda((Serializable)filedNameObject);
            String filedName = this.analyseGetterName(serializedLambda.getImplMethodName());
            columnName = columnNameMap.get(filedName);
        }

        this.inBuffer.append(columnName).append(MontageSqlConstant.SPACE);
        this.inBuffer.append(inOrNotInNode.getKeywork());
        this.inBuffer.append(MontageSqlConstant.LEFT_BRACKET);
        List<Object> valueList;
        Object object = inOrNotInNode.getObject();
        if(object.getClass().isArray()){
            valueList = new ArrayList<>(Arrays.asList((Object[])object));
        }else{
            valueList = new ArrayList<>((Collection) object);
        }
        for(int i = 0; i < valueList.size(); i++){
            Object value = valueList.get(i);
            String templateName = whereTemplateName(value, inOrNotInNode.getKeywork(), dynamicContextMap);
            this.inBuffer.append(templateName);
            if(i < valueList.size() - 1){
                this.inBuffer.append(MontageSqlConstant.COMMA);
            }
        }
        this.inBuffer.append(MontageSqlConstant.RIGHT_BRACKET);

        inOrNotIn(inNodeQueue.nextNonius(), dynamicContextMap, columnNameMap);
    }

    public void existsOrNotExists(NodeIterator<ExistsOrNotExistsNode> existOrNotExistQueue, Map<String, Object> dynamicContextMap) {
        ExistsOrNotExistsNode existsOrNotExistsNode = existOrNotExistQueue.currentNode();
        if(existsOrNotExistsNode == null){
            return;
        }
        if(!existOrNotExistQueue.isFirst()){
            this.existsOrNotExistsBuffer.append(MontageSqlConstant.AND);
        }

        String keywork = existsOrNotExistsNode.getKeywork();
        String sqlScript = existsOrNotExistsNode.getSqlScript();
        Object[] values = existsOrNotExistsNode.getValue();
        if(values != null && values.length > 0){
            List<Integer> indexList = new ArrayList<>();
            //解析sql
            Matcher matcher = Pattern.compile("\\{[0-9]\\d*}").matcher(sqlScript);
            int matcher_start = 0;
            while (matcher.find(matcher_start)){
                Integer index = Integer.valueOf(matcher.group(0).replace("{", "").replace("}", ""));
                indexList.add(index);
                matcher_start = matcher.end();
            }
            if(!CollectionUtil.isEmtpy(indexList)){
                //升序排序
                indexList.sort(Comparator.comparing(Integer::new));
                for(int i = 0; i < values.length && i < indexList.size(); i++){
                    String uUid = createUUid();
                    String templateName = "#{" + uUid + "}";
                    sqlScript = sqlScript.replace("{" + indexList.get(i) + "}", templateName);
                    dynamicContextMap.put(uUid, values[i]);
                }
            }
        }
        this.existsOrNotExistsBuffer.append(keywork).append(MontageSqlConstant.LEFT_BRACKET).append(MontageSqlConstant.SPACE).append(sqlScript).append(MontageSqlConstant.RIGHT_BRACKET);
        existsOrNotExists(existOrNotExistQueue.nextNonius(), dynamicContextMap);
    }

    public void mapCondition(Map<String, Map<String, Object>> filedColumnMap, Map<String, Object> dynamicContextMap){
        if(filedColumnMap.size() > 0){
            for(Map.Entry<String, Map<String, Object>> entrySet: filedColumnMap.entrySet()){
                String filedName = entrySet.getKey();//属性名
                filedName = filedName.replace(".", "");
                Map<String, Object> valueMap = entrySet.getValue();
                for(Map.Entry<String, Object> entry: valueMap.entrySet()){
                    String columnName = entry.getKey();//表名
                    Object value = entry.getValue();
                    if(dynamicContextMap != null){
                        dynamicContextMap.put(filedName, value);
                    }
                    value = "#{" + filedName + "}";
                    this.mapBuffer.append(columnName).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.EQ).append(value).append(MontageSqlConstant.SPACE).append(MontageSqlConstant.AND);
                }
            }
        }
    }

    //TODO:此处代码需要优化
    public StringBuffer build(Map<String, String> columnNameMap) {

        String selectColumnStr = this.selectColumnBuffer.toString();
        String selectTableStr = this.selectTableBuffer.toString();
        String deleteTableStr = this.deleteTableBuffer.toString();
        String updateTableStr = this.updateTableBuffer.toString();
        String setColumnStr = this.setColumnBuffer.toString();
        String onConditionStr = this.onBuffer.toString();
        String whereConditionStr = this.whereBuffer.toString();
        String mapConditionStr = this.mapBuffer.toString();
        String inStr = this.inBuffer.toString();
        String existsOrNotExistsStr = this.existsOrNotExistsBuffer.toString();
        String groupByStr = this.groupByBuffer.toString();
        String havingStr = this.havingBuffer.toString();
        String orderStr = this.orderBuffer.toString();

        StringBuffer sqlBuffer = new StringBuffer();
        if(!StringUtils.isEmpty(deleteTableStr)){
            sqlBuffer.append(deleteTableStr);
        }else if(!StringUtils.isEmpty(updateTableStr)){
            sqlBuffer.append(updateTableStr);
            sqlBuffer.append(setColumnStr);
        }
        else{
            sqlBuffer.append(selectColumnStr);
            sqlBuffer.append(selectTableStr);
            sqlBuffer.append(onConditionStr);
        }

        if(StringUtils.isEmpty(whereConditionStr) && !StringUtils.isEmpty(mapConditionStr)){
            sqlBuffer.append(MontageSqlConstant.WHERE).append(mapConditionStr, 0, mapConditionStr.length() - 4);
        }else if(StringUtils.isEmpty(mapConditionStr) && !StringUtils.isEmpty(whereConditionStr)){
            sqlBuffer.append(MontageSqlConstant.WHERE).append(whereConditionStr);
        }else if(!StringUtils.isEmpty(mapConditionStr) && !StringUtils.isEmpty(whereConditionStr)){
            sqlBuffer.append(MontageSqlConstant.WHERE).append(mapConditionStr).append(whereConditionStr);
        }

        if(!StringUtils.isEmpty(inStr)){
            if(!StringUtils.isEmpty(whereConditionStr) || !StringUtils.isEmpty(mapConditionStr)){
                sqlBuffer.append(MontageSqlConstant.AND);
            }else{
                sqlBuffer.append(MontageSqlConstant.WHERE);
            }
            sqlBuffer.append(inStr);
        }
        //TODO:此处代码需要优化
        if(!StringUtils.isEmpty(existsOrNotExistsStr)){
            if(!StringUtils.isEmpty(whereConditionStr) || !StringUtils.isEmpty(mapConditionStr) || !StringUtils.isEmpty(inStr)){
                sqlBuffer.append(MontageSqlConstant.AND);
            }else{
                sqlBuffer.append(MontageSqlConstant.WHERE);
            }
            sqlBuffer.append(existsOrNotExistsStr);
        }
        sqlBuffer.append(groupByStr);
        sqlBuffer.append(havingStr);
        sqlBuffer.append(orderStr);
        return sqlBuffer;
    }

    /**
     * 解析lambda的get方法名获取filedName
     * @param implMethodName
     * @return
     */
    private String analyseGetterName(String implMethodName) {
        String filedName = implMethodName.substring(3);
        return filedName.substring(0, 1).toLowerCase().concat(filedName.substring(1));
    }
}
