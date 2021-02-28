package com.tailwolf.mybatis.core;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.util.CollectionUtil;
import com.tailwolf.mybatis.core.util.ColumnModelUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 创建MappedStatement的类
 * @author tailwolf
 * @date 2020-09-22
 */
public abstract class MappedStatementBuild {
    protected Class entityClazz;
    protected Configuration configuration;

    public MappedStatementBuild(Class entityClazz, Configuration configuration){
        this.entityClazz = entityClazz;
        this.configuration = configuration;
    }

    protected static String doc = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"
            + "<mapper >$sqlTag$</mapper>";

    public abstract Map<String, MappedStatement> crateMappedStatementMap() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, IOException, ClassNotFoundException;

    /**
     * 生成通过主键查询实体类的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected XNode findByPk(String method, Configuration configuration) throws IOException, ClassNotFoundException {
        List<ColumnModel> columnModelList = this.getColumnModelList();
        if(CollectionUtil.isEmtpy(columnModelList)){
            throw new MybatisCompleteRuntimeException("实体类必须要有属性！");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("<select id=\"").append(method).append("\" parameterType=\"").append(this.entityClazz.getName()).append("\"").append(" resultType=\"").append(this.entityClazz.getName()).append("\">");
        buffer.append("select * from ").append(this.getTableName());
        buffer.append(" where ");
        for(ColumnModel columnModel: columnModelList){
            boolean pk = columnModel.isPk();
            if (pk) {
                String columnName = columnModel.getColumnName();
                Field field = columnModel.getField();
                String name = field.getName();
                buffer.append(columnName).append(" = #{").append(name).append("}");
            }
        }
        buffer.append("</select>");
        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("select");
    }

    /**
     * 生成查询实体类列表的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode findList(String method, Configuration configuration) throws IOException {
        List<ColumnModel> columnModelList = this.getColumnModelList();
        StringBuffer buffer = new StringBuffer();
        buffer.append("<select id=\"").append(method).append("\" resultType=\"").append(this.entityClazz.getName()).append("\">");
        buffer.append("select * from ").append(this.getTableName());
        buffer.append("<where>");
        for (ColumnModel columnModel : columnModelList) {
            Field field = columnModel.getField();
            String name = field.getName();
            Object columnName = columnModel.getColumnName();
            buffer.append("<if test=\"").append(name).append(" != null\">");
            buffer.append("and ").append(columnName).append(" = #{").append(name).append("}");
            buffer.append("</if>");
        }
        buffer.append("</where>");
        buffer.append("</select>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("select");
    }

    /**
     * 生成删除dsl操作的XNode，该节点并不是最终形态，会在dsl拦截器里拼接真正的sql
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode dslDelete(String method, Configuration configuration) throws IOException {
        StringBuffer sqlTagBuffer = new StringBuffer();
        sqlTagBuffer.append("<delete id=\"").append(method).append("\"").append(">");
        sqlTagBuffer.append("delete * from ").append(this.getTableName());
        sqlTagBuffer.append("</delete>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", sqlTagBuffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("delete");
    }

    /**
     * 生成更新dsl操作的XNode，该节点并不是最终形态，会在dsl拦截器里拼接真正的sql
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode dslUpdate(String method, Configuration configuration) throws IOException {
        StringBuffer sqlTagBuffer = new StringBuffer();
        sqlTagBuffer.append("<update id=\"").append(method).append("\"").append(">");
        sqlTagBuffer.append("update ").append(this.getTableName());
        sqlTagBuffer.append("</update>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", sqlTagBuffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("update");
    }

    /**
     * 生成查询dsl操作的XNode，该节点并不是最终形态，会在dsl拦截器里拼接真正的sql
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode dslQuery(String method, Configuration configuration) throws IOException {
        StringBuffer sqlTagBuffer = new StringBuffer();
        sqlTagBuffer.append("<select id=\"").append(method).append("\"").append(">");
        sqlTagBuffer.append("select * from").append(this.getTableName());
        sqlTagBuffer.append("</select>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", sqlTagBuffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("select");
    }

    /**
     * 生成通过主键批量删除的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode deleteBatchByPk(String method, Configuration configuration) throws IOException {
        List<ColumnModel> columnModelList = this.getColumnModelList();
        String pkName = "";
        String pkColumn = "";
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();//实体类属性
            String name = field.getName();//实体类属性名
            boolean pk = columnModel.isPk();
            String columnName = columnModel.getColumnName();

            if(pk){//是否主键
                pkName = name;
                pkColumn = columnName;
                break;
            }
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("<delete id=\"").append(method).append("\">");
        buffer.append("DELETE FROM ").append(this.getTableName());
        buffer.append(" where ").append(pkColumn).append(" in");
        buffer.append("<foreach collection =\"list\" item=\"item\" index= \"index\" open=\"(\" separator =\",\" close=\")\">");
        buffer.append("#{item.").append(pkName).append("}");
        buffer.append("</foreach>");
        buffer.append("</delete>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, this.configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("delete");
    }

    /**
     * 生成删除的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode delete(String method, Configuration configuration) throws IOException {

        StringBuffer buffer = new StringBuffer();
        buffer.append("<delete id=\"").append(method).append(this.entityClazz.getName()).append("\">");
        buffer.append("DELETE FROM ").append(this.getTableName());
        buffer.append("<where>");
        List<ColumnModel> columnModelList = this.getColumnModelList();
        for (ColumnModel columnModel : columnModelList) {
            Field field = columnModel.getField();
            String name = field.getName();
            String columnName = columnModel.getColumnName();
            buffer.append("<if test=\"").append(name).append(" != null\">");
            buffer.append("and ").append(columnName).append(" = #{").append(name).append("}");
            buffer.append("</if>");
        }
        buffer.append("</where>");
        buffer.append("</delete>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("delete");
    }

    /**
     * 生成通过主键删除的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode deleteByPk(String method, Configuration configuration) throws IOException {
        String pkName = "";
        String pkColumn = "";
        List<ColumnModel> columnModelList = this.getColumnModelList();
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();
            String name = field.getName();
            String columnName = columnModel.getColumnName();

            boolean pk = columnModel.isPk();
            if(pk){//是否主键
                pkName = name;
                pkColumn = columnName;
                break;
            }
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("<delete id=\"").append(method).append("\">");
        buffer.append("DELETE FROM ").append(this.getTableName());
        buffer.append("<where> ");
        buffer.append(pkColumn).append(" = ").append("#{").append(pkName).append("}");
        buffer.append("</where>");
        buffer.append("</delete>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, this.configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("delete");
    }

    /**
     * 生成通过主键更新的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode updateByPk(String method, Configuration configuration) throws IOException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<update id=\"").append(method).append("\">");

        buffer.append("update ").append(this.getTableName());
        buffer.append("<set>");
        String pkName = "";
        String pkColumn = "";
        List<ColumnModel> columnModelList = this.getColumnModelList();
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();//实体类字段
            String name = field.getName();//实体类字段名
            String columnName = columnModel.getColumnName();

            boolean pk = columnModel.isPk();
            if(pk){//是否主键
                pkName = name;
                pkColumn = columnName;
                continue;
            }
            buffer.append("<if test=\"").append(name).append(" != null\">");
            buffer.append(columnName).append(" = #{").append(name).append("},");
            buffer.append("</if>");
        }
        buffer.append("</set>");
        buffer.append("where ").append(pkColumn).append(" = #{").append(pkName).append("}");
        buffer.append("</update>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, this.configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("update");
    }

    /**
     * 生成通过主键批量更新的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode updateBatchByPk(String method, Configuration configuration) throws IOException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<update").append(" ").append("id = ").append("\"").append(method).append("\"").append(">");
        buffer.append("update").append(" ").append(this.getTableName());
        buffer.append("<trim prefix = \"set\" suffixOverrides = \",\">");

        String pkName = "";
        String pkColumn = "";
        List<ColumnModel> columnModelList = this.getColumnModelList();
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();
            String name = field.getName();
            String columnName = columnModel.getColumnName();

            boolean pk = columnModel.isPk();
            if(pk){//是否主键
                pkName = name;
                pkColumn = columnName;
                continue;
            }

            buffer.append("<trim prefix = \"").append(columnName).append(" = case\" suffix = \"end,\">");
            buffer.append("<foreach collection = \"list\" item = \"item\" index = \"index\">");
            buffer.append("<if test = \"item.").append(name).append("!= null\">");
            buffer.append("when ").append(pkColumn).append(" = #{item.").append(pkName).append("}").append(" then #{item.").append(name).append("}");
            buffer.append("</if>").append("</foreach>").append("</trim>");
        }
        buffer.append("</trim>");
        buffer.append("where id in (");
        buffer.append("<foreach collection = \"list\" separator=\",\" item = \"item\" index = \"index\">");
        buffer.append("#{item.").append(pkName).append("}");
        buffer.append("</foreach>").append(")</update>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes(), "");
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, this.configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("update");
    }

    /**
     * 生成插入的XNode
     * @param method
     * @param configuration
     * @return
     * @throws IOException
     */
    protected XNode insert(String method, Configuration configuration) throws IOException {
        List<ColumnModel> columnModelList = this.getColumnModelList();
        StringBuilder columnsBuffer = new StringBuilder();
        StringBuilder fieldsBuffer = new StringBuilder();
        String pkName = "";
        for(int i = 0; i < columnModelList.size(); i++){
            ColumnModel columnModel = columnModelList.get(i);
            Field field = columnModel.getField();
            String name = field.getName();
            String columnName = columnModel.getColumnName();

            boolean pk = columnModel.isPk();
            if(pk){//是否主键
                pkName = name;
                continue;
            }
            columnsBuffer.append("<if test=\"").append(name).append(" != null \">").append(columnName).append(",").append("</if>");
            fieldsBuffer.append("<if test=\"").append(name).append(" != null \">").append("#{").append(name).append("},").append("</if>");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("<insert id=\"").append(method).append("\" useGeneratedKeys=\"true\" ");
        if(!StringUtils.isEmpty(pkName)){
            buffer.append("keyProperty=\"").append(pkName).append("\"");
        }
        buffer.append(">");
        buffer.append("insert into ").append(this.getTableName()).append(" (");
        buffer.append("<trim suffixOverrides=\",\">").append(columnsBuffer.toString()).append("</trim>").append(") values");
        buffer.append("(").append("<trim suffixOverrides=\",\">").append(fieldsBuffer.toString()).append("</trim>").append(")");
        buffer.append("</insert>");

        Resource resource = new ByteArrayResource(doc.replace("$sqlTag$", buffer.toString()).getBytes());
//        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(),
//                configuration, resource.toString(), configuration.getSqlFragments());
//        xmlMapperBuilder.parse();
        XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, this.configuration.getVariables(), new XMLMapperEntityResolver());
        return xPathParser.evalNode("/mapper").evalNode("insert");
    }

