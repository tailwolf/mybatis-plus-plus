package com.tailwolf.mybatis.generator.constant;

import java.io.File;

/**
 * 代码生成器需要的常量
 * @author tailwolf
 * @date 2021-01-28
 */
public class GeneratorConstant {
    //实体类模板路径
    public final static String ENTITY_TEMPLATE_PATH =  "template" + File.separator + "EntityTemplate.ftl";
    //mapper类模板路径
    public final static String MAPPER_TEMPLATE_PATH =  "template" + File.separator + "MapperTemplate.ftl";
    //service类模板路径
    public final static String SERVICE_TEMPLATE_PATH =  "template" + File.separator + "ServiceTemplate.ftl";
    //serviceImpl类模板路径
    public final static String SERVICE_IMPL_TEMPLATE_PATH =  "template" + File.separator + "ServiceImplTemplate.ftl";
    //controller类模板路径
    public final static String CONTROLLER_TEMPLATE_PATH =  "template" + File.separator + "ControllerTemplate.ftl";
    //mapperXml模板路径
    public final static String MAPPER_XML_TEMPLATE_PATH =  "template" + File.separator + "MapperXmlTemplate.ftl";

    //java扩展名
    public final static String JAVA_EXT = ".java";
    //XML扩展名
    public final static String XML_EXT = ".xml";
}
