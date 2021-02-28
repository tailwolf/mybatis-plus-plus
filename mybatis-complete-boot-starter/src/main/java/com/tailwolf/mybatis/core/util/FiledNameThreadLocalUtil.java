package com.tailwolf.mybatis.core.util;

/**
 * 因为cglib动态代理回调方法的返回值类型必须要和被代理方法返回类型一致，不然会报类型转换异常。
 * 所以采用这个工具类保存回调方法的返回值
 * @author tailwolf
 * @date 2020-08-23
 */
public class FiledNameThreadLocalUtil {
    private static final ThreadLocal<String> FILED_NAME_THREAD_LOCAL = new ThreadLocal<>();

    private FiledNameThreadLocalUtil(){}

    public static void setValue(String filedName){
        FILED_NAME_THREAD_LOCAL.set(filedName);
    }

    public static String getValue(){
        return FILED_NAME_THREAD_LOCAL.get();
    }

    public static void removeValue(){
        FILED_NAME_THREAD_LOCAL.remove();
    }
}
