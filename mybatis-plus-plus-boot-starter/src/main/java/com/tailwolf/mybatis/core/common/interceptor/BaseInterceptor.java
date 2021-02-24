package com.tailwolf.mybatis.core.common.interceptor;

import com.tailwolf.mybatis.core.ColumnModel;
import com.tailwolf.mybatis.core.annotation.*;
import com.tailwolf.mybatis.core.annotation.Collection;
import com.tailwolf.mybatis.core.dsl.interceptor.DslSqlInterceptor;
import com.tailwolf.mybatis.core.util.CollectionUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.statement.CallableStatementHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.constant.MontageSqlConstant;
import com.tailwolf.mybatis.core.exception.MybatisPlusPlusRuntimeException;
import com.tailwolf.mybatis.core.util.ColumnModelUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * mybatis拦截器的基础类，用来复用方法
 * @author tailwolf
 * @date 2020-11-23
 */
public class BaseInterceptor {
    protected Log log = LogFactory.getLog(DslSqlInterceptor.class);
    /**
     * 获取StatementHandler
     * @param target
     * @return
     */
    protected  <T> T getTarget(Object target){
        if(!(target instanceof Proxy)){
            return (T) target;
        }

        target = Proxy.getInvocationHandler(target);
        target = ReflectionUtil.getProperty(target, InterceptorConstant.TARGET);
        return getTarget(target);
    }

    /**
     * 创建属性名和表名的映射
     * @param columnModelList
     * @param tableAliasName
     * @param tableName
     * @return
     */
    protected Map<String, String> getFiledColumnNameMap(List<ColumnModel> columnModelList, String tableAliasName, String tableName) {
        Map<String, String> filedColumnNameMap = new HashMap<>();
        for(ColumnModel columnModel: columnModelList){
            Field field = columnModel.getField();
            String name = field.getName();
            if(!org.springframework.util.StringUtils.isEmpty(tableName)){
                name = tableName + "." + name;
            }
            String columnName = columnModel.getColumnName();
            if(!org.springframework.util.StringUtils.isEmpty(tableAliasName)){
                columnName = tableAliasName + "." + columnName;
            }

            filedColumnNameMap.put(name, columnName);
        }
        return filedColumnNameMap;
    }

    /**
     * 拼接resultMap里的result标签
     * @param columnModelList
     * @param resultMapBuffer
     */
    protected void montageResultLable(List<ColumnModel> columnModelList, StringBuffer resultMapBuffer){
        Map<String, String> filedColumnMap = getFiledColumnNameMap(columnModelList, null, null);
        for(var entry: filedColumnMap.entrySet()){
            String columnName = entry.getKey();
            String filedName = entry.getValue();
            resultMapBuffer.append("<result column=\"").append(filedName).append("\" property=\"").append(columnName).append("\"/>");
        }
    }

    /**
     * 拼接resultMap里面的子标签
     * @param labelType         resultMap子标签类型，association或者collection
     * @param field             字段属性
     * @param resultMapBuffer   保存resultMap标签的字符串
     * @param configuration     mybatis的Configuration
     * @param type              resultMap子标签里的映射对象类型，如果是association，该值是javaType，如果是collection，该值是ofType
     * @param annotationClazz   resultMap子标签注解的Class
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws InvocationTargetException
     */
    protected void montageSubObject(String labelType, Field field, StringBuffer resultMapBuffer, Configuration configuration, String type, Class annotationClazz) throws IllegalAccessException, NoSuchMethodException, IOException, InvocationTargetException {
        String name = field.getName();
        Annotation annotation = field.getAnnotation(annotationClazz);

        Class<?> collectionClazz = null;
        if("association".equals(labelType)){
            collectionClazz = field.getType();
        }else{
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            LinkedHashMap<String, Class<?>> memberValues = (LinkedHashMap)ReflectionUtil.getProperty(invocationHandler, "memberValues");
            collectionClazz = memberValues.get("clazz");
        }

        resultMapBuffer.append("<").append(labelType).append(" property=\"").append(name).append("\" ").append(type).append("=\"").append(collectionClazz.getName()).append("\">");
        List<Field> filedList = ReflectionUtil.getAllFields(collectionClazz);
        List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(filedList, new ArrayList<>());
        montageResultLable(columnModelList, resultMapBuffer);
        resultMapBuffer.append("</").append(labelType).append(">");

        createResultMap(collectionClazz, configuration, resultMapBuffer);
    }

