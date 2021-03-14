package com.tailwolf.mybatis.constant;

/**
 * 拼接sql语句的常量类
 * @author tailwolf
 * @date 2020-06-02
 */
public class MontageSqlConstant {
    public static final String SELECT = "SELECT ";
    public static final String DELETE = "DELETE ";
    public static final String UPDATE = "UPDATE ";
    public static final String FROM = "FROM ";
    public static final String WHERE = "WHERE ";
    public static final String SET = "SET ";
    public static final String ON = "ON ";
    public static final String AND = "AND ";
    public static final String OR = "OR ";
    public static final String GT = "> ";
    public static final String GE = ">= ";
    public static final String LT = "< ";
    public static final String LE = "<= ";
    public static final String NE = "!= ";
    public static final String EQ = "= ";
    public static final String IN = "IN ";
    public static final String IS = "IS ";
    public static final String NOT_IN = "NOT IN ";
    public static final String LIKE = "LIKE ";
    public static final String LEFT_LIKE = " LIKE ";
    public static final String RIGHT_LIKE = "LIKE  ";
    public static final String DESC = "desc ";
    public static final String ASC = "asc ";

    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ") ";
    public static final String SPACE = " ";
    public static final String BLANK = "";
    public static final String COMMA = ", ";

    public static final String LEFT_JOIN = "LEFT JOIN ";
    public static final String RIGHT_JOIN = "RIGHT JOIN ";
    public static final String INNER_JOIN = "INNER JOIN ";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String GROUP_BY = "GROUP BY ";
    public static final String HAVING = "HAVING ";

    public static final String EXISTS = "EXISTS";
    public static final String NOT_EXISTS = "NOT EXISTS";

    public static final String T1 = "t1";
    public static final String T2 = "t2";

    public static final String STAR_KEY = "* ";
    public static final String ALL = STAR_KEY + SPACE;
    public static final String COUNT = "COUNT";
    public static final String COUNT_TEMPLATE = "COUNT($)";
    public static final String COUNT_ALL = "COUNT(*)";

    public static final String AND_START = "and_start";
    public static final String AND_END = "and_end";
    public static final String OR_START = "or_start";
    public static final String OR_END = "or_end";

    public static final String POINT = ".";
}
