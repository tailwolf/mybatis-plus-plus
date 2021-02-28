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

    private List<String> dslMapperList;

    public DslMappedStatementBuild(Class entityClazz, List<String> dslMapperList, Configuration configuration){
        super(entityClazz, configuration);
        this.dslMapperList = dslMapperList;
    }

    @Override
    public Map<String, MappedStatement> crateMappedStatementMap() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, IOException {
        Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
        XNode xNode;
        for(String dslMapper: dslMapperList){
            if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslQuery".equals(dslMapper)){
                xNode = this.dslQuery(dslMapper, configuration);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslQueryOne".equals(dslMapper)){
                xNode = this.dslQuery(dslMapper, configuration);
            }else if("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslDelete".equals(dslMapper)){
                xNode = this.dslDelete(dslMapper, configuration);
            }else if("com.tailwolf.mybatis.core.common.dao.DslOptMapper.joinQuery".equals(dslMapper)){
                xNode = this.dslQuery(dslMapper, configuration);
            } else{
                xNode = this.dslUpdate(dslMapper, configuration);
            }
            MappedStatement mappedStatement = this.getMappedStatement(dslMapper, xNode);
            mappedStatementMap.put(dslMapper, mappedStatement);
        }
        return mappedStatementMap;
    }
}
