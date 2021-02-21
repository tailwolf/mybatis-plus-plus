package com.tailwolf.mybatis.datasourse;

import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.context.ApplicationContext;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.dsl.wrapper.base.QueryBaseWrapper;
import com.tailwolf.mybatis.core.proxy.ExecutorProxy;
import com.tailwolf.mybatis.core.util.ApplicationContextUtil;
import com.tailwolf.mybatis.core.util.ArrayUtils;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 数据源切换的核心逻辑
 * @author tailwolf
 * @date 2020-12-29
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SwitchDataSourseInterceptor extends BaseInterceptor implements Interceptor {
    private List<String> dslMapperList = new ArrayList<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor originalExecutor = this.getTarget(invocation.getTarget());
        Executor delegateExecutor = (Executor)ReflectionUtil.getProperty(originalExecutor, InterceptorConstant.DELEGATE);
        Transaction transaction = delegateExecutor.getTransaction();
        //获取数据源名称
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement)args[0];
        String mappedStatementId = mappedStatement.getId();
        String dataSourceName = "";
        if(this.dslMapperList.contains(mappedStatementId)){
            Object parameter = args[1];
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
            QueryBaseWrapper dslWrapper = (QueryBaseWrapper)paramMap.get("dslWrapper");

            dataSourceName = (String)ReflectionUtil.getProperty(dslWrapper, "dataSource");
        }else{
            //com.sun.proxy.$Proxy75.test(Unknown Source)
            String[] splitStr = mappedStatementId.split("\\.");
            //mapper接口类名
            String idClassName = ArrayUtils.joinToString(splitStr, ".", 0, splitStr.length - 2);
            //mapper接口方法名
            String idMethodName = splitStr[splitStr.length-1];
            //mapper接口的代理方法全名
            String middleMethodName = "com.sun.proxy.$Proxy75." + idMethodName;

            //不要一看到类似go to的用法就觉得有问题，这里使用很适合
            StackTraceElement[] stackArray = Thread.currentThread().getStackTrace();
            end: for(int i = 0; i < stackArray.length; i++){
                StackTraceElement stackTraceElement = stackArray[i];
                //调用栈的类名
                String className = stackTraceElement.getClassName();
                //调用栈的方法名
                String methodName = stackTraceElement.getMethodName();
                //调用栈的方法全名
                String declareMethodName = className + "." + methodName;

                if(declareMethodName.equals(middleMethodName)){
                    Class<?> idClass = Class.forName(idClassName);
                    Method[] declaredMethods = idClass.getDeclaredMethods();
                    for(Method method: declaredMethods){
                        if(method.getName().equals(idMethodName)){
                            //查看该方法上是否有@DataSource注解
                            com.tailwolf.mybatis.datasourse.DataSource annotation = method.getAnnotation(com.tailwolf.mybatis.datasourse.DataSource.class);
                            if(annotation != null){
                                dataSourceName = annotation.name();
                            }else{
                                //查看该方法所在的类上有没有该注解
                                annotation = idClass.getAnnotation(com.tailwolf.mybatis.datasourse.DataSource.class);
                                if(annotation != null){//有注解
                                    dataSourceName = annotation.name();
                                }else{//无注解
                                    dataSourceName = stackTraceDataSource(i+1, stackArray);
                                }
                            }
                            break end;
                        }
                    }

                }
            }
        }

        ExecutorProxy.DataSourceName.setDataSourceName(dataSourceName);
        //切换数据源
        DataSource originalDataSource = DataSourceThreadLocal.getValue();
        if(StringUtils.isEmpty(dataSourceName)){
            //把数据源切换回去
            if(originalDataSource != null){
                ReflectionUtil.setProperty(transaction, "dataSource", originalDataSource);
                //清空connection
                ReflectionUtil.setProperty(transaction, "connection", null);
                //清空缓存到的statementMap
                Map<String, Object> statementMap = (HashMap)ReflectionUtil.getProperty(delegateExecutor, "statementMap");
                String lastSql = (String)ReflectionUtil.getProperty(originalExecutor, "sql");
                Object statement = statementMap.get(ReflectionUtil.getProperty(delegateExecutor, "sql"));
                if(statement != null){
                    statementMap.remove(lastSql);
                }
            }
        }else{
            ApplicationContext applicationContext = ApplicationContextUtil.applicationContext;
            DataSource dataSource = (DataSource)applicationContext.getBean(dataSourceName);
            DataSource lastDataSource = (DataSource)ReflectionUtil.getProperty(transaction, "dataSource");
            ReflectionUtil.setProperty(transaction, "dataSource", dataSource);
            //清空connection
            ReflectionUtil.setProperty(transaction, "connection", null);
            //清空缓存到的statementMap
            String lastSql = (String)ReflectionUtil.getProperty(mappedStatement.getSqlSource(), "sql");
            Map<String, Object> statementMap = (HashMap)ReflectionUtil.getProperty(delegateExecutor, "statementMap");
            Object statement = statementMap.get(ReflectionUtil.getProperty(delegateExecutor, "sql"));
            if(statement != null){
                statementMap.remove(lastSql);
            }
            //把数据源切换回去
            if(originalDataSource == null){
                DataSourceThreadLocal.setValue(lastDataSource);
            }
        }

        return invocation.proceed();
    }

    /**
     * 继续沿着调用栈寻找方法或者类上的@DataSource注解
     * @param index         开始下标
     * @param stackArray    调用栈的数组
     * @return
     * @throws ClassNotFoundException
     */
    private String stackTraceDataSource(int index, StackTraceElement[] stackArray) throws ClassNotFoundException {
        for(int i = index; i < stackArray.length; i++){
            StackTraceElement stackTraceElement = stackArray[i];
            String fileName = stackTraceElement.getFileName();
            if(StringUtils.isEmpty(fileName)){
                continue;
            }
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            Class<?> clazz = Class.forName(className);
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for(Method method: declaredMethods){
                if(method.getName().equals(methodName)){
                    com.tailwolf.mybatis.datasourse.DataSource annotation = method.getAnnotation(com.tailwolf.mybatis.datasourse.DataSource.class);
                    if(annotation != null){
                        return annotation.name();
                    }else{
                        //查看该方法所在的类上有没有该注解
                        annotation = clazz.getAnnotation(com.tailwolf.mybatis.datasourse.DataSource.class);
                        if(annotation != null){//有注解
                            return annotation.name();
                        }
                    }
                }
            }
        }

        return null;
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
    }
}
