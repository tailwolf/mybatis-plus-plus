package com.tailwolf.mybatis.log;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.BaseJdbcLogger;
import org.apache.ibatis.logging.jdbc.ResultSetLogger;
import org.apache.ibatis.reflection.ExceptionUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 该类大部分代码来自mybatis的org.apache.ibatis.logging.jdbc.PreparedStatementLogger
 * @author tailwolf
 * @date 2020-12-12
 */
public class PreparedStatementLogger extends BaseJdbcLogger implements InvocationHandler {
    private PreparedStatement statement;
    /**
     * 相比mybatis原来的org.apache.ibatis.logging.jdbc.PreparedStatementLogger类，这个类多了一个preparingSql属性，
     * 该属性保存的是预编译语句
     */
    private String preparingSql;

    private PreparedStatementLogger(PreparedStatement stmt, Log statementLog, int queryStack, String preparingSql) {
        super(statementLog, queryStack);
        this.statement = stmt;
        this.preparingSql = preparingSql;
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            } else {
                ResultSet rs;
                if (EXECUTE_METHODS.contains(method.getName())) {
                    if (this.isDebugEnabled()) {
                        /**
                         * 使用入参填充预编译语句里面占位符 '?'，使之变成一个完整的sql语句，然后打印输出
                         */
                        List<Object> columnValueList = (List)ReflectionUtil.getProperty(this, "columnValues");
                        for(Object columnValue: columnValueList){
                            if(columnValue instanceof Number){
                                preparingSql = preparingSql.replaceFirst("\\?", columnValue + "");
                            }else{
                                preparingSql = preparingSql.replaceFirst("\\?", "'" + columnValue + "'");
                            }

                        }
                        this.debug("       SQL: " + preparingSql, true);
                    }

                    this.clearColumnInfo();
                    long beforeMillis = System.currentTimeMillis();
                    if ("executeQuery".equals(method.getName())) {
                        rs = (ResultSet)method.invoke(this.statement, params);
                        long afterMillis = System.currentTimeMillis();
                        debug("  duration: " + (afterMillis - beforeMillis), true);
                        return rs == null ? null : ResultSetLogger.newInstance(rs, this.statementLog, this.queryStack);
                    } else {
                        Object invoke = method.invoke(this.statement, params);
                        long afterMillis = System.currentTimeMillis();
                        debug("  duration: " + (afterMillis - beforeMillis) + "ms", true);
                        return invoke;
                    }
                } else if (SET_METHODS.contains(method.getName())) {
                    if ("setNull".equals(method.getName())) {
                        this.setColumn(params[0], (Object)null);
                    } else {
                        this.setColumn(params[0], params[1]);
                    }

                    return method.invoke(this.statement, params);
                } else if ("getResultSet".equals(method.getName())) {
                    rs = (ResultSet)method.invoke(this.statement, params);
                    return rs == null ? null : ResultSetLogger.newInstance(rs, this.statementLog, this.queryStack);
                } else if ("getUpdateCount".equals(method.getName())) {
                    int updateCount = (Integer)method.invoke(this.statement, params);
                    if (updateCount != -1) {
                        this.debug("   Updates: " + updateCount, false);
                    }

                    return updateCount;
                } else {
                    return method.invoke(this.statement, params);
                }
            }
        } catch (Throwable var5) {
            throw ExceptionUtil.unwrapThrowable(var5);
        }
    }

    public static PreparedStatement newInstance(PreparedStatement stmt, Log statementLog, int queryStack, String preparingSql) {
        InvocationHandler handler = new PreparedStatementLogger(stmt, statementLog, queryStack, preparingSql);
        ClassLoader cl = PreparedStatement.class.getClassLoader();
        return (PreparedStatement) Proxy.newProxyInstance(cl, new Class[]{PreparedStatement.class, CallableStatement.class}, handler);
    }

    public PreparedStatement getPreparedStatement() {
        return this.statement;
    }
}
