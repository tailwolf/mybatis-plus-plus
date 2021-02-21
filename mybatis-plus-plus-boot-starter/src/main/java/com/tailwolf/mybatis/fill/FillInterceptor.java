package com.tailwolf.mybatis.fill;

import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.common.interceptor.BaseInterceptor;
import com.tailwolf.mybatis.core.util.ApplicationContextUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 该拦截器的是给指定的字段填充值
 * @author tailwolf
 * @date 2021-01-24
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class FillInterceptor extends BaseInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PreparedStatementHandler preparedStatementHandler = this.getPreparedStatementHandler(invocation.getTarget());
        if(preparedStatementHandler == null){
            return invocation.proceed();
        }

        BoundSql boundSql = preparedStatementHandler.getBoundSql();
        String sql = boundSql.getSql();
        MappedStatement mappedStatement = (MappedStatement) ReflectionUtil.getProperty(preparedStatementHandler, InterceptorConstant.MAPPEDS_TATEMENT);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        FillFieldHandler fillFieldHandler = fillFieldHandler();
        if(SqlCommandType.UPDATE == sqlCommandType && fillFieldHandler != null){
            //获取update语句里面set子句的字段名
            Update update = (Update) CCJSqlParserUtil.parse(sql);
            List<Column> columnList = update.getColumns();
            //获取updateFill里面的属性
            UpdateFill updateFill = new UpdateFill();
            fillFieldHandler.updateFill(updateFill);
            Map<String, Object> properties = updateFill.getProperties();
            //求updateFill里有，而columnList里没有的列名
            List<String> updateColumnList = new ArrayList<>(properties.keySet());
            updateColumnList.remove(columnList);
            for(String updateColumn: updateColumnList){
                Object updateValue = properties.get(updateColumn);

                List<Expression> expressionList = update.getExpressions();
                Expression newExpression = createExpression(updateValue);
                expressionList.add(newExpression);

                update.setExpressions(expressionList);
                columnList.add(new net.sf.jsqlparser.schema.Column(updateColumn));
                update.setColumns(columnList);
                String newSql = update.toString();
                ReflectionUtil.setProperty(boundSql, "sql", newSql);
            }
        }else if(SqlCommandType.INSERT == sqlCommandType && fillFieldHandler != null){
            Insert insert = (Insert)CCJSqlParserUtil.parse(sql);
            List<Column> columnList = insert.getColumns();
            InsertFill insertFill = new InsertFill();
            fillFieldHandler.insertFill(insertFill);
            Map<String, Object> properties = insertFill.getProperties();
            //求insertFill里有，而columnList里没有的列名
            List<String> insertColumnList = new ArrayList<>(properties.keySet());
            insertColumnList.remove(columnList);
            for(String insertColumn: insertColumnList){
                Object insertValue = properties.get(insertColumn);

                ItemsList itemsList = insert.getItemsList();
                setItemsValue(insertValue, itemsList);
                columnList.add(new net.sf.jsqlparser.schema.Column(insertColumn));
                insert.setColumns(columnList);
                String newSql = insert.toString();
                ReflectionUtil.setProperty(boundSql, "sql", newSql);
            }
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

    private FillFieldHandler fillFieldHandler(){
        FillFieldHandler fillFieldHandler = null;
        try {
            fillFieldHandler = ApplicationContextUtil.applicationContext.getBean(FillFieldHandler.class);
        }catch (NoSuchBeanDefinitionException ignored){
        }

        return fillFieldHandler;
    }

    private void setItemsValue(Object insertValue, ItemsList itemsList){
        if(itemsList instanceof ExpressionList){
            ExpressionList expressionList = (ExpressionList)itemsList;
            List<Expression> expressions = expressionList.getExpressions();
            Expression newExpression = createExpression(insertValue);
            expressions.add(newExpression);
        }else{
            MultiExpressionList multiExpressionList = (MultiExpressionList)itemsList;
            List<ExpressionList> exprList = multiExpressionList.getExprList();
            for(ExpressionList expressionList: exprList){
                setItemsValue(insertValue, expressionList);
            }
        }
    }

    private Expression createExpression(Object value){
        if(value instanceof Long){
            return new LongValue(value.toString());
        }else if(value instanceof Double){
            return new DoubleValue(value.toString());
        }
        else if(value instanceof Date){
            return new StringValue(DateToString((Date)value));
        }else{
            return new StringValue(value.toString());
        }
    }
}