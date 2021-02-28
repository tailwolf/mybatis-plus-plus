package com.tailwolf.mybatis.core;

import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.tailwolf.mybatis.core.proxy.MybatisCompleteMapperProxy;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 代理mapper对象
 * @author tailwolf
 * @date 2020-11-15
 */
public class MapperProxyProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Proxy){
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(bean);
            if(invocationHandler instanceof MapperProxy){
                MapperProxy mapperProxy = (MapperProxy)invocationHandler;
                SqlSession sqlSession = (SqlSession)ReflectionUtil.getProperty(mapperProxy, "sqlSession");
                Class mapperInterface = (Class)ReflectionUtil.getProperty(mapperProxy, "mapperInterface");
                Map methodCache = (Map)ReflectionUtil.getProperty(mapperProxy, "methodCache");
                MybatisCompleteMapperProxy mybatisCompleteMapperProxy = new MybatisCompleteMapperProxy(sqlSession, mapperInterface, methodCache);
                return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mybatisCompleteMapperProxy);
            }

        }
        return bean;
    }
}
