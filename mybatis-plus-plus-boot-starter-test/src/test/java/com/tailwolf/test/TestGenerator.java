package com.tailwolf.test;

import com.tailwolf.mybatis.generator.CodeGenerator;
import com.tailwolf.mybatis.generator.config.DsConfig;
import com.tailwolf.mybatis.generator.config.EntityConfig;
import com.tailwolf.mybatis.generator.config.ParentConfig;
import com.tailwolf.mybatis.generator.config.PathConfig;
import com.tailwolf.parent.CommonModel;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 代码生成
 */
public class TestGenerator {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, TemplateException, IOException {
        CodeGenerator codeGenerator = new CodeGenerator();
        //配置数据源
        DsConfig dbConfig = new DsConfig();
        dbConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbConfig.setUrl("jdbc:mysql://localhost:3306/mybatis-plus-plus?useSSL=false&useUnicode=true&characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dbConfig.setUsername("root");
        dbConfig.setPassword("12345678");
        dbConfig.setTableNames(new String[] {"sys_user","sys_role","sys_user_role","project","task"});
        codeGenerator.setDsConfig(dbConfig);

        //配置生成路径
        PathConfig pathConfig = new PathConfig();
        //java文件的绝对根路径
        pathConfig.setRootPath("D:\\IdeaProject\\mybatis-plus-plus\\mybatis-plus-plus-starter-test\\src\\main\\java");
        //java文件的包路径
        pathConfig.setPackageName("com.tailwolf");
        //xml文件的绝对根路径
        pathConfig.setXmlRootPath("D:\\IdeaProject\\mybatis-plus-plus\\mybatis-plus-plus-starter-test\\src\\main\\resources");
        //xml文件的包路径
        pathConfig.setXmlMapperPackageName("mybatis.mapper");
        codeGenerator.setPathConfig(pathConfig);

        //实体配置类
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setMethodFormat(2);
        //排除某些字段
//        entityConfig.setExColumnList(List.of("create_on","create_by"));
        codeGenerator.setEntityConfig(entityConfig);


//        ParentConfig parentConfig = new ParentConfig();
//        parentConfig.setEntityParent(CommonModel.class);
//        parentConfig.setControllerParent(BaseController.class);
//        codeGenerator.setParentConfig(parentConfig);
        codeGenerator.setCoverFile(true);
        codeGenerator.execute();
    }
}
