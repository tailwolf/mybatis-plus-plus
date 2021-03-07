package com.tailwolf.mybatis.core.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.dsl.wrapper.base.BaseWrapper;
import com.tailwolf.mybatis.log.ConnectionLogger;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * cglib代理Executor
 * @author tailwolf
 * @date 2020-12-10
 */
public class ExecutorProxy implements MethodInterceptor {
    //是否开启打印完整sql的功能
    private boolean completeSql = false;
    //被cglib代理后的Executor
    private Executor cglibExecutor;
    //未被cglib代理的Executor
    private Executor pluginExecutor;
    //原始的Executor
    private Executor originalExecutor;
    //一级缓存key
    private CacheKey cacheKey;
    //保存了BoundSql
    private BoundSql boundSql;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String name = method.getName();
        if("update".equals(name) && args.length == 2){
            Object parameter = args[1];
            BaseWrapper baseWrapper = getBaseWrapper(parameter);
            if(baseWrapper != null){
                Object parameterObject = ReflectionUtil.getProperty(baseWrapper, "parameterObject");
                args[1] = parameterObject;
            }
        }
        else if("query".equals(name) && args.length == 4){
            //这里生成一级缓存key的时候，加多了一个数据源名称因素，避免同事务下同样的sql语句但数据源不同，导致击中相同的缓存
            MappedStatement mappedStatement = (MappedStatement)args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds)args[2];
            ResultHandler resultHandler = (ResultHandler)args[3];

            BaseWrapper baseWrapper = getBaseWrapper(parameter);
            Object parameterObject = null;
            CacheKey cacheKey = null;
            String dataSourceName;
            if(baseWrapper != null){
                parameterObject = ReflectionUtil.getProperty(baseWrapper, "parameterObject");
                dataSourceName = (String)ReflectionUtil.getProperty(baseWrapper, "dataSource");
            }else{
                parameterObject = parameter;
                dataSourceName = DataSourceName.getDataSourceName();
                DataSourceName.clean();
            }
            BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
            cacheKey = pluginExecutor.createCacheKey(mappedStatement, parameterObject, rowBounds, boundSql);
            if(!StringUtils.isEmpty(dataSourceName)){
                cacheKey.update(dataSourceName);
            }
            this.cacheKey = cacheKey;
            //设置参数对象，给下游拦截器使用
            ReflectionUtil.setProperty(boundSql, "parameterObject", parameter);
            this.boundSql = boundSql;
            /**
             * 这里对originalExecutor的原始对象做一次cglib代理。
             * 原因是query(MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class)方法内部会执行
             * query(MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class)方法，
             * 原来的mybatis动态代理无法代理到该方法，所以这里用cglib进行代理，拦截对象是ExecutorProxy。
             *
             * 这样，既不会使原来的Plugin代理失效，又能代理到之前mybatis动态代理无法代理到的方法
             */
            ReflectionUtil.setProperty(originalExecutor, InterceptorConstant.DELEGATE, cglibExecutor);
            return pluginExecutor.query(mappedStatement, parameterObject, rowBounds, resultHandler);
        }else if("query".equals(name) && args.length == 6){
            args[4] = this.cacheKey;
            args[5] = this.boundSql;
//            return methodProxy.invokeSuper(cglibExecutor, args);
        }
        /**
         * 这里代理getConnection重新生成了mybayis++自己写的ConnectionLogger，而不要mybatis的org.apache.ibatis.logging.jdbc.ConnectionLogger
         * 目的是为了定制sql语句打印功能
         */
        else if("getConnection".equals(name) && completeSql){
            Transaction transaction = (Transaction)ReflectionUtil.getProperty(obj, "transaction");
            int queryStack = (int)ReflectionUtil.getProperty(obj, "queryStack");
            Connection connection = transaction.getConnection();
            Log log = (Log)args[0];
            return log.isDebugEnabled() ? ConnectionLogger.newInstance(connection, log, queryStack) : connection;
        }

        return methodProxy.invokeSuper(cglibExecutor, args);
    }

    public void setCglibExecutor(Executor cglibExecutor) {
        this.cglibExecutor = cglibExecutor;
    }

    public void setCompleteSql(boolean completeSql) {
        this.completeSql = completeSql;
    }

    public void setPluginExecutor(Executor pluginExecutor) {
        this.pluginExecutor = pluginExecutor;
    }

    public void setOriginalExecutor(Executor originalExecutor) {
        this.originalExecutor = originalExecutor;
    }

    /**
     * 返回入参的BaseWrapper。
     * 如果有BaseWrapper，说明是dsl操作，否则就不是dsl操作，它们多数据源的实现方式不同
     * @param parameter
     * @return
     */
    private BaseWrapper getBaseWrapper(Object parameter){
        if(!(parameter instanceof MapperMethod.ParamMap)){
            return null;
        }

        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
        BaseWrapper dslWrapper;
        try {
            dslWrapper = (BaseWrapper) paramMap.get("dslWrapper");
        }catch (BindingException be){
            return null;
        }
        return dslWrapper;
    }

    public static class DataSourceName{
        private DataSourceName(){}

        private static final ThreadLocal<String> NAME = new ThreadLocal<>();

        public static String getDataSourceName(){
            return NAME.get();
        }

        public static void setDataSourceName(String dataSourceName){
            NAME.set(dataSourceName);
        }

        public static void clean(){
            NAME.remove();
        }
    }
}
