package com.tailwolf.mybatis.paging;

import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import com.tailwolf.mybatis.core.dsl.build.DslMappedStatementBuild;
import com.tailwolf.mybatis.core.util.ReflectionUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.dsl.wrapper.base.QueryBaseWrapper;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 分页拦截器
 * @author tailwolf
 * @date 2020-05-29
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PagingInterceptor extends BaseInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PreparedStatementHandler preparedStatementHandler = this.getPreparedStatementHandler(invocation.getTarget());
        if(preparedStatementHandler == null){
            return invocation.proceed();
        }
        BoundSql boundSql = preparedStatementHandler.getBoundSql();
        MappedStatement mappedStatement = (MappedStatement) ReflectionUtil.getProperty(preparedStatementHandler, InterceptorConstant.MAPPEDS_TATEMENT);
        if(!DslMappedStatementBuild.DSL_CRUD_ID_SET.contains(mappedStatement.getId())){
            return invocation.proceed();
        }

        Configuration configuration = mappedStatement.getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
        QueryBaseWrapper dslWrapper = (QueryBaseWrapper)paramMap.get("dslWrapper");
        if(dslWrapper instanceof EntityQuery){
            EntityQuery entiryQuery = (EntityQuery)dslWrapper;
            this.paging(entiryQuery, (Connection) invocation.getArgs()[0], dslWrapper.getParameterObject(), configuration, boundSql, mappedStatement, preparedStatementHandler);
        }else if(dslWrapper instanceof JoinQuery){
            JoinQuery joinQuery = (JoinQuery)dslWrapper;
            this.paging(joinQuery, (Connection) invocation.getArgs()[0], dslWrapper.getParameterObject(), configuration, boundSql, mappedStatement, preparedStatementHandler);
        }else{
            return invocation.proceed();
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 重置ParameterHandler
     * @param configuration
     * @param preparedStatementHandler
     * @param mappedStatement
     * @param entity
     * @param newBoundSql
     * @throws IllegalAccessException
     */
    public void resetParameterHandler(Configuration configuration, PreparedStatementHandler preparedStatementHandler, MappedStatement mappedStatement,
                                             Object entity, BoundSql newBoundSql) throws IllegalAccessException {
        ParameterHandler parameterHandler = configuration.newParameterHandler(mappedStatement, entity, newBoundSql);
        ReflectionUtil.setProperty(preparedStatementHandler, InterceptorConstant.PARAMETER_HANDLER, parameterHandler);
    }

    /**
     * 分页时候获得数量总数量
     * @param configuration
     * @param boundSql
     * @param connection
     * @param parameterObject
     * @return
     * @throws SQLException
     */
    public int getPageTotal(Configuration configuration, BoundSql boundSql, Connection connection, Object parameterObject) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = boundSql.getSql();
            String totalSql = "SELECT COUNT(*) FROM (" + sql + ") TEMP";
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

            BoundSql countBoundSql = new BoundSql(configuration, totalSql, parameterMappings, parameterObject);
            List<ParameterMapping> newParameterMappings = countBoundSql.getParameterMappings();

            preparedStatement = connection.prepareStatement(totalSql);
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (newParameterMappings != null) {
                for(int i = 0; i < newParameterMappings.size(); ++i) {
                    ParameterMapping parameterMapping = (ParameterMapping)newParameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {
                        String propertyName = parameterMapping.getProperty();
                        Object value;
                        if (countBoundSql.hasAdditionalParameter(propertyName)) {
                            value = countBoundSql.getAdditionalParameter(propertyName);
                        } else if (parameterObject == null) {
                            value = null;
                        } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                            value = parameterObject;
                        } else {
                            MetaObject metaObject = configuration.newMetaObject(parameterObject);
                            value = metaObject.getValue(propertyName);
                        }

                        TypeHandler typeHandler = parameterMapping.getTypeHandler();
                        JdbcType jdbcType = parameterMapping.getJdbcType();
                        if (value == null && jdbcType == null) {
                            jdbcType = configuration.getJdbcTypeForNull();
                        }
                        typeHandler.setParameter(preparedStatement, i + 1, value, jdbcType);
                    }
                }
            }

            //执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    /**
     * 设置分页
     * @param boundSql
     *        被设置的对象
     * @param currentPage
     *        分页，当前页
     * @param pageSize
     *        分页，页面大小
     * @throws IllegalAccessException
     */
    public void setPage(BoundSql boundSql, int currentPage, int pageSize) throws IllegalAccessException {
        int offset = currentPage * pageSize;
        String sql = boundSql.getSql();
        String resetSql = sql + " limit " + offset + "," + pageSize;

        ReflectionUtil.setProperty(boundSql, InterceptorConstant.SQL, resetSql);
    }

    /**
     * 分页
     * @param query
     * @param connection
     * @param configuration
     * @param newBoundSql
     * @param mappedStatement
     * @param preparedStatementHandler
     * @throws IllegalAccessException
     */
    public void paging(QueryBaseWrapper query, Connection connection, Object entity, Configuration configuration, BoundSql newBoundSql, MappedStatement mappedStatement, PreparedStatementHandler preparedStatementHandler) throws IllegalAccessException, SQLException {
        Limiter limiter = query.createLimiter();
        Integer currentPage = limiter.getCurrentPage();
        Integer pageSize = limiter.getPageSize();
        if(currentPage != null && currentPage >= 0 && pageSize != null && pageSize > 0){
            int pageTotal = this.getPageTotal(configuration, newBoundSql, connection, entity);
            query.setPageTotal(pageTotal);
            this.setPage(newBoundSql, currentPage - 1, pageSize);
        }

        this.resetParameterHandler(configuration, preparedStatementHandler, mappedStatement, entity, newBoundSql);
    }
}
