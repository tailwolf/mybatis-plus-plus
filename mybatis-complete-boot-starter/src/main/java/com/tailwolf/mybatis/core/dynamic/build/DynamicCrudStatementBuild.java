package com.tailwolf.mybatis.core.dynamic.build;

import com.tailwolf.mybatis.core.MappedStatementBuild;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 该类是用来创建实体类增删改查的MappedStatement
 * @author tailwolf
 * @date 2020-09-22
 */
public class DynamicCrudStatementBuild extends MappedStatementBuild {

    public static Set<String> DYNAMIC_TABLE_CRUD_ID_SET = new HashSet<>();

    private String mapper;

    public DynamicCrudStatementBuild(String mapper, Class entityClazz, Configuration configuration){
        super(entityClazz, configuration);
        this.mapper = mapper;
    }

    @Override
    public Map<String, MappedStatement> crateMappedStatementMap() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        //插入实体类
        String id = mapper + "." + "insert";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        XNode Node = insert(id);
        MappedStatement insertEntityMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, insertEntityMappedStatement);
//        //批量插入实体类
//        id = typeName + "." + "insertBatch";
//        ENTITY_CRUD_ID_SET.add(id);
//        Node = insertBatch(id, configuration);
//        MappedStatement insertBatchMappedStatement = this.getMappedStatement(id, Node);
//        mappedStatementMap.put(id, insertBatchMappedStatement);
        //通过主键更新实体类
        id = mapper + "." + "updateByPk";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = updateByPk(id);
        MappedStatement updateByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, updateByPkMappedStatement);
        //通过主键批量删除
        id = mapper + "." + "deleteBatchByPk";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = deleteBatchByPk(id);
        MappedStatement deleteBatchByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteBatchByPkMappedStatement);
        //通过主键删除
        id = mapper + "." + "deleteByPk";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = deleteByPk(id);
        MappedStatement deleteByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteByPkMappedStatement);
        //通过实体类删除
        id = mapper + "." + "delete";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = delete(id);
        MappedStatement deleteMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteMappedStatement);
        //通过实体类查询列表
        id = mapper + "." + "findList";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = findList(id);
        MappedStatement getListMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, getListMappedStatement);
        //通过主键查询实体类
        id = mapper + "." + "findByPk";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = findByPk(id);
        MappedStatement findByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, findByPkMappedStatement);
        //通过实体类查询一个实体类
        id = mapper + "." + "findOne";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = findList(id);
        MappedStatement findOneMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, findOneMappedStatement);
        //通过主键批量更新
        id = mapper + "." + "updateBatchByPk";
        DYNAMIC_TABLE_CRUD_ID_SET.add(id);
        Node = updateBatchByPk(id);
        MappedStatement updateBatchByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, updateBatchByPkMappedStatement);

        return mappedStatementMap;
    }
}
