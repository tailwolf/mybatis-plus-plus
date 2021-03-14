package com.tailwolf.mybatis.core.dsl.interceptor;

import com.alibaba.fastjson.JSON;
import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import com.tailwolf.mybatis.core.dsl.node.ConditionNode;
import com.tailwolf.mybatis.core.util.ColumnModelUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import com.tailwolf.mybatis.core.dsl.build.ConditionsBuilder;
import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.dsl.wrapper.base.UpdateBaseWrapper;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityUpdate;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.dsl.iterator.NodeIterator;
import com.tailwolf.mybatis.core.ColumnModel;
import com.tailwolf.mybatis.core.dsl.node.SetNode;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 该拦截器主要是用来处理dsl操作，生成sql的
 * @author tailwolf
 * @date 2020-09-17
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class DslSqlInterceptor extends BaseInterceptor implements Interceptor {
    private List<String> dslMapperList = new ArrayList<>();
    private String logicDeleteField;
    private Integer logicNotDeleteValue = 0;
    private Integer logicDeleteValue = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Configuration configuration = mappedStatement.getConfiguration();

        if(!this.dslMapperList.contains(mappedStatement.getId())){
            return invocation.proceed();
        }

        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) invocation.getArgs()[1];
        Object dslWrapper = paramMap.get("dslWrapper");
        SqlSource sqlSource = null;
        //单表查询
        if(dslWrapper instanceof EntityQuery){
            EntityQuery entiryQuery = (EntityQuery) dslWrapper;
            Object entity = entiryQuery.getEntity();
            ResultMap inlineResultMap = (new ResultMap.Builder(configuration, mappedStatement.getId() + "-Inline", Map.class, new ArrayList<>(), (Boolean) null)).build();
            List<ResultMap> resultMaps = new ArrayList<>();
            resultMaps.add(inlineResultMap);
            ReflectionUtil.setProperty(mappedStatement, "resultMaps", resultMaps);

            Class<?> entityClazz = entity.getClass();
            Map<String, Object> dynamicContextMap = new HashMap<>();
            Table tableAnnot = entityClazz.getAnnotation(Table.class);
            if (tableAnnot == null) {
                throw new MybatisCompleteRuntimeException("实体类必需加上注解@Table");
            }
            String tableName = tableAnnot.tableName();

            List<Field> beanAnnotateFieldList = ReflectionUtil.getAllFields(entityClazz);
            List<ColumnModel> annotateColumnModelList = ColumnModelUtil.createColumnModel(beanAnnotateFieldList, new ArrayList<>());
            Map<String, Map<String, Object>> filedColumnMap = this.getFiledColumnMap(annotateColumnModelList, entity, null, null);
            Map<String, String> filedColumnNameMap = this.getFiledColumnNameMap(annotateColumnModelList, null, null);

            ConditionsBuilder conditionsBuilder = new ConditionsBuilder(tableName);
            conditionsBuilder.montageSingleSelectNode(entiryQuery.getSelectNodeQueue(), filedColumnNameMap, null);
            conditionsBuilder.montageWhereConditions(dynamicContextMap, entiryQuery.getWhereConditionsQueue(), filedColumnMap, filedColumnNameMap);
            conditionsBuilder.mapCondition(filedColumnMap, dynamicContextMap);
            conditionsBuilder.inOrNotIn(entiryQuery.getInOrNotInNodeQueue(), dynamicContextMap, filedColumnNameMap);
            conditionsBuilder.existsOrNotExists(entiryQuery.getExistOrNotExistQueue(), dynamicContextMap);
            conditionsBuilder.montageGroupByNode(entiryQuery.getGroupByNodeQueue(), filedColumnNameMap);
            conditionsBuilder.montageHavingNode(dynamicContextMap, entiryQuery.getHavingNodeQueue(), filedColumnNameMap);
            conditionsBuilder.order(entiryQuery.getOrderConditionsQueue(), filedColumnNameMap);

            DynamicContext context = new DynamicContext(configuration, filedColumnNameMap);
            SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
            Class<?> parameterType = dynamicContextMap.getClass();
            String sql = conditionsBuilder.build(filedColumnNameMap).toString();
            sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
            sql = (String)ReflectionUtil.getProperty(sqlSource, "sql");
            sql = this.structureLogicDelete(mappedStatement.getSqlCommandType(), entity,
                    this.logicDeleteField, this.logicNotDeleteValue, logicDeleteValue, sql, null, annotateColumnModelList);

            ReflectionUtil.setProperty(sqlSource, "sql", sql);
            ReflectionUtil.setProperty(mappedStatement, "sqlSource", sqlSource);
            ReflectionUtil.setProperty(dslWrapper, "parameterObject", dynamicContextMap);
        }
        //双表查询
        else if(dslWrapper instanceof JoinQuery){
            JoinQuery joinQuery = (JoinQuery)dslWrapper;
            Object fromObject = joinQuery.getFromObject();
            Object joinObject = joinQuery.getJoinObject();
            if(fromObject == null  || joinObject == null){
                throw new MybatisCompleteRuntimeException("fromObject或joinObject不能为空");
            }

            ResultMap resultMap = this.createResultMap(paramMap, configuration);
            List<ResultMap> resultMaps = new ArrayList<>();
            resultMaps.add(resultMap);
            ReflectionUtil.setProperty(mappedStatement, "resultMaps", resultMaps);

            Class<?> fromObjectClazz = fromObject.getClass();
            Class<?> joinObjectClazz = joinObject.getClass();
            Table fromObjectTableAnnot = fromObjectClazz.getAnnotation(Table.class);
            Table joinObjectTableAnnot = joinObjectClazz.getAnnotation(Table.class);
            String fromObjectTableName = fromObjectTableAnnot.tableName();
            String joinObjectTableName = joinObjectTableAnnot.tableName();

            Map<String, Object> dynamicContextMap = new HashMap<>();
            NodeIterator onConditionNodeIterator = joinQuery.getOnConditionsQueue();
            //from
            List<Field> fromObjectAnnotateFieldList = ReflectionUtil.getAllFields(fromObject.getClass());
            List<ColumnModel> fromAnnotateColumnModelList = ColumnModelUtil.createColumnModel(fromObjectAnnotateFieldList, new ArrayList<>());
            Map<String, Map<String, Object>> fromFiledColumnMap = this.getFiledColumnMap(fromAnnotateColumnModelList, fromObject, fromObjectTableName, MontageSqlConstant.T1);
            Map<String, String> fromFiledColumnNameMap = this.getFiledColumnNameMap(fromAnnotateColumnModelList, MontageSqlConstant.T1, fromObjectTableName);
            //join
            List<Field> joinObjectAnnotateFieldList = ReflectionUtil.getAllFields(joinObject.getClass());
            List<ColumnModel> joinAnnotateColumnModelList = ColumnModelUtil.createColumnModel(joinObjectAnnotateFieldList, new ArrayList<>());
            Map<String, Map<String, Object>> joinFiledColumnMap = this.getFiledColumnMap(joinAnnotateColumnModelList, joinObject, joinObjectTableName, MontageSqlConstant.T2);
            Map<String, String> joinFiledColumnNameMap = this.getFiledColumnNameMap(joinAnnotateColumnModelList, MontageSqlConstant.T2, joinObjectTableName);

            ConditionsBuilder conditionsBuilder = new ConditionsBuilder(joinQuery, fromObjectTableName, joinObjectTableName);
            Map<String, String> allColumnNameMap = new HashMap<>();
            allColumnNameMap.putAll(fromFiledColumnNameMap);
            allColumnNameMap.putAll(joinFiledColumnNameMap);
            conditionsBuilder.montageOnConditions(dynamicContextMap, onConditionNodeIterator, allColumnNameMap);
//            conditionsBuilder.montageJoinWhereConditions(dynamicContextMap, onConditionNodeIterator, allColumnNameMap);

            NodeIterator<ConditionNode> whereConditionsQueue = joinQuery.getWhereConditionsQueue();
            Map<String, Map<String, Object>> allFiledColumnMap = new HashMap<>();
            allFiledColumnMap.putAll(fromFiledColumnMap);
            allFiledColumnMap.putAll(joinFiledColumnMap);
            conditionsBuilder.montageJoinWhereConditions(dynamicContextMap, whereConditionsQueue, allColumnNameMap);
            conditionsBuilder.mapCondition(allFiledColumnMap, dynamicContextMap);
            conditionsBuilder.inOrNotIn(joinQuery.getInOrNotInNodeQueue(), dynamicContextMap, allColumnNameMap);
            conditionsBuilder.montageJoinSelectNode(joinQuery.getSelectNodeQueue(), allColumnNameMap, fromObjectTableName);
            //montageJoinSelectNode
            conditionsBuilder.existsOrNotExists(joinQuery.getExistOrNotExistQueue(), dynamicContextMap);
            conditionsBuilder.montageGroupByNode(joinQuery.getGroupByNodeQueue(), allColumnNameMap);
            conditionsBuilder.montageHavingNode(dynamicContextMap, joinQuery.getHavingNodeNodeQueue(), allColumnNameMap);
            conditionsBuilder.order(joinQuery.getOrderConditionsQueue(), allColumnNameMap);

            DynamicContext context = new DynamicContext(configuration, dynamicContextMap);
            SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
            Class<?> parameterType = dynamicContextMap.getClass();
            String sql = conditionsBuilder.build(allColumnNameMap).toString();
            sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
            sql = (String)ReflectionUtil.getProperty(sqlSource, "sql");
            sql = this.structureLogicDelete(mappedStatement.getSqlCommandType(), fromObject,
                    this.logicDeleteField, this.logicNotDeleteValue, logicDeleteValue, sql, MontageSqlConstant.T1, fromAnnotateColumnModelList);
            sql = this.structureLogicDelete(mappedStatement.getSqlCommandType(), joinObject,
                    this.logicDeleteField, this.logicNotDeleteValue, logicDeleteValue , sql, MontageSqlConstant.T2, joinAnnotateColumnModelList);

            ReflectionUtil.setProperty(sqlSource, "sql", sql);
            ReflectionUtil.setProperty(mappedStatement, "sqlSource", sqlSource);
            ReflectionUtil.setProperty(dslWrapper, "parameterObject", dynamicContextMap);
        }
        //单表更新或删除
        else if(dslWrapper instanceof UpdateBaseWrapper){
            UpdateBaseWrapper updateBaseWrapper;
            if(dslWrapper instanceof EntityDelete){
                updateBaseWrapper = (EntityDelete) dslWrapper;
            }else{
                updateBaseWrapper = (EntityUpdate) dslWrapper;
            }

            Object entity = updateBaseWrapper.getEntity();
            if(entity == null){
                throw new MybatisCompleteRuntimeException("条件构造器的实体类参数不能为空！");
            }
            ReflectionUtil.setProperty(updateBaseWrapper, "entity", entity);
            ResultMap inlineResultMap = (new ResultMap.Builder(configuration, mappedStatement.getId() + "-Inline", entity.getClass(), new ArrayList<>(), (Boolean)null)).build();
            List<ResultMap> resultMaps = new ArrayList<>();
            resultMaps.add(inlineResultMap);
            ReflectionUtil.setProperty(mappedStatement, "resultMaps", resultMaps);

            Class<?> entityClazz = entity.getClass();
            Table tableAnnot = entityClazz.getAnnotation(Table.class);
            if(tableAnnot == null){
                throw new MybatisCompleteRuntimeException("实体类必需加上注解@Table");
            }
            String tableName = tableAnnot.tableName();

            List<Field> beanAnnotateFieldList = ReflectionUtil.getAllFields(entity.getClass());
            List<ColumnModel> annotateColumnModelList = ColumnModelUtil.createColumnModel(beanAnnotateFieldList, new ArrayList<>());
            Map<String, Map<String, Object>> filedColumnMap = this.getFiledColumnMap(annotateColumnModelList, entity, null, null);
            Map<String, String> columnNameMap = this.getFiledColumnNameMap(annotateColumnModelList, null, null);

            ConditionsBuilder conditionsBuilder = new ConditionsBuilder(updateBaseWrapper, tableName);
            Map<String, Object> dynamicContextMap = new HashMap<>();
            if(dslWrapper instanceof EntityUpdate){
                NodeIterator<SetNode> setNodeIterator = ((EntityUpdate) dslWrapper).getSetNodeQueue();
                if(setNodeIterator.isEmpty()){
                    throw new MybatisCompleteRuntimeException("更新操作必须设置更新的属性！");
                }
                conditionsBuilder.montageSetNode(setNodeIterator, dynamicContextMap, filedColumnMap, columnNameMap);
            }
            conditionsBuilder.montageWhereConditions(dynamicContextMap, updateBaseWrapper.getWhereConditionsQueue(), filedColumnMap, columnNameMap);
            conditionsBuilder.mapCondition(filedColumnMap, dynamicContextMap);
            conditionsBuilder.inOrNotIn(updateBaseWrapper.getInOrNotInNodeQueue(), dynamicContextMap, columnNameMap);
            conditionsBuilder.existsOrNotExists(updateBaseWrapper.getExistOrNotExistQueue(), dynamicContextMap);
            String sql = conditionsBuilder.build(null).toString();

            DynamicContext context = new DynamicContext(configuration, dynamicContextMap);
            SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
            Class<?> parameterType = dynamicContextMap.getClass();
            sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
            sql = this.structureLogicDelete(mappedStatement.getSqlCommandType(), entity,
                    this.logicDeleteField, this.logicNotDeleteValue, this.logicDeleteValue , (String)ReflectionUtil.getProperty(sqlSource, "sql"), null, annotateColumnModelList);
            entity = JSON.parseObject(JSON.toJSONString(dynamicContextMap), entity.getClass());
            sql = this.structureOptimisticLock(mappedStatement.getSqlCommandType(), entity, sql, annotateColumnModelList);
            ReflectionUtil.setProperty(sqlSource, "sql", sql);
            ReflectionUtil.setProperty(dslWrapper, "parameterObject", dynamicContextMap);
            ReflectionUtil.setProperty(mappedStatement, "sqlSource", sqlSource);
        }
        else{
            invocation.proceed();
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.dslMapperList.add(properties.getProperty(InterceptorConstant.DSL_QUERY));
        this.dslMapperList.add(properties.getProperty(InterceptorConstant.DSL_QUERY_ONE));
        this.dslMapperList.add(properties.getProperty(InterceptorConstant.DSL_UPDATE));
        this.dslMapperList.add(properties.getProperty(InterceptorConstant.DSL_DELETE));
        this.dslMapperList.add(properties.getProperty(InterceptorConstant.JOIN_QUERY));
        this.logicDeleteField = properties.getProperty(InterceptorConstant.FIELD);
        String logicNotDeleteValueProperty = properties.getProperty(InterceptorConstant.LOGIC_NOT_DELETE_VALUE);
        this.logicNotDeleteValue = StringUtils.isEmpty(logicNotDeleteValueProperty)?null:Integer.valueOf(logicNotDeleteValueProperty);
        String logicDeleteValueProperty = properties.getProperty(InterceptorConstant.LOGIC_DELETE_VALUE);
        this.logicDeleteValue = StringUtils.isEmpty(logicDeleteValueProperty)?null:Integer.valueOf(logicDeleteValueProperty);
    }
}