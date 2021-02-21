package com.tailwolf.mybatis.core.proxy;

/**
 * 通用查询条件构造器的代理Handler
 * @author tailwolf
 * @date 2020-08-12
 */
public class CommonQueryHandler {
    private static final CommonQueryInterceptor COMMON_QUERY_INTERCEPTOR = new CommonQueryInterceptor();
    private CommonQueryHandler(){}

    public static CommonQueryInterceptor getInterceptor() {
        return COMMON_QUERY_INTERCEPTOR;
    }
}
