package com.tailwolf.mybatis.datasourse;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.tailwolf.mybatis.constant.ThirdPartyConstant;
import com.tailwolf.mybatis.core.util.ApplicationContextUtil;
import com.tailwolf.mybatis.core.util.BeanConvUtil;
import com.tailwolf.mybatis.core.util.ReflectionUtil;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 该类的作用是将配置文件的数据源信息注入到容器
 * @author tailwolf
 * @date 2020-09-17
 */
public class DiDataSourceAware implements ApplicationContextAware {

    private List<DataSourceProperties> propertiesList;

    /**
     * 获取的applicationContext实际上是AnnotationConfigApplicationContext实例
     * AnnotationConfigApplicationContext继承自GenericApplicationContext，
     * GenericApplicationContext有属性DefaultListableBeanFactory，
     * DefaultListableBeanFactory有方法registerSingleton，
     * 可以通过registerSingleton方法向容器中出入单例的bean
     * @param applicationContext
     *        ApplicationContext实例
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            ApplicationContextUtil.applicationContext = applicationContext;
            //获取bean工厂实现类的registerSingleton方法对象
            Object beanFactory = ReflectionUtil.getProperty(applicationContext, ThirdPartyConstant.BEAN_FACTORY);
            Class<?> beanFactoryClass = beanFactory.getClass();
            Method registerSingleton = beanFactoryClass.getDeclaredMethod(ThirdPartyConstant.REGISTER_SINGLETON, String.class, Object.class);

            if(this.propertiesList != null  && this.propertiesList.size() > 0){
                this.propertiesList.forEach(item -> {
                    try {
                        Map<String, String> propertiesMap = BeanConvUtil.beanToMap(item);
                        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
                        ReflectionUtil.setProperty(dataSourceBuilder, "properties", propertiesMap);
                        DataSource dataSource = dataSourceBuilder.build();

                        //执行bean工厂的registerSingleton方法，注入bean
                        registerSingleton.invoke(beanFactory, propertiesMap.get("name"), dataSource);
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPropertiesList(List<DataSourceProperties> propertiesList) {
        this.propertiesList = propertiesList;
    }
}