    /**
     * 根据传进来的method(id)和XNode生成MappedStatement对象
     * @param method
     * @param xNode
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    protected MappedStatement getMappedStatement(String method, XNode xNode) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        XMLLanguageDriver lang = new XMLLanguageDriver();
        Log statementLog = LogFactory.getLog(entityClazz.getName());

        ParameterMap defaultParameterMap = (new ParameterMap.Builder(configuration, "defaultParameterMap", (Class) null, new ArrayList())).build();

        Constructor constructor = MappedStatement.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        ResultMap inlineResultMap = (new org.apache.ibatis.mapping.ResultMap.Builder(configuration, method + "-Inline", entityClazz, new ArrayList(), (Boolean)null)).build();
        List<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(inlineResultMap);

        MappedStatement mappedStatement = (MappedStatement)constructor.newInstance();
        String nodeName = xNode.getNode().getNodeName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        String keyStatementId = method + "!selectKey";
        Object keyGenerator;
        if (configuration.hasKeyGenerator(keyStatementId)) {
            keyGenerator = configuration.getKeyGenerator(keyStatementId);
        } else {
            keyGenerator = xNode.getBooleanAttribute("useGeneratedKeys", configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType)) ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
        }
        ReflectionUtil.setProperty(mappedStatement, "id", method);//dao层接口
        ReflectionUtil.setProperty(mappedStatement, "resultMaps", resultMaps);//结果集
        ReflectionUtil.setProperty(mappedStatement, "keyGenerator", keyGenerator);//主键返回器
        ReflectionUtil.setProperty(mappedStatement, "sqlCommandType", sqlCommandType);//SQL语句类型


        SqlSource sqlSource = lang.createSqlSource(configuration, xNode, null);
        String keyProperty = xNode.getStringAttribute("keyProperty");
        Method delimitedStringMethod = MappedStatement.class.getDeclaredMethod("delimitedStringToArray", String.class);
        delimitedStringMethod.setAccessible(true);
        Object keyProperties = delimitedStringMethod.invoke(mappedStatement, keyProperty);
        //为mappedStatement属性赋值
        ReflectionUtil.setProperty(mappedStatement, "configuration", configuration);
        ReflectionUtil.setProperty(mappedStatement, "statementType", StatementType.PREPARED);//默认预编译
        ReflectionUtil.setProperty(mappedStatement, "sqlSource", sqlSource);//sql具体的语句
        ReflectionUtil.setProperty(mappedStatement, "lang", lang);
        ReflectionUtil.setProperty(mappedStatement, "parameterMap",  defaultParameterMap);//参数Map
        ReflectionUtil.setProperty(mappedStatement, "statementLog", statementLog);//日志
        ReflectionUtil.setProperty(mappedStatement, "keyProperties", keyProperties);//主键返回的对象
        return mappedStatement;
    }

    /**
     * 获取ColumnModel集合
     * @return
     */
    protected List<ColumnModel> getColumnModelList(){
        List<Field> allFields = ReflectionUtil.getAllFields(entityClazz);
        return ColumnModelUtil.createColumnModel(allFields, new ArrayList<>());
    }

    /**
     * 获取表名
     * @return
     */
    protected String getTableName(){
        Table tableAnnot = (Table)entityClazz.getAnnotation(Table.class);
        return tableAnnot.tableName();
    }
}
