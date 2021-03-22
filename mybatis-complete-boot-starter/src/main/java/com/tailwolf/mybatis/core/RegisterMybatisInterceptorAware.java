package com.tailwolf.mybatis.core;

import com.tailwolf.mybatis.core.dynamic.interceptor.DynamicStatementInterceptor;
import com.tailwolf.mybatis.core.dsl.interceptor.DslSqlInterceptor;
import com.tailwolf.mybatis.datasourse.SwitchDataSourseInterceptor;
import com.tailwolf.mybatis.fill.FillInterceptor;
import com.tailwolf.mybatis.paging.PagingInterceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.tailwolf.mybatis.constant.ThirdPartyConstant;

import java.util.Properties;

/**
 * 注册MyBatis拦截器
 * @author tailwolf
 * @date 2020-03-24
 */
public class RegisterMybatisInterceptorAware implements ApplicationContextAware {

    private Properties properties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SqlSessionFactory sqlSessionFactory = applicationContext.getBean(ThirdPartyConstant.SQL_SESSION_FACTORY, SqlSessionFactory.class);
        Configuration configuration = sqlSessionFactory.getConfiguration();

        /**
         * 添加拦截器，同一拦截层次，后添加，先执行
         */
        //分页拦截器
        PagingInterceptor pagingInterceptor = new PagingInterceptor();
        pagingInterceptor.setProperties(properties);
        configuration.addInterceptor(pagingInterceptor);
        //创建cglib代理Executor拦截器
        CgLibExecutorInterceptor cgLibExecutorInterceptor = new CgLibExecutorInterceptor();
        cgLibExecutorInterceptor.setProperties(properties);
        configuration.addInterceptor(cgLibExecutorInterceptor);
        //替换MappedStatement的id拦截器
//        EntitySqlInterceptor entitySqlInterceptor = new EntitySqlInterceptor();
//        entitySqlInterceptor.setProperties(properties);
//        configuration.addInterceptor(entitySqlInterceptor);
        //数据源切换拦截器
        SwitchDataSourseInterceptor switchDataSourseInterceptor = new SwitchDataSourseInterceptor();
        switchDataSourseInterceptor.setProperties(properties);
        configuration.addInterceptor(switchDataSourseInterceptor);
        //拼接sql拦截器
        DslSqlInterceptor dslSqlInterceptor = new DslSqlInterceptor();
        dslSqlInterceptor.setProperties(properties);
        configuration.addInterceptor(dslSqlInterceptor);

        //填充字段拦截器
        FillInterceptor fillInterceptor = new FillInterceptor();
        fillInterceptor.setProperties(properties);
        configuration.addInterceptor(fillInterceptor);
        //实体类增删改查拦截器
        DynamicStatementInterceptor entityStatementInterceptor = new DynamicStatementInterceptor();
        entityStatementInterceptor.setProperties(properties);
        configuration.addInterceptor(entityStatementInterceptor);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
