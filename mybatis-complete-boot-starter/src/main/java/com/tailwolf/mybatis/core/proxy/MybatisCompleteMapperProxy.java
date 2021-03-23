package com.tailwolf.mybatis.core.proxy;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.lang.UsesJava7;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;
import com.tailwolf.mybatis.core.exception.MybatisCompleteRuntimeException;
import com.tailwolf.mybatis.core.ColumnModel;
import com.tailwolf.mybatis.core.util.ColumnModelUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 该类替代了mybatis的MapperProxy，
 * 作用就是将EntityOptMapper的findByPk，deleteByPk，deleteBatchByPk方法里的非实体类(当然入参也可以是实体类)转成实体类
 * @author tailwolf
 * @date 2020-11-15
 */
public class MybatisCompleteMapperProxy implements InvocationHandler, Serializable {
    private static final long serialVersionUID = -6424540398559729838L;
    private final SqlSession sqlSession;
    private final Class mapperInterface;
    private final Map<Method, MapperMethod> methodCache;

    public MybatisCompleteMapperProxy(SqlSession sqlSession, Class mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Type[] genericInterfaces = mapperInterface.getGenericInterfaces();
        if(genericInterfaces.length > 0){
            Type genericInterface = genericInterfaces[0];
            String typeName = genericInterface.getTypeName();
            String className = typeName.replace("com.tailwolf.mybatis.core.common.dao.EntityOptMapper<", "").replace(">", "");
            if((method.getName().equals("findByPk") || method.getName().equals("deleteByPk")) && args.length == 1){
                Object arg = args[0];
                if(!className.equals(arg.getClass().getName())){
                    Class<?> clazz = Class.forName(className);
                    Object entity = clazz.newInstance();
                    List<Field> allFieldList = ReflectionUtil.getAllFields(clazz);
                    List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(allFieldList, new ArrayList<>());

                    for(int i = 0; i < columnModelList.size(); i++){
                        ColumnModel columnModel = columnModelList.get(i);
                        Field field = columnModel.getField();
                        String fieldName = field.getName();
                        boolean pk = columnModel.isPk();

                        if(pk){
                            String fieldType = field.getType().getName();
                            String argType = arg.getClass().getName();
                            setAndConverValue(fieldType, argType, fieldName, entity, arg);
                            args[0] = entity;
                            break;
                        }
                    }
                }
            }else if(method.getName().equals("deleteBatchByPk") && args.length == 1){
                Object arg = args[0];
                if(arg instanceof Collection){
                    Collection collection = (Collection)arg;
                    Class<?> clazz = null;
                    String pkFieldName = null;
                    Field pkField = null;
                    end: for(Object obj: collection){
                        if(!className.equals(obj.getClass().getName())){
                            clazz = Class.forName(className);
                            List<Field> allFields = ReflectionUtil.getAllFields(clazz);
                            List<ColumnModel> columnModelList = ColumnModelUtil.createColumnModel(allFields, new ArrayList<>());
                            for(int i = 0; i < columnModelList.size(); i++){
                                ColumnModel columnModel = columnModelList.get(i);
                                Field field = columnModel.getField();
                                String fieldName = field.getName();
                                boolean pk = columnModel.isPk();

                                if(pk){
                                    pkFieldName = fieldName;
                                    pkField = field;
                                    break end;
                                }
                            }
                        }
                    }

                    if(clazz != null){
                        if(StringUtils.isEmpty(pkFieldName)){
                            throw new MybatisCompleteRuntimeException("该实体类注解无主键！");
                        }

                        List entityList = new ArrayList();
                        String fieldType = pkField.getType().getName();
                        String argType = arg.getClass().getName();
                        for(Object obj: collection){
                            Object entity = clazz.newInstance();
                            setAndConverValue(fieldType, argType, pkFieldName, entity, obj);
                            entityList.add(entity);
                        }
                        args[0] = entityList;
                    }
                }
            }else if((method.getName().equals("dslQueryOne") || method.getName().equals("dslQuery") || method.getName().equals("dslDelete") || method.getName().equals("dslUpdate")) && args.length == 1){
                Object arg = args[0];
                Class<?> clazz = Class.forName(className);
                Object entity = clazz.newInstance();
                ReflectionUtil.setProperty(arg, "entity", entity);
            }
        }

        Class<? extends Method> aClass = method.getClass();
        String name = method.getName();
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }

            if (this.isDefaultMethod(method)) {
                return this.invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable var5) {
            throw ExceptionUtil.unwrapThrowable(var5);
        }

        MapperMethod mapperMethod = this.cachedMapperMethod(method);
        return mapperMethod.execute(this.sqlSession, args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = (MapperMethod)this.methodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration());
            this.methodCache.put(method, mapperMethod);
        }

        return mapperMethod;
    }

    @UsesJava7
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        Class<?> declaringClass = method.getDeclaringClass();
        return ((MethodHandles.Lookup)constructor.newInstance(declaringClass, 15)).unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & 1033) == 1 && method.getDeclaringClass().isInterface();
    }

    private void setAndConverValue(String fieldType, String argType, String fieldName, Object entity, Object arg) throws IllegalAccessException {
        if(fieldType.equals(argType)){
            ReflectionUtil.setProperty(entity, fieldName, arg);
        }else{
            if(fieldType.equals("java.lang.Short")){
                arg = Short.valueOf(arg.toString());
            }else if(fieldType.equals("java.lang.Integer")){
                arg = Integer.valueOf(arg.toString());
                ReflectionUtil.setProperty(entity, fieldName, Integer.valueOf(arg.toString()));
            }else if(fieldType.equals("java.lang.Long")){
                arg = Long.valueOf(arg.toString());
                ReflectionUtil.setProperty(entity, fieldName, Long.valueOf(arg.toString()));
            }else if(fieldType.equals("java.lang.String")){
                arg = String.valueOf(arg.toString());
            }
            ReflectionUtil.setProperty(entity, fieldName, arg);
        }
    }
}
