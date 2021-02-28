package com.tailwolf.mybatis.log;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.BaseJdbcLogger;
import org.apache.ibatis.logging.jdbc.StatementLogger;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 该类大部分代码来自mybatis的org.apache.ibatis.logging.jdbc.ConnectionLogger
 * @author tailwolf
 * @date 2020-12-12
 */
public final class ConnectionLogger extends BaseJdbcLogger implements InvocationHandler {
    private Connection connection;

    private ConnectionLogger(Connection conn, Log statementLog, int queryStack) {
        super(statementLog, queryStack);
        this.connection = conn;
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            } else {
                PreparedStatement preparedStatement;
                if ("prepareStatement".equals(method.getName())) {

                    preparedStatement = (PreparedStatement)method.invoke(this.connection, params);
                    /**
                     * 使用PreparedStatementLogger动态代理了PreparedStatement
                     * 这里使用mybatis-complete的PreparedStatementLogger替代了mybatis的org.apache.ibatis.logging.jdbc.PreparedStatementLogger
                     */
                    preparedStatement = PreparedStatementLogger.newInstance(preparedStatement, this.statementLog, this.queryStack, this.removeBreakingWhitespace((String)params[0]));
                    return preparedStatement;
                } else if ("prepareCall".equals(method.getName())) {
                    preparedStatement = (PreparedStatement)method.invoke(this.connection, params);
                    preparedStatement = PreparedStatementLogger.newInstance(preparedStatement, this.statementLog, this.queryStack, this.removeBreakingWhitespace((String)params[0]));
                    return preparedStatement;
                } else if ("createStatement".equals(method.getName())) {
                    Statement stmt = (Statement)method.invoke(this.connection, params);
                    stmt = StatementLogger.newInstance(stmt, this.statementLog, this.queryStack);
                    return stmt;
                } else {
                    return method.invoke(this.connection, params);
                }
            }
        } catch (Throwable var5) {
            throw ExceptionUtil.unwrapThrowable(var5);
        }
    }

    public static Connection newInstance(Connection conn, Log statementLog, int queryStack) {
        InvocationHandler handler = new ConnectionLogger(conn, statementLog, queryStack);
        ClassLoader cl = Connection.class.getClassLoader();
        return (Connection) Proxy.newProxyInstance(cl, new Class[]{Connection.class}, handler);
    }

    public Connection getConnection() {
        return this.connection;
    }
}
