package com.tailwolf.mybatis.core.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * bean转换工具类
 * @author tailwolf
 * @date 2020-09-17
 */
public class BeanConvUtil {

    /**
     * 使bean转化成Map
     * @param obj
     *        bean对象
     * @return
     * @throws IllegalAccessException
     */
    public static <K, V> Map<K, V> beanToMap(Object obj) throws IllegalAccessException {
        Map<K, V> map = new HashMap<>();
        Class<?> aClass = obj.getClass();
        return beanToMap(obj,aClass, map);
    }

    private static <K, V> Map<K, V> beanToMap(Object obj, Class<?> aClass, Map<K, V> map) throws IllegalAccessException {
        if(aClass == null){
            return map;
        }

        Field[] declaredFields = aClass.getDeclaredFields();
        for(Field field: declaredFields){
            field.setAccessible(true);
            map.put((K)field.getName(), (V)field.get(obj));
        }

        return beanToMap(obj,aClass.getSuperclass(), map);
    }
}
