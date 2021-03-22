package com.tailwolf.mybatis.core;

import com.tailwolf.mybatis.core.dynamic.build.DynamicCrudStatementBuild;
import com.tailwolf.mybatis.core.dsl.build.DslMappedStatementBuild;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import com.tailwolf.mybatis.constant.ThirdPartyConstant;
import com.tailwolf.mybatis.core.util.ReflectionUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang3.ClassUtils.PACKAGE_SEPARATOR;

/**
 * 该类的作用生成了mybatis-complete所有所需的MappedStatement，包括了dsl和实体类增删改查常用的mapper
 * @author tailwolf
 * @date 2020-09-26
 */
public class CommonMapperAware implements ApplicationContextAware {

    public void addMapperEntityMap(Map<String, Class<?>> mapperEntityMap, String...basePackageArray) throws IOException, ClassNotFoundException {
        for(String basePackage: basePackageArray){
            if(StringUtils.hasText(basePackage)){
                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage.replace(PACKAGE_SEPARATOR, "/") + '/' + "**/*.class";
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);

                for(Resource resource: resources){
                    Class<?> mapperClass = Class.forName(basePackage + PACKAGE_SEPARATOR + resource.getFilename().replace(".class", ""));
                    String typeName = mapperClass.getGenericInterfaces()[0].getTypeName();
                    if(typeName.startsWith("com.tailwolf.mybatis.core.common.dao.EntityOptMapper")){
                        String entityReference = typeName.replace("com.tailwolf.mybatis.core.common.dao.EntityOptMapper<", "").replace(">", "");
                        mapperEntityMap.put(mapperClass.getName(), Class.forName(entityReference));
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try{
            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MapperScan.class);
            Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();

            Map<String, Class<?>> mapperEntityMap = new HashMap<>();
            for(Map.Entry<String, Object> entry: entries){
                MapperScan mapperScanAnnotation = AnnotationUtils.findAnnotation(entry.getValue().getClass(), MapperScan.class);

                String[] basePackageArray = mapperScanAnnotation.basePackages();
                addMapperEntityMap(mapperEntityMap, basePackageArray);

                String[] valueArray = mapperScanAnnotation.value();
                addMapperEntityMap(mapperEntityMap, valueArray);

                Class<?>[] basePackageClasseArray = mapperScanAnnotation.basePackageClasses();
                for(Class clazz: basePackageClasseArray){
                    String packageName = ClassUtils.getPackageName(clazz);
                    addMapperEntityMap(mapperEntityMap, packageName);
                }
            }

            SqlSessionFactory sqlSessionFactory = applicationContext.getBean(ThirdPartyConstant.SQL_SESSION_FACTORY, SqlSessionFactory.class);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
            if(mapperEntityMap.size() > 0){
                for(Map.Entry<String, Class<?>> entry: mapperEntityMap.entrySet()){
                    String mapper = entry.getKey();
                    Class<?> bean = entry.getValue();
//                    MappedStatementBuild mappedStatementBuild = MappedStatementFactory.createMappedStatementBuild(bean, dslMapperList, daoMapperList, configuration);
                    Map<String, MappedStatement> entityCrudStatementMap = new DynamicCrudStatementBuild(mapper, bean, configuration).crateMappedStatementMap();
                    Map<String, MappedStatement> joinDslMappedStatementMap = new DslMappedStatementBuild(mapper, bean, configuration).crateMappedStatementMap();
                    mappedStatementMap.putAll(entityCrudStatementMap);
                    mappedStatementMap.putAll(joinDslMappedStatementMap);
                }
            }

            if(mappedStatementMap.size() > 0){
                Map<String, MappedStatement> property = (Map<String, MappedStatement>) ReflectionUtil.getProperty(configuration, "mappedStatements");
                mappedStatementMap.putAll(property);
                ReflectionUtil.setProperty(configuration, "mappedStatements", mappedStatementMap);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new ApplicationContextException(ex.getMessage());//如果出现异常，必须停止项目继续启动
        }
    }
}
