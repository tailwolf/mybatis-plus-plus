package com.tailwolf.mybatis.core.api.interceptor;

import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.util.*;

/**
 * 主要用来处理是基于实体类的增删改查。
 * 1.把dao的增删改查的MappedStatement的id，换成每个实体类唯一的。
 *   比如用户调用wolfweb.mybatis.dao.EntityOptMapper.insert接口，入参是com.User，这里，
 *   获取到的MappedStatement的id就是wolfweb.mybatis.dao.EntityOptMapper.insert，需要把这个MappedStatement的id
 *   替换成com.User.insert。
 * 2.给基于主键的重新生成删除和查询的sql
 * @author tailwolf
 * @date 2020-10-06
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class EntitySqlInterceptor extends BaseInterceptor implements Interceptor {
    private static Set<String> daoMapperSet = new HashSet<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Executor executor = this.getTarget(invocation.getTarget());
        Configuration configuration;

        String mappedStatementId = mappedStatement.getId();
        if(this.daoMapperSet.contains(mappedStatementId)){
            //替换MappedStatement的id
            Object delegate = ReflectionUtil.getProperty(executor, "delegate");
            configuration = (Configuration) ReflectionUtil.getProperty(delegate, "configuration");
            Map<String, MappedStatement> mappedStatementMap = (Map)ReflectionUtil.getProperty(configuration, "mappedStatements");
            Object entity = invocation.getArgs()[1];
            if(entity instanceof DefaultSqlSession.StrictMap){
                DefaultSqlSession.StrictMap strictMap = (DefaultSqlSession.StrictMap)entity;
                List list = (List)strictMap.get("list");
                entity = list.get(0);
            }
            String[] split = mappedStatementId.split("\\.");
            String mappedStatementKey = entity.getClass().getTypeName() + "." + split[split.length - 1];
            MappedStatement entityMappedStatement = mappedStatementMap.get(mappedStatementKey);
            Object[] args = invocation.getArgs();
            args[0] = entityMappedStatement;
            ReflectionUtil.setProperty(invocation, "args", args);

        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.INSERT));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.UPDATE_BY_PK));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.UPDATE_BATCH_BY_PK));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.DELETE_BATCH_BY_PK));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.DELETE_BY_PK));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.DELETE));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.FIND_LIST));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.FIND_ONE));
        this.daoMapperSet.add(properties.getProperty(InterceptorConstant.FIND_BY_PK));
    }
}
