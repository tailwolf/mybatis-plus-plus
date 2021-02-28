package com.tailwolf.mybatis.generator.type;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 从jdbc类型映射到java类型
 * @author tailwolf
 * @date 2021-01-27
 */
public class TypeHandler {

    private static final Map<JDBCType, Class<?>> JDBC_TYPE_TO_JAVA_TYPE = new HashMap<>();

    static {
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.VARCHAR, String.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.CHAR, String.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.BLOB, byte[].class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.LONGVARCHAR, String.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.TINYINT, Integer.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.INTEGER, Integer.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.BIGINT, Integer.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.SMALLINT, Integer.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.BIT, Boolean.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.FLOAT, Float.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.DOUBLE, Double.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.DECIMAL, BigDecimal.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.BOOLEAN, Integer.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.DATE, Date.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.TIME, LocalTime.class);
        JDBC_TYPE_TO_JAVA_TYPE.put(JDBCType.TIMESTAMP, LocalDateTime.class);
    }

    public static Class<?> jdbcTypeToJavaType(JDBCType jdbcType){
        return JDBC_TYPE_TO_JAVA_TYPE.get(jdbcType);
    }
}
