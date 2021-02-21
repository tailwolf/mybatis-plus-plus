package com.tailwolf.mybatis.core.exception;

/**
 * Mybatis++的运行时异常
 * @author tailwolf
 * @date 2020-05-29
 */
public class MybatisPlusPlusRuntimeException extends RuntimeException {
    public MybatisPlusPlusRuntimeException() {
        super();
    }

    public MybatisPlusPlusRuntimeException(String message) {
        super(message);
    }
}
