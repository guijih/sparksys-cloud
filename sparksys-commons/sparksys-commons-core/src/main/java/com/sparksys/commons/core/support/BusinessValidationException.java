package com.sparksys.commons.core.support;

/**
 * description: 校验异常类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:49:20
 */
public class BusinessValidationException extends Exception {

    private static final long serialVersionUID = -7571216052553061849L;

    public BusinessValidationException(Throwable cause) {
        super(cause);
    }

    public BusinessValidationException(String message) {
        super(message);
    }
}
