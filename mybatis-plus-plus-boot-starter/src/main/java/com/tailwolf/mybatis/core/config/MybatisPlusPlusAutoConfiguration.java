package com.tailwolf.mybatis.core.config;

import com.tailwolf.mybatis.config.DbConfig;
import com.tailwolf.mybatis.config.LogConfig;
import com.tailwolf.mybatis.core.MapperProxyProcessor;
import com.tailwolf.mybatis.core.RegisterMybatisInterceptorAware;
import com.tailwolf.mybatis.core.CommonMapperAware;
import com.tailwolf.mybatis.datasourse.DiDataSourceAware;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tailwolf.mybatis.constant.InterceptorConstant;
import com.tailwolf.mybatis.core.dsl.JoinOptService;

import java.util.*;


/**
 * mybatis++自动配置类
 * @author tailwolf
 * @date 2020-09-06
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusPlusProperties.class})
public class MybatisPlusPlusAutoConfiguration {
    private final MybatisPlusPlusProperties mybatisPlusPlusProperties;

    public List<String> dslMapperList;
    public List<String> daoMapperList;

    public MybatisPlusPlusAutoConfiguration(MybatisPlusPlusProperties mybatisPlusPlusProperties){
        this.mybatisPlusPlusProperties = mybatisPlusPlusProperties;

        this.dslMapperList = MapperListProperties.dslMapperList;
        this.daoMapperList = MapperListProperties.daoMapperList;
    }

    /**
     * 注入双表查询服务
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JoinOptService dslOptService(){
        return new JoinOptService();
    }

    /**
     * 注入生成增删改查的MappedStatement类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public CommonMapperAware crudMapperAware(){
        CommonMapperAware crudMapperAware = new CommonMapperAware();
        crudMapperAware.setDaoMapperList(daoMapperList);
        crudMapperAware.setDslMapperList(dslMapperList);
        return crudMapperAware;
    }

    /**
     * 添加MyBatis拦截器注册类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RegisterMybatisInterceptorAware registerMybatisInterceptorAware(){
        RegisterMybatisInterceptorAware registerMybatisInterceptorAware = new RegisterMybatisInterceptorAware();
        Properties properties = new Properties();
        this.setProperties(properties);
        registerMybatisInterceptorAware.setProperties(properties);
        return registerMybatisInterceptorAware;
    }

    @Bean
    @ConditionalOnMissingBean
    public DiDataSourceAware diDataSourceAware(){
        DiDataSourceAware diDataSourceAware = new DiDataSourceAware();
        diDataSourceAware.setPropertiesList(mybatisPlusPlusProperties.getPropertiesList());
        return diDataSourceAware;
    }

    @Bean
    @ConditionalOnMissingBean
    public MapperProxyProcessor mapperProxyProcessor(){
        MapperProxyProcessor mapperProxyProcessor = new MapperProxyProcessor();
        return mapperProxyProcessor;
    }

    private void setProperties(Properties properties){
        properties.setProperty(InterceptorConstant.DSL_QUERY, dslMapperList.get(0));
        properties.setProperty(InterceptorConstant.DSL_QUERY_ONE, dslMapperList.get(1));
        properties.setProperty(InterceptorConstant.DSL_DELETE, dslMapperList.get(2));
        properties.setProperty(InterceptorConstant.DSL_UPDATE, dslMapperList.get(3));
        properties.setProperty(InterceptorConstant.JOIN_QUERY, dslMapperList.get(4));

        properties.setProperty(InterceptorConstant.INSERT, daoMapperList.get(0));
        properties.setProperty(InterceptorConstant.UPDATE_BY_PK, daoMapperList.get(1));
        properties.setProperty(InterceptorConstant.UPDATE_BATCH_BY_PK, daoMapperList.get(2));
        properties.setProperty(InterceptorConstant.DELETE_BATCH_BY_PK, daoMapperList.get(3));
        properties.setProperty(InterceptorConstant.DELETE_BY_PK, daoMapperList.get(4));
        properties.setProperty(InterceptorConstant.DELETE, daoMapperList.get(5));
        properties.setProperty(InterceptorConstant.FIND_LIST, daoMapperList.get(6));
        properties.setProperty(InterceptorConstant.FIND_ONE, daoMapperList.get(7));
        properties.setProperty(InterceptorConstant.FIND_BY_PK, daoMapperList.get(8));

        DbConfig dbConfig = mybatisPlusPlusProperties.getDbConfig();
        if(dbConfig != null){
            String logicDeleteField = dbConfig.getLogicDeleteField();
            if(!StringUtils.isEmpty(logicDeleteField)){
                properties.setProperty(InterceptorConstant.FIELD, logicDeleteField);
            }
            String logicDeleteValue = dbConfig.getLogicDeleteValue();
            if(!StringUtils.isEmpty(logicDeleteValue)){
                properties.setProperty(InterceptorConstant.LOGIC_DELETE_VALUE, logicDeleteValue);
            }
            String logicNotDeleteValue = dbConfig.getLogicNotDeleteValue();
            if(!StringUtils.isEmpty(logicDeleteValue)){
                properties.setProperty(InterceptorConstant.LOGIC_NOT_DELETE_VALUE, logicNotDeleteValue);
            }
        }

        LogConfig logConfig = mybatisPlusPlusProperties.getLogConfig();
        if(logConfig != null){
            boolean completeSql = logConfig.getCompleteSql();
            properties.setProperty(InterceptorConstant.COMPLETE_SQL, String.valueOf(completeSql));
        }
    }
}
