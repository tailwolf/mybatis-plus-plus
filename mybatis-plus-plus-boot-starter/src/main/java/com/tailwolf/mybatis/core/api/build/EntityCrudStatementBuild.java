package com.tailwolf.mybatis.core.api.build;

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
public class EntityCrudStatementBuild extends MappedStatementBuild {

    public static Set<String> ENTITY_CRUD_ID_SET = new HashSet<>();

    public EntityCrudStatementBuild(Class entityClazz, Configuration configuration){
        super(entityClazz, configuration);
    }

    @Override
    public Map<String, MappedStatement> crateMappedStatementMap() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String typeName = entityClazz.getTypeName();
        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        //插入实体类
        String id = typeName + "." + "insert";
        ENTITY_CRUD_ID_SET.add(id);
        XNode Node = insert(id, configuration);
        MappedStatement insertEntityMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, insertEntityMappedStatement);
//        //批量插入实体类
//        id = typeName + "." + "insertBatch";
//        ENTITY_CRUD_ID_SET.add(id);
//        Node = insertBatch(id, configuration);
//        MappedStatement insertBatchMappedStatement = this.getMappedStatement(id, Node);
//        mappedStatementMap.put(id, insertBatchMappedStatement);
        //通过主键更新实体类
        id = typeName + "." + "updateByPk";
        ENTITY_CRUD_ID_SET.add(id);
        Node = updateByPk(id, configuration);
        MappedStatement updateByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, updateByPkMappedStatement);
        //通过主键批量删除
        id = typeName + "." + "deleteBatchByPk";
        ENTITY_CRUD_ID_SET.add(id);
        Node = deleteBatchByPk(id, configuration);
        MappedStatement deleteBatchByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteBatchByPkMappedStatement);
        //通过主键删除
        id = typeName + "." + "deleteByPk";
        ENTITY_CRUD_ID_SET.add(id);
        Node = deleteByPk(id, configuration);
        MappedStatement deleteByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteByPkMappedStatement);
        //通过实体类删除
        id = typeName + "." + "delete";
        ENTITY_CRUD_ID_SET.add(id);
        Node = deleteByPk(id, configuration);
        MappedStatement deleteMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, deleteMappedStatement);
        //通过实体类查询列表
        id = typeName + "." + "findList";
        ENTITY_CRUD_ID_SET.add(id);
        Node = findList(id, configuration);
        MappedStatement getListMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, getListMappedStatement);
        //通过主键查询实体类
        id = typeName + "." + "findByPk";
        ENTITY_CRUD_ID_SET.add(id);
        Node = findByPk(id, configuration);
        MappedStatement findByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, findByPkMappedStatement);
        //通过实体类查询一个实体类
        id = typeName + "." + "findOne";
        ENTITY_CRUD_ID_SET.add(id);
        Node = findList(id, configuration);
        MappedStatement findOneMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, findOneMappedStatement);
        //通过主键批量更新
        id = typeName + "." + "updateBatchByPk";
        ENTITY_CRUD_ID_SET.add(id);
        Node = updateBatchByPk(id, configuration);
        MappedStatement updateBatchByPkMappedStatement = this.getMappedStatement(id, Node);
        mappedStatementMap.put(id, updateBatchByPkMappedStatement);
        return mappedStatementMap;
    }
}
