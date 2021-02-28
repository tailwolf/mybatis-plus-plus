package com.tailwolf.mybatis.core.util;

import com.tailwolf.mybatis.core.ColumnModel;
import com.tailwolf.mybatis.core.annotation.Column;
import com.tailwolf.mybatis.core.annotation.Pk;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ColumnModel工具类
 * @author tailwolf
 * @date 2020-11-24
 */
public class ColumnModelUtil {
    /**
     * 生成List<ColumnModel>
     * @param fieldList
     * @return
     */
    public static List<ColumnModel> createColumnModel(List<Field> fieldList, List<Class> notExistAnnoList){
        List<ColumnModel> columnModelList = new ArrayList<>();
        end:for(Field field: fieldList){
            for(Class clazz: notExistAnnoList){
                Annotation annotation = field.getAnnotation(clazz);
                if(annotation != null){
                    break end;
                }
            }

            ColumnModel columnModel = new ColumnModel();
            columnModel.setField(field);

            Pk ps = field.getAnnotation(Pk.class);
            if(ps != null){
                columnModel.setPk(true);
            }else{
                columnModel.setPk(false);
            }

            Column column = field.getAnnotation(Column.class);
            String columnName = "";
            if(column != null){
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(column);
                LinkedHashMap memberValues = (LinkedHashMap)ReflectionUtil.getProperty(invocationHandler, "memberValues");
                boolean exist = (boolean)memberValues.get("exist");
                if(!exist){
                    continue;
                }
                columnName = (String)memberValues.get("name");
                if(StringUtils.isEmpty(columnName)){
                    columnName = humpToLine(field.getName());
                }
            }else{
                columnName = humpToLine(field.getName());
            }
            columnModel.setColumnName(columnName);
            columnModelList.add(columnModel);

        }

        //筛选主键
        List<ColumnModel> findPkList = columnModelList.stream().filter(ColumnModel::isPk).collect(Collectors.toList());
        if(CollectionUtil.isEmtpy(findPkList)){
            columnModelList = columnModelList.stream().map(columnModel -> {
                if("id".equals(columnModel.getField().getName())){
                    columnModel.setPk(true);
                }
                return columnModel;
            }).collect(Collectors.toList());
        }

        return columnModelList;
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