    /**
     * 创建ResultMap对象
     * @param paramMap          封装了mapper方法的入参
     * @param configuration     mybatis的Configuration
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    protected ResultMap createResultMap(MapperMethod.ParamMap<?> paramMap, Configuration configuration) throws IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException {
        StringBuffer resultMapBuffer = new StringBuffer();
        Class<?> returnEntity = (Class<?>)paramMap.get("returnEntity");
        resultMapBuffer.append("<resultMap type=\"").append(returnEntity.getName()).append("\" id=\"");
        resultMapBuffer.append(UUID.randomUUID().toString());
        resultMapBuffer.append("\">");

        List<Field> fieldList = ReflectionUtil.getAllFields(returnEntity);
        List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(fieldList, List.of(Association.class, Collection.class));
        montageResultLable(columnModelList, resultMapBuffer);
        createResultMap(returnEntity, configuration, resultMapBuffer);
        resultMapBuffer.append("</resultMap>");

        String doc = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"
                + "<mapper >$sqlTag$</mapper>";
        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", resultMapBuffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        XNode xNode = xPathParser.evalNode("/mapper").evalNode("resultMap");

        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, null, null);
        Method resultMapElement = xmlMapperBuilder.getClass().getDeclaredMethod("resultMapElement", XNode.class, List.class);
        resultMapElement.setAccessible(true);

        return (ResultMap)resultMapElement.invoke(xmlMapperBuilder, xNode, new ArrayList<>());

    }

    /**
     *
     * @param clazz
     * @param configuration
     * @param resultMapBuffer
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    protected void createResultMap(Class clazz, Configuration configuration, StringBuffer resultMapBuffer) throws IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException {
        List<Field> fileds = ReflectionUtil.getAllFields(clazz);
        if(CollectionUtil.isEmtpy(fileds)){
            return;
        }

        Field[] associationFileds = ReflectionUtil.getFileds(clazz, Association.class);
        if(associationFileds != null && associationFileds.length > 0){
            for(Field field: associationFileds){
                montageSubObject("association", field, resultMapBuffer, configuration, "javaType", Association.class);
            }
        }
        Field[] collectionFileds = ReflectionUtil.getFileds(clazz, Collection.class);
        if(collectionFileds != null && collectionFileds.length > 0){
            for(Field field: collectionFileds){
                montageSubObject("collection", field, resultMapBuffer, configuration, "ofType", Collection.class);
            }
        }
    }

    /**
     * 构造逻辑删除sql
     * 当同时配置了逻辑删除和逻辑非删除字段，才同时开启逻辑删除功能
     * @param entity
     * @param logicDeleteField
     * @param logicDeleteValue
     * @param sql
     * @param tableAliasName
     * @return
     * @throws JSQLParserException
     */
    protected String structureLogicDelete(SqlCommandType sqlCommandType, Object entity, String logicDeleteField,
                                              Integer logicNotDeleteValue, Integer logicDeleteValue, String sql, String tableAliasName, List<ColumnModel> columnModelList) throws JSQLParserException, IllegalAccessException {
        if(logicNotDeleteValue == null || logicDeleteValue == null){
            return sql;
        }
        ColumnModel columnModel = this.getColumnModel(entity.getClass(), TableLogic.class, logicDeleteField, columnModelList);
        if(columnModel == null){
            return sql;
        }

        String columnName = columnModel.getColumnName();
        if(!StringUtils.isEmpty(tableAliasName)){
            columnName = tableAliasName + "." + columnName;
        }

        if(SqlCommandType.SELECT == sqlCommandType){
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            PlainSelect plainSelect = (PlainSelect)selectBody;
            Expression expressionWhere = plainSelect.getWhere();
            if(expressionWhere == null){
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new net.sf.jsqlparser.schema.Column(columnName));
                equalsTo.setRightExpression(new LongValue(logicNotDeleteValue));
                plainSelect.setWhere(equalsTo);
            }else{
                Expression singleExpression = getSingleExpression(columnName, expressionWhere);
                if(singleExpression != null){
                    return sql;
                }

                String whereSql = expressionWhere.toString();
                whereSql = whereSql + MontageSqlConstant.SPACE + MontageSqlConstant.AND + columnName + MontageSqlConstant.SPACE + MontageSqlConstant.EQ + logicNotDeleteValue;
                Expression where = CCJSqlParserUtil.parseCondExpression(whereSql);
                ((PlainSelect) select.getSelectBody()).setWhere(where);
            }
            sql = select.toString();

        }else if(SqlCommandType.DELETE == sqlCommandType){

            Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
            net.sf.jsqlparser.schema.Table table = delete.getTable();
            String tableName = table.getName();
            String newSql = SqlCommandType.UPDATE + MontageSqlConstant.SPACE + tableName + MontageSqlConstant.SPACE
                    + MontageSqlConstant.SET + columnName + MontageSqlConstant.SPACE + MontageSqlConstant.EQ + logicDeleteValue;
            Update update = (Update) CCJSqlParserUtil.parse(newSql);

            Expression expressionWhere = delete.getWhere();
            if(expressionWhere == null){
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new net.sf.jsqlparser.schema.Column(columnName));
                equalsTo.setRightExpression(new LongValue(logicNotDeleteValue));
                update.setWhere(equalsTo);
            }else{
                Expression singleExpression = getSingleExpression(columnName, expressionWhere);
                if(singleExpression != null){
                    update.setWhere(expressionWhere);
                }else{
                    String whereSql = expressionWhere.toString();
                    whereSql = whereSql + MontageSqlConstant.SPACE + MontageSqlConstant.AND + columnName + MontageSqlConstant.SPACE + MontageSqlConstant.EQ + logicNotDeleteValue;
                    Expression where = CCJSqlParserUtil.parseCondExpression(whereSql);
                    update.setWhere(where);
                }
            }
            sql = update.toString();
        }else if(SqlCommandType.UPDATE == sqlCommandType){
            Update update = (Update) CCJSqlParserUtil.parse(sql);
            Expression expressionWhere = update.getWhere();
            if(expressionWhere == null){
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new net.sf.jsqlparser.schema.Column(columnName));
                equalsTo.setRightExpression(new LongValue(logicDeleteValue));
                update.setWhere(equalsTo);
            }else{
                Expression singleExpression = getSingleExpression(columnName, expressionWhere);
                if(singleExpression != null){
                    return sql;
                }

                String whereSql = expressionWhere.toString();
                whereSql = whereSql + MontageSqlConstant.SPACE + MontageSqlConstant.AND + MontageSqlConstant.SPACE + columnName + MontageSqlConstant.SPACE + MontageSqlConstant.EQ + logicNotDeleteValue;
                Expression where = CCJSqlParserUtil.parseCondExpression(whereSql);
                update.setWhere(where);
            }
            sql = update.toString();
        } else{
            if(ReflectionUtil.getProperty(entity, logicDeleteField) == null){
                ReflectionUtil.setProperty(entity, logicDeleteField, logicNotDeleteValue);
            }
        }

        return sql;
    }

    /**
     * @param entityClazz
     * @param appointFieldName
     * @return
     */
    public ColumnModel getColumnModel(Class<?> entityClazz, Class annotationClass, String appointFieldName, List<ColumnModel> columnModelList){
        List<Field> fieldList = ReflectionUtil.getAllFields(entityClazz);
        Field[] fields = ReflectionUtil.retainFieldByAnnotation(fieldList.toArray(new Field[0]), annotationClass);
        if(fields.length > 1){
            throw new MybatisPlusPlusRuntimeException("实体类上最多只能有一个属性被@TableLogic注解修饰！");
        }else if(fields.length == 1){
            for(ColumnModel columnModel: columnModelList){
                if(columnModel.getField().getName().equals(fields[0].getName())){
                    return columnModel;
                }
            }
        }else if(!StringUtils.isEmpty(appointFieldName)){
            for(ColumnModel columnModel: columnModelList){
                if(columnModel.getField().getName().equals(appointFieldName)){
                    return columnModel;
                }
            }
        }

        return null;
    }

    /**
     * 构造并发版本控制的sql
     * 当更新的时候，@Version注解的字段(版本控制字段)上有值，同时：
     * 1.SET语句不存在版本字段，则会给版本字段SET新的值，新值 = 原来的值 + 1
     * 2.SET语句存在版本字段，则跳过
     *
     * 另外，如果若where条件里没有版本控制字段或者没有值，说明不使用乐观锁功能，则跳过。
     * @param sqlCommandType
     * @param entity
     * @param sql
     * @param columnModelList
     * @return
     * @throws JSQLParserException
     * @throws IllegalAccessException
     */
    protected String structureOptimisticLock(SqlCommandType sqlCommandType, Object entity, String sql, List<ColumnModel> columnModelList) throws JSQLParserException, IllegalAccessException {

        if(SqlCommandType.UPDATE != sqlCommandType){
            return sql;
        }
        //检查实体类是否存在带有@Version注解的属性
        ColumnModel columnModel = getColumnModel(entity.getClass(), Version.class, null, columnModelList);
        if(columnModel == null){
            return sql;
        }
        //获取列名
        String columnName = columnModel.getColumnName();
        Update update = (Update) CCJSqlParserUtil.parse(sql);
        Expression expressionWhere = update.getWhere();
        if(expressionWhere == null){
            return sql;
        }
        Expression singleExpression = getSingleExpression(columnName, expressionWhere);
        if(singleExpression == null){
            return sql;
        }
        //当设置的版本字段条件不是等于条件时，也应该跳过
        if(!(singleExpression instanceof EqualsTo)){
            return sql;
        }
        //当SET语句存在版本字段时，则跳过
        List<net.sf.jsqlparser.schema.Column> columnList = update.getColumns();
        for(net.sf.jsqlparser.schema.Column column: columnList){
            if(columnName.equals(column.getColumnName())){
                return sql;
            }
        }

        String fieldName = columnModel.getField().getName();
        Object value = ReflectionUtil.getProperty(entity, fieldName);
        //where条件里版本控制字段没有值，则跳过
        if(value == null){
            return sql;
        }
        List<Expression> expressionList = update.getExpressions();
        expressionList.add(new LongValue((int) value + 1));
        update.setExpressions(expressionList);
        columnList.add(new net.sf.jsqlparser.schema.Column(columnName));
        update.setColumns(columnList);

        return update.toString();
    }

    /**
     * 返回指定列名的Expression
     * @param columnName    列名
     * @param expression    EqualsTo
     * @return
     */
    private Expression getSingleExpression(String columnName, Expression expression){
        if(expression instanceof ComparisonOperator){
            ComparisonOperator comparisonOperator = (ComparisonOperator)expression;
            if(comparisonOperator.getLeftExpression().toString().equalsIgnoreCase(columnName)){
                return comparisonOperator;
            }

        }else if(expression instanceof IsNullExpression){
            IsNullExpression isNullExpression = (IsNullExpression)expression;
            if(isNullExpression.getLeftExpression().toString().equalsIgnoreCase(columnName)){
                return isNullExpression;
            }
        }else if(expression instanceof InExpression){
            InExpression inExpression = (InExpression)expression;
            if(inExpression.getLeftExpression().toString().equalsIgnoreCase(columnName)){
                return inExpression;
            }
        }else if(expression instanceof LikeExpression){
            LikeExpression likeExpression = (LikeExpression)expression;
            if(likeExpression.getLeftExpression().toString().equalsIgnoreCase(columnName)){
                return likeExpression;
            }
        }
        else if(expression instanceof Parenthesis){
            Parenthesis parenthesis = (Parenthesis)expression;
            return getSingleExpression(columnName, parenthesis.getExpression());
        }
        else{
            AndExpression andExpression = (AndExpression)expression;
            Expression leftExpression = andExpression.getLeftExpression();
            Expression leftSingleExpression = getSingleExpression(columnName, leftExpression);
            if(leftSingleExpression != null){
                return leftSingleExpression;
            }
            Expression rightExpression = andExpression.getRightExpression();
            Expression rightSingleExpression = getSingleExpression(columnName, rightExpression);
            if(rightSingleExpression != null){
                return rightSingleExpression;
            }
        }

        return null;
    }

    /**
     * key，属性名。value，数据库字段名和属性值的map
     * @param columnModelList
     * @param object
     * @param tableName
     * @return
     * @throws IllegalAccessException
     */
    protected Map<String, Map<String, Object>> getFiledColumnMap(List<ColumnModel> columnModelList, Object object, String tableName, String tableAliasName) throws IllegalAccessException {
        Map<String, Map<String, Object>> filedColumnMap = new HashMap<>();
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();
            //属性名
            String name = field.getName();
            if(!StringUtils.isEmpty(tableName)){
                name = tableName + "." + name;
            }
            field.setAccessible(true);
            //属性值
            Object value = field.get(object);
            if(value == null){
                continue;
            }

            String columnName = columnModel.getColumnName();
            if(!StringUtils.isEmpty(tableAliasName)){
                columnName = tableAliasName + MontageSqlConstant.POINT + columnName;
            }
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put(columnName, value);
            filedColumnMap.put(name, columnMap);
        }
        return filedColumnMap;
    }

    /**
     * 获取StatementHandler
     * @param target
     * @return
     */
    public PreparedStatementHandler getPreparedStatementHandler(Object target){
        RoutingStatementHandler routingStatementHandler = getTarget(target);
        StatementHandler statementHandler = (StatementHandler)ReflectionUtil.getProperty(routingStatementHandler, InterceptorConstant.DELEGATE);
        if(statementHandler instanceof CallableStatementHandler){
            return null;
        }
        return (PreparedStatementHandler) statementHandler;

    }

    public String DateToString(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

}
