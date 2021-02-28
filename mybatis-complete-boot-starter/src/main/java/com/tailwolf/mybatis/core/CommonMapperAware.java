package com.tailwolf.mybatis.core;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.constant.ThirdPartyConstant;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.util.*;

/**
 * 该类的作用生成了mybatis-complete所有所需的MappedStatement，包括了dsl和实体类增删改查常用的mapper
 * @author tailwolf
 * @date 2020-09-26
 */
public class CommonMapperAware implements ApplicationContextAware {

    public List<String> dslMapperList;
    public List<String> daoMapperList;
    public static final Set<String> ENTITY_TYPE_NAME_SET = new HashSet<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try{
//            Map<String, DslOptMapper> beansOfType = applicationContext.getBeansOfType(DslOptMapper.class);
            Map<String, Object> beansMap = applicationContext.getBeansWithAnnotation(Table.class);
            SqlSessionFactory sqlSessionFactory = applicationContext.getBean(ThirdPartyConstant.SQL_SESSION_FACTORY, SqlSessionFactory.class);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            Map<String, MappedStatement> crudMappedStatement = new HashMap<>();
            if(beansMap.size() > 0){
                for(Map.Entry<String, Object> entry: beansMap.entrySet()){
                    Object bean = entry.getValue();
                    ENTITY_TYPE_NAME_SET.add(bean.getClass().getTypeName());
                    MappedStatementBuild mappedStatementBuild = MappedStatementFactory.createMappedStatementBuild(bean, dslMapperList, daoMapperList, configuration);
                    Map<String, MappedStatement> mappedStatementMap = mappedStatementBuild.crateMappedStatementMap();
                    crudMappedStatement.putAll(mappedStatementMap);
                }
            }

            if(crudMappedStatement.size() > 0){
                Map<String, MappedStatement> property = (Map<String, MappedStatement>) ReflectionUtil.getProperty(configuration, "mappedStatements");
                crudMappedStatement.putAll(property);
                crudMappedStatement.putAll(crudMappedStatement);
                ReflectionUtil.setProperty(configuration, "mappedStatements", crudMappedStatement);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new ApplicationContextException(ex.getMessage());//如果出现异常，必须停止项目继续启动
        }
    }

    public List<String> getDslMapperList() {
        return dslMapperList;
    }

    public void setDslMapperList(List<String> dslMapperList) {
        this.dslMapperList = dslMapperList;
    }

    public List<String> getDaoMapperList() {
        return daoMapperList;
    }

    public void setDaoMapperList(List<String> daoMapperList) {
        this.daoMapperList = daoMapperList;
    }
}
