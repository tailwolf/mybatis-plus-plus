package com.tailwolf.mybatis.generator;

import com.tailwolf.mybatis.generator.config.DsConfig;
import com.tailwolf.mybatis.generator.config.EntityConfig;
import com.tailwolf.mybatis.generator.config.ParentConfig;
import com.tailwolf.mybatis.generator.config.PathConfig;
import com.tailwolf.mybatis.generator.constant.GeneratorConstant;
import com.tailwolf.mybatis.generator.properties.*;
import com.tailwolf.mybatis.generator.type.TypeHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代码生成器
 * @author tailwolf
 * @date 2021-01-27
 */
public class CodeGenerator {
    //数据库配置
    private DsConfig dsConfig;
    //路径配置
    private PathConfig pathConfig;
    //生成实体类的配置
    private EntityConfig entityConfig;
    //生成java代码类的父类的配置
    private ParentConfig parentConfig;
    //是否覆盖已存在的文件，默认false
    private boolean isCoverFile = false;

    public void setDsConfig(DsConfig dsConfig) {
        this.dsConfig = dsConfig;
    }

    public void setPathConfig(PathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public void setCoverFile(boolean coverFile) {
        this.isCoverFile = coverFile;
    }

    public void setParentConfig(ParentConfig parentConfig) {
        this.parentConfig = parentConfig;
    }

    /**
     * 开始执行
     */
    public void execute() throws IOException, ClassNotFoundException, SQLException, TemplateException {
        String url = dsConfig.getUrl();
        String username = dsConfig.getUsername();
        String password = dsConfig.getPassword();
        Class.forName(dsConfig.getDriverClassName());
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement statement = conn.createStatement();
        Statement columnsStatement = conn.createStatement();

        //实体类
        List<EntityProperties> entityPropertiesList = new ArrayList<>();
        //Mapper
        List<MapperProperties> mapperPropertiesList = new ArrayList<>();
        //Service
        List<ServiceProperties> servicePropertiesList = new ArrayList<>();
        //ServiceImpl
        List<ServiceImplProperties> serviceImplPropertiesList = new ArrayList<>();
        //Controller
        List<ControllerProperties> controllerPropertiesList = new ArrayList<>();
        //MapperXml
        List<MapperXmlProperties> mapperXmlPropertiesList = new ArrayList<>();

        //包名
        String packageName = pathConfig.getPackageName();
        //表名集合
        String[] tableNames = dsConfig.getTableNames();
        for(String tableName: tableNames){
            //实体类集合
            String entityPackageName = packageName + ".entity";
            String entityName = tableNameToClassName(tableName);
            EntityProperties entityProperties = new EntityProperties(entityPackageName, entityName, tableName);
            if(parentConfig != null && parentConfig.getEntityParent() != null){
                Class<?> entityParent = parentConfig.getEntityParent();
                //父类名称和父类引用
                String typeName = entityParent.getTypeName();
                entityProperties.setEntityParentRef(typeName);
                entityProperties.setEntityParentName(typeName.substring(typeName.lastIndexOf(".")+1));
            }

            //mapper类集合
            String mapperPackageName = packageName + ".mapper";
            String entityReference = entityPackageName + "." + entityName;
            String mapperName = entityName + "Mapper";
            MapperProperties mapperProperties = new MapperProperties(mapperPackageName, entityReference, mapperName, entityName);
            mapperPropertiesList.add(mapperProperties);
            //service类集合
            String servicePackageName = packageName + ".service";
            String serviceName = entityName + "Service";
            ServiceProperties serviceProperties = new ServiceProperties(servicePackageName, entityReference, serviceName, entityName);
            servicePropertiesList.add(serviceProperties);
            //serviceImpl类集合
            String mapperReference = mapperPackageName + "." + mapperName;
            String serviceReference = servicePackageName + "." + serviceName;
            String serviceImplName = entityName + "ServiceImpl";
            ServiceImplProperties serviceImplProperties = new ServiceImplProperties(packageName + ".service.impl",
                    entityReference, mapperReference, serviceReference, serviceName, mapperName, serviceImplName, entityName);
            serviceImplPropertiesList.add(serviceImplProperties);
            //controller类集合
            String controllerPackageName = packageName + ".controller";
            String controllerName = entityName + "controller";
            ControllerProperties controllerProperties = new ControllerProperties(controllerPackageName, entityName, serviceReference, serviceName, controllerName);
            controllerPropertiesList.add(controllerProperties);
            if(parentConfig != null && parentConfig.getControllerParent() != null){
                Class<?> controllerParent = parentConfig.getControllerParent();
                //父类名称和父类引用
                String typeName = controllerParent.getTypeName();
                controllerProperties.setControllerParentRef(typeName);
                controllerProperties.setControllerParentName(typeName.substring(typeName.lastIndexOf(".")+1));
            }
            //mapperXml文件集合
            if(StringUtils.isEmpty(pathConfig.getXmlMapperPackageName()) || StringUtils.isEmpty(pathConfig.getXmlRootPath())){
                pathConfig.setXmlMapperPackageName(mapperPackageName);
                pathConfig.setXmlRootPath(pathConfig.getRootPath());
            }
            MapperXmlProperties mapperXmlProperties = new MapperXmlProperties(pathConfig.getXmlMapperPackageName(), entityName, mapperName, mapperReference);
            mapperXmlPropertiesList.add(mapperXmlProperties);

            ResultSet resultSet = null;
            ResultSet columnsResultSet = null;
            try{
                resultSet = statement.executeQuery("select * from " + tableName);
                columnsResultSet = columnsStatement.executeQuery("show full columns from " + tableName);

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                List<String> exColumnList = entityConfig.getExColumnList();
                for(int i = 1; i <= columnCount; i++){
                    columnsResultSet.next();

                    //获取每一列的类型
                    int columnType = metaData.getColumnType(i);
                    //转为java类型
                    Class<?> javaType = TypeHandler.jdbcTypeToJavaType(JDBCType.valueOf(columnType));
                    //列名
                    String columnName = metaData.getColumnName(i);
                    if(exColumnList.contains(columnName)){
                        continue;
                    }
                    //注释
                    String comment = columnsResultSet.getString("Comment");

                    entityProperties.addField(javaType, lineToHump(columnName), "".equals(comment)?null:comment);
                    if(entityConfig == null){
                        entityProperties.setMethodFormat(1);
                    }else{
                        entityProperties.setMethodFormat(entityConfig.getMethodFormat());
                    }
                    entityPropertiesList.add(entityProperties);
                }
            }finally {
                if(resultSet != null){
                    resultSet.close();
                }
                if(columnsResultSet != null){
                    columnsResultSet.close();
                }
            }
        }

        statement.close();
        columnsStatement.close();
        conn.close();
        //输出实体类
        outEntity(entityPropertiesList);
        //Mapper类
        outMapper(mapperPropertiesList);
        //Service类
        outService(servicePropertiesList);
        //ServiceImpl类
        outServiceImpl(serviceImplPropertiesList);
        //controller类
        outController(controllerPropertiesList);
        //MapperXml文件
        outMapperXml(mapperXmlPropertiesList);
    }

    //下划线转驼峰
    private String lineToHump(String str){
        Pattern linePattern = Pattern.compile("_(\\w)");

        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            str = str.toLowerCase();
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * 数据表转类名
     * @param tableName 数据表名
     * @return
     */
    private String tableNameToClassName(String tableName){
        tableName = lineToHump(tableName);
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
    }

    /**
     * 输出实体类
     * @param entityPropertiesList
     * @throws IOException
     * @throws TemplateException
     */
    private void outEntity(List<EntityProperties> entityPropertiesList) throws IOException, TemplateException {
        for(EntityProperties entityProperties: entityPropertiesList){

            Template template = template(GeneratorConstant.ENTITY_TEMPLATE_PATH);
            //拼接路径
            String fileName = entityProperties.getEntityName().concat(GeneratorConstant.JAVA_EXT);
            process(template, entityProperties, fileName, pathConfig.getRootPath());
        }
    }

    /**
     * 输出mapper文件
     * @param mapperPropertiesList
     */
    private void outMapper(List<MapperProperties> mapperPropertiesList) throws IOException, TemplateException {
        for(MapperProperties mapperProperties: mapperPropertiesList){
            Template template = template(GeneratorConstant.MAPPER_TEMPLATE_PATH);
            // 处理路径问题
            String fileName = mapperProperties.getMapperName().concat(GeneratorConstant.JAVA_EXT);
            process(template, mapperProperties, fileName, pathConfig.getRootPath());
        }
    }

    /**
     * 输出service文件
     * @param servicePropertiesList
     */
    private void outService(List<ServiceProperties> servicePropertiesList) throws IOException, TemplateException {
        for(ServiceProperties servicePropertiesst: servicePropertiesList){
            Template template = template(GeneratorConstant.SERVICE_TEMPLATE_PATH);
            // 处理路径问题
            String fileName = servicePropertiesst.getServiceName().concat(GeneratorConstant.JAVA_EXT);
            process(template, servicePropertiesst, fileName, pathConfig.getRootPath());
        }
    }

    /**
     * 输出serviceImpl文件
     * @param serviceImplPropertiesList
     */
    private void outServiceImpl(List<ServiceImplProperties> serviceImplPropertiesList) throws IOException, TemplateException {
        for(ServiceImplProperties serviceImplProperties: serviceImplPropertiesList){
            Template template = template(GeneratorConstant.SERVICE_IMPL_TEMPLATE_PATH);
            // 处理路径问题
            String fileName = serviceImplProperties.getServiceImplName().concat(GeneratorConstant.JAVA_EXT);
            process(template, serviceImplProperties, fileName, pathConfig.getRootPath());
        }
    }

    /**
     * 输出controller类
     * @param controllerPropertiesList
     */
    private void outController(List<ControllerProperties> controllerPropertiesList) throws IOException, TemplateException {
        for(ControllerProperties controllerProperties: controllerPropertiesList){
            Template template = template(GeneratorConstant.CONTROLLER_TEMPLATE_PATH);
            // 处理路径问题
            String fileName = controllerProperties.getControllerName().concat(GeneratorConstant.JAVA_EXT);
            process(template, controllerProperties, fileName, pathConfig.getRootPath());
        }
    }

    /**
     * 输出MapperXml文件
     * @param mapperXmlPropertiesList
     */
    private void outMapperXml(List<MapperXmlProperties> mapperXmlPropertiesList) throws IOException, TemplateException {
        for(MapperXmlProperties mapperXmlProperties: mapperXmlPropertiesList){
            Template template = template(GeneratorConstant.MAPPER_XML_TEMPLATE_PATH);
            //获取文件名
            String fileName = mapperXmlProperties.getMapperXmlName().concat(GeneratorConstant.XML_EXT);
            process(template, mapperXmlProperties, fileName, pathConfig.getXmlRootPath());
        }
    }

    private Template template(String templatePath) throws IOException {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        //获取FreeMarker模板
        configuration.setClassForTemplateLoading(CodeGenerator.class, "/");
        // 设置FreeMarker生成Word文档所需要的模板
        return configuration.getTemplate(templatePath, "UTF-8");
    }

    private void process(Template template, BaseProperties properties, String fileName, String rootPath) throws IOException, TemplateException {
        String folderPath = rootPath.concat(Stream.of(properties.getPackageName().split("\\."))
                .collect(Collectors.joining(File.separator, File.separator, "")));
        //判断文件目录是否存在，不存在则新建一个文件
        File folderFile = new File(folderPath);
        if(!folderFile.exists()){
            folderFile.mkdir();
        }
        String out = folderPath + File.separator + fileName;
        File outFile = new File(out);
        //是否删除再创建
        if(isCoverFile && outFile.exists()){
            outFile.delete();
        }
        if(!outFile.exists()){
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(out));
            template.process(properties, outputStreamWriter);
        }
    }
}