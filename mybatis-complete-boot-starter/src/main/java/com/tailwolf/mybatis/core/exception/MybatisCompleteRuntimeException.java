package com.tailwolf.mybatis.core.exception;

/**
 * mybatis-complete的运行时异常
 * @author tailwolf
 * @date 2020-05-29
 */
public class MybatisCompleteRuntimeException extends RuntimeException {
    public MybatisCompleteRuntimeException() {
        super();
    }

    public MybatisCompleteRuntimeException(String message) {
        super(message);
    }
}
