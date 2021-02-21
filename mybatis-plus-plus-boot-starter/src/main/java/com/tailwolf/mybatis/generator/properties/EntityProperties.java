package com.tailwolf.mybatis.generator.properties;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 生成实体类的配置属性
 * @author tailwolf
 * @date 2021-01-27
 */
public class EntityProperties extends BaseProperties{
    //表名
    private String tableName;
    //属性集合。需要改写，equals hash，保证名字可不重复，类型可重复
    private final Set<Field> fields = new LinkedHashSet<>();
    //导入类的不重复集合
    private final Set<String> imports = new LinkedHashSet<>();
    //父类引用
    private String entityParentRef;
    //父类的名称
    private String entityParentName;

    //1代表生成getset方法，2代表使用lombok。默认是1
    private Integer methodFormat;

    public EntityProperties(String packageName, String entityName, String tableName){
        super(packageName, entityName);
        this.tableName = tableName;
    }

    public void addField(Class<?> type, String fieldName, String comment) {
        // 处理 java.lang
        final String pattern = "java.lang";
        String fieldType = type.getName();
        if (!fieldType.startsWith(pattern)) {
            // 处理导包
            imports.add(fieldType);
        }
        Field field = new Field();
        // 处理成员属性的格式
        int i = fieldType.lastIndexOf(".");
        field.setFieldType(fieldType.substring(i + 1));
        field.setFieldName(fieldName);
        field.setComment(comment);
        fields.add(field);
    }

    public Set<Field> getFields() {
        return fields;
    }

    public Set<String> getImports() {
        return imports;
    }

    public Integer getMethodFormat() {
        return methodFormat;
    }

    public void setMethodFormat(Integer methodFormat) {
        this.methodFormat = methodFormat;
    }

    public String getEntityParentRef() {
        return entityParentRef;
    }

    public void setEntityParentRef(String entityParentRef) {
        this.entityParentRef = entityParentRef;
    }

    public String getEntityParentName() {
        return entityParentName;
    }

    public void setEntityParentName(String entityParentName) {
        this.entityParentName = entityParentName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 成员属性封装对象.
     */
    public static class Field {
        // 成员属性类型
        private String fieldType;
        // 成员属性名称
        private String fieldName;
        // 成员属性注释
        private String comment;

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        /**
         * 一个类的成员属性 一个名称只能出现一次
         * 我们可以通过覆写equals hash 方法 然后放入Set
         *
         * @param o 另一个成员属性
         * @return 比较结果
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Field field = (Field) o;
            return Objects.equals(fieldName, field.fieldName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldType, fieldName);
        }
    }
}
