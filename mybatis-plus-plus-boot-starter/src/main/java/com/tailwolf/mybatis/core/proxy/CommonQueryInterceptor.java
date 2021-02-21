package com.tailwolf.mybatis.core.proxy;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import com.tailwolf.mybatis.core.annotation.Table;
import com.tailwolf.mybatis.core.util.FiledNameThreadLocalUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.lang.reflect.Method;

/**
 * cglib代理拦截器
 * @author tailwolf
 */
public class CommonQueryInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Class<?> clazz = (Class<?>) ReflectionUtil.getProperty(method, "clazz");
        String tableName = clazz.getAnnotation(Table.class).tableName();
        Signature sig1 = (Signature) ReflectionUtil.getProperty(methodProxy, "sig1");
        String filedName = sig1.getName().substring(3);
        FiledNameThreadLocalUtil.setValue(tableName + "." + filedName.substring(0,1).toLowerCase().concat(filedName.substring(1)));

        return methodProxy.invokeSuper(object, args);
    }
}
