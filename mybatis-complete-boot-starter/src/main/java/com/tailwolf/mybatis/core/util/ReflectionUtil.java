package com.tailwolf.mybatis.core.util;

import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.ColumnModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * @author tailwolf
 * @date 2020-02-09
 */
public class ReflectionUtil {
    /**
     * @param obj
     *        获取的对象
     * @param name
     *        需要获取的属性名
     */
    public static Object getProperty(Object obj, String name){
        Class<?> clazz = obj.getClass();
        Object value = null;
        return getProperty(clazz, obj, value, name);
    }

    /**
     * 递归获取属性值
     * @param clazz
     * @param obj
     * @param value
     * @param name
     * @return
     */
    private static Object getProperty(Class<?> clazz, Object obj, Object value, String name){
        if(clazz == null){
            return null;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field: declaredFields){
            if(field.getName().equals(name)){
                field.setAccessible(true);
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        if(value != null){
            return value;
        }
        Class<?> superclass = clazz.getSuperclass();
        return getProperty(superclass, obj, value, name);
    }

    /**
     *
     * @param obj
     *        设置的对象
     * @param name
     *        需要设置的属性名
     * @param value
     *        值
     */
    public static void setProperty(Object obj, String name, Object value) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();//当前类所有属性
        for(Field field: fields){
            if(field.getName().equals(name)){
                field.setAccessible(true);
                field.set(obj, value);
                return;
            }
        }
        setProperty(obj, name, value, clazz.getSuperclass());
    }

    private static void setProperty(Object obj, String name, Object value, Class<?> clazz) throws IllegalAccessException {
        if(clazz == null){
            return;
        }

        Field[] superFields = clazz.getDeclaredFields();
        for(Field superField: superFields){
            if(superField.getName().equals(name)){
                superField.setAccessible(true);
                superField.setAccessible(true);
                superField.set(obj, value);
                return;
            }
        }

        setProperty(obj, name, value, clazz.getSuperclass());
    }

    /**
     * 保留拥有特定注解的Field
     * @param fields
     * @param clazz
     */
    public static Field[] retainFieldByAnnotation(Field[] fields, Class clazz){
        List<Field> fieldList = new ArrayList<>();
        for(Field field: fields){
            Annotation[] annotations = field.getAnnotations();
            for(int i = 0; i < annotations.length; i++){
                Annotation annotation = annotations[i];
                Class<? extends Annotation> annotationClazz = annotation.annotationType();
                if(clazz.getName().equals(annotationClazz.getName())){
                    fieldList.add(field);
                    break;
                }
            }
        }

        return fieldList.toArray(new Field[0]);
    }

    /**
     * 获取clazz的所有Field
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class clazz){
        List<Field> fieldList = new ArrayList<>();
        getAllFields(clazz, fieldList);

        return fieldList;
    }

    /**
     * 获取clazz的所有Field
     * @param clazz
     * @return
     */
    private static void getAllFields(Class clazz, List<Field> fieldList){
        if(clazz == null){
            return;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        if(declaredFields.length > 0){
            fieldList.addAll(Arrays.asList(declaredFields));
        }
        getAllFields(clazz.getSuperclass(), fieldList);
    }

    /**
     * 获取对象的Field数组
     * @param beanClazz
     *        对象
     * @param annotateClass
     *        注解的Class，用来筛选
     */
    public static Field[] getFileds(Class beanClazz, Class annotateClass) throws IllegalAccessException {
        List<Field> allFieldList = getAllFields(beanClazz);
        if(CollectionUtil.isEmtpy(allFieldList)){
            throw new MybatisCompleteRuntimeException("对象属性不能为空！");
        }

        Field[] fields = allFieldList.toArray(new Field[0]);
        //是否过滤属性上的annotateClass注解
        if(annotateClass != null){
            fields = retainFieldByAnnotation(fields, annotateClass);
        }
        if(fields.length < 1){
            return null;
        }
        return fields;
    }

    /**
     * 获取主键属性
     * @param obj
     * @return
     */
    public static Field getPkProperty(Object obj) {
        List<Field> allFieldList = ReflectionUtil.getAllFields(obj.getClass());
        List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(allFieldList, new ArrayList<>());
        if(CollectionUtil.isEmtpy(columnModelList)){
            return null;
        }

        for(ColumnModel columnModel: columnModelList){
            Field field = columnModel.getField();
            boolean pk = columnModel.isPk();

            if(pk){
                return field;
            }
        }
        return null;
    }
}
