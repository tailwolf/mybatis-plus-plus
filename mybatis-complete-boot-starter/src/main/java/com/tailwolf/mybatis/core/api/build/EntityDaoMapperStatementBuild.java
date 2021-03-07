package com.tailwolf.mybatis.core.api.build;

import com.tailwolf.mybatis.core.MappedStatementBuild;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类是用来创建实体类增删改查接口的MappedStatement
 * @author tailwolf
 * @date 2020-09-22
 */
public class EntityDaoMapperStatementBuild extends MappedStatementBuild {
    private List<String> daoMapperList;

    public EntityDaoMapperStatementBuild(Class entityClazz, List<String> daoMapperList, Configuration configuration){
        super(entityClazz, configuration);
        this.daoMapperList = daoMapperList;
    }

    @Override
    public Map<String, MappedStatement> crateMappedStatementMap() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        XNode xNode;
        for(String daoMapper: daoMapperList){
            if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.insert".equals(daoMapper)){
                xNode = insert(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.updateByPk".equals(daoMapper)){
                xNode = updateByPk(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.deleteBatchByPk".equals(daoMapper)){
                xNode = deleteBatchByPk(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.deleteByPk".equals(daoMapper)){
                xNode = deleteByPk(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.delete".equals(daoMapper)){
                xNode = delete(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findList".equals(daoMapper)){
                xNode = findList(daoMapper);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findOne".equals(daoMapper)){
                xNode = findList(daoMapper);
            }
            else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findByPk".equals(daoMapper)){
                xNode = findByPk(daoMapper);
            }
            else{
                xNode = updateBatchByPk(daoMapper);
            }
            MappedStatement mappedStatement = this.getMappedStatement(daoMapper, xNode);
            mappedStatementMap.put(daoMapper, mappedStatement);
        }
        return mappedStatementMap;
    }
}
