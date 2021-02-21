package com.tailwolf.mybatis.core;

import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import net.sf.cglib.proxy.Enhancer;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.proxy.ExecutorProxy;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.util.*;

/**
 * 该拦截器类的主要作用是用cglib代理Executor
 * @author tailwolf
 * @date 2021-01-02
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class CgLibExecutorInterceptor extends BaseInterceptor implements Interceptor {
    //是否打印完整sql的开关
    private boolean completeSql = false;
    //保存了delegateExecutor的Class
    private Class<?> enhancerSuperClass;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Executor pluginExecutor = (Executor)invocation.getTarget();
        Executor originalExecutor = this.getTarget(invocation.getTarget());
        Executor delegateExecutor = (Executor)ReflectionUtil.getProperty(originalExecutor, InterceptorConstant.DELEGATE);
        if(this.enhancerSuperClass == null){
            this.enhancerSuperClass = delegateExecutor.getClass();
        }
        Configuration configuration = mappedStatement.getConfiguration();

        //CGLIB代理
        ExecutorProxy executorProxy = new ExecutorProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(enhancerSuperClass);
        enhancer.setCallback(executorProxy);
        Executor cglibExecutor = (Executor)enhancer.create(new Class[]{Configuration.class, Transaction.class}, new Object[] {configuration, delegateExecutor.getTransaction()});
        executorProxy.setCompleteSql(completeSql);
        executorProxy.setCglibExecutor(cglibExecutor);
        executorProxy.setPluginExecutor(pluginExecutor);
        executorProxy.setOriginalExecutor(originalExecutor);
        ReflectionUtil.setProperty(invocation, "target", cglibExecutor);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.completeSql = Boolean.parseBoolean(properties.getProperty(InterceptorConstant.COMPLETE_SQL));
    }
}
