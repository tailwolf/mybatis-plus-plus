package com.tailwolf.mybatis.core.util;

import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SerializedLambdaUtil工具类
 * @author tailwolf
 * @date 2020-02-09
 */
public class SerializedLambdaUtil {
    /**
     * 获取lambda表达式的序列化形式
     * @param fn
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        return (SerializedLambda) method.invoke(fn);
    }

    /**
     * 获取SerializedLambda对应的Class
     * @param serializedLambda
     * @return
     */
    public static Class getImplClass(SerializedLambda serializedLambda){
        String implClassStr = serializedLambda.getImplClass();
        implClassStr = implClassStr.replace('/', '.');
        try {
            Class<?> entityClass = Class.forName(implClassStr);
            return entityClass;
        } catch (ClassNotFoundException var2) {
            throw new MybatisCompleteRuntimeException("找不到该实体类的Class!");
        }
    }
}
