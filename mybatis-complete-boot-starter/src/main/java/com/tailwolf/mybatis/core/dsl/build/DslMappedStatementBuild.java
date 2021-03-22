package com.tailwolf.mybatis.core.dsl.build;

import com.tailwolf.mybatis.core.MappedStatementBuild;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 该类是用来创建dsl接口的MappedStatement
 * @author tailwolf
 * @date 2020-09-22
 */
public class DslMappedStatementBuild extends MappedStatementBuild {

    public static Set<String> DSL_CRUD_ID_SET = new HashSet<>();

    private String mapper;

    public DslMappedStatementBuild(String mapper, Class entityClazz, Configuration configuration){
        super(entityClazz, configuration);
        this.mapper = mapper;
    }

    @Override
    public Map<String, MappedStatement> crateMappedStatementMap() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, IOException {
        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        //双表dsl查询
        String id = "com.tailwolf.mybatis.core.common.dao.DslOptMapper.joinQuery";
        DSL_CRUD_ID_SET.add(id);
        XNode Node = dslQuery(id);
        MappedStatement insertEntityMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, insertEntityMappedStatement);

        //单表dsl查询
        id = mapper + "." + "dslQuery";
        DSL_CRUD_ID_SET.add(id);
        Node = dslQuery(id);
        MappedStatement dslQueryMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, dslQueryMappedStatement);
        //单表dsl查询返回一个对象
        id = mapper + "." + "dslQueryOne";
        DSL_CRUD_ID_SET.add(id);
        Node = dslQuery(id);
        MappedStatement dslQueryOneMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, dslQueryOneMappedStatement);
        //单表dsl删除
        id = mapper + "." + "dslDelete";
        DSL_CRUD_ID_SET.add(id);
        Node = dslQuery(id);
        MappedStatement dslDeleteMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, dslDeleteMappedStatement);
        //单表dsl更新
        id = mapper + "." + "dslUpdate";
        DSL_CRUD_ID_SET.add(id);
        Node = dslQuery(id);
        MappedStatement dslUpdateMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, dslUpdateMappedStatement);
        return mappedStatementMap;
    }
}
