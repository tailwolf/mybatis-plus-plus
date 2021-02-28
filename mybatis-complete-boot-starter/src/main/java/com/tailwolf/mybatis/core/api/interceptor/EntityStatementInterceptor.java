package com.tailwolf.mybatis.core.api.interceptor;

import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.core.api.build.EntityCrudStatementBuild;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.ColumnModel;
import com.tailwolf.mybatis.core.util.ColumnModelUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

/**
 * mybatis-complete的实体类增删改查拦截器
 * @author tailwolf
 * @date 2020-09-06
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class EntityStatementInterceptor extends BaseInterceptor implements Interceptor {

    private String logicDeleteField;
    private Integer logicDeleteValue = 1;
    private Integer logicNotDeleteValue = 0;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PreparedStatementHandler preparedStatementHandler = this.getPreparedStatementHandler(invocation.getTarget());
        if(preparedStatementHandler == null){
            return invocation.proceed();
        }

        BoundSql boundSql = preparedStatementHandler.getBoundSql();
        MappedStatement mappedStatement = (MappedStatement)ReflectionUtil.getProperty(preparedStatementHandler, InterceptorConstant.MAPPEDS_TATEMENT);
        String mappedStatementId = mappedStatement.getId();
        if(!EntityCrudStatementBuild.ENTITY_CRUD_ID_SET.contains(mappedStatementId)){
            return invocation.proceed();
        }

        Object parameterObject = boundSql.getParameterObject();
        Class<?> entityClazz = boundSql.getParameterObject().getClass();
        if(parameterObject instanceof DefaultSqlSession.StrictMap){
            DefaultSqlSession.StrictMap strictMap = (DefaultSqlSession.StrictMap)parameterObject;
            List list = (List)strictMap.get("list");
            entityClazz = list.get(0).getClass();
        }

        Table tableAnnot = entityClazz.getAnnotation(Table.class);
        if(tableAnnot == null){
            throw new MybatisCompleteRuntimeException("实体类必需加上注解@Table");
        }

        List<Field> allFieldList = ReflectionUtil.getAllFields(entityClazz);
        List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(allFieldList, new ArrayList<>());
        String sql = this.structureLogicDelete(mappedStatement.getSqlCommandType(), boundSql.getParameterObject(),
                this.logicDeleteField, this.logicNotDeleteValue, this.logicDeleteValue , boundSql.getSql(), null, columnModelList);
        sql = this.structureOptimisticLock(mappedStatement.getSqlCommandType(), boundSql.getParameterObject(), sql, columnModelList);
        ReflectionUtil.setProperty(boundSql, "sql", sql);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.logicDeleteField = properties.getProperty(InterceptorConstant.FIELD);
        String logicDeleteValueProperty = properties.getProperty(InterceptorConstant.LOGIC_DELETE_VALUE);
        this.logicDeleteValue = StringUtils.isEmpty(logicDeleteValueProperty)?null:Integer.valueOf(logicDeleteValueProperty);
        String logicNotDeleteValueProperty = properties.getProperty(InterceptorConstant.LOGIC_NOT_DELETE_VALUE);
        this.logicNotDeleteValue = StringUtils.isEmpty(logicNotDeleteValueProperty)?null:Integer.valueOf(logicNotDeleteValueProperty);
    }
}
