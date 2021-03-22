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
 * mybatis-complete自动配置类
 * @author tailwolf
 * @date 2020-09-06
 */
@Configuration
@EnableConfigurationProperties({MybatisCompleteProperties.class})
public class MybatisCompleteAutoConfiguration {
    private final MybatisCompleteProperties mybatisCompleteProperties;

    public List<String> dslMapperList;
    public List<String> daoMapperList;

    public MybatisCompleteAutoConfiguration(MybatisCompleteProperties mybatisCompleteProperties){
        this.mybatisCompleteProperties = mybatisCompleteProperties;
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
        diDataSourceAware.setPropertiesList(mybatisCompleteProperties.getPropertiesList());
        return diDataSourceAware;
    }

    @Bean
    @ConditionalOnMissingBean
    public MapperProxyProcessor mapperProxyProcessor(){
        MapperProxyProcessor mapperProxyProcessor = new MapperProxyProcessor();
        return mapperProxyProcessor;
    }

    private void setProperties(Properties properties){
        DbConfig dbConfig = mybatisCompleteProperties.getDbConfig();
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

        LogConfig logConfig = mybatisCompleteProperties.getLogConfig();
        if(logConfig != null){
            boolean completeSql = logConfig.getCompleteSql();
            properties.setProperty(InterceptorConstant.COMPLETE_SQL, String.valueOf(completeSql));
        }
    }
}
